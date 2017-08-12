package com.tsinghua.unionbackend.db.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.JSONException;

import com.tsinghua.unionbackend.db.beans.Activity;
import com.tsinghua.unionbackend.db.beans.User;
import com.tsinghua.unionbackend.util.UnionException;

public class ActivityModel extends Model {

	public ActivityModel() throws UnionException {
		super();
	}

	public ArrayList<Activity> getMyActivity(int userId) throws UnionException {
		try {
			Statement stat = con.createStatement();
			ArrayList<Activity> ret = new ArrayList<Activity>();
			String sql = "select * from participant "
					+ "join activity on participant.activity_id=activity.id "
					+ "where participant_id = %d";
			sql = String.format(sql, userId);
			ResultSet res = stat.executeQuery(sql);
			while (res.next())
				ret.add(new Activity(res));
			return ret;
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}

	String[] timColumns = new String[] { "is_reply", "reply_file",
			"reply_content", "photo_file", "report_file" };

	public void addTime(Activity reply) {
		try {
			for (int i = 0; i < timColumns.length; ++i) {
				if (reply.has(timColumns[i]))
					reply.put(timColumns[i] + "_time", utils.getCurrentTime());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void updateActivity(Activity activity) throws UnionException {
		try {
			String sql = activity.sqlUpdate("activity", new String[] { "id" });
			Statement stat = con.createStatement();
			stat.executeUpdate(sql);
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}

	public void addAuth(int activity_id, int authority_id) {
		try {
			String sql = "INSERT INTO activity_auth (activity_id, authority_id) VALUES (%d, %d)";
			sql = String.format(sql, activity_id, authority_id);
			PreparedStatement stat = con.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			stat.executeUpdate();
			ResultSet res = stat.getGeneratedKeys();
			res.next();
			//return res.getInt(1);
		} catch (SQLException e) {
			//throw new UnionException(e);
			e.printStackTrace();
		}
	}

	public int insertActivity(int eventId, User user) throws UnionException {
		try {
			String sql = "INSERT INTO activity (event_id, user_id, belongs) VALUES (?, ?, ?)";
			PreparedStatement stat = con.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			stat.setInt(1, eventId);
			stat.setInt(2, user.getInt("id"));
			stat.setString(3, user.getString("belongs"));
			stat.execute();
			ResultSet res = stat.getGeneratedKeys();
			res.next();
			int activity_id = res.getInt(1);
			sql = "INSERT INTO activity_auth (activity_id, authority_id) VALUES ("
					+ activity_id + ", " + user.getInt("id") + ")";
			stat = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stat.execute();
			return activity_id;
		} catch (SQLException e) {
			throw new UnionException(e);
		} catch (JSONException e) {
			throw new UnionException(e);
		}
	}

	public Activity getActivity(int activity_id) throws UnionException {
		try {
			String sql = "SELECT * FROM activity WHERE id = " + activity_id;
			Statement stat = con.createStatement();
			ResultSet res = stat.executeQuery(sql);
			res.next();
			return new Activity(res);
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}

	public ArrayList<Activity> getActivity(String key, String eventId)
			throws UnionException {
		try {
			String sql = "SELECT activity.*, user.realname FROM activity JOIN user ON activity.user_id=user.id WHERE "
					+ key + " = " + eventId;
			Statement stat = con.createStatement();
			ResultSet res = stat.executeQuery(sql);
			ArrayList<Activity> ret = new ArrayList<Activity>();
			while (res.next())
				ret.add(new Activity(res));
			return ret;
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}

	public Activity getActivityAndEvent(int activityId) throws UnionException {
		try {
			String sql = "select * FROM activity "
					+ "JOIN event ON activity.event_id=event.id "
					+ "WHERE activity.id = %d";
			sql = String.format(sql, activityId);
			Statement stat = con.createStatement();
			ResultSet res = stat.executeQuery(sql);
			res.next();
			return new Activity(res, true);
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}

	public ArrayList<Activity> getRecvActivity(int userId)
			throws UnionException {
		try {
			Statement stat = con.createStatement();
			ArrayList<Activity> ret = new ArrayList<Activity>();
			String sql;
			sql = "select event.*,activity.id FROM activity "
					+ "JOIN event ON activity.event_id=event.id "
					+ "WHERE activity.id IN (SELECT activity_id FROM activity_auth WHERE authority_id = %d)";
			sql = String.format(sql, userId);
			ResultSet res = stat.executeQuery(sql);
			while (res.next()) {
				Activity tmp = new Activity(res, true);
				Activity activity = new Activity(tmp.getJSONObject("event")
						.toString());
				activity.put("activity_id", tmp.getJSONObject("activity")
						.getInt("id"));
				ret.add(activity);
			}
			return ret;
		} catch (SQLException e) {
			throw new UnionException(e);
		} catch (JSONException e) {
			throw new UnionException(e);
		}
	}

	public void setState(int event_id) throws UnionException {
		try {
			String sql = "UPDATE activity SET status = 1 WHERE event_id = "
					+ event_id;
			Statement stat = con.createStatement();
			stat.executeUpdate(sql);
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}

	public void setFile(int activity_id, String key, int file_id)
			throws UnionException {
		try {
			String ins = "";
			for (int i = 0; i < timColumns.length; ++i) {
				if (key.equals(timColumns[i]))
					ins = ", " + timColumns[i] + "_time = '"
							+ utils.getCurrentTime() + "' ";
			}
			String sql = "UPDATE activity SET %s = %d " + ins
					+ " WHERE id = %d";
			sql = String.format(sql, key, file_id, activity_id);
			Statement stat = con.createStatement();
			stat.executeUpdate(sql);
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}

	public static void main(String[] s) {

	}

	public ArrayList<Activity> getCreatedActivity(int user_id)
			throws UnionException {
		try {
			ArrayList<Activity> ret = new ArrayList<Activity>();
			Statement stat = con.createStatement();
			String sql = "select * FROM activity "
					+ "JOIN event ON activity.event_id=event.id "
					+ "WHERE user_id = %d and activity_created=1";
			sql = String.format(sql, user_id);
			ResultSet res = stat.executeQuery(sql);
			while (res.next())
				ret.add(new Activity(res, true));
			return ret;
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}

	public void endActivity(int activity_id) throws UnionException {
		try {
			String sql = "UPDATE activity SET status = 1 WHERE id = "
					+ activity_id;
			Statement stat = con.createStatement();
			stat.executeUpdate(sql);
		} catch (SQLException e) {
			throw new UnionException(e);
		}

	}

	/*
	public Activity getActivityByEventUser(String event_id, String no)
			throws UnionException {
		try {
			String sql = "SELECT * FROM activity WHERE id IN "
					+ "(SELECT activity_id FROM activity_auth WHERE authority_id = %s)"
					+ " AND event_id = %s";
			sql = String.format(sql, no, event_id);
			Statement stat = con.createStatement();
			ResultSet res = stat.executeQuery(sql);
			if (!res.next())
				throw new UnionException("无法签到：你不属于这个活动，或者没有这个活动！");
			return new Activity(res);
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}
	*/
}
