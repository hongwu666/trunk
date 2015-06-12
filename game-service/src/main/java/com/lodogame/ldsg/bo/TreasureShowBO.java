package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 聚宝盆界面显示
 * 
 * @author
 *
 */
@Compress
public class TreasureShowBO {

	@Mapper(name = "type")
	private int type;// 聚宝盆类型

	@Mapper(name = "on")
	private int openNum;// 开启次数

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getOpenNum() {
		return openNum;
	}

	public void setOpenNum(int openNum) {
		this.openNum = openNum;
	}

}
