package vn.gpay.gsmart.core.api.timesheetinout;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
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
import vn.gpay.gsmart.core.timesheetinout.TimeSheetDaily;
import vn.gpay.gsmart.core.timesheetinout.TimeSheetInOut;
import vn.gpay.gsmart.core.utils.AtributeFixValues;
import vn.gpay.gsmart.core.utils.HttpPost;
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
			String date_from = entity.fromdate;
			String date_to = entity.todate;
			long orgid_link = entity.orgid_link;
			
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
            
            //truyen param theo
            appParNode.put("date_from", date_from);
            appParNode.put("date_to", date_to);
            appParNode.put("orgid_link", orgid_link);
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

            DateFormat df_gio = new SimpleDateFormat("H:m:s");
            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd H:m:s");
            DateFormat df_ngay = new SimpleDateFormat("dd-MM-yyyy");
			List<TimeSheetInOut> lsttimesheetinout = objectMapper.readValue(result, new TypeReference<List<TimeSheetInOut>>() {});
			for(TimeSheetInOut inout : lsttimesheetinout) {
				Personel person = personService.getPersonelBycode(inout.getPersonel_code());
				if(person!=null) {
					inout.setFullname(person.getFullname());
					inout.setPersonel_code(person.getCode());
					String time = df_gio.format(df2.parse(inout.getTime()));
					String ngay = df_ngay.format(df2.parse(inout.getTime()));
					inout.setTime(time);
					inout.setDay(ngay);
				}
					
			}
			lsttimesheetinout.removeIf(c -> c.getFullname() == null);
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
	
	@RequestMapping(value = "/get_daily", method = RequestMethod.POST)
	public ResponseEntity<getDailyResponse> getDaily(@RequestBody getDailyRequest entity) {
		getDailyResponse response = new getDailyResponse();
		try {
			int month = entity.month;
			int year = entity.year;
			long grantid_link = entity.grantid_link;
			long orgid_link = entity.orgid_link;
			
			String urlPush = AtributeFixValues.url_timesheet+"/timesheet/getlist_daily";
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
            
            //truyen param theo
            appParNode.put("month", month);
            appParNode.put("year", year);
            appParNode.put("orgid_link", orgid_link);
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
            
			List<TimeSheetDaily> lsttimesheetinout = objectMapper.readValue(result, new TypeReference<List<TimeSheetDaily>>() {});
			List<TimeSheetDaily> list_remove = new ArrayList<TimeSheetDaily>();
			
			//Lay ds nhan vien theo orgid 
			List<Personel> list_person = personService.getby_org(orgid_link, 1);
			Map<Integer, Personel> map_person = new HashedMap<>();
			for(Personel person : list_person) {
				int i_person_code = Integer.parseInt(person.getCode());
				map_person.put(i_person_code, person);
			}
						
			for(TimeSheetDaily daily : lsttimesheetinout) {
				if(map_person.containsKey(Integer.parseInt(daily.getPersonnel_code()))) {
					Personel person = map_person.get(Integer.parseInt(daily.getPersonnel_code()));
					
					daily.setFullname(person.getFullname());
					daily.setPersonnel_code(person.getCode());
					
					if(grantid_link!= 0) {
						if(!person.getOrgid_link().equals(grantid_link)) {
							list_remove.add(daily);
						}
					}
				}
				else {
					list_remove.add(daily);
				}
//				Personel person = personService.getPersonelBycode(daily.getPersonnel_code());
//				if(person!=null) {
//					daily.setFullname(person.getFullname());
//					daily.setPersonnel_code(person.getCode());
//				}
//				if(grantid_link!= 0) {
//					if(!person.getOrgid_link().equals(grantid_link)) {
//						list_remove.add(daily);
//					}
//				}
			}
			lsttimesheetinout.removeIf(c -> c.getFullname() == null);
			lsttimesheetinout.removeAll(list_remove);
			
			response.data =lsttimesheetinout;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			System.out.println(e.getMessage());
		}
		return new ResponseEntity<getDailyResponse>(response, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/calculate_daily", method = RequestMethod.POST)
	public ResponseEntity<getDailyResponse> calculateDaily(@RequestBody getDailyRequest entity) {
		getDailyResponse response = new getDailyResponse();
		try {
			long orgid_link = entity.orgid_link;
			
			String urlPost = AtributeFixValues.url_timesheet+"/timesheet/calculate_daily";
			
            
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode appParNode = objectMapper.createObjectNode();
            
            //truyen param theo
            appParNode.put("orgid_link", orgid_link);
            String jsonReq = objectMapper.writeValueAsString(appParNode);
            
            HttpPost http = new HttpPost();
            String result = http.getDataFromHttpPost(jsonReq, urlPost);
            if("\"OK\"".equals(result))			
            	response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
            else 
            	response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<getDailyResponse>(response, HttpStatus.OK);
		
	}
	
}
