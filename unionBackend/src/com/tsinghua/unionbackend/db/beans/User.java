package com.tsinghua.unionbackend.db.beans;

import java.sql.ResultSet;

import org.json.JSONException;

import com.tsinghua.unionbackend.util.UnionException;

public class User extends Bean {

	public User() {
		super();
	}

	public User(ResultSet res) throws UnionException {
		super(res);
	}

	public User(String s) throws JSONException {
		super(s);
	}

	public User(Bean bean) throws JSONException {
		super(bean.toString());
	}
}
