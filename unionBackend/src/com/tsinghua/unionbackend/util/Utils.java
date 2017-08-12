package com.tsinghua.unionbackend.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.tsinghua.unionbackend.db.beans.User;

public class Utils {

	HttpServletRequest request;
	HttpServletResponse response;

	public HttpSession session;

	User user = null;

	public String path;
	public String attachmentPathRelative = "attachment/";

	public Utils() {
	}

	public String datetimeLocalToTimestamp(String datetime) {
		Timestamp t = Timestamp.valueOf(datetime.replace("T", " "));
		return t.toString();
	}

	public JSONObject getJsonFromRequest(HttpServletRequest request)
			throws UnionException {
		try {
			StringBuffer sb = new StringBuffer();
			BufferedReader reader = request.getReader();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			JSONObject ret = new JSONObject(sb.toString());
			return ret;
		} catch (Exception e) {
			throw new UnionException(e, "HTTP参数错误，无效的JSON格式");
		}
	}

	public Utils(HttpServletRequest request, HttpServletResponse response) {
		try {
			this.request = request;
			this.response = response;
			path = request.getServletContext().getRealPath("/");
			setHttpHeader(request, response);

			session = request.getSession();
			session.setMaxInactiveInterval(20 * 60);

			Object obj = session.getAttribute("user");
			if (obj != null)
				user = (User) obj;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("THIS SHOULD NOT HAPPEN");
		}
	}

	public void setHttpHeader(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain;charset=UTF-8");
			response.addHeader("Access-Control-Allow-Origin",
					"http://grayluck.vicp.cc");
			// "http://localhost");
			response.addHeader("Access-Control-Allow-Methods",
					"GET, PUT, POST, OPTIONS, DELETE");
			response.addHeader("Access-Control-Allow-Headers", "Content-Type");
			response.addHeader("Access-Control-Max-Age", "86400");
			response.addHeader("Access-Control-Allow-Credentials", "true");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getAttachmentPath() {
		String tmp = path + attachmentPathRelative;
		new File(tmp).mkdir();
		return tmp;
	}

	public String getRsaPath() {
		String tmp = path + "RSA/";
		new File(tmp).mkdir();
		return tmp;
	}

	public void testMode() {
		/*
		 * HttpSession session = request.getSession();
		 * session.setAttribute("user", "test");
		 */
	}

	public String err(String msg) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("error", msg);
			return obj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "This could not be happen!";
	}

	public String ok() {
		JSONObject obj = new JSONObject();
		return ok(obj);
	}

	public String ok(JSONObject obj) {
		try {
			obj.put("result", "200 OK");
			return obj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "This could not be happen!";
	}

	public String ok(String key, String value) {
		try {
			JSONObject obj = new JSONObject();
			obj.put(key, value);
			return ok(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return err("PLEASE DONT HACK ME.");
	}

	public void log(Object s) {
		System.out.println(s);
	}

	public String getSubmittedFileName(Part part) {
		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {
				String fileName = cd.substring(cd.indexOf('=') + 1).trim()
						.replace("\"", "");
				return fileName.substring(fileName.lastIndexOf('/') + 1)
						.substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
			}
		}
		return null;
	}

	public File partToFile(Part p, String s) {
		try {
			InputStream in;
			OutputStream out;
			File f;
			f = new File(s);
			in = p.getInputStream();
			out = new FileOutputStream(f);
			byte[] buf = new byte[1024];
			int length = -1;
			while ((length = in.read(buf)) != -1)
				out.write(buf, 0, length);
			in.close();
			out.close();
			return f;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public File partToFile(Part p) {
		String path = getAttachmentPath();
		String filname = getSubmittedFileName(p);
		String localName = appendTimestamp(filname);
		return partToFile(p, path + localName);
	}

	public File urlToFile(String url) {
		return new File(this.path + url);
	}

	public String getCurrentTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar
				.getInstance().getTime());
	}

	public String getTimestamp() {
		return new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar
				.getInstance().getTime());
	}

	public String appendTimestamp(String name) {
		String ext = FilenameUtils.getExtension(name);
		String filename = FilenameUtils.getBaseName(name);
		return filename + "_" + getTimestamp() + "." + ext;
	}

	public String getFullLocalName(String name) {
		return this.getAttachmentPath() + appendTimestamp(name);
	}

	public String getFullLocalName(Part part) {
		return this.getFullLocalName(getSubmittedFileName(part));
	}

	public void genKey(String usr) {
		PemDecrypter dec = new PemDecrypter();
		dec.genKey(usr, this.getRsaPath());
	}

	public String decryptRSA(String user, String s) throws UnionException {
		try {
			PemDecrypter dec = new PemDecrypter();
			return dec.decrypt(user, getRsaPath(), s);
		} catch (Exception e) {
			throw new UnionException("Decrypt failed!");
		}
	}

	public String getPubKey(String user) throws UnionException {
		try {
			String path = this.getRsaPath() + user + "_pub.pem";
			File fil = new File(path);
			DataInputStream dis = new DataInputStream(new FileInputStream(fil));
			byte[] b = new byte[(int) fil.length()];
			dis.readFully(b);
			dis.close();
			return new String(b);
		} catch (Exception e) {
			throw new UnionException(e);
		}
	}

	private String hexEncode(byte[] aInput) {
		StringBuilder result = new StringBuilder();
		char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		for (int idx = 0; idx < aInput.length; ++idx) {
			byte b = aInput[idx];
			result.append(digits[(b & 0xf0) >> 4]);
			result.append(digits[b & 0x0f]);
		}
		return result.toString();
	}

	/*
	public String genToken() {
		try {
			SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
			String randomNum = new Integer(prng.nextInt()).toString();
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			byte[] result = sha.digest(randomNum.getBytes());
			return hexEncode(result);
		} catch (NoSuchAlgorithmException ex) {
			System.err.println(ex);
			return null;
		}
	}
	*/

	public User getCurrentUser() throws UnionException {
		if (user == null)
			throw new UnionException("用户未登录！");
		return user;
	}

	public int getCurrentUserId() throws UnionException {
		try {
			return getCurrentUser().getInt("id");
		} catch (JSONException e) {
			throw new UnionException("JSON ERROR");
		}
	}

	public void setCurrentUser(User res) {
		try {
			session.setAttribute("user", new User(res));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("这不应该发生！");
		}
	}

	public void checkPerm() throws UnionException {
		try {
			User user = getCurrentUser();
			if (user.getInt("is_root") != 1)
				throw new UnionException("您不是管理员，无法进行这项操作！");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String doPost(String url, ArrayList<NameValuePair> params)
			throws UnionException {
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			CloseableHttpResponse response = client.execute(httpPost);
			if (response.getStatusLine().getStatusCode() != 200)
				throw new UnionException("Server error");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			String ret = br.readLine();
			br.close();
			return ret;
		} catch (Exception e) {
			throw new UnionException(e);
		}
	}

	public String doGet(String url) throws UnionException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		try {
			CloseableHttpResponse response = httpclient.execute(httpget);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			String ret = br.readLine();
			br.close();
			return ret;
		} catch (Exception e) {
			throw new UnionException(e);
		}
	}

	static String appid = "ghhd";
	static String appkey = "Gh_#hd%tt";

	public String md5(String s) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(s.getBytes());
			byte byteData[] = md.digest();

			// convert the byte to hex format method 1
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			return sb.toString();
		} catch (Exception e) {
			return "";
		}
	}

	public String getServerTimeStamp() throws UnionException {
		return doGet("http://weixin.cic.tsinghua.edu.cn/cop/getTimestamp.php");
	}

	public void postMsg(String content, String target) throws UnionException {
		String timestamp = getServerTimeStamp();
		String key = md5(appid + timestamp + appkey);
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userlist", target));
		params.add(new BasicNameValuePair("safe", "0"));
		params.add(new BasicNameValuePair("msg", content));
		String url = "http://weixin.cic.tsinghua.edu.cn/cop/sendmsg.php?"
				+ "app=" + appid + "&key=" + key + "&timestamp=" + timestamp
				+ "&type=text";
		String ret = doPost(url, params);
	}

	public void postAll(String content) throws UnionException {
		postMsg(content, "@all");
	}

	public void postUserList(String content, String[] target)
			throws UnionException {
		postMsg(content, String.join("|", target));
	}

	public String getNoByTicket(String ticket) throws UnionException {
		return doGet("http://weixin.cic.tsinghua.edu.cn/cop/getUserInfoFromTicket.php?ticket="
				+ ticket);
	}

	public static void main(String[] args) {
		try {
			Utils utils = new Utils();
			utils.postAll("测试");
		} catch (UnionException e) {
			e.printStackTrace();
		}
	}

}
