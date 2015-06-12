package com.lodogame.ldsg.web.model.jinli;

import java.util.List;

public class JinLiLoginResult {

	private int r;

	private List<JinLiUserInfo> ply;

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public List<JinLiUserInfo> getPly() {
		return ply;
	}

	public void setPly(List<JinLiUserInfo> ply) {
		this.ply = ply;
	}

	public String getUserId() {
		if (ply.size() > 0) {
			return ply.get(0).getPid();
		}
		return null;
	}

}
