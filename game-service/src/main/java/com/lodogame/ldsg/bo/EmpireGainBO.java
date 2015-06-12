package com.lodogame.ldsg.bo;

import java.util.ArrayList;
import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class EmpireGainBO {

	/**
	 * 获得资源
	 */
	@Mapper(name="list")
	private List<DropDescBO> list=new ArrayList<DropDescBO>();
	
}
