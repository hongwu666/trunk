package com.lodogame.ldsg.web.controller.payback;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.lodogame.ldsg.web.factory.PartnerServiceFactory;
import com.lodogame.ldsg.web.model.oppo.OppoPaymentObj;
import com.lodogame.ldsg.web.sdk.OppoSdk;
import com.lodogame.ldsg.web.service.PartnerService;

@Controller
public class OppoController {

	private static Logger LOG = Logger.getLogger(OppoController.class);
	private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCmreYIkPwVovKR8rLHWlFVw7YDfm9uQOJKL89Smt6ypXGVdrAKKl0wNYc3/jecAoPi2ylChfa2iRu5gunJyNmpWZzlCNRIau55fxGW0XEu553IiprOZcaw5OuYGlf60ga8QT6qToP0/dpiL/ZbmNUO9kUhosIjEu22uFgR+5cYyQIDAQAB";
	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/oppoPayment.do", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView oppoCallBack(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Map<String, String> rt = new HashMap<String, String>();
		OppoPaymentObj paymentObj = new OppoPaymentObj();

		NotifyRequestEntity e = new NotifyRequestEntity();
		e.setNotifyId(req.getParameter("notifyId"));
		e.setPartnerOrder(req.getParameter("partnerOrder"));
		e.setProductName(req.getParameter("productName"));
		e.setProductDesc(req.getParameter("productDesc"));
		e.setPrice(Integer.parseInt(req.getParameter("price")));
		e.setCount(Integer.parseInt(req.getParameter("count")));
		e.setAttach(req.getParameter("attach"));
		e.setSign(req.getParameter("sign"));
		String baseString = getBaseString(e);
		String sign = req.getParameter("sign");
		try {
			if (!doCheck(baseString, req.getParameter("sign"), PUBLIC_KEY)) {
				LOG.error("验签失败baseString=" + baseString + ", sing=" + sign);
				rt.put("result", "FAIL");
				rt.put("resultMsg", "签名错误");
				LOG.error("签名错误");
				ModelAndView modelView = new ModelAndView("empty", "output", rt);
				return modelView;
			}

			paymentObj.setCount(Integer.valueOf(req.getParameter("count")));
			paymentObj.setCpOrderId(req.getParameter("partnerOrder"));
			paymentObj.setExtInfo(req.getParameter("attach"));
			paymentObj.setPrice(Integer.valueOf(req.getParameter("price")));
			paymentObj.setSdkOrderId(req.getParameter("notifyId"));

			PartnerService ps = serviceFactory.getBean(OppoSdk.instance().getPartnerId());

			if (ps.doPayment(paymentObj)) {
				rt.put("result", "OK");
			} else {
				rt.put("result", "FAIL");
			}
		} catch (Exception ex) {
			LOG.error(ex);
			rt.put("result", "FAIL");
		}

		ModelAndView modelView = new ModelAndView("empty", "output", rt);
		return modelView;

	}

	private String getBaseString(NotifyRequestEntity ne) {
		StringBuilder sb = new StringBuilder();
		sb.append("notifyId=").append(ne.getNotifyId());
		sb.append("&partnerOrder=").append(ne.getPartnerOrder());
		sb.append("&productName=").append(ne.getProductName());
		sb.append("&productDesc=").append(ne.getProductDesc());
		sb.append("&price=").append(ne.getPrice());
		sb.append("&count=").append(ne.getCount());
		sb.append("&attach=").append(ne.getAttach());
		return sb.toString();
	}

	public boolean doCheck(String content, String sign, String publicKey) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] encodedKey = Base64.decodeBase64(publicKey);
		PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

		java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");

		signature.initVerify(pubKey);
		signature.update(content.getBytes("UTF-8"));
		boolean bverify = signature.verify(Base64.decodeBase64(sign));
		return bverify;
	}

	class NotifyRequestEntity {
		private String notifyId;
		private String partnerOrder;
		private String productName;
		private String productDesc;
		private int price;
		private int count;
		private String attach;
		private String sign;

		public String getNotifyId() {
			return notifyId == null ? "" : notifyId;
		}

		public void setNotifyId(String notifyId) {
			this.notifyId = notifyId;
		}

		public String getPartnerOrder() {
			return partnerOrder == null ? "" : partnerOrder;
		}

		public void setPartnerOrder(String partnerOrder) {
			this.partnerOrder = partnerOrder;
		}

		public String getProductName() {
			return productName == null ? "" : productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public String getProductDesc() {
			return productDesc == null ? "" : productDesc;
		}

		public void setProductDesc(String productDesc) {
			this.productDesc = productDesc;
		}

		public int getPrice() {
			return price;
		}

		public void setPrice(int price) {
			this.price = price;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public String getAttach() {
			return attach == null ? "" : attach;
		}

		public void setAttach(String attach) {
			this.attach = attach;
		}

		public String getSign() {
			return sign == null ? "" : sign;
		}

		public void setSign(String sign) {
			this.sign = sign;
		}
	}
}
