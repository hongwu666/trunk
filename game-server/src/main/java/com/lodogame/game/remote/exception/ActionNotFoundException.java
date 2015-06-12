package com.lodogame.game.remote.exception;

public class ActionNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String action;

	public ActionNotFoundException(String action) {
		super();
		this.action = action;
	}

	@Override
	public String getMessage() {
		return "action not found.[" + this.action + "]";
	}

}
