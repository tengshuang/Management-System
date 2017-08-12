package com.tsinghua.unionbackend.api.event;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tsinghua.unionbackend.db.beans.Event;
import com.tsinghua.unionbackend.db.beans.User;
import com.tsinghua.unionbackend.db.model.ActivityModel;
import com.tsinghua.unionbackend.db.model.EventModel;
import com.tsinghua.unionbackend.db.model.UserModel;
import com.tsinghua.unionbackend.util.UnionException;
import com.tsinghua.unionbackend.util.Utils;

/**
 * Servlet implementation class CreateEvent
 */
@WebServlet("/api/CreateEvent")
public class CreateEvent extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateEvent() {
		super();
	}

	@Override
	protected void doOptions(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		new Utils().setHttpHeader(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Utils utils = new Utils(request, response);
		utils.testMode();
		PrintWriter writer = response.getWriter();
		try {
			JSONObject param = utils.getJsonFromRequest(request);
			ActivityModel activityModel = new ActivityModel();
			EventModel eventModel = new EventModel();
			UserModel userModel = new UserModel();
			// the "event" parameter should be a JSON string
			System.out.println(param);
			Event event = new Event(param.getString("event"));
			event.put("creator", 1);
			int eventId = eventModel.createEvent(event);
			/*
			 * JSONArray postTarget = param.getJSONArray("postTarget");
			 * ArrayList<User> targetList = userModel
			 * .getUserByPermission(postTarget);
			 */
			JSONArray postTargetList = new JSONArray(
					param.getString("postTarget"));
			Set<String> target = new HashSet<String>();
			for (int i = 0; i < postTargetList.length(); ++i) {
				String targetListArr = userModel.queryBean("target", "id",
						postTargetList.getString(i)).getString("content");
				JSONArray targetList = new JSONArray(targetListArr);
				for (int j = 0; j < targetList.length(); ++j) {
					target.add(targetList.getString(j));
				}
			}
			for (String no : target) {
				User user = new User(userModel.queryBean("user", "no", no));
				activityModel.insertActivity(eventId, user);
			}
			eventModel.wechatPostEvent(event,
					target.toArray(new String[target.size()]), utils);
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
