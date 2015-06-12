

import java.text.SimpleDateFormat;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtil {

	private static ObjectMapper objectMapper = new ObjectMapper();
	static {
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
	}

	public static <T> T decode(String json, Class<T> pojoClass) {
		if (json == null)
			return null;
		try {
			return objectMapper.readValue(json, pojoClass);
		} catch (Exception e) {
			System.out.println("decode json error : "+json);
			e.printStackTrace();
			return null;
		}
	}

	public static String encode(Object o) {
		try {
			return objectMapper.writeValueAsString(o);
		} catch (Exception e) {
			return null;
		}
	}

	public static Object get(String json, String key) {
		Map map = decode(json, Map.class);
		if (map == null)
			return null;
		return map.get(key);
	}
}
