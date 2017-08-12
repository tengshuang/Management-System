package com.tsinghua.unionbackend.db.beans;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.tsinghua.unionbackend.util.UnionException;

public class Bean extends JSONObject {

	public Bean() {

	}

	public Bean(String s) throws JSONException {
		super(s);
	}

	public Bean(ResultSet res) throws UnionException {
		this(res, false);
	}

	public Bean(ResultSet res, boolean split) throws UnionException {
		try {
			ResultSetMetaData rsmd = res.getMetaData();
			int numColumns = rsmd.getColumnCount();
			String[] columnNames = new String[numColumns];
			int[] columnTypes = new int[numColumns];

			for (int i = 0; i < columnNames.length; i++) {
				columnNames[i] = rsmd.getColumnLabel(i + 1);
				columnTypes[i] = rsmd.getColumnType(i + 1);
				String tname = rsmd.getTableName(i + 1);
				if (split && !this.has(tname))
					this.put(tname, new JSONObject());
			}

			for (int i = 0; i < columnNames.length; i++) {
				String nam = columnNames[i];
				JSONObject me = this;
				if (split)
					me = this.getJSONObject(rsmd.getTableName(i + 1));
				String tmps = res.getString(i + 1);
				if (tmps == null || tmps.equals("null")) {
					me.put(nam, JSONObject.NULL);
					continue;
				}
				if (columnTypes[i] == Types.INTEGER) {
					me.put(nam, res.getInt(i + 1));
					continue;
				}
				if (columnTypes[i] == Types.BOOLEAN
						|| columnTypes[i] == Types.BIT) {
					me.put(nam, res.getBoolean(i + 1));
					continue;
				}
				me.put(nam, tmps);
			}
		} catch (SQLException e) {
			throw new UnionException(e);
		} catch (JSONException e) {
			throw new UnionException(e);
		}
	}

	public String sqlInsert(String table) throws UnionException {
		try {
			String sKey = "", sItem = "";
			Iterator<?> k = keys();
			while (k.hasNext()) {
				String key = (String) (k.next());
				String item = this.getString(key);
				if (item.toLowerCase().equals("true"))
					item = "1";
				if (item.toLowerCase().equals("false"))
					item = "0";
				if (item.equals("null"))
					continue;
				if (sKey.length() > 0)
					sKey += ",";
				sKey += key;
				if (sItem.length() > 0)
					sItem += ",";
				sItem += "'" + item.replace("'", "\\'") + "'";
			}
			String ret = "insert into %s (%s) values (%s)";
			return String.format(ret, table, sKey, sItem);
		} catch (JSONException e) {
			throw new UnionException(e);
		}
	}

	public String sqlUpdate(String table, String[] args) throws UnionException {
		try {
			List<String> argLst = Arrays.asList(args);
			String ret = "UPDATE " + table + " SET ";
			String s = "";
			Iterator<?> k = keys();
			while (k.hasNext()) {
				String key = (String) (k.next());
				if (argLst.contains(key))
					continue;
				String item = this.getString(key);
				if (item.equals("null"))
					continue;
				if (s.length() > 0)
					s += ", ";
				s += key + "=" + "'" + item.replace("'", "\\'") + "'";
			}
			ret += s + " WHERE ";
			String cond = "";
			for (int i = 0; i < args.length; ++i) {
				if (i > 0)
					cond += " and ";
				cond += args[i] + " = '" + this.getString(args[i]) + "'";
			}
			ret += cond;
			return ret;
		} catch (JSONException e) {
			throw new UnionException(e);
		}
	}

	public boolean isTrue(String key) {
		try {
			String tmp = this.getString(key);
			tmp = tmp.toLowerCase();
			if (tmp.equals("0") || tmp.equals("false") || tmp.equals("-1"))
				return false;
			return true;
		} catch (JSONException e) {
			return false;
		}
	}

}
