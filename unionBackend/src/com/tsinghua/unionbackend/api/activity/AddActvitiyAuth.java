package com.tsinghua.unionbackend.api.activity;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tsinghua.unionbackend.db.beans.Activity;
import com.tsinghua.unionbackend.db.beans.Bean;
import com.tsinghua.unionbackend.db.beans.Event;
import com.tsinghua.unionbackend.db.model.ActivityModel;
import com.tsinghua.unionbackend.db.model.EventModel;
import com.tsinghua.unionbackend.db.model.UserModel;
import com.tsinghua.unionbackend.util.UnionException;
import com.tsinghua.unionbackend.util.Utils;

/**
 * Servlet implementation class AddActvitiyAuth
 */
@WebServlet("/api/AddActivityAuth")
public class AddActvitiyAuth extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddActvitiyAuth() {
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
			int activity = param.getInt("activity_id");
			JSONArray authList = param.getJSONArray("authority_id");
			ActivityModel model = new ActivityModel();
			// int id = model.addAuth(activity, auth);
			for (int i = 0; i < authList.length(); ++i)
				model.addAuth(activity, authList.getInt(i));
			UserModel userModel = new UserModel();
			Activity activityBean = model.getActivity(activity);
			EventModel eventModel = new EventModel();
			Event event = eventModel.getSingleEvent("id", activityBean.getString("event_id"));
			String content = eventModel.getEventPostContent(event) + "您已被添加为授权人\n";
			ArrayList<String> userList = new ArrayList<String>();
			for(int i = 0; i < authList.length(); ++i) {
				Bean bean = userModel.queryBean("user", "id", authList.getString(i));
				String no = bean.getString("no");
				userList.add(no);
			}
			utils.postUserList(content, userList.toArray(new String[userList.size()]));
			JSONObject obj = new JSONObject();
			// obj.put("activity_auth_id", id);
			writer.write(utils.ok(obj));
		} catch (JSONException e) {
			e.printStackTrace();
			writer.write(utils.err("json error"));
		} catch (UnionException e) {
			e.printStackTrace();
			writer.write(utils.err(e.ps));
		}
	}

}
