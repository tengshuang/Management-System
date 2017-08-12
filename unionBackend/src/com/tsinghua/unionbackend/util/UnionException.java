package com.tsinghua.unionbackend.util;

public class UnionException extends Exception {

	private static final long serialVersionUID = 905546913841684249L;
	public String ps = "未知错误。";

	public UnionException(Exception e, String msg) {
		super(e);
		ps = msg;
	}

	public UnionException(Exception e) {
		super(e);
	}

	public UnionException(String s) {
		super(s);
		ps = s;
	}

	public void deb() {
		System.out.println("vvvvvvvvvvvvvvvvvvvv");
		this.printStackTrace();
		System.out.println(this.getMessage());
		System.out.println("^^^^^^^^^^^^^^^^^^^^");
	}
}
