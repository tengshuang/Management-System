package com.tsinghua.unionbackend.db.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.tsinghua.unionbackend.db.beans.Message;
import com.tsinghua.unionbackend.util.UnionException;

public class MessageModel extends Model {

	public MessageModel() throws UnionException {
		super();
	}

	public Message newMessage(Message msg) throws UnionException {
		try {
			String sql = msg.sqlInsert("message");
			PreparedStatement stat = con.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			stat.executeUpdate();
			ResultSet res = stat.getGeneratedKeys();
			res.next();
			int id = res.getInt(1);
			Statement stmt = con.createStatement();
			res = stmt
					.executeQuery("SELECT message.*, user.realname FROM message "
							+ "JOIN user ON message.user_id = user.id "
							+ "WHERE message.id=" + id);
			res.next();
			return new Message(res);
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}

	public void modifyMessage(int id, String content, String attachment)
			throws UnionException {
		try {
			String sql = "UPDATE message " + " SET content=?, attachment=?"
					+ " WHERE id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, content);
			stmt.setString(2, attachment);
			stmt.setInt(3, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}

	public ArrayList<Message> getMessage(int event_id) throws UnionException {
		try {
			ArrayList<Message> ret = new ArrayList<Message>();
			Statement stat = con.createStatement();
			String sql = "SELECT message.*, user.realname FROM message "
					+ "JOIN user ON message.user_id = user.id "
					+ "WHERE event_id=" + event_id + " ORDER BY time DESC";
			ResultSet res = stat.executeQuery(sql);
			while (res.next()) {
				ret.add(new Message(res));
			}
			return ret;
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}

}
