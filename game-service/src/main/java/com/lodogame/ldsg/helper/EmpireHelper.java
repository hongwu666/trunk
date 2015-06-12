package com.lodogame.ldsg.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.math.RandomUtils;

import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.bo.EmpireOccupyBO;
import com.lodogame.ldsg.factory.NameFactory;
import com.lodogame.model.EmpireGain;
import com.lodogame.model.EmpireSystemUpEnum;

public class EmpireHelper {

	private static Map<Integer, Integer> idMap = new HashMap<Integer, Integer>();
	private static Map<Integer, Map<Integer, Integer>> typeMap = new HashMap<Integer, Map<Integer, Integer>>();
	/**
	 * 根据半小时结算一次规则，获取结算次数
	 * 
	 * @param times
	 * @return
	 */
	public static int getCountTimes(Date endDate, Date startDate) {
		Date now = new Date();
		long diff;
		if (now.after(endDate)) {
			diff = endDate.getTime() - startDate.getTime();
		} else {
			diff = now.getTime() - startDate.getTime();
		}

		return (int) (diff / 30 / 60 / 1000);
	}

	public static String compoundGain(List<EmpireGain> list) {
		StringBuffer toolIds=new StringBuffer();
		typeMap.clear();
		idMap.clear();
		for (EmpireGain empireGain : list) {
			int type = empireGain.getToolType();
			int id = empireGain.getToolId();
			int num = empireGain.getToolNum();
			if (typeMap.containsKey(type) && typeMap.get(type).containsKey(id)) {
				typeMap.get(type).put(id, typeMap.get(type).get(id) + num);
			}else if(typeMap.containsKey(type)){
				
				typeMap.get(type).put(id, num);
			}else{
				idMap.clear();
				idMap.put(id, num);
				typeMap.put(type,idMap );
			}
			
		}
		for(int toolType:typeMap.keySet()){
			for(Entry<Integer, Integer> entry:typeMap.get(toolType).entrySet()){				
				toolIds.append(toolType).append(",").append(entry.getKey()).append(",").append(entry.getValue()).append("|");
			}
		}
		return toolIds.toString();
	}
	
	public static EmpireOccupyBO createRobot(){
		EmpireOccupyBO bo = new EmpireOccupyBO();
		
		bo.setName(NameFactory.getInstance().getName());
		bo.setPercent(EmpireSystemUpEnum.A.toString());
		bo.setPos(0);
		bo.setPower(RandomUtils.nextInt(30000)+5000);
		bo.setUserId("12");
		bo.setEnemy(0);
		bo.setTime(DateUtils.add(new Date(), Calendar.HOUR_OF_DAY,-12).getTime());
		
		return bo;
	}
	public static void main(String[] args) {
		
	}
}
