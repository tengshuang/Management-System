package com.tsinghua.unionbackend.db.beans;

import java.sql.ResultSet;

import org.json.JSONException;

import com.tsinghua.unionbackend.util.UnionException;

public class Event extends Bean {

	public Event(String s) throws JSONException {
		super(s);
	}

	public Event(ResultSet res) throws UnionException {
		super(res);
	}

}
