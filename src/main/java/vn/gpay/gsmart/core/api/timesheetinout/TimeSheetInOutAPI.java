package vn.gpay.gsmart.core.api.timesheetinout;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.gpay.gsmart.core.personel.IPersonnel_Service;
import vn.gpay.gsmart.core.personel.Personel;
import vn.gpay.gsmart.core.timesheetinout.TimeSheetInOut;
import vn.gpay.gsmart.core.utils.AtributeFixValues;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/timesheetinout")
public class TimeSheetInOutAPI {
	@Autowired
	IPersonnel_Service personService;
	//lấy tất cả  danh sách 
	@RequestMapping(value = "/getall", method = RequestMethod.POST)
	public ResponseEntity<TimeSheetInOut_load_response> timesheetinout_GetAll(@RequestBody TimeSheetInOut_load_request entity) {
		TimeSheetInOut_load_response response = new TimeSheetInOut_load_response();
		try {
			String urlPush = AtributeFixValues.url_timesheet+"/timesheet/getlist";
			URL url = new URL(urlPush);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
//            conn.setRequestProperty("authorization", token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");
            
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode appParNode = objectMapper.createObjectNode();
            
//            appParNode.put("userid", entity.user.getId());
//            appParNode.put("enable", enabled);
            String jsonReq = objectMapper.writeValueAsString(appParNode);
            
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

			List<TimeSheetInOut> lsttimesheetinout = objectMapper.readValue(result, new TypeReference<List<TimeSheetInOut>>() {});
			for(TimeSheetInOut inout : lsttimesheetinout) {
				Personel person = personService.getPersonelBycode(inout.getPersonel_code());
				inout.setFullname(person.getFullname());
			}
			response.data =lsttimesheetinout;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			System.out.println(e.getMessage());
		}
		return new ResponseEntity<TimeSheetInOut_load_response>(response, HttpStatus.OK);
		
	}
}
