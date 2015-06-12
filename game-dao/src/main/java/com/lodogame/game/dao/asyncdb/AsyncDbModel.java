package com.lodogame.game.dao.asyncdb;

import java.util.List;

public class AsyncDbModel<T> {

	private String table;

	private List<T> ts;

	private T t;

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public List<T> getTs() {
		return ts;
	}

	public void setTs(List<T> ts) {
		this.ts = ts;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

}
