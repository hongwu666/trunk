package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * @author weq
 *
 */
@Compress
public class TreasurePriceBo {
	@Mapper(name = "tb")
	private int tb;
	@Mapper(name = "wh")
	private int wh;
	@Mapper(name = "hl")
	private int hl;
	@Mapper(name = "jy")
	private int jy;
	public int getTb() {
		return tb;
	}
	public void setTb(int tb) {
		this.tb = tb;
	}
	public int getWh() {
		return wh;
	}
	public void setWh(int wh) {
		this.wh = wh;
	}
	public int getHl() {
		return hl;
	}
	public void setHl(int hl) {
		this.hl = hl;
	}
	public int getJy() {
		return jy;
	}
	public void setJy(int jy) {
		this.jy = jy;
	}
	
	
}
