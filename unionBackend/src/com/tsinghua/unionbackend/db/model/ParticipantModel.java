package com.tsinghua.unionbackend.db.model;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.tsinghua.unionbackend.util.UnionException;

public class ParticipantModel extends Model {

	public ParticipantModel() throws UnionException {
		super();
	}

	public String addParticipants(int activity,
			ArrayList<Integer> participantList) {
		try {
			Statement stat = con.createStatement();
			String sql = "";
			for (int i = 0; i < participantList.size(); ++i) {
				sql = String
						.format("INSERT INTO participant (activity_id, participant_id, checkin_stat) VALUES (%d,%d,'no show')",
								activity, participantList.get(i));
				stat.executeUpdate(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return utils.err("sql error");
		}
		return utils.ok();
	}

	public String addParticipant(int activity, int participant) {
		try {
			Statement stat = con.createStatement();
			String sql = String
					.format("INSERT INTO participant (activity_id, participant_id, checkin_stat) VALUES (%d,%d,'no show')",
							activity, participant);
			stat.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return utils.err("sql error");
		}
		return utils.ok();
	}

	public String removeParticipant(int activity, int participant) {
		try {
			Statement stat = con.createStatement();
			String sql = String
					.format("DELETE FROM participant WHERE activity_id=%d and participant_id=%d",
							activity, participant);
			stat.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return utils.err("sql error");
		}
		return utils.ok();
	}

	public static void main(String[] args) {
		try {
			ParticipantModel model = new ParticipantModel();
			ArrayList<Integer> test = new ArrayList<Integer>();
			test.add(1);
			test.add(3);
			test.add(2);
			System.out.println(model.addParticipants(1234, test));
			System.out.println(model.addParticipant(1234, 4));
			System.out.println(model.removeParticipant(1234, 3));
		} catch (Exception e) {

		}
	}
}
