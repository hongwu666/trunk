package com.lodogame.ldsg.web.sdk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.lodogame.ldsg.web.model.jinli.JinLiLoginResult;
import com.lodogame.ldsg.web.util.Json;
import com.lodogame.ldsg.web.util.jinli.Base64;

public class JinLiSdk {

	private static final Logger logger = Logger.getLogger(JinLiSdk.class);

	private static JinLiSdk ins;

	private static Properties prop;

	private String port = "443";
	private String verify_url = "https://id.gionee.com:" + port + "/account/verify.do";
	private String apiKey = null; // 替换成商户申请获取的APIKey
	private String secretKey = null; // 替换成商户申请获取的SecretKey
	private String host = "id.gionee.com";
	private String url = "/account/verify.do";
	private String method = "POST";
	private String payBackUrl = null;
	private String privateKey = null;
	private String publicKey = null;
	private String partnerId = null;

	public static JinLiSdk instance() {
		synchronized (JinLiSdk.class) {
			if (ins == null) {
				ins = new JinLiSdk();
			}
		}
		return ins;
	}

	private JinLiSdk() {
		loadSdkProperties();
	}

	public void reload() {
		loadSdkProperties();
	}

	public String getPublicKey() {
		return publicKey;
	}

	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			apiKey = prop.getProperty("JinLiSdk.apiKey");
			secretKey = prop.getProperty("JinLiSdk.secretKey");
			payBackUrl = prop.getProperty("JinLiSdk.payBackUrl");
			privateKey = prop.getProperty("JinLiSdk.privateKey");
			partnerId = prop.getProperty("JinLiSdk.partnerId");
			publicKey = prop.getProperty("JinLiSdk.publicKey");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String builderAuthorization() {

		Long ts = System.currentTimeMillis() / 1000;
		String nonce = StringUtil.randomStr().substring(0, 8);
		String mac = CryptoUtility.macSig(host, port, secretKey, ts.toString(), nonce, method, url);
		mac = mac.replace("\n", "");
		StringBuilder authStr = new StringBuilder();
		authStr.append("MAC ");
		authStr.append(String.format("id=\"%s\"", apiKey));
		authStr.append(String.format(",ts=\"%s\"", ts));
		authStr.append(String.format(",nonce=\"%s\"", nonce));
		authStr.append(String.format(",mac=\"%s\"", mac));
		return authStr.toString();
	}

	static class CryptoUtility {

		private static final String MAC_NAME = "HmacSHA1";

		public static String macSig(String host, String port, String macKey, String timestamp, String nonce, String method, String uri) {
			// 1. build mac string
			// 2. hmac-sha1
			// 3. base64-encoded

			StringBuffer buffer = new StringBuffer();
			buffer.append(timestamp).append("\n");
			buffer.append(nonce).append("\n");
			buffer.append(method.toUpperCase()).append("\n");
			buffer.append(uri).append("\n");
			buffer.append(host.toLowerCase()).append("\n");
			buffer.append(port).append("\n");
			buffer.append("\n");
			String text = buffer.toString();

			byte[] ciphertext = null;
			try {
				ciphertext = hmacSHA1Encrypt(macKey, text);
			} catch (Throwable e) {
				e.printStackTrace();
				return null;
			}

			String sigString = Base64.encodeToString(ciphertext, Base64.DEFAULT);
			return sigString;
		}

		public static byte[] hmacSHA1Encrypt(String encryptKey, String encryptText) throws InvalidKeyException, NoSuchAlgorithmException {
			Mac mac = Mac.getInstance(MAC_NAME);
			mac.init(new SecretKeySpec(StringUtil.getBytes(encryptKey), MAC_NAME));
			return mac.doFinal(StringUtil.getBytes(encryptText));
		}

	}

	static class StringUtil {
		public static final String UTF8 = "UTF-8";
		private static final byte[] BYTEARRAY = new byte[0];

		public static boolean isNullOrEmpty(String s) {
			if (s == null || s.isEmpty() || s.trim().isEmpty())
				return true;
			return false;
		}

		public static String randomStr() {
			return CamelUtility.uuidToString(UUID.randomUUID());
		}

		public static byte[] getBytes(String value) {
			return getBytes(value, UTF8);
		}

		public static byte[] getBytes(String value, String charset) {
			if (isNullOrEmpty(value))
				return BYTEARRAY;
			if (isNullOrEmpty(charset))
				charset = UTF8;
			try {
				return value.getBytes(charset);
			} catch (UnsupportedEncodingException e) {
				return BYTEARRAY;
			}
		}
	}

	static class CamelUtility {
		public static final int SizeOfUUID = 16;
		private static final int SizeOfLong = 8;
		private static final int BitsOfByte = 8;
		private static final int MBLShift = (SizeOfLong - 1) * BitsOfByte;

		private static final char[] HEX_CHAR_TABLE = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

		public static String uuidToString(UUID uuid) {
			long[] ll = { uuid.getMostSignificantBits(), uuid.getLeastSignificantBits() };
			StringBuilder str = new StringBuilder(SizeOfUUID * 2);
			for (int m = 0; m < ll.length; ++m) {
				for (int i = MBLShift; i > 0; i -= BitsOfByte)
					formatAsHex((byte) (ll[m] >>> i), str);
				formatAsHex((byte) (ll[m]), str);
			}
			return str.toString();
		}

		public static void formatAsHex(byte b, StringBuilder s) {
			s.append(HEX_CHAR_TABLE[(b >>> 4) & 0x0F]);
			s.append(HEX_CHAR_TABLE[b & 0x0F]);
		}

	}

	static class MyX509TrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {

			return null;
		}

	}

	public String getPayBackUrl() {
		return payBackUrl;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public String getApiKey() {
		return apiKey;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public String verify(String amigoToken) {

		HttpsURLConnection httpURLConnection = null;
		OutputStream out;

		TrustManager[] tm = { new MyX509TrustManager() };
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL sendUrl = new URL(verify_url);
			httpURLConnection = (HttpsURLConnection) sendUrl.openConnection();
			httpURLConnection.setSSLSocketFactory(ssf);
			httpURLConnection.setDoInput(true); // true表示允许获得输入流,读取服务器响应的数据,该属性默认值为true
			httpURLConnection.setDoOutput(true); // true表示允许获得输出流,向远程服务器发送数据,该属性默认值为false
			httpURLConnection.setUseCaches(false); // 禁止缓存
			int timeout = 30000;
			httpURLConnection.setReadTimeout(timeout); // 30秒读取超时
			httpURLConnection.setConnectTimeout(timeout); // 30秒连接超时
			String method = "POST";
			httpURLConnection.setRequestMethod(method);
			httpURLConnection.setRequestProperty("Content-Type", "application/json");
			httpURLConnection.setRequestProperty("Authorization", builderAuthorization());
			out = httpURLConnection.getOutputStream();
			out.write(amigoToken.getBytes());
			out.flush();
			out.close();
			InputStream in = httpURLConnection.getInputStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			byte[] buff = new byte[1024];
			int len = -1;
			while ((len = in.read(buff)) != -1) {
				buffer.write(buff, 0, len);
			}
			String json = buffer.toString();
			JinLiLoginResult result = Json.toObject(json, JinLiLoginResult.class);
			if (result.getR() > 0) {
				logger.error("金立用户登陆验证失败.token[" + amigoToken + "], result[" + json + "]");
				return null;
			}
			return result.getUserId();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}

	}
}
