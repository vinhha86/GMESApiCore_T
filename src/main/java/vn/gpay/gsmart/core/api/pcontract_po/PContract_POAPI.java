package vn.gpay.gsmart.core.api.pcontract_po;

import java.util.ArrayList;
import java.util.Calendar;
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

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.pcontract_po_shipping.IPContract_PO_ShippingService;
import vn.gpay.gsmart.core.pcontract_po_shipping.PContract_PO_Shipping;
import vn.gpay.gsmart.core.pcontract_price.IPContract_Price_DService;
import vn.gpay.gsmart.core.pcontract_price.IPContract_Price_Service;
import vn.gpay.gsmart.core.pcontract_price.PContract_Price;
import vn.gpay.gsmart.core.pcontract_price.PContract_Price_D;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder_req.IPOrder_Req_Service;
import vn.gpay.gsmart.core.porder_req.POrder_Req;
import vn.gpay.gsmart.core.productpairing.IProductPairingService;
import vn.gpay.gsmart.core.productpairing.ProductPairing;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.task_object.ITask_Object_Service;
import vn.gpay.gsmart.core.task_object.Task_Object;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.POStatus;
import vn.gpay.gsmart.core.utils.POrderReqStatus;
import vn.gpay.gsmart.core.utils.ResponseMessage;
import vn.gpay.gsmart.core.utils.TaskObjectType_Name;


@RestController
@RequestMapping("/api/v1/pcontract_po")
 public class PContract_POAPI {
	@Autowired IPContract_POService pcontract_POService;
	@Autowired IPContract_Price_Service pcontractpriceService;
	@Autowired IPContract_Price_DService pcontractpriceDService;
	@Autowired IPOrder_Service porderService;
	@Autowired private IPOrder_Req_Service porder_req_Service;
	@Autowired IPContract_PO_ShippingService poshippingService;
	@Autowired Common commonService;
	@Autowired ITask_Object_Service taskobjectService;
	@Autowired IProductPairingService productpairService;
	@Autowired IPOrder_Req_Service reqService;
	
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public ResponseEntity<PContract_pocreate_response> PContractCreate(@RequestBody PContract_pocreate_request entity,HttpServletRequest request ) {
		PContract_pocreate_response response = new PContract_pocreate_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long usercreatedid_link = user.getId();
			long pcontractid_link = entity.pcontractid_link;
			
			PContract_PO pcontract_po = entity.data;
			if( pcontract_po.getId() == null) {
				pcontract_po.setOrgrootid_link(orgrootid_link);
				pcontract_po.setUsercreatedid_link(usercreatedid_link);
				pcontract_po.setDatecreated(new Date());
				pcontract_po.setPcontractid_link(pcontractid_link);
				pcontract_po.setStatus(POStatus.PO_STATUS_UNCONFIRM);
			}
//			else {
//				PContract_PO pcontract_po_old = pcontract_POService.findOne(pcontract_po.getId());
//				pcontract_po.setOrgrootid_link(pcontract_po_old.getOrgrootid_link());
//				pcontract_po.setUsercreatedid_link(pcontract_po_old.getUsercreatedid_link());
//				pcontract_po.setDatecreated(pcontract_po_old.getDatecreated());
//			}
			pcontract_po = pcontract_POService.save(pcontract_po);
			
			long pcontract_poid_link = pcontract_po.getId();
			//Update Price cua PO hien tai
			updatePriceList(usercreatedid_link, orgrootid_link, pcontractid_link,pcontract_poid_link,entity.data.getPcontract_price());
			
			//Update List Price cua cac PO co Shipdate sau PO hien tai va cua cung PContract va Product
			List<PContract_PO> listpo_latershipdate = pcontract_POService.getPO_LaterShipdate(orgrootid_link, pcontractid_link, pcontract_po.getProductid_link(), pcontract_po.getShipdate());
			for(PContract_PO thePO_Latershipdate: listpo_latershipdate){
				thePO_Latershipdate.setSewtarget_percent(pcontract_po.getSewtarget_percent());
				thePO_Latershipdate.setExchangerate(pcontract_po.getExchangerate());
				pcontract_POService.save(thePO_Latershipdate);
				updatePriceList(usercreatedid_link, orgrootid_link, pcontractid_link,thePO_Latershipdate.getId(),entity.data.getPcontract_price());
			}
			
			//Update POrder_Req
			List<POrder_Req> lst_porders = entity.po_orders;
//			String po_code = pcontract_po.getPo_vendor().length() > 0?pcontract_po.getPo_vendor():pcontract_po.getPo_buyer();
			for(POrder_Req porder : lst_porders) {
				if (null == porder.getId() || 0 == porder.getId()){
					//Them moi POrder
					POrder_Req porder_req = new POrder_Req();
					
					porder_req.setPcontractid_link(pcontractid_link);
					porder_req.setPcontract_poid_link(pcontract_poid_link);
					
					porder_req.setTotalorder(porder.getTotalorder());
					porder_req.setGranttoorgid_link(porder.getGranttoorgid_link());
					porder_req.setAmount_inset(porder.getAmount_inset());
					
					porder_req.setOrgrootid_link(orgrootid_link);
					porder_req.setProductid_link(porder.getProductid_link());
					porder_req.setOrderdate(new Date());
					porder_req.setUsercreatedid_link(user.getId());
					porder_req.setStatus(POrderReqStatus.STATUS_FREE);
					porder_req.setTimecreated(new Date());
					porder_req.setIs_calculate(porder.getIs_calculate());
					
					//Save to DB
					long porder_req_id_link = porder_req_Service.savePOrder_Req(porder_req);
					
					//Create taskboard
					long orgid_link = porder.getGranttoorgid_link();
					long tasktypeid_link = 0;
					List<Task_Object> list_object = new ArrayList<Task_Object>();
					
					Task_Object object_pcontract = new Task_Object();
					object_pcontract.setId(null);
					object_pcontract.setObjectid_link(pcontractid_link);
					object_pcontract.setOrgrootid_link(orgrootid_link);
					object_pcontract.setTaskobjecttypeid_link((long)TaskObjectType_Name.DonHang);
					list_object.add(object_pcontract);
					
					Task_Object object_pcontractpo = new Task_Object();
					object_pcontractpo.setId(null);
					object_pcontractpo.setObjectid_link(pcontract_poid_link);
					object_pcontractpo.setOrgrootid_link(orgrootid_link);
					object_pcontractpo.setTaskobjecttypeid_link((long)TaskObjectType_Name.DonHangPO);
					list_object.add(object_pcontractpo);
					
					Task_Object object_porder_req = new Task_Object();
					object_porder_req.setId(null);
					object_porder_req.setObjectid_link(porder_req_id_link);
					object_porder_req.setOrgrootid_link(orgrootid_link);
					object_porder_req.setTaskobjecttypeid_link((long)TaskObjectType_Name.YeuCauSanXuat);
					list_object.add(object_porder_req);
					
					commonService.CreateTask(orgrootid_link, orgid_link, usercreatedid_link, tasktypeid_link, list_object, null);
				} else {
					POrder_Req porder_req = porder_req_Service.findOne(porder.getId());
					porder_req.setTotalorder(porder.getTotalorder());
					porder_req.setIs_calculate(porder.getIs_calculate());
					//Save to DB
					porder_req_Service.savePOrder_Req(porder_req);
				}
			}
			
			//Response to Client
			response.id = pcontract_po.getId();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));	
			
			return new ResponseEntity<PContract_pocreate_response>(response, HttpStatus.OK);
			
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<PContract_pocreate_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	private void updatePriceList(Long usercreatedid_link, Long orgrootid_link, Long pcontractid_link, Long pcontract_poid_link, List<PContract_Price> list_price_new){
		//Xoa list price cu của PO
		List<PContract_Price> list_price = pcontractpriceService.getPrice_ByPO(pcontract_poid_link);
		for(PContract_Price price : list_price) {
			pcontractpriceService.delete(price);
		}
		List<PContract_Price_D> list_price_d = pcontractpriceDService.getPrice_D_ByPO(pcontract_poid_link);
		for(PContract_Price_D price_d : list_price_d) {
			pcontractpriceDService.delete(price_d);
		}

		//them Price list moi
		for(PContract_Price price : list_price_new) {
			PContract_Price newPrice = new PContract_Price();
//			newPrice.setId(null);
			newPrice.setPcontract_poid_link(pcontract_poid_link);
			newPrice.setPcontractid_link(pcontractid_link);
			newPrice.setOrgrootid_link(orgrootid_link);
			
			newPrice.setProductid_link(price.getProductid_link());
			newPrice.setSizesetid_link(price.getSizesetid_link());
			newPrice.setPrice_cmp(price.getPrice_cmp());
			newPrice.setPrice_fob(price.getPrice_fob());
			newPrice.setPrice_sewingcost(price.getPrice_sewingcost());
			newPrice.setPrice_sewingtarget(price.getPrice_sewingtarget());
			newPrice.setPrice_vendortarget(price.getPrice_vendortarget());
			newPrice.setTotalprice(price.getTotalprice());
			newPrice.setSalaryfund(price.getSalaryfund());
			newPrice.setQuantity(price.getQuantity());
			newPrice.setIs_fix(price.getIs_fix());
			
			for(PContract_Price_D price_d: price.getPcontract_price_d()){
				PContract_Price_D newPrice_D = new PContract_Price_D();
				newPrice_D.setPcontract_poid_link(pcontract_poid_link);
				newPrice_D.setPcontractid_link(pcontractid_link);
				newPrice_D.setOrgrootid_link(orgrootid_link);
				
				newPrice_D.setProductid_link(price_d.getProductid_link());
				newPrice_D.setPrice(price_d.getPrice());
				newPrice_D.setCurrencyid_link(price_d.getCurrencyid_link());
				newPrice_D.setExchangerate(price_d.getExchangerate());
				newPrice_D.setCost(price_d.getCost());
				newPrice_D.setIsfob(price_d.getIsfob());
				newPrice_D.setFobpriceid_link(price_d.getFobpriceid_link());
				newPrice_D.setSizesetid_link(price_d.getSizesetid_link());
				newPrice_D.setQuota(price_d.getQuota());
				newPrice_D.setUnitprice(price_d.getUnitprice());
				newPrice_D.setUnitid_link(price_d.getUnitid_link());
				newPrice_D.setUsercreatedid_link(usercreatedid_link);
				newPrice_D.setDatecreated(new Date());
				
				newPrice.getPcontract_price_d().add(newPrice_D);
			}
			pcontractpriceService.save(newPrice);
		}		
	}
	
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public ResponseEntity<PContract_pocreate_response> PContractUpdate(@RequestBody PContract_pocreate_request entity,HttpServletRequest request ) {
		PContract_pocreate_response response = new PContract_pocreate_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long usercreatedid_link = user.getId();
			
			PContract_PO pcontract_po = entity.data;
			pcontract_po = pcontract_POService.save(pcontract_po);
			
			//Update POrder_Req
//			int total = 0;
			List<POrder_Req> lst_porders = entity.po_orders;
//			String po_code = pcontract_po.getPo_vendor().length() > 0?pcontract_po.getPo_vendor():pcontract_po.getPo_buyer();
			for(POrder_Req porder : lst_porders) {
//				total += porder.getTotalorder();
				if (null == porder.getId() || 0 == porder.getId()) {
					//Them moi POrder
					POrder_Req porder_req = new POrder_Req();
					
					porder_req.setPcontractid_link(pcontract_po.getPcontractid_link());
					porder_req.setPcontract_poid_link(pcontract_po.getId());
					
					porder_req.setTotalorder(porder.getTotalorder());
					porder_req.setGranttoorgid_link(porder.getGranttoorgid_link());
					porder_req.setAmount_inset(porder.getAmount_inset());
					
					porder_req.setOrgrootid_link(orgrootid_link);
					porder_req.setProductid_link(porder.getProductid_link());
					porder_req.setOrderdate(new Date());
					porder_req.setUsercreatedid_link(usercreatedid_link);
					porder_req.setStatus(POrderReqStatus.STATUS_FREE);
					porder_req.setTimecreated(new Date());
					
					//Save to DB
					porder_req_Service.savePOrder_Req(porder_req);
				} else {
					POrder_Req porder_req = porder_req_Service.findOne(porder.getId());
					porder_req.setTotalorder(porder.getTotalorder());
					//Save to DB
					porder_req_Service.savePOrder_Req(porder_req);
				}
			}
//			pcontract_po.setPo_quantity(total);
//			pcontract_po = pcontract_POService.save(pcontract_po);
			
			//Response to Client
			response.id = pcontract_po.getId();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));	
			
			return new ResponseEntity<PContract_pocreate_response>(response, HttpStatus.OK);
			
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<PContract_pocreate_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getbycontractproduct",method = RequestMethod.POST)
	public ResponseEntity<PContract_getbycontractproduct_response> getPOByContractProduct(@RequestBody PContract_getbycontractproduct_request entity,HttpServletRequest request ) {
		PContract_getbycontractproduct_response response = new PContract_getbycontractproduct_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long orgid_link = user.getOrgid_link();
			
			
			List<PContract_PO> pcontract = pcontract_POService.getPOByContractProduct(orgrootid_link, entity.pcontractid_link,
					entity.productid_link, user.getId(), orgid_link);
			response.data = pcontract;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContract_getbycontractproduct_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<PContract_getbycontractproduct_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/get_productiondate",method = RequestMethod.POST)
	public ResponseEntity<get_productiondate_response> getProductionDate(@RequestBody get_productiondate_request entity,HttpServletRequest request ) {
		get_productiondate_response response = new get_productiondate_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			Date date = entity.date_material;
			int amount = entity.amount_day + 1;
			int year = Calendar.getInstance().get(Calendar.YEAR);
			
			Date production_date = commonService.Date_Add_with_holiday(date, amount, orgrootid_link, year);
			response.productiondate = commonService.getBeginOfDate(production_date);
			response.duration = commonService.getDuration(response.productiondate, entity.shipdate, orgrootid_link, year);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<get_productiondate_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<get_productiondate_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getleafonly",method = RequestMethod.POST)
	public ResponseEntity<PContract_getbycontractproduct_response> getPOLeafOnly(@RequestBody PContract_getbycontractproduct_request entity,HttpServletRequest request ) {
		PContract_getbycontractproduct_response response = new PContract_getbycontractproduct_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long orgid_link = user.getOrgid_link();
			
			List<PContract_PO> pcontract = pcontract_POService.getPO_LeafOnly(orgrootid_link, entity.pcontractid_link, entity.productid_link, user.getId(), orgid_link);
			response.data = pcontract;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContract_getbycontractproduct_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<PContract_getbycontractproduct_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getleafonly_bycontract",method = RequestMethod.POST)
	public ResponseEntity<PContract_getbycontractproduct_response> getPOLeafOnly_ByPContract(@RequestBody PContract_getbycontractproduct_request entity,HttpServletRequest request ) {
		PContract_getbycontractproduct_response response = new PContract_getbycontractproduct_response();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			long orgrootid_link = user.getRootorgid_link();
			
			List<PContract_PO> pcontract = pcontract_POService.getPOLeafOnlyByContract(entity.pcontractid_link, entity.productid_link);
			if(entity.pcontractpo_id_link > 0) {
				pcontract.removeIf(c-> c.getId() != entity.pcontractpo_id_link);
			}
			response.data = pcontract;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContract_getbycontractproduct_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<PContract_getbycontractproduct_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getpo_offer_accept",method = RequestMethod.POST)
	public ResponseEntity<PContract_getbycontractproduct_response> getPOAccept(@RequestBody getoffer_accept_request entity,HttpServletRequest request ) {
		PContract_getbycontractproduct_response response = new PContract_getbycontractproduct_response();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			long orgrootid_link = user.getRootorgid_link();
			
			List<PContract_PO> pcontract = pcontract_POService.getPO_Offer_Accept_ByPContract(entity.pcontractid_link, entity.productid_link);
			response.data = pcontract;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContract_getbycontractproduct_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<PContract_getbycontractproduct_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/accept",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Accept(@RequestBody PContractPO_accept_request entity,
			HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long orgid_link = entity.orgid_link;
			long userid_link = user.getId();
			
			PContract_PO po = pcontract_POService.findOne(entity.pcontract_poid_link);
			if(po.getPo_quantity() != po.getAmount_org()) {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("Số lượng PO không trùng với số lượng phân về cho các xưởng!");
			    return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
			}
			po.setOrgmerchandiseid_link(entity.orgid_link);
			po.setMerchandiserid_link(entity.userid_link);
			po.setStatus(POStatus.PO_STATUS_CONFIRMED);
			
			pcontract_POService.save(po);	
			
			//Sinh PO
			//kiem tra po da co con hay chua thi moi sinh po con
			if(po.getSub_po().size() == 0) {
				PContract_PO ponew = new PContract_PO();
				ponew.setId(null);
				ponew.setCode(po.getCode());
				ponew.setActual_quantity(po.getActual_quantity());
				ponew.setActual_shipdate(po.getActual_shipdate());
				ponew.setCurrencyid_link(po.getCurrencyid_link());
				ponew.setDatecreated(new Date());
				ponew.setEtm_avr(po.getEtm_avr());
				ponew.setEtm_from(po.getEtm_from());
				ponew.setEtm_to(po.getEtm_to());
				ponew.setExchangerate(po.getExchangerate());
				ponew.setIs_tbd(po.getIs_tbd());
				ponew.setIsauto_calculate(po.getIsauto_calculate());
				ponew.setMatdate(po.getMatdate());
				ponew.setMerchandiserid_link(po.getMerchandiserid_link());
				ponew.setOrgmerchandiseid_link(po.getOrgmerchandiseid_link());
				ponew.setPackingnotice(po.getPackingnotice());
				ponew.setParentpoid_link(po.getId());
				ponew.setPcontractid_link(po.getPcontractid_link());
				ponew.setPo_buyer(po.getPo_buyer());
				ponew.setPo_vendor(po.getPo_vendor());
				ponew.setPortfromid_link(po.getPortfromid_link());
				ponew.setPorttoid_link(po.getPorttoid_link());
				ponew.setPrice_add(po.getPrice_add());
				ponew.setPrice_cmp(po.getPrice_cmp());
				ponew.setPrice_sweingfact(po.getPrice_sweingfact());
				ponew.setPrice_sweingtarget(po.getPrice_sweingtarget());
				ponew.setProductid_link(po.getProductid_link());
				ponew.setProductiondate(po.getProductiondate());
				ponew.setProductiondays(po.getProductiondays());
				ponew.setSalaryfund(po.getSalaryfund());
				ponew.setSewtarget_percent(po.getSewtarget_percent());
				ponew.setShipdate(po.getShipdate());
				ponew.setStatus(po.getStatus());
				ponew.setUnitid_link(po.getUnitid_link());
				ponew.setUsercreatedid_link(userid_link);
				ponew.setPo_quantity(po.getPo_quantity());
				ponew = pcontract_POService.save(ponew);
				
				List<POrder_Req> list_req = porder_req_Service.getByPO(po.getId());
				
				for(POrder_Req porder : list_req) {
//					
					POrder_Req porder_req = new POrder_Req();
					
					porder_req.setPcontractid_link(ponew.getPcontractid_link());
					porder_req.setPcontract_poid_link(ponew.getId());
					
					porder_req.setTotalorder(porder.getTotalorder());
					porder_req.setGranttoorgid_link(porder.getGranttoorgid_link());
					porder_req.setAmount_inset(porder.getAmount_inset());
					
					porder_req.setOrgrootid_link(orgrootid_link);
					porder_req.setProductid_link(porder.getProductid_link());
					porder_req.setOrderdate(new Date());
					porder_req.setUsercreatedid_link(userid_link);
					porder_req.setStatus(POrderReqStatus.STATUS_FREE);
					porder_req.setTimecreated(new Date());
					
					//Save to DB
					porder_req_Service.savePOrder_Req(porder_req);
				}
			}
			
			
			//Sinh Cong viec
			long pcontractid_link = po.getPcontractid_link();
			long pcontract_poid_link = po.getId();
			long productid_link = po.getProductid_link();
			
			
			
			long userinchargeid_link = entity.userid_link;
			//Kiem tra san pham co phai la san pham bo hay ko. Neu la san pham bo thi phai tao task cho tung san pham con
			List<ProductPairing> listpair = productpairService.getproduct_pairing_detail_bycontract(orgrootid_link, pcontractid_link, productid_link);
			if(listpair.size() == 0) {
				List<Task_Object> list_object = new ArrayList<Task_Object>();
				
				Task_Object object_pcontract = new Task_Object();
				object_pcontract.setId(null);
				object_pcontract.setObjectid_link(pcontractid_link);
				object_pcontract.setOrgrootid_link(orgrootid_link);
				object_pcontract.setTaskobjecttypeid_link((long)TaskObjectType_Name.DonHang);
				list_object.add(object_pcontract);
				
				Task_Object object_pcontractpo = new Task_Object();
				object_pcontractpo.setId(null);
				object_pcontractpo.setObjectid_link(pcontract_poid_link);
				object_pcontractpo.setOrgrootid_link(orgrootid_link);
				object_pcontractpo.setTaskobjecttypeid_link((long)TaskObjectType_Name.DonHangPO);
				list_object.add(object_pcontractpo);
				
				Task_Object object_product = new Task_Object();
				object_product.setId(null);
				object_product.setObjectid_link(productid_link);
				object_product.setOrgrootid_link(orgrootid_link);
				object_product.setTaskobjecttypeid_link((long)TaskObjectType_Name.SanPham);
				list_object.add(object_product);
				
				long tasktypeid_link_chitiet = 1; // chi tiet don hang
				commonService.CreateTask(orgrootid_link, orgid_link, userid_link, tasktypeid_link_chitiet, list_object, userinchargeid_link);
				
				long tasktypeid_link_haiquan = 2; // dinh muc hai quan
				commonService.CreateTask(orgrootid_link, orgid_link, userid_link, tasktypeid_link_haiquan, list_object, userinchargeid_link);
				
				long tasktypeid_link_candoi = 3; // dinh muc can doi
				commonService.CreateTask(orgrootid_link, orgid_link, userid_link, tasktypeid_link_candoi, list_object, userinchargeid_link);
			}
			else {
				for(ProductPairing pair : listpair) {
					List<Task_Object> list_object = new ArrayList<Task_Object>();
					
					Task_Object object_pcontract = new Task_Object();
					object_pcontract.setId(null);
					object_pcontract.setObjectid_link(pcontractid_link);
					object_pcontract.setOrgrootid_link(orgrootid_link);
					object_pcontract.setTaskobjecttypeid_link((long)TaskObjectType_Name.DonHang);
					list_object.add(object_pcontract);
					
					Task_Object object_pcontractpo = new Task_Object();
					object_pcontractpo.setId(null);
					object_pcontractpo.setObjectid_link(pcontract_poid_link);
					object_pcontractpo.setOrgrootid_link(orgrootid_link);
					object_pcontractpo.setTaskobjecttypeid_link((long)TaskObjectType_Name.DonHangPO);
					list_object.add(object_pcontractpo);
					
					Task_Object object_product = new Task_Object();
					object_product.setId(null);
					object_product.setObjectid_link(pair.getProductid_link());
					object_product.setOrgrootid_link(orgrootid_link);
					object_product.setTaskobjecttypeid_link((long)TaskObjectType_Name.SanPham);
					list_object.add(object_product);
					
					long tasktypeid_link_chitiet = 1; // chi tiet don hang
					commonService.CreateTask(orgrootid_link, orgid_link, userid_link, tasktypeid_link_chitiet, list_object, userinchargeid_link);
					
					long tasktypeid_link_haiquan = 2; // dinh muc hai quan
					commonService.CreateTask(orgrootid_link, orgid_link, userid_link, tasktypeid_link_haiquan, list_object, userinchargeid_link);
					
					long tasktypeid_link_candoi = 3; // dinh muc can doi
					commonService.CreateTask(orgrootid_link, orgid_link, userid_link, tasktypeid_link_candoi, list_object, userinchargeid_link);
				}
			}
			
			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage("Có lỗi trong quá trình xác nhận! Bạn vui lòng thử lại.");
		    return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getbycontract",method = RequestMethod.POST)
	public ResponseEntity<PContract_getbycontractproduct_response> getPOByContract(@RequestBody PContract_getbycontractproduct_request entity,HttpServletRequest request ) {
		PContract_getbycontractproduct_response response = new PContract_getbycontractproduct_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			
			List<PContract_PO> pcontract = pcontract_POService.getPOByContract(orgrootid_link, entity.pcontractid_link);
			response.data = pcontract;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContract_getbycontractproduct_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<PContract_getbycontractproduct_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getone",method = RequestMethod.POST)
	public ResponseEntity<PContract_pogetone_response> PContractGetOne(@RequestBody PContract_pogetone_request entity,HttpServletRequest request ) {
		PContract_pogetone_response response = new PContract_pogetone_response();
		try {
			
			response.data = pcontract_POService.findOne(entity.id); 
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContract_pogetone_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<PContract_pogetone_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> PContract_PODelete(@RequestBody PContract_podelete_request entity
			,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {

			PContract_PO thePO = pcontract_POService.findOne(entity.id);
			if (null != thePO){
				//Check if having POrder? refuse deleting if have
				if (porderService.getByContractAndPO(thePO.getPcontractid_link(), thePO.getId()).size() > 0){
					response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
					response.setMessage("Hiện vẫn đang có Lệnh SX của đơn hàng! Cần xóa hết Lệnh SX trước khi xóa đơn hàng");
					return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);				
				}
				
				//Delete POrder_Req
				for(POrder_Req thePOrder_Req: porder_req_Service.getByPO(thePO.getId())){
					porder_req_Service.delete(thePOrder_Req);
				}
				
				//Delete Shipping
				for(PContract_PO_Shipping theShipping: poshippingService.getByPOID(thePO.getId())){
					poshippingService.delete(theShipping);
				}
				
				//Delete PO Prices
				for(PContract_Price thePrice: thePO.getPcontract_price()){
					for (PContract_Price_D thePrice_D: thePrice.getPcontract_price_d()){
						pcontractpriceDService.delete(thePrice_D);
					}
					pcontractpriceService.delete(thePrice);
				}
				
				//Delete PO
				pcontract_POService.delete(thePO);
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("Đơn hàng không tồn tại");
				return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);				
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	    
	}
	
	@RequestMapping(value = "/getByContractAndProductBuyerCodeAndPOBuyer",method = RequestMethod.POST)
	public ResponseEntity<PContract_getbycontractproduct_response> getByContractAndProductBuyerCodeAndPOBuyer(@RequestBody PContract_getbycontractproductbuyercodepobuyer_request entity,HttpServletRequest request ) {
		PContract_getbycontractproduct_response response = new PContract_getbycontractproduct_response();
		try {
			List<PContract_PO> pcontractpo = pcontract_POService.getPcontractPoByPContractAndPOBuyer(entity.pcontractid_link, entity.po_buyer, entity.buyercode);
			response.data = pcontractpo;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContract_getbycontractproduct_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<PContract_getbycontractproduct_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
}
