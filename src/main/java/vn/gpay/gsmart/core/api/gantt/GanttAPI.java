package vn.gpay.gsmart.core.api.gantt;

import java.util.ArrayList;
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

import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.org.OrgServiceImpl;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO_Gantt;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder.POrder_Service;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/gantt")
public class GanttAPI {
	@Autowired OrgServiceImpl orgService;
	@Autowired POrder_Service porderService;
	
	@RequestMapping(value = "/getporder_po_gantt",method = RequestMethod.POST)
	public ResponseEntity<gantt_getbydate_porder_po_response> GetAll(HttpServletRequest request,
			@RequestBody gantt_getbydate_porder_po_request entity) {
		gantt_getbydate_porder_po_response response = new gantt_getbydate_porder_po_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgid_link = user.getOrgid_link();
			Date startdate = entity.porder_from;
			Date todate = entity.porder_to;
			
			String[] listtype = entity.listid.split(",");
			List<String> list = new ArrayList<String>();
			for (String string : listtype) {
				list.add(string);
			}
			
			//Lay danh sach nhung don vi duoc phep xem cua user dang dang nhap
			List<Org> listorg = orgService.getorgChildrenbyOrg(orgid_link, list);
			
			List<PContract_PO_Gantt> list_gantt = new ArrayList<PContract_PO_Gantt>();
			//tao id tu tang 
			long id = 0;
			
			for(Org org_factory : listorg) {
				long orgid = org_factory.getId();
				Date startdate_org = null, enddate_org = null;
				
				id++;
				//Sinh Nha may
				PContract_PO_Gantt gantt_org = new PContract_PO_Gantt();
				gantt_org.setExpanded(false);
				gantt_org.setId(id);
				gantt_org.setId_origin(org_factory.getId());
				gantt_org.setLeaf(false);
				gantt_org.setName(org_factory.getName());
				gantt_org.setParentId(null);
				gantt_org.setRollup(false);
				gantt_org.setIconCls("x-fa fa-home");
				
				//lay cac to cua nha may
				List<Org> listorg_grantt = orgService.getorgChildrenbyOrg(orgid, list);
				for(Org org_grantt : listorg_grantt) {
					Date startdate_grantt = null, enddate_grantt = null;
					
					PContract_PO_Gantt gant_orggrantt = new PContract_PO_Gantt();
					id++;
					
					gant_orggrantt.setExpanded(false);
					gant_orggrantt.setId(id);
					gant_orggrantt.setId_origin(org_grantt.getId());
					gant_orggrantt.setLeaf(false);
					gant_orggrantt.setName(org_grantt.getName());
					gant_orggrantt.setRollup(false);
					gant_orggrantt.setIconCls("x-fa fa-home");
					
					gantt_org.getChildren().add(gant_orggrantt);
				}
				
				//Them lenh free vao moi nha may
				PContract_PO_Gantt gant_folderfree = new PContract_PO_Gantt();
				
				id++;
				gant_folderfree.setExpanded(false);
				gant_folderfree.setId(id);
				gant_folderfree.setId_origin(0);
				gant_folderfree.setLeaf(false);
				gant_folderfree.setName("Lệnh chưa phân");
				gant_folderfree.setRollup(false);
				gant_folderfree.setIconCls("x-fa fa-industry");
				
				//Lay nhung lenh chua phan chuyen cua moi nha may
				List<POrder> listporder_free = porderService.get_free_bygolivedate(startdate, todate, org_factory.getId());
				for(POrder porder_free : listporder_free) {
					PContract_PO_Gantt gantt_porderfree = new PContract_PO_Gantt();
					
					id++;
					gantt_porderfree.setExpanded(false);
					gantt_porderfree.setId(id);
					gantt_porderfree.setId_origin(porder_free.getId());
					gantt_porderfree.setLeaf(false);
					gantt_porderfree.setName(porder_free.getOrdercode());
					gantt_porderfree.setRollup(false);
					gantt_porderfree.setIconCls("x-fa fa-industry");
					gantt_porderfree.setStartDate(porder_free.getProductiondate());
					gantt_porderfree.setEndDate(porder_free.getGolivedate());
					
					gant_folderfree.getChildren().add(gantt_porderfree);
				}
				
				gantt_org.getChildren().add(gant_folderfree);
				
				list_gantt.add(gantt_org);
			}
			
			response.children = list_gantt;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<gantt_getbydate_porder_po_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<gantt_getbydate_porder_po_response>(HttpStatus.OK);
		}
	}
}
