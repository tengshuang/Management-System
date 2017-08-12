package com.tsinghua.unionbackend.api.activity;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.tsinghua.unionbackend.db.beans.Activity;
import com.tsinghua.unionbackend.db.beans.User;
import com.tsinghua.unionbackend.db.model.ActivityModel;
import com.tsinghua.unionbackend.db.model.UserModel;
import com.tsinghua.unionbackend.util.UnionException;
import com.tsinghua.unionbackend.util.Utils;

/**
 * Servlet implementation class CreateActivity
 */
@WebServlet("/api/CreateActivity")
public class CreateActivity extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateActivity() {
		super();
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
			Activity activity = new Activity(param.getString("activity"));
			activity.put("creation_time", utils.getCurrentTime());
			int activity_id = activity.getInt("id");
			ActivityModel model = new ActivityModel();
			model.updateActivity(activity);
			activity = model.getActivity(activity_id);
			UserModel userModel = new UserModel();
			ArrayList<User> userLst = userModel.getUserByBelongs(activity
					.getString("belongs"));
			userModel.pushActivityToAll(activity, utils);
			writer.write(utils.ok());
		} catch (JSONException e) {
			e.printStackTrace();
			writer.write(utils.err("json error"));
		} catch (UnionException e) {
			e.printStackTrace();
			writer.write(utils.err(e.ps));
		}
	}
}
