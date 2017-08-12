package com.tsinghua.unionbackend.api.activity;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.tsinghua.unionbackend.db.beans.Activity;
import com.tsinghua.unionbackend.db.beans.Bean;
import com.tsinghua.unionbackend.db.beans.Event;
import com.tsinghua.unionbackend.db.model.ActivityModel;
import com.tsinghua.unionbackend.db.model.EventModel;
import com.tsinghua.unionbackend.util.UnionException;
import com.tsinghua.unionbackend.util.Utils;

/**
 * Servlet implementation class RenoticeActivity
 */
@WebServlet("/api/RenoticeActivity")
public class RenoticeActivity extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RenoticeActivity() {
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
			int activity_id = param.getInt("activity_id");
			EventModel eventModel = new EventModel();
			ActivityModel activityModel = new ActivityModel();
			Activity activity = activityModel.getActivity(activity_id);
			Bean user = eventModel.queryBean("user", "id",
					activity.getString("user_id"));
			Event event = eventModel.getSingleEvent("id",
					activity.getString("event_id"));
			eventModel.wechatPostEvent(event,
					new String[] { user.getString("no") }, utils);
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
