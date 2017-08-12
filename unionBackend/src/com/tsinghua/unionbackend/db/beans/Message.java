package com.tsinghua.unionbackend.db.beans;

import java.sql.ResultSet;

import org.json.JSONException;

import com.tsinghua.unionbackend.util.UnionException;

public class Message extends Bean {

	public Message(String s) throws JSONException {
		super(s);
	}

	public Message(ResultSet res) throws UnionException {
		super(res);
	}
}
