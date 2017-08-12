package com.tsinghua.unionbackend.db.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.tsinghua.unionbackend.db.beans.Event;
import com.tsinghua.unionbackend.util.UnionException;
import com.tsinghua.unionbackend.util.Utils;

public class EventModel extends Model {

	public EventModel() throws UnionException {
		super();
	}

	public List<Event> getListEvent(String field, String name, boolean strict)
			throws UnionException {
		try {
			Statement stat = con.createStatement();
			String sql = "select * from event where `" + field + "` like '%"
					+ name + "%'";
			if (strict)
				sql = "select * from event where `" + field + "` = '" + name
						+ "'";
			System.out.println(sql);
			ResultSet ret = stat.executeQuery(sql);

			List<Event> list = new ArrayList<Event>();

			while (ret.next()) {
				list.add(new Event(ret));
			}
			System.out.println(list);
			return list;
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}

	public Event getSingleEvent(String field, String name)
			throws UnionException {
		try {
			Statement stat = con.createStatement();
			String sql = "select * from event where `" + field + "` = '" + name
					+ "'";
			System.out.println(sql);
			ResultSet ret = stat.executeQuery(sql);
			if (ret.next()) {
				return new Event(ret);
			}
			return null;
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}

	public int createEvent(Event event) throws UnionException {
		try {
			// TODO no error check proceed
			String sql = event.sqlInsert("event");
			PreparedStatement stat = con.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			stat.executeUpdate();
			ResultSet res = stat.getGeneratedKeys();
			res.next();
			return res.getInt(1);
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}

	public String getEventPostContent(Event event) throws UnionException {
		try {
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", "活动名称"));
			params.add(new BasicNameValuePair("location", "活动地点"));
			params.add(new BasicNameValuePair("start_date", "开始时间"));
			params.add(new BasicNameValuePair("end_date", "截止时间"));
			params.add(new BasicNameValuePair("detail", "详情"));
			params.add(new BasicNameValuePair("comment", "备注"));
			params.add(new BasicNameValuePair("department", "部门"));
	
			String content = "";
			for (int i = 0; i < params.size(); ++i) {
				if (event.has(params.get(i).getName()) && !event.getString(params.get(i).getName()).equals("null"))
					content += params.get(i).getValue() + "："
							+ event.getString(params.get(i).getName()) + "\n";
			}
			if (event.isTrue("feedback"))
				content += "需要反馈\n";
			if (event.isTrue("activity"))
				content += "需要创建子活动\n";
			if (event.isTrue("attachment"))
				content += "有附件\n";
			return content;
		} catch(Exception e) {
			throw new UnionException(e);
		}
	}
	
	public void updateEvent(Event event) throws UnionException {
		try {
			String sql = event.sqlUpdate("event", new String[] { "id" });
			Statement stat = con.createStatement();
			stat.executeUpdate(sql);
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}

	public void wechatPostEvent(Event event, String[] target, Utils utils)
			throws UnionException {
		try {
			String content = getEventPostContent(event);
			utils.postUserList(content, target);
		} catch (Exception e) {
			throw new UnionException(e);
		}
	}
	/*
	 * // this main is only for test purpose public static void main(String[]
	 * args) { try { EventModel model = new EventModel(); Event p = new
	 * Event("{'status':1}"); // model.updateEvent(1, p); } catch (Exception e)
	 * { e.printStackTrace(); } }
	 */

}
