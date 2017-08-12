package com.tsinghua.unionbackend.api.event;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.tsinghua.unionbackend.db.beans.Event;
import com.tsinghua.unionbackend.db.model.ActivityModel;
import com.tsinghua.unionbackend.db.model.EventModel;
import com.tsinghua.unionbackend.util.UnionException;
import com.tsinghua.unionbackend.util.Utils;

/**
 * Servlet implementation class UpdateEvent
 */
@WebServlet("/api/UpdateEvent")
public class UpdateEvent extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateEvent() {
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
			EventModel model = new EventModel();
			ActivityModel activityModel = new ActivityModel();
			Event event = new Event(param.getString("option"));
			if (event.getString("status").equals("1")) {
				activityModel.setState(event.getInt("id"));
			}
			model.updateEvent(event);
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
