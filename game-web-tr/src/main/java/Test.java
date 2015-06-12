import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.lodogame.ldsg.web.model.mol.MOLPayment;
import com.lodogame.ldsg.web.model.mycard.CheckEligibleRequest;
import com.lodogame.ldsg.web.model.mycard.MyCardServerToServerPayment;
import com.lodogame.ldsg.web.util.IDGenerator;
import com.lodogame.ldsg.web.util.Json;
import com.lodogame.ldsg.web.util.UrlRequestUtils;


public class Test {

	
	/**
	 * {"order":"1057_2450_1_134315287_1413970822710","money":"1.00","mid":"134315287","result":"1","time":"20141022174201","signature":"37d7b644043139d580d9188438df19dd","ext":"0aea0a401e5f4ea98dda7a1fa82bf28a"}
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		System.out.println(IDGenerator.getID());
	}

}
