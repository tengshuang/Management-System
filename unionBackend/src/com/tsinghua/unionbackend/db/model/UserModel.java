package com.tsinghua.unionbackend.db.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import com.tsinghua.unionbackend.db.beans.Activity;
import com.tsinghua.unionbackend.db.beans.User;
import com.tsinghua.unionbackend.util.UnionException;
import com.tsinghua.unionbackend.util.Utils;

public class UserModel extends Model {

	public UserModel() throws UnionException {
		super();
	}

	public ArrayList<User> getUserBlur(String search) throws UnionException {
		try {
			Statement stat = con.createStatement();
			String sql = "select id, no, belongs, realname from user where no like '%"
					+ search + "%' OR realname like '%" + search + "%'";
			ArrayList<User> ret = new ArrayList<User>();
			ResultSet res = stat.executeQuery(sql);
			while (res.next()) {
				ret.add(new User(res));
			}
			return ret;
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}

	public ArrayList<User> getUserByPermission(JSONArray perm)
			throws UnionException {
		try {
			ArrayList<User> ret = new ArrayList<User>();
			String sql = "SELECT id,belongs FROM user WHERE ";
			for (int i = 0; i < perm.length(); ++i) {
				if (i > 0)
					sql += "OR ";
				sql += perm.getString(i) + "=1 ";
			}
			Statement stat = con.createStatement();
			ResultSet res = stat.executeQuery(sql);
			while (res.next())
				ret.add(new User(res));
			return ret;
		} catch (SQLException e) {
			throw new UnionException(e);
		} catch (JSONException e) {
			throw new UnionException(e);
		}
	}

	public ArrayList<User> getUserByBelongs(String belongs)
			throws UnionException {
		try {
			ArrayList<User> ret = new ArrayList<User>();
			String sql = "SELECT * FROM user WHERE belongs = '%s'";
			sql = String.format(sql, belongs);
			Statement stat = con.createStatement();
			ResultSet res = stat.executeQuery(sql);
			while (res.next())
				ret.add(new User(res));
			return ret;
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}

	public int insertUser(User user) throws UnionException {
		try {
			String sql = user.sqlInsert("user");
			PreparedStatement stat = con.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			stat.executeUpdate();
			ResultSet res = stat.getGeneratedKeys();
			res.next();
			int id = res.getInt(1);
			return id;
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}

	public List<User> getParticipants(String activity_id) throws UnionException {
		try {
			String sql = "SELECT user.no, user.realname, user.belongs, participant.checkin_stat "
					+ "FROM participant JOIN user ON participant.participant_id=user.id WHERE participant."
					+ activity_id;
			sql = String.format(sql, activity_id);
			Statement stat = con.createStatement();
			ResultSet res = stat.executeQuery(sql);
			ArrayList<User> ret = new ArrayList<User>();
			while (res.next()) {
				User user = new User(res);
				String checkin_type = "";
				switch (user.getInt("checkin_stat")) {
				case 1:
					checkin_type = "二维码";
					break;
				case 2:
					checkin_type = "手动签到";
					break;
				}
				user.put("checkin_type", checkin_type);
				ret.add(user);
			}
			return ret;
		} catch (SQLException e) {
			throw new UnionException(e);
		} catch (JSONException e) {
			throw new UnionException(e);
		}
	}

	public String getPostContent(Activity activity) throws UnionException {
		try {
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", "活动名称"));
			params.add(new BasicNameValuePair("location", "活动地点"));
			params.add(new BasicNameValuePair("tim", "活动时间"));
			params.add(new BasicNameValuePair("belongs", "所属分工会"));
			params.add(new BasicNameValuePair("detail", "详情"));
			params.add(new BasicNameValuePair("comment", "备注"));
			String content = "";
			for (int i = 0; i < params.size(); ++i) {
				if (activity.has(params.get(i).getName())
						&& !activity.getString(params.get(i).getName()).equals(
								"null"))
					content += params.get(i).getValue() + "："
							+ activity.getString(params.get(i).getName())
							+ "\n";
			}
			return content;
		}
		catch(Exception e) {
			throw new UnionException(e);
		}
	}
	
	public void pushActivityToAll(Activity activity, Utils utils)
			throws UnionException {
		try {
			String content = getPostContent(activity);
			utils.postAll(content);
		} catch (Exception e) {
			throw new UnionException(e);
		}
	}

	public static void main(String[] args) {
		try {
			Utils utils = new Utils();
			ActivityModel model = new ActivityModel();
			Activity activity = new Activity(model.queryBean("activity", "id",
					"1"));
			UserModel userModel = new UserModel();
			userModel.pushActivityToAll(activity, utils);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
