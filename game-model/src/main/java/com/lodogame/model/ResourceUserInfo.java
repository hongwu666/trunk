package com.lodogame.model;

import java.util.ArrayList;
import java.util.List;

public class ResourceUserInfo {
	private List<ResourceGk> gks;
	private List<ResourceNum> nums=new ArrayList<ResourceNum>();
	private String userId;
	
	
	public ResourceUserInfo(List<ResourceGk> gks, List<ResourceNum>  nums, String userId) {
		super();
		this.gks = gks;
		this.nums = nums;
		this.userId = userId;
	}

	public List<ResourceNum>  getNums() {
		return nums;
	}

	public void setNums(List<ResourceNum>  nums) {
		this.nums = nums;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<ResourceGk> getGks() {
		return gks;
	}
	
	public List<ResourceGk> getGks(int gk){
		List<ResourceGk> gkl = new ArrayList<ResourceGk>();
		for(ResourceGk temp : gks){
			if(temp.getFbType()==gk)
				gkl.add(temp);
		}
		return gkl;
	}

	public void setGks(List<ResourceGk> gks) {
		this.gks = gks;
	}
	
	public ResourceGk getGk(int type,int dif){
		for(ResourceGk temp : gks)
			if(temp.getFbType()==type && temp.getFbDif()==dif)
				return temp;
		return null;
	}
	public ResourceNum getNum(int type){
		for(ResourceNum num:getNums()){
			if (num.getType()==type) {
				return num;
			}
		}
		return null;
	}
	public void setNum(ResourceNum num){
		getNums().add(num);
	}
	public ResourceGkStart getGkStart(int type, int dif, int g) {
		ResourceGk gk = getGk(type, dif);
		if(gk==null)
			return null;
		ResourceGkStart st = null;
		for(ResourceGkStart temp : gk.getStarts())
			if(temp.getGk() == g)
				st=temp;
		return st;
	}
	
	
	public ResourceGkPkLog getGkLog(int type, int dif, int g) {
		ResourceGk gk = getGk(type, dif);
		if(gk==null)
			return null;
		ResourceGkPkLog st = null;
		for(ResourceGkPkLog temp : gk.getLogs())
			if(temp.getGk() == g)
				st=temp;
		return st;
	}
	
	public int reset(int fbType,int fbDif){
		int id = -100;
		for(int i=0;i<gks.size();i++){
			ResourceGk gk = gks.get(i);
			if(gk.getFbType()==fbType && gk.getFbDif()==fbDif){
				id=gk.getId();
				gk.reset();
				break;
			}
		}
		return id;
	}
}
