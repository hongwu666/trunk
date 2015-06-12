package com.lodogame.model;

public class DiskInfo {
	
	/**
	 * 磁盘总容量，GB为单位
	 */
	private int totalSpace;
	
	/**
	 * 磁盘可用空间，GB为单位
	 */
	private int usableSpace;
	
	
	public DiskInfo(int totalSpace, int usableSpace) {
		super();
		this.totalSpace = totalSpace;
		this.usableSpace = usableSpace;
	}
	public int getTotalSpace() {
		return totalSpace;
	}
	public void setTotalSpace(int totalSpace) {
		this.totalSpace = totalSpace;
	}
	public int getUsableSpace() {
		return usableSpace;
	}
	public void setUsableSpace(int usableSpace) {
		this.usableSpace = usableSpace;
	}
	
}
