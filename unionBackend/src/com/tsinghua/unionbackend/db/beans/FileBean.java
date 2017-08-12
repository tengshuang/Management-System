package com.tsinghua.unionbackend.db.beans;

import java.sql.ResultSet;

import com.tsinghua.unionbackend.util.UnionException;

public class FileBean extends Bean {

	public FileBean() {
		super();
	}

	public FileBean(ResultSet res) throws UnionException {
		super(res);
	}

}
