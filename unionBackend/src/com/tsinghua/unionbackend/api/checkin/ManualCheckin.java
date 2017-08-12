package com.tsinghua.unionbackend.api.checkin;

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
import com.tsinghua.unionbackend.db.beans.User;
import com.tsinghua.unionbackend.db.model.UserModel;
import com.tsinghua.unionbackend.util.UnionException;
import com.tsinghua.unionbackend.util.Utils;

/**
 * Servlet implementation class ManualCheckin
 */
@WebServlet("/api/ManualCheckin")
public class ManualCheckin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ManualCheckin() {
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
			int activity_id = param.getInt("activity_id");
			JSONArray participants = param.getJSONArray("participants");
			UserModel model = new UserModel();
			for (int i = 0; i < participants.length(); ++i) {
				Bean bean = new Bean();
				bean.put("activity_id", activity_id);
				bean.put("participant_id", participants.get(i));
				bean.put("checkin_stat", 2);
				model.insertBean("participant", bean);
			}
			List<User> lst = model
					.getParticipants("activity_id=" + activity_id);
			JSONObject ret = new JSONObject();
			ret.put("user", lst);
			writer.write(utils.ok(ret));
		} catch (JSONException e) {
			e.printStackTrace();
			writer.write(utils.err("json error"));
		} catch (UnionException e) {
			e.printStackTrace();
			writer.write(utils.err("签到失败，不能重复签到！"));
		}
	}
}
