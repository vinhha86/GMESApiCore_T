package vn.gpay.gsmart.core.api.porder_list;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.pcontract.IPContractService;
import vn.gpay.gsmart.core.pcontract.PContract;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/porderlist")
public class POrderListAPI {
	@Autowired private IPOrder_Service porderService;
	@Autowired private IPContractService pcontractService;
	
	@RequestMapping(value = "/getall",method = RequestMethod.POST)
	public ResponseEntity<PorderList_getlist_response> POrderGetAll(HttpServletRequest request ) {
		PorderList_getlist_response response = new PorderList_getlist_response();
		try {
			
			response.data = porderService.findAll();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PorderList_getlist_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<PorderList_getlist_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getallbysearch",method = RequestMethod.POST)
	public ResponseEntity<PorderList_getlist_response> POrderGetAllBySearch(@RequestBody POrderList_getlist_request entity, HttpServletRequest request ) {
		PorderList_getlist_response response = new PorderList_getlist_response();
		try {
			response.data = porderService.getPOrderListBySearch(
					entity.ordercode, // ordercode
					entity.po, // po
					entity.style, // style
					entity.buyerid, // buyerid
					entity.vendorid, // vendorid
					entity.orderdatefrom, // orderdatefrom
					entity.orderdateto // orderdateto
					);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PorderList_getlist_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<PorderList_getlist_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getallbuyername",method = RequestMethod.POST)
	public ResponseEntity<POrderList_getListString_response> POrderGetAllBuyer(HttpServletRequest request ) {
		POrderList_getListString_response response = new POrderList_getListString_response();
		try {
			
			List<POrder> allPOrder = porderService.findAll();
			List<HashMap<String, Object>> buyers = new ArrayList<HashMap<String, Object>>();
			List<String> stringBuyers = new ArrayList<String>();
			
			for(POrder porder : allPOrder) {
				if(!stringBuyers.contains(porder.getBuyername())) {
					HashMap<String, Object> temp = new HashMap<>();
					stringBuyers.add(porder.getBuyername());
					temp.put("buyername", porder.getBuyername());
					
					PContract pcontract = pcontractService.findOne(porder.getPcontractid_link());
					Long orgbuyerid_link = pcontract.getOrgbuyerid_link();
					temp.put("orgbuyerid_link", orgbuyerid_link);
					buyers.add(temp);
				}
			}
			
			response.data = buyers;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderList_getListString_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<POrderList_getListString_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getallvendorname",method = RequestMethod.POST)
	public ResponseEntity<POrderList_getListString_response> POrderGetAllVendor(HttpServletRequest request ) {
		POrderList_getListString_response response = new POrderList_getListString_response();
		try {
			
			List<POrder> allPOrder = porderService.findAll();
			List<HashMap<String, Object>> vendors = new ArrayList<HashMap<String, Object>>();
			List<String> stringVendors = new ArrayList<String>();
			
			for(POrder porder : allPOrder) {
				if(!stringVendors.contains(porder.getVendorname())) {
					HashMap<String, Object> temp = new HashMap<>();
					stringVendors.add(porder.getVendorname());
					temp.put("vendorname", porder.getVendorname());
					
					PContract pcontract = pcontractService.findOne(porder.getPcontractid_link());
					Long orgvendorid_link = pcontract.getOrgvendorid_link();
					temp.put("orgvendorid_link", orgvendorid_link);
					vendors.add(temp);
				}
			}
			
			response.data = vendors;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderList_getListString_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<POrderList_getListString_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	
}
