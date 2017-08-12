package com.tsinghua.unionbackend.api.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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
 * Servlet implementation class GetUserPubKey
 */
@WebServlet("/api/SearchUser")
public class SearchUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchUser() {
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
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Utils utils = new Utils(request, response);
		utils.testMode();
		PrintWriter writer = response.getWriter();
		try {
			// utils.checkPerm();
			String search = request.getParameter("search");
			User user = new User();
			user.put("no", search);
			user.put("realname", search);
			UserModel model = new UserModel();
			List<User> users = model.getUserBlur(search);
			JSONObject ret = new JSONObject();
			ret.put("users", users);
			writer.write(utils.ok(ret));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnionException e) {
			e.deb();
		}
	}
}
