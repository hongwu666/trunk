package com.lodogame.model;

public enum EmpireSystemUpEnum {

	A(100),
	B(120),
	C(150),
	D(200);
	private int percent;

	private EmpireSystemUpEnum(int percent) {
		this.percent = percent;
	}
	
	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public static EmpireSystemUpEnum get(int percent){
		for(EmpireSystemUpEnum upenum:EmpireSystemUpEnum.values()){
			if(upenum.getPercent()==percent){
				return upenum;
			}
		}
		return null;
	}
	
}
