package com.tsinghua.unionbackend.api.checkin;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;

import com.tsinghua.unionbackend.db.beans.User;
import com.tsinghua.unionbackend.db.model.FileModel;
import com.tsinghua.unionbackend.db.model.UserModel;
import com.tsinghua.unionbackend.util.UnionException;
import com.tsinghua.unionbackend.util.Utils;

/**
 * Servlet implementation class GenerateExcel
 */
@WebServlet("/api/GenerateExcel")
public class GenerateExcel extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GenerateExcel() {
		super();
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
		Utils utils = new Utils(request, response);
		utils.testMode();
		PrintWriter writer = response.getWriter();
		try {
			String activity_id = request.getParameter("activity_id");
			String event_id = request.getParameter("event_id");
			UserModel model = new UserModel();
			List<User> lst;
			if (activity_id != null)
				lst = model.getParticipants("activity_id=" + activity_id);
			else
				lst = model.getParticipants("event_id=" + event_id);
			FileModel fileModel = new FileModel();
			JSONObject fileParam = fileModel.allocateFile("participant.xlsx",
					utils);

			XSSFWorkbook wb = new XSSFWorkbook();
			Sheet sheet = wb.createSheet();
			Row row = sheet.createRow(0);
			Cell cell = row.createCell(0);
			cell.setCellValue("姓名");
			cell = row.createCell(1);
			cell.setCellValue("工作证号");
			cell = row.createCell(2);
			cell.setCellValue("部门");
			cell = row.createCell(3);
			cell.setCellValue("签到方式");
			for (int i = 0; i < lst.size(); ++i) {
				row = sheet.createRow(i + 1);
				cell = row.createCell(0);
				cell.setCellValue(lst.get(i).getString("realname"));
				cell = row.createCell(1);
				cell.setCellValue(lst.get(i).getString("no"));
				cell = row.createCell(2);
				cell.setCellValue(lst.get(i).getString("belongs"));
				cell = row.createCell(3);
				cell.setCellValue(lst.get(i).getString("checkin_type"));
			}
			FileOutputStream fileOut = new FileOutputStream(
					fileParam.getString("path"));
			wb.write(fileOut);
			fileOut.close();
			wb.close();

			writer.write(utils.ok(fileParam));
		} catch (JSONException e) {
			e.printStackTrace();
			writer.write(utils.err("json error"));
		} catch (UnionException e) {
			e.printStackTrace();
			writer.write(utils.err(e.ps));
		}
	}

	public static void main(String[] args) {
		try {
			XSSFWorkbook wb = new XSSFWorkbook();
			Sheet sheet = wb.createSheet();
			Row row = sheet.createRow(0);
			Cell cell = row.createCell(0);
			cell.setCellValue(100);
			FileOutputStream fileOut = new FileOutputStream("d:/test.xlsx");
			wb.write(fileOut);
			fileOut.close();
			wb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
