package com.tsinghua.unionbackend.db.beans;

import java.sql.ResultSet;

import org.json.JSONException;

import com.tsinghua.unionbackend.util.UnionException;

public class Activity extends Bean {

	public Activity(String s) throws JSONException {
		super(s);
	}

	public Activity(ResultSet res) throws UnionException {
		super(res);
	}

	public Activity(ResultSet res, boolean prefix) throws UnionException {
		super(res, prefix);
	}

	public Activity(Bean bean) throws JSONException {
		super(bean.toString());
	}
}
