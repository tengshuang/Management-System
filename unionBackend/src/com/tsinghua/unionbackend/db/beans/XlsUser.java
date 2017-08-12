package com.tsinghua.unionbackend.db.beans;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONException;

public class XlsUser extends Bean {

	public static ArrayList<XlsUser> getXlsUserFromFile(String xls) {
		ArrayList<XlsUser> ret = new ArrayList<XlsUser>();
		try {
			DataFormatter df = new DataFormatter();
			InputStream inp = new FileInputStream(xls);
			Workbook wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheetAt(0);
			ArrayList<String> th = new ArrayList<String>();
			Row row;
			row = sheet.getRow(0);
			Cell cell;
			for (int i = 0; (cell = row.getCell(i)) != null; ++i)
				th.add(df.formatCellValue(cell));
			for (int x = 1; (row = sheet.getRow(x)) != null; ++x) {
				XlsUser p = new XlsUser();
				for (int y = 0; y < th.size(); ++y) {
					if ((cell = row.getCell(y)) == null)
						continue;
					p.put(th.get(y), df.formatCellValue(cell));
				}
				ret.add(p);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/*
	 * public static void main(String[] s) {
	 * XlsUser.getXlsUserFromFile("������1.xlsx"); }
	 */
}
