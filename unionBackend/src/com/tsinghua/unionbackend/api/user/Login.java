package com.tsinghua.unionbackend.api.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.tsinghua.unionbackend.db.beans.User;
import com.tsinghua.unionbackend.db.model.UserModel;
import com.tsinghua.unionbackend.util.UnionException;
import com.tsinghua.unionbackend.util.Utils;

/**
 * Servlet implementation class Login
 */
@WebServlet("/api/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doOptions(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		new Utils().setHttpHeader(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Utils utils = new Utils(request, response);
		utils.testMode();
		PrintWriter writer = response.getWriter();
		try {
			JSONObject param = utils.getJsonFromRequest(request);
			User res = null;
			UserModel model = new UserModel();
			if (param.has("test_no")) {
				res = new User(model.queryBean("user", "no",
						param.getString("test_no")));
			} else if (param.has("ticket")) {
				String ticket = param.getString("ticket");
				String no = utils.getNoByTicket(ticket);
				res = new User(model.queryBean("user", "no", no));
			} else {
				String no = param.getString("no");
				String insecure = null;
				if (param.has("insecure"))
					insecure = param.getString("insecure");
				String secret = param.getString("password");
				String password = null;
				if (insecure != null)
					password = secret;
				else
					password = utils.decryptRSA(no, secret);
				// String password = param.getString("password");
				User user = new User();
				user.put("no", no);
				user.put("password", password);
				try {
					res = new User(model.queryBean("user", new String[] { "no",
							"password" }, user));
				} catch (UnionException e) {
					throw new UnionException("工作证号或者密码错误！");
				}

			}
			utils.setCurrentUser(res);
			res.remove("password");

			JSONObject ret = new JSONObject();
			ret.put("user", res);
			writer.write(utils.ok(ret));
		} catch (JSONException e) {
			e.printStackTrace();
			writer.write(utils.err("json error"));
		} catch (UnionException e) {
			e.printStackTrace();
			writer.write(utils.err(e.ps));
		}
	}
}
