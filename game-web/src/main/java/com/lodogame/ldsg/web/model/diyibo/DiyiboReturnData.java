package com.lodogame.ldsg.web.model.diyibo;

public class DiyiboReturnData {

	public class DiyiboReturnState {
		private String code;
		private String msg;
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		
	}
	
	public class DiyiboUserData {
		private String userid;
		private String username;
		public String getUserid() {
			return userid;
		}
		public void setUserid(String userid) {
			this.userid = userid;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
	}
	
	private String id;
	private String sign;
	private DiyiboReturnState state;
	private DiyiboUserData data;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public DiyiboReturnState getState() {
		return state;
	}
	public void setState(DiyiboReturnState state) {
		this.state = state;
	}
	public DiyiboUserData getData() {
		return data;
	}
	public void setData(DiyiboUserData data) {
		this.data = data;
	}
}
