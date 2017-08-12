package com.tsinghua.unionbackend.api.message;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.tsinghua.unionbackend.db.model.MessageModel;
import com.tsinghua.unionbackend.util.Utils;

/**
 * Servlet implementation class UpdateMessage
 */
@WebServlet("/api/UpdateMessage")
@MultipartConfig
public class UpdateMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public UpdateMessage() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected void doOptions(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		new Utils().setHttpHeader(request, response);
	}

	// TODO not implemented
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		Utils utils = new Utils(request, response);
		String ret = "";
		
		int id = Integer.parseInt(request.getParameter("id"));
		String content = request.getParameter("content");
		Part filePart = request.getPart("attachment");
		String filePath = "";
		if(filePart != null)
		{
			String filename = utils.getSubmittedFileName(filePart);
			String destName = utils.appendTimestamp(filename);
			filePath = utils.attachmentPathRelative + destName;
			utils.partToFile(filePart, utils.path + filePath);
		}
		try {
			MessageModel msgModel = new MessageModel();
			msgModel.modifyMessage(id, content, filePath);
		} catch(SQLException e) {
			ret = utils.err("SQL ERROR");
			writer.write(ret);
			return;
		}
		writer.write(utils.ok());
		*/
	}

}
