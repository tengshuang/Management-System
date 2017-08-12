package com.tsinghua.unionbackend.api.event;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.tsinghua.unionbackend.db.beans.Event;
import com.tsinghua.unionbackend.db.model.EventModel;
import com.tsinghua.unionbackend.util.Utils;

@WebServlet("/api/GetListEvent")
public class GetListEvent extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GetListEvent() {
		super();
	}

	@Override
	protected void doOptions(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		new Utils().setHttpHeader(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Utils utils = new Utils(request, response);
			utils.testMode();
			PrintWriter writer = response.getWriter();

			EventModel eventModel = new EventModel();
			String name = request.getParameter("name");
			System.out.println("GetEvent: name " + name);
			String creator = request.getParameter("creator");
			System.out.println("GetEvent: creator " + creator);
			List<Event> res = null;
			if (name != null)
				res = eventModel.getListEvent("name", name, false);
			else if (creator != null)
				res = eventModel.getListEvent("creator", creator, true);
			if (res != null) {
				JSONObject result = new JSONObject();
				result.put("eventList", res.toString());
				writer.write(utils.ok(result));
			} else
				writer.write(utils.err("Not found"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
