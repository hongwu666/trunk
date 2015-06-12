package com.lodogame.ldsg.web.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.lodogame.ldsg.web.bo.OrderBO;
import com.lodogame.ldsg.web.constants.OrderStatus;
import com.lodogame.ldsg.web.constants.ServiceReturnCode;
import com.lodogame.ldsg.web.dao.PaymentOrderDao;
import com.lodogame.ldsg.web.exception.ServiceException;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;
import com.lodogame.ldsg.web.model.PurchaseInfo;
import com.lodogame.ldsg.web.model.PaymentOrder;
import com.lodogame.ldsg.web.sdk.GameApiSdk;
import com.lodogame.ldsg.web.service.PartnerService;
import com.lodogame.ldsg.web.service.ServerService;
import com.lodogame.ldsg.web.util.EncryptUtil;
import com.lodogame.ldsg.web.util.Json;

/**
 * 订单相关的接口
 * 
 * @author CJ
 * 
 */
@Controller
public class OrderController {

	private static final Logger LOG = Logger.getLogger(OrderController.class);

	private static final String crcKey = "$#@$%%eweqwlkfef";

	@Autowired
	private PartnerServiceFactory serviceFactory;

	@Autowired
	private PaymentOrderDao paymentOrderDao;

	@Autowired
	private ServerService serverService;

	/**
	 * 查询订单
	 */
	@RequestMapping("/webApi/getOrder.do")
	public ModelAndView getOrder(HttpServletRequest req, HttpServletResponse res) {

		Map<String, Object> map = new HashMap<String, Object>();

		String timestamp = req.getParameter("timestamp");
		String sign = req.getParameter("sign");
		String orderNo = req.getParameter("orderNo");

		int rc = 1000;

		String checkSign = EncryptUtil.getMD5(timestamp + orderNo + crcKey);

		if (!StringUtils.equalsIgnoreCase(sign, checkSign)) {
			rc = 2001;
		} else {
			PaymentOrder paymentOrder = this.paymentOrderDao.get(orderNo);
			if (paymentOrder != null) {
				map.put("order", paymentOrder);
			} else {
				rc = 2002;
			}
		}

		map.put("rc", rc);

		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		return modelView;
	}

	/**
	 * 补单
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping("/webApi/fillOrder.do")
	public ModelAndView fillOrder(HttpServletRequest req, HttpServletResponse res) {

		Map<String, Object> map = new HashMap<String, Object>();

		String timestamp = req.getParameter("timestamp");
		String sign = req.getParameter("sign");
		String orderNo = req.getParameter("orderNo");
		String partnerOrderId = req.getParameter("partnerOrderId");

		int rc = 1000;

		String checkSign = EncryptUtil.getMD5(timestamp + orderNo + partnerOrderId + crcKey);

		if (!StringUtils.equalsIgnoreCase(sign, checkSign)) {
			rc = 2001;
		} else {
			PaymentOrder paymentOrder = this.paymentOrderDao.get(orderNo);
			if (paymentOrder != null) {

				if (paymentOrder.getStatus() == OrderStatus.STATUS_NEW) {
					String orderId = paymentOrder.getOrderId();
					BigDecimal finishAmount = paymentOrder.getAmount();
					int gold = (int) (finishAmount.doubleValue() * 10);

					// 更新订单状态
					if (this.paymentOrderDao.updateStatus(orderId, OrderStatus.STATUS_FINISH, partnerOrderId, finishAmount, "")) {
						int port = serverService.getServerHttpPort(paymentOrder.getServerId());
						// 请求游戏服，发放游戏货币
						if (!GameApiSdk.getInstance().doPayment(paymentOrder.getPartnerId(), paymentOrder.getServerId(), port, paymentOrder.getPartnerUserId(), paymentOrder.getWaresId(), "", paymentOrder.getOrderId(),
								finishAmount, gold, "", "compensate")) {
							// 如果失败，要把订单置为未支付
							this.paymentOrderDao.updateStatus(orderId, OrderStatus.STATUS_NOT_PAY, orderId, finishAmount, "");
							LOG.error("发货失败");
							rc = 2003;
						} else {
							LOG.info("支付成功");
						}
					}
				} else {
					rc = 2004;
				}

			} else {
				rc = 2002;
			}
		}

		map.put("rc", rc);

		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		return modelView;
	}

	
	
	/**
	 * 创建订单，安卓，iOS，越狱都调用这个接口在登陆服上创建订单
	 */
	@RequestMapping("/webApi/createOrder.do")
	@ResponseBody
	public String createOrder(HttpServletRequest req, HttpServletResponse res) {
		
		PurchaseInfo order = new PurchaseInfo(req);

		OrderBO orderBO = tryCreateOrderBO(order);
		
		return Json.toJson(orderBO);
	}

	private OrderBO tryCreateOrderBO(PurchaseInfo order) {
		PartnerService partnerService = serviceFactory.getBean(order.getPartnerId());

		OrderBO orderBO = new OrderBO();
		int rc = ServiceReturnCode.SUCCESS;

		try {
			orderBO = partnerService.createOrder(order);
			orderBO.setNotifyUrl(partnerService.getPayBackUrl());
		} catch (ServiceException se) {
			LOG.error(se.getMessage());
			rc = se.getCode();
		} catch (Exception e) {
			LOG.error(e.getMessage());
			rc = ServiceReturnCode.FAILD;
		}

		orderBO.setRc(rc);
		
		return orderBO;
	}
}
