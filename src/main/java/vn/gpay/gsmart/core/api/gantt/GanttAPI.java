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
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
import vn.gpay.gsmart.core.porder_grant.POrderGrant_Service;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/gantt")
public class GanttAPI {
	@Autowired OrgServiceImpl orgService;
	@Autowired POrder_Service porderService;
	@Autowired POrderGrant_Service granttService;
	
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
				gantt_org.setCode(org_factory.getCode());
				gantt_org.setId_origin(org_factory.getId());
				gantt_org.setLeaf(false);
				gantt_org.setName(org_factory.getName());
				gantt_org.setParentId(null);
				gantt_org.setRollup(false);
				gantt_org.setIconCls("x-fa fa-home");
				gantt_org.setOrgtypeid_link(org_factory.getOrgtypeid_link());
				gantt_org.setParentid_origin(0);
				
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
					gant_orggrantt.setRollup(true);
					gant_orggrantt.setIconCls("x-fa fa-home");
					gant_orggrantt.setCode(org_grantt.getCode());
					gant_orggrantt.setOrgtypeid_link(org_grantt.getOrgtypeid_link());
					gant_orggrantt.setParentid_origin(org_factory.getId());
					
					
					//Lay nhung lenh cua cac to 
					List<POrderGrant> list_porder = granttService.get_granted_bygolivedate(startdate, todate, org_grantt.getId());
					
					for(POrderGrant porder_grant : list_porder) {
						
						if(startdate_grantt==null) {
							startdate_grantt = porder_grant.getProductiondate();
						} else {
							startdate_grantt = startdate_grantt.compareTo(porder_grant.getProductiondate()) > 0 ? porder_grant.getProductiondate() : startdate_grantt;
						}
						
						if(enddate_grantt==null) {
							enddate_grantt = porder_grant.getGolivedate();
						} else {
							enddate_grantt = enddate_grantt.compareTo(porder_grant.getGolivedate()) < 0 ? porder_grant.getGolivedate() : enddate_grantt;
						}
						
						if(startdate_org==null) {
							startdate_org = porder_grant.getProductiondate();
						} else {
							startdate_org = startdate_org.compareTo(porder_grant.getProductiondate()) > 0 ? porder_grant.getProductiondate() : startdate_org;
						}
						
						if(enddate_org==null) {
							enddate_org = porder_grant.getGolivedate();
						} else {
							enddate_org = enddate_org.compareTo(porder_grant.getGolivedate()) < 0 ? porder_grant.getGolivedate() : enddate_org;
						}
						
						PContract_PO_Gantt gant_porder = new PContract_PO_Gantt();
						id++;
						
						gant_porder.setExpanded(false);
						gant_porder.setId(id);
						gant_porder.setId_origin(org_grantt.getId());
						gant_porder.setLeaf(false);
						gant_porder.setName(porder_grant.getOrdercode());
						gant_porder.setRollup(true);
						gant_porder.setIconCls("x-fa fa-industry");
						gant_porder.setClss(porder_grant.getCls());
						gant_porder.setStartDate(porder_grant.getProductiondate());
						gant_porder.setEndDate(porder_grant.getGolivedate());
						gant_porder.setMahang(porder_grant.getMaHang());
						
						//Tao ke hoach cua lenh
						PContract_PO_Gantt gant_porderKH = new PContract_PO_Gantt();
						id++;
						
						gant_porderKH.setExpanded(false);
						gant_porderKH.setId(id);
						gant_porderKH.setId_origin(org_grantt.getId());
						gant_porderKH.setLeaf(true);
						gant_porderKH.setName("Kế hoạch");
						gant_porderKH.setRollup(false);
						gant_porderKH.setIconCls("x-fa fa-calendar-edit");
						gant_porderKH.setStartDate(porder_grant.getProductiondate());
						gant_porderKH.setEndDate(porder_grant.getGolivedate());
						gant_porderKH.setTotalpackage(porder_grant.getTotalpackage());
						gant_porderKH.setMahang(porder_grant.getMaHang());
						
						gant_porder.getChildren().add(gant_porderKH);						
						
						gant_orggrantt.getChildren().add(gant_porder);
					}
					gant_orggrantt.setStartDate(startdate_grantt);
					gant_orggrantt.setEndDate(enddate_grantt);
					gantt_org.getChildren().add(gant_orggrantt);
				}
				
				//Them lenh free vao moi nha may
				PContract_PO_Gantt gant_folderfree = new PContract_PO_Gantt();
				Date start_gant_folderfree = null, end_gant_folderfree = null;
				
				id++;
				gant_folderfree.setExpanded(false);
				gant_folderfree.setId(id);
				gant_folderfree.setId_origin(0);
				gant_folderfree.setLeaf(false);
				gant_folderfree.setName("Lệnh chưa phân");
				gant_folderfree.setRollup(false);
				gant_folderfree.setIconCls("x-fa fa-industry");
				gant_folderfree.setParentid_origin(org_factory.getId());
				
				//Lay nhung lenh chua phan chuyen cua moi nha may
				List<POrder> listporder_free = porderService.get_free_bygolivedate(startdate, todate, org_factory.getId());
				for(POrder porder_free : listporder_free) {
					
					if(start_gant_folderfree==null) {
						start_gant_folderfree = porder_free.getProductiondate();
					}
					else {
						start_gant_folderfree = start_gant_folderfree.compareTo(porder_free.getProductiondate()) > 0 ? porder_free.getProductiondate() : start_gant_folderfree;
					}
					
					if(end_gant_folderfree==null) {
						end_gant_folderfree = porder_free.getGolivedate();
					}
					else {
						end_gant_folderfree = end_gant_folderfree.compareTo(porder_free.getGolivedate()) < 0 ? porder_free.getGolivedate() : end_gant_folderfree;
					}
					
					if(startdate_org==null) {
						startdate_org = porder_free.getProductiondate();
					}
					else {
						startdate_org = startdate_org.compareTo(porder_free.getProductiondate()) > 0 ? porder_free.getProductiondate() : startdate_org;
					}
					
					if(enddate_org==null) {
						enddate_org = porder_free.getGolivedate();
					}
					else {
						enddate_org = enddate_org.compareTo(porder_free.getGolivedate()) < 0 ? porder_free.getGolivedate() : enddate_org;
					}
					
					PContract_PO_Gantt gantt_porderfree = new PContract_PO_Gantt();
					
					id++;
					gantt_porderfree.setExpanded(false);
					gantt_porderfree.setId(id);
					gantt_porderfree.setId_origin(porder_free.getId());
					gantt_porderfree.setLeaf(true);
					gantt_porderfree.setName(porder_free.getOrdercode());
					gantt_porderfree.setRollup(false);
					gantt_porderfree.setIconCls("x-fa fa-industry");
					gantt_porderfree.setStartDate(porder_free.getProductiondate());
					gantt_porderfree.setEndDate(porder_free.getGolivedate());
					gantt_porderfree.setParentid_origin(0);
					gantt_porderfree.setTotalpackage(porder_free.getTotalorder());
									
					
					gant_folderfree.getChildren().add(gantt_porderfree);
				}
				
				gant_folderfree.setStartDate(start_gant_folderfree);
				gant_folderfree.setEndDate(end_gant_folderfree);
				gantt_org.getChildren().add(gant_folderfree);
				
				gantt_org.setStartDate(startdate_org);
				gantt_org.setEndDate(enddate_org);
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
