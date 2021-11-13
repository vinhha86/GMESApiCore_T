package vn.gpay.gsmart.core;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class Log4jCommon {
    private static Logger LOGGER = Logger.getLogger(Log4jCommon.class.getName());
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
    
    public static void log_StartApp() {
    	LOGGER.info("Start Gmes App: " + formatter.format(new Date()));
    }
//    public static void log_deleteStockin(String username, Long stockin_id, String stockincode, Long pcontractid_link) 
//    {
//        LOGGER.info(formatter.format(new Date()) + "/Delete Stockin - User: " + username + " - ObjID: " + stockin_id + " - ObjCode: " + stockincode + " - Contract_ID: " + pcontractid_link);
//    }
}
