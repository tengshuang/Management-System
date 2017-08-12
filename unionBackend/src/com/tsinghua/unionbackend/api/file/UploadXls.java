package com.tsinghua.unionbackend.api.file;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.JSONObject;

import com.tsinghua.unionbackend.db.beans.XlsUser;
import com.tsinghua.unionbackend.db.model.FileModel;
import com.tsinghua.unionbackend.util.UnionException;
import com.tsinghua.unionbackend.util.Utils;

/**
 * Servlet implementation class UploadXls
 */
@WebServlet("/api/UploadXls")
@MultipartConfig
@Deprecated
public class UploadXls extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadXls() {
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
			FileModel model = new FileModel();
			String filename = utils.getFullLocalName(part);
			model.uploadPart(part, utils);
			ArrayList<XlsUser> users = XlsUser.getXlsUserFromFile(filename);
			JSONObject obj = new JSONObject();
			writer.write(utils.ok(obj));
		} catch (UnionException e) {
			e.printStackTrace();
			writer.write(utils.err(e.ps));
		}
	}

}
