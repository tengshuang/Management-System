package com.tsinghua.unionbackend.api.message;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.tsinghua.unionbackend.db.beans.Message;
import com.tsinghua.unionbackend.db.model.MessageModel;
import com.tsinghua.unionbackend.util.UnionException;
import com.tsinghua.unionbackend.util.Utils;

/**
 * Servlet implementation class NewMessage
 */
@WebServlet("/api/NewMessage")
public class NewMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public NewMessage() {
		super();
	}

	@Override
	protected void doOptions(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		new Utils().setHttpHeader(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Utils utils = new Utils(request, response);
		utils.testMode();
		PrintWriter writer = response.getWriter();
		try {
			JSONObject param = utils.getJsonFromRequest(request);
			Message msg = new Message(param.getString("message"));
			MessageModel model = new MessageModel();
			Message message = model.newMessage(msg);
			JSONObject obj = new JSONObject();
			obj.put("message", message);
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
