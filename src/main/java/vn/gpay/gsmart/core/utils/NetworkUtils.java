package vn.gpay.gsmart.core.utils;

import javax.servlet.http.HttpServletRequest;

public class NetworkUtils {

	public static String getClientIp(HttpServletRequest request) {

      String remoteAddr = "";

      if (request != null) {
          remoteAddr = request.getHeader("X-FORWARDED-FOR");
          if (remoteAddr == null || "".equals(remoteAddr)) {
              remoteAddr = request.getRemoteAddr();
          }
      }

      return remoteAddr;
	}
}
