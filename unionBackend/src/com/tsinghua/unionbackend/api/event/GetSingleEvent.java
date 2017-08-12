package com.tsinghua.unionbackend.api.event;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.tsinghua.unionbackend.db.beans.Event;
import com.tsinghua.unionbackend.db.model.EventModel;
import com.tsinghua.unionbackend.util.Utils;

/**
 * Servlet implementation class GetSingleEvent
 */
@WebServlet("/api/GetSingleEvent")
public class GetSingleEvent extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetSingleEvent() {
		super();
		// TODO Auto-generated constructor stub
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
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Utils utils = new Utils(request, response);
			utils.testMode();
			PrintWriter writer = response.getWriter();

			EventModel eventModel = new EventModel();
			String event_id = request.getParameter("event_id");
			System.out.println("GetSingleEvent: event_id " + event_id);

			Event res = null;
			if (event_id != null) {
				res = eventModel.getSingleEvent("id", event_id);
				if (res != null) {
					JSONObject result = new JSONObject();
					result.put("eventSingle", res.toString());
					writer.write(utils.ok(result));
					return;
				}
			}
			writer.write(utils.err("Not found"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
