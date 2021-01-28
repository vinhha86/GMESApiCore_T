package vn.gpay.gsmart.core.api.cutplan;

import java.util.Date;
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

import vn.gpay.gsmart.core.cutplan.CutPlan;
import vn.gpay.gsmart.core.cutplan.CutPlan_Row;
import vn.gpay.gsmart.core.cutplan.ICutPlan_Row_Service;
import vn.gpay.gsmart.core.cutplan.ICutPlan_Service;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.CutPlanRowType;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/cutplan")
public class CutPlanAPI {
	@Autowired ICutPlan_Service cutplanService;
	@Autowired ICutPlan_Row_Service cutplanrowService;
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<create_cutplan_response> CreateCutPlan(HttpServletRequest request,
			@RequestBody create_cutplan_request entity) {
		create_cutplan_response response = new create_cutplan_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Long skuid_link = entity.skuid_link;
			Long porderid_link = entity.porderid_link;
			Long orgrootid_link = user.getRootorgid_link();
			Date current = new Date();
			
			//Kiem tra xem npl da co so do hay chua
			List<CutPlan> list_cutplan = cutplanService.getby_sku_and_porder(skuid_link, porderid_link, orgrootid_link);
			if(list_cutplan.size() == 0) {
				//tao 2 row mac dinh de them vao plan
				CutPlan_Row row_yeucau = new CutPlan_Row();
				row_yeucau.setCode("SL yêu cầu");
				row_yeucau.setId(null);
				row_yeucau.setName("SL yêu cầu");
				row_yeucau.setType(CutPlanRowType.yeucau);
				row_yeucau.setNgay(current);
				
				row_yeucau = cutplanrowService.save(row_yeucau);
				
				CutPlan_Row row_catdu = new CutPlan_Row();
				row_catdu.setCode("SL cắt dư");
				row_catdu.setId(null);
				row_catdu.setName("SL cắt dư");
				row_catdu.setType(CutPlanRowType.catdu);
				row_catdu.setNgay(current);
				
				row_catdu = cutplanrowService.save(row_catdu);
				
				//them vao plan
				CutPlan plan_yc = new CutPlan();
				plan_yc.setCreateddate(current);
				plan_yc.setCreateduserid_link(user.getId());
				plan_yc.setCutplanrowid_link(row_yeucau.getId());
				plan_yc.setId(null);
				plan_yc.setOrgrootid_link(orgrootid_link);
				plan_yc.setPorderid_link(porderid_link);
				plan_yc.setSkuid_link(skuid_link);
				
				cutplanService.save(plan_yc);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<create_cutplan_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<create_cutplan_response>(response, HttpStatus.OK);
		}
	}
}
