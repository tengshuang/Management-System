package com.tsinghua.unionbackend.api.file;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.JSONException;
import org.json.JSONObject;

import com.tsinghua.unionbackend.db.model.ActivityModel;
import com.tsinghua.unionbackend.db.model.FileModel;
import com.tsinghua.unionbackend.util.UnionException;
import com.tsinghua.unionbackend.util.Utils;

/**
 * Servlet implementation class UploadFile
 */
@WebServlet("/api/UploadFile")
@MultipartConfig
public class UploadFile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadFile() {
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
			Part part = request.getPart("file");
			String table = request.getParameter("table");
			String key = request.getParameter("key");
			String sid = request.getParameter("id");
			System.out.println(sid);
			FileModel model = new FileModel();
			int file_id = model.uploadPart(part, utils);
			if (table != null && key != null && sid != null) {
				int id = Integer.parseInt(sid);
				if (table.equals("activity")) {
					ActivityModel activityModel = new ActivityModel();
					activityModel.setFile(id, key, file_id);
				}
			}
			JSONObject obj = new JSONObject();
			obj.put("file_id", file_id);
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
