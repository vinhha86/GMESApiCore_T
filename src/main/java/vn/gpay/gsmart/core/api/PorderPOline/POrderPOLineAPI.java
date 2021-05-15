package vn.gpay.gsmart.core.api.PorderPOline;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porders_poline.IPOrder_POLine_Service;
import vn.gpay.gsmart.core.porders_poline.POrder_POLine;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/porderpoline")
public class POrderPOLineAPI {
	@Autowired IPOrder_POLine_Service porder_line_Service;
	
	
	@RequestMapping(value = "/add_porder",method = RequestMethod.POST)
	public ResponseEntity<add_porder_response> AddPorder(@RequestBody add_porder_request entity,HttpServletRequest request ) {
		add_porder_response response = new add_porder_response();
		try {
			Long pcontract_poid_link = entity.pcontract_poid_link;
			
			for(POrder porder : entity.data) {
				POrder_POLine porder_line = new POrder_POLine();
				porder_line.setId(null);
				porder_line.setPcontract_poid_link(pcontract_poid_link);
				porder_line.setPorderid_link(porder.getId());
				
				porder_line_Service.save(porder_line);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
		    return new ResponseEntity<add_porder_response>(response, HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<add_porder_response>(response, HttpStatus.OK);
		}
	}  
	
	@RequestMapping(value = "/getporder_by_po",method = RequestMethod.POST)
	public ResponseEntity<getporder_by_po_response> GetPOrderByPO(@RequestBody getporder_by_po_request entity,HttpServletRequest request ) {
		getporder_by_po_response response = new getporder_by_po_response();
		try {
			Long pcontract_poid_link = entity.pcontract_poid_link;
			response.data = porder_line_Service.getporder_by_po(pcontract_poid_link);			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
		    return new ResponseEntity<getporder_by_po_response>(response, HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<getporder_by_po_response>(response, HttpStatus.OK);
		}
	}  
}
