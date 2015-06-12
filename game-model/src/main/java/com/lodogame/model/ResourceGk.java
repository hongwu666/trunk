package com.lodogame.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResourceGk {
	private String userId;
	private int id;
	private int fbType;
	private int fbDif;
	private Date createdTime;
	private List<ResourceGkStart> starts = new ArrayList<ResourceGkStart>();
	private List<ResourceGkPkLog> logs = new ArrayList<ResourceGkPkLog>();
	
	public ResourceGk() {
		super();
	}

	public ResourceGk(String userId, int id, int fbType, int fbDif) {
		super();
		this.userId = userId;
		this.id = id;
		this.fbType = fbType;
		this.fbDif = fbDif;
		this.createdTime = new Date();
	}

	public List<ResourceGkStart> getStarts() {
		return starts;
	}

	public void setStarts(List<ResourceGkStart> starts) {
		if(starts==null)return;
		for(ResourceGkStart temp : starts)
			temp.setUserId(userId);
		this.starts = starts;
	}

	public List<ResourceGkPkLog> getLogs() {
		return logs;
	}

	public void setLogs(List<ResourceGkPkLog> logs) {
		if(logs==null)return;
		for(ResourceGkPkLog temp : logs)
			temp.setUserId(userId);
		this.logs = logs;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


	public int getFbType() {
		return fbType;
	}

	public void setFbType(int fbType) {
		this.fbType = fbType;
	}

	public int getFbDif() {
		return fbDif;
	}

	public void setFbDif(int fbDif) {
		this.fbDif = fbDif;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public void reset() {
		this.logs.clear();
		this.starts.clear();
	}

	
}
