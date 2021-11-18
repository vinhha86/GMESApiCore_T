package vn.gpay.gsmart.core;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class Log4jCommon {
	private static Logger LOGGER = Logger.getLogger(Log4jCommon.class.getName());
	private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public static void log_StartApp() {
		LOGGER.info("Start Gmes App: " + formatter.format(new Date()));
	}

	public static void move_pordergrant(String username, Long grantid, Long orggrantto, Long orggrantfrom,
			String function, String masp) {
		LOGGER.info(formatter.format(new Date()) + "/" + function + " - User: " + username + " - orggrantto: "
				+ orggrantto + " - orggrantfrom: " + orggrantfrom + " - masp: " + masp);
	}
}
