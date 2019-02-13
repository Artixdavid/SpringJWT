package com.test.app.util.paginator;

public class PageItem {

	private int num;
	private boolean actual;

	public PageItem(int num, boolean actual) {
		this.num = num;
		this.actual = actual;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public boolean getActual() {
		return actual;
	}

	public void setActual(boolean actual) {
		this.actual = actual;
	}

}
