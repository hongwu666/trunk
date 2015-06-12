package com.lodogame.ldsg.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.ResourceJdBo;
import com.lodogame.ldsg.bo.ResourceSsBo;
import com.lodogame.ldsg.bo.ResourceViewBO;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.ResourceService;

public class ResourceAction  extends LogicRequestAction{
	
	@Autowired
	ResourceService resourceService;
	
	@Autowired
	PushHandler pushHandler;
	
	public Response show(){
		String userId = getUid();
		int fbType=getInt("fbType",0);
		ResourceViewBO bo = resourceService.show(userId, fbType);
		int b= resourceService.getBuyPrice(userId);
		set("view", bo);
		set("p", b);
		return render();
	}
	
	public Response showJd(){
		String userId = getUid();
		int fbType=getInt("fbType",0);
		int fbDif=getInt("fbDif",0);
		List<ResourceJdBo> list = resourceService.showJd(userId, fbType, fbDif);
		List<ResourceSsBo> bo = resourceService.showSs(userId, fbType, fbDif);
		set("jd",list );
		set("bo", bo);
		return render();
	}
	
	public Response  buyNum(){
		String userId = getUid();
		int num = getInt("num",1);
		int type=getInt("type",1);
		int v = resourceService.buyNum(userId, num,type);
		int p = resourceService.getBuyPrice(userId);
		set("v", v);
		set("p", p);
		pushHandler.pushUser(userId);
		return render();
	}
	//进入关卡
	public Response  startShow(){
		String userId = getUid();
		int fbType = getInt("fbType", 0);
		int fbDif = getInt("fbDif", 0);
		int g = getInt("g", 0);
		int v = resourceService.startShow(userId, fbType, fbDif, g);
		set("v", v);
		return render();
	}
	//一键五星
	public Response  startOneKey(){
		String userId = getUid();
		int fbType = getInt("fbType", 0);
		int fbDif = getInt("fbDif", 0);
		int g = getInt("g", 0);
		int v = resourceService.startOneKey(userId, fbType, fbDif, g);
		set("v", v);
		pushHandler.pushUser(userId);
		return render();
	}
	
	public Response startUp(){
		String userId = getUid();
		int fbType = getInt("fbType", 0);
		int fbDif = getInt("fbDif", 0);
		int g = getInt("g", 0);
		int v = resourceService.startUp(userId, fbType, fbDif, g);
		set("v", v);
		pushHandler.pushUser(userId);
		return render();
	}
	
	public Response reset(){
		String userId = getUid();
		int fbType = getInt("fbType", 0);
		int fbDif = getInt("fbDif", 0);
		resourceService.reset(userId, fbType, fbDif);
		return render();
	}
}
