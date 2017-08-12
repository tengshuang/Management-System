package com.tsinghua.unionbackend.api.postTarget;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tsinghua.unionbackend.db.beans.Bean;
import com.tsinghua.unionbackend.db.beans.FileBean;
import com.tsinghua.unionbackend.db.beans.XlsUser;
import com.tsinghua.unionbackend.db.model.FileModel;
import com.tsinghua.unionbackend.db.model.Model;
import com.tsinghua.unionbackend.util.UnionException;
import com.tsinghua.unionbackend.util.Utils;

/**
 * Servlet implementation class GetPostTarget
 */
@WebServlet("/api/NewPostTarget")
public class NewPostTarget extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NewPostTarget() {
		super();
	}

	@Override
	protected void doOptions(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		new Utils().setHttpHeader(request, response);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Utils utils = new Utils(request, response);
		utils.testMode();
		PrintWriter writer = response.getWriter();
		try {
			utils.checkPerm();
			JSONObject param = utils.getJsonFromRequest(request);
			String listname = param.getString("listname");
			int fileId = param.getInt("fileId");
			FileModel fileModel = new FileModel();
			FileBean fileBean = fileModel.getFile(fileId);
			File file = utils.urlToFile(fileBean.getString("url"));
			List<XlsUser> userLst = XlsUser.getXlsUserFromFile(file
					.getAbsolutePath());
			JSONObject ret = new JSONObject();
			JSONArray tmp = new JSONArray();
			for (XlsUser user : userLst) {
				String no = user.get("工作证号").toString();
				try {
					fileModel.queryBean("user", "no", no);
				} catch (UnionException e) {
					throw new UnionException(e, "不存在的工作证号：" + no);
				}
				tmp.put(no);
			}
			Bean target = new Bean();
			target.put("listname", listname);
			target.put("content", tmp.toString());
			Model model = new Model();
			int id = model.insertBean("target", target);
			ret.put("id", id);
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
