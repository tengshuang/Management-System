package com.tsinghua.unionbackend.db.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.tsinghua.unionbackend.db.beans.Bean;
import com.tsinghua.unionbackend.util.UnionException;
import com.tsinghua.unionbackend.util.Utils;

public class Model {

	static Connection con = null;

	Utils utils;

	public Model() throws UnionException {
		utils = new Utils();
		if (con == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/union_manage", "root",
						"admin");
			} catch (SQLException e) {
				try {
					con = DriverManager.getConnection(
							"jdbc:mysql://localhost:3306/union_manage", "root",
							"");
				} catch (SQLException err) {
					throw new UnionException(err, "无法连接数据库！");
				}
			} catch (ClassNotFoundException e) {
				throw new UnionException("无法连接数据库！");
			}
		}
	}

	public void close() {
		try {
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Bean queryBean(String table, String column, String s)
			throws UnionException {
		try {
			JSONObject tmp = new JSONObject();
			tmp.put(column, s);
			return queryBean(table, new String[] { column }, tmp);
		} catch (JSONException e) {
			throw new UnionException(e);
		}
	}

	public Bean queryBean(String table, String[] column, JSONObject p)
			throws UnionException {
		List<Bean> res = queryBeans(table, column, p);
		if (res.size() == 0)
			throw new UnionException("没有符合条件的数据。");
		return res.get(0);
	}

	public List<Bean> queryBeans(String table, String[] column, JSONObject p)
			throws UnionException {
		return queryBeans(table, "*", column, p);
	}

	public List<Bean> queryBeans(String table, String resultColumns)
			throws UnionException {
		return queryBeans(table, resultColumns, null, null);
	}

	public List<Bean> queryBeans(String table, String resultColumns,
			String[] column, JSONObject p) throws UnionException {
		try {
			List<Bean> ret = new ArrayList<Bean>();
			String sql = "SELECT " + resultColumns + " FROM " + table;
			if (column != null) {
				sql += " WHERE ";
				for (int i = 0; i < column.length; ++i) {
					if (p.getString(column[i]) == null)
						throw new UnionException("请求参数不足，请联系开发者。");
					if (i > 0)
						sql += "AND ";
					sql += column[i] + " = ? ";
				}
			}
			PreparedStatement stat = con.prepareStatement(sql);
			if (column != null)
				for (int i = 0; i < column.length; ++i)
					stat.setString(i + 1, p.getString(column[i]));
			ResultSet res = stat.executeQuery();
			while (res.next())
				ret.add(new Bean(res));
			return ret;
		} catch (JSONException e) {
			throw new UnionException(e);
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}

	public void updateBean(String table, String column, Bean p)
			throws UnionException {
		updateBean(table, new String[] { column }, p);
	}

	public void updateBean(String table, String[] column, Bean p)
			throws UnionException {
		try {
			String sql = p.sqlUpdate(table, column);
			Statement stat = con.createStatement();
			stat.executeUpdate(sql);
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}

	public List<Integer> insertBeans(String table, List<Bean> lst)
			throws UnionException {
		try {
			List<Integer> ret = new ArrayList<Integer>();
			for (Bean p : lst) {
				String sql = p.sqlInsert(table);
				PreparedStatement stat = con.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);
				stat.executeUpdate();
				ResultSet res = stat.getGeneratedKeys();
				res.next();
				ret.add(res.getInt(1));
			}
			return ret;
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}

	public int insertBean(String table, Bean bean) throws UnionException {
		List<Bean> tmp = new ArrayList<Bean>();
		tmp.add(bean);
		List<Integer> res = insertBeans(table, tmp);
		return res.get(0);
	}

	public void removeBean(String table, String[] column, Bean p)
			throws UnionException {
		try {
			List<Bean> ret = new ArrayList<Bean>();
			String sql = "DELETE FROM " + table + " WHERE ";
			for (int i = 0; i < column.length; ++i) {
				if (p.getString(column[i]) == null)
					throw new UnionException("请求参数不足，请联系开发者。");
				if (i > 0)
					sql += "and ";
				sql += column[i] + " = ? ";
			}
			PreparedStatement stat = con.prepareStatement(sql);
			for (int i = 0; i < column.length; ++i)
				stat.setString(i + 1, p.getString(column[i]));
			stat.execute();
		} catch (JSONException e) {
			throw new UnionException(e);
		} catch (SQLException e) {
			throw new UnionException(e);
		}
	}

	public void removeBean(String table, String column, String value)
			throws UnionException {
		try {
			Bean bean = new Bean();
			bean.put(column, value);
			removeBean(table, new String[] { column }, bean);
		} catch (JSONException e) {
			throw new UnionException(e);
		}
	}
}
