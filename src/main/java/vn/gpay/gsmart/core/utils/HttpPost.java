package vn.gpay.gsmart.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpPost {
public String getDataFromHttpPost(String jsonReq, String urlPost) throws IOException {
	URL url = new URL(urlPost);
	
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setDoOutput(true);
    conn.setDoInput(true);
//    conn.setRequestProperty("authorization", token);
    conn.setRequestProperty("Content-Type", "application/json");
    conn.setRequestProperty("Accept", "application/json");
    conn.setRequestMethod("POST");
    
    OutputStream os = conn.getOutputStream();
    os.write(jsonReq.getBytes());
    os.flush();
             
    String result = "";
	String line;
	
    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    while ((line = rd.readLine()) != null) {
    	result += line;
    }
    rd.close();
    
    conn.disconnect();
    
	return result;
}
}
