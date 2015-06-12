package com.lodogame.ldsg.web.sdk;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import com.lodogame.ldsg.web.model.wandoujia.WandoujiaPaymentObj;
import com.lodogame.ldsg.web.util.UrlRequestUtils;
import com.lodogame.ldsg.web.util.UrlRequestUtils.Mode;
import com.lodogame.ldsg.web.util.anzhi.Base64;

public class WandoujiaSdk {

	private static final Logger logger = Logger.getLogger(WandoujiaSdk.class);

	private static WandoujiaSdk ins;

	private static Properties prop;

    public final static String WandouPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCd95FnJFhPinpNiE/h4VA6bU1rzRa5+a25BxsnFX8TzquWxqDCoe4xG6QKXMXuKvV57tTRpzRo2jeto40eHKClzEgjx9lTYVb2RFHHFWio/YGTfnqIPTVpi7d7uHY+0FZ0lYL5LlW4E2+CQMxFOPRwfqGzMjs1SDlH7lVrLEVy6QIDAQAB";

    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";
    
	private String partnerId;
	private String appId;
	private String checkSessionUrl;

	public static WandoujiaSdk instance() {
		synchronized (WandoujiaSdk.class) {
			if (ins == null) {
				ins = new WandoujiaSdk();
			}
		}

		return ins;
	}

	private WandoujiaSdk() {
		loadSdkProperties();
	}

	public void reload() {
		loadSdkProperties();
	}

	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			appId = prop.getProperty("WandoujiaSdk.appId");
			partnerId = prop.getProperty("WandoujiaSdk.partnerId");
			checkSessionUrl = prop.getProperty("WandoujiaSdk.checkSessionUrl");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean verifySession(String session, String uid, long timestamp) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appId", appId);
		params.put("uid", uid);
		params.put("token", session);
		String result = UrlRequestUtils.execute(checkSessionUrl, params, Mode.GET);
		if ("true".equals(result)) {
			return true;
		}
		return false;
	}

	public boolean checkPayCallbackSign(WandoujiaPaymentObj paymentObj) {
		try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(WandouPublicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

            signature.initVerify(pubKey);
            signature.update(paymentObj.getData().getBytes("utf-8"));

            boolean bverify = signature.verify(Base64.decode(paymentObj.getSign()));
            return bverify;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
	}

	public String getPartnerId() {
		return partnerId;
	}

}
