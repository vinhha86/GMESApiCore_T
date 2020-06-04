package vn.gpay.gsmart.core.api.branch;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.branch.Branch;
import vn.gpay.gsmart.core.branch.IBranchService;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/branch")
public class BranchAPI {
	@Autowired IBranchService branchservice;
	
	@RequestMapping(value = "/getall", method = RequestMethod.POST)
	public ResponseEntity<Branch_getall_response> Product_GetAll(HttpServletRequest request,
			@RequestBody Branch_getall_request entity) {
		Branch_getall_response response = new Branch_getall_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			List<Branch> branch = branchservice.getall_byorgrootid(user.getRootorgid_link());
			
			if(entity.isAll) {
				Branch b = new Branch();
				b.setId((long)0);
				b.setName("Tất cả");
				branch.add(0, b);
			}
			
			response.data = branch;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Branch_getall_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Branch_getall_response>(response, HttpStatus.OK);
		}
	}
}
