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

import com.tsinghua.unionbackend.db.model.ParticipantModel;
import com.tsinghua.unionbackend.util.UnionException;
import com.tsinghua.unionbackend.util.Utils;

/**
 * Servlet implementation class AddPariticipantList
 */
@WebServlet("/AddPariticipantList")
@Deprecated
public class AddPariticipantList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddPariticipantList() {
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
			JSONArray jlst = param.getJSONArray("participant_list");
			ArrayList<Integer> lst = new ArrayList<Integer>();
			for (int i = 0; i < jlst.length(); ++i)
				lst.add(jlst.getInt(i));

			ParticipantModel partModel = new ParticipantModel();
			partModel.addParticipants(activity, lst);
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
