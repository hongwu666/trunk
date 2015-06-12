package com.lodogame.ldsg.bo;

import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class GemBO {
	@Mapper(name="ps")
	private List<GemPackBO> packs;
	@Mapper(name="os")
	private List<GemOpenBO> open;
	@Mapper(name="auto")
	private List<Integer> auto;
	@Mapper(name="log")
	private List<GemAltarUserLogBO> log ;
	
	
	
	public List<GemAltarUserLogBO> getLog() {
		return log;
	}
	public void setLog(List<GemAltarUserLogBO> log) {
		this.log = log;
	}
	public List<Integer> getAuto() {
		return auto;
	}
	public void setAuto(List<Integer> auto) {
		this.auto = auto;
	}
	public List<GemPackBO> getPacks() {
		return packs;
	}
	public void setPacks(List<GemPackBO> packs) {
		this.packs = packs;
	}
	public List<GemOpenBO> getOpen() {
		return open;
	}
	public void setOpen(List<GemOpenBO> open) {
		this.open = open;
	}
	
	
}
