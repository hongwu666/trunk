package com.lodogame.ldsg.web.bo;

public class ApplePaybackBO {

	private String errcode;

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public ApplePaybackBO(String errcode) {
		super();
		this.errcode = errcode;
	}
	
	public ApplePaybackBO() {
		
	}
}
