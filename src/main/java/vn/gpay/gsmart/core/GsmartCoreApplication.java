package vn.gpay.gsmart.core;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableAuthorizationServer
//@EnableResourceServer
public class GsmartCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(GsmartCoreApplication.class, args);
	}
	
//	@PostConstruct
//    public void init(){
//      // Setting Spring Boot SetTimeZone
//      TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
//    }
}
