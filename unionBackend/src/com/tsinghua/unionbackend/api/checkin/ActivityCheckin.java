package com.tsinghua.unionbackend.api.checkin;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.tsinghua.unionbackend.db.beans.Bean;
import com.tsinghua.unionbackend.db.model.Model;
import com.tsinghua.unionbackend.util.UnionException;
import com.tsinghua.unionbackend.util.Utils;

/**
 * Servlet implementation class ActivityCheckin
 */
@WebServlet("/api/ActivityCheckin")
public class ActivityCheckin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ActivityCheckin() {
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
			// JSONObject param = utils.getJsonFromRequest(request);
			String ticket = request.getParameter("ticket");
			String[] content = request.getParameter("content").split(":");
			String id = content[1];
			String no = utils.getNoByTicket(ticket);
			Model model = new Model();
			Bean user = model.queryBean("user", "no", no);
			String participant_id = user.getString("id");
			if (content[0].equals("activity")) {
				Bean bean = new Bean();
				bean.put("activity_id", id);
				bean.put("participant_id", participant_id);
				bean.put("checkin_stat", 1);
				model.insertBean("participant", bean);
				writer.write("签到成功！");
			} else if (content[0].equals("event")) {
				Bean bean = new Bean();
				bean.put("event_id", id);
				bean.put("participant_id", participant_id);
				bean.put("checkin_stat", 1);
				model.insertBean("participant", bean);
				writer.write("签到成功！");
			}
		} catch (JSONException e) {
			e.printStackTrace();
			writer.write("签到失败……");
		} catch (UnionException e) {
			e.printStackTrace();
			writer.write("签到失败……");
		}
	}
}
