package vn.gpay.gsmart.core.api.invoice;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.base.ResponseError;
import vn.gpay.gsmart.core.invoice.IInvoiceDService;
import vn.gpay.gsmart.core.invoice.IInvoiceService;
import vn.gpay.gsmart.core.invoice.Invoice;
import vn.gpay.gsmart.core.invoice.InvoiceD;
import vn.gpay.gsmart.core.packinglist.IPackingListService;
import vn.gpay.gsmart.core.packinglist.PackingList;
import vn.gpay.gsmart.core.security.GpayAuthentication;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.sku.SKU;
import vn.gpay.gsmart.core.stocking_uniquecode.IStocking_UniqueCode_Service;
import vn.gpay.gsmart.core.stocking_uniquecode.Stocking_UniqueCode;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/invoice")
public class InvoiceAPI {

	@Autowired IInvoiceService invoiceService;
	@Autowired IInvoiceDService invoiceDService;
	@Autowired IPackingListService packingListService;
	@Autowired ISKU_Service skuService;
	@Autowired Common commonService;
	@Autowired IStocking_UniqueCode_Service stockingService;
	
	@RequestMapping(value = "/invoice_create",method = RequestMethod.POST)
	public ResponseEntity<?> InvoiceCreate(@RequestBody InvoiceCreateRequest entity,HttpServletRequest request ) {
		Invoice_create_response response = new Invoice_create_response();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			boolean isNew = false;
			
			Invoice invoice = entity.data;
			 if(invoice.getId()==null) {
				 isNew = true;
				 invoice.setInvoicenumber(commonService.getInvoiceNumber());
				 invoice.setUsercreateid_link(user.getUserId());
				 invoice.setTimecreate(new Date());
				 invoice.setOrgrootid_link(user.getRootorgid_link());
				 invoice.setStatus(1);
			    }else {
			    	invoice.setLastuserupdateid_link(user.getUserId());
			    	invoice.setLasttimeupdate(new Date());
			    }
			 
			 invoice.getInvoice_d().forEach(invoiced -> {
				 if(invoiced.getId()==null) {
					 invoiced.setUsercreateid_link(user.getUserId());
					 invoiced.setTimecreate(new Date());
					 invoiced.setOrgrootid_link(user.getRootorgid_link());
				 }else {
					 invoiced.setOrgrootid_link(user.getRootorgid_link());
					 invoiced.setLastuserupdateid_link(user.getUserId());
				     invoiced.setLasttimeupdate(new Date());
				 }
				 
//				 invoiced.getPackinglist().forEach(pklist -> {
//					 float netweight = 0 ,grossweight = 0  ,yds = 0  ;
//					 grossweight += (float)pklist.getGrossweight();
//					 netweight += pklist.getNetweight();
//					 yds += pklist.getYdsorigin();
//					 
//					 if(pklist.getId()==null) {
//						 pklist.setUsercreateid_link(user.getUserId());
//						 pklist.setTimecreate(new Date());
//						 pklist.setOrgrootid_link(user.getRootorgid_link());
//						 pklist.setSkuid_link(invoiced.getSkuid_link());
//						 pklist.setInvoiceid_link(invoice.getId());
//					 }else {
//						 pklist.setLastuserupdateid_link(user.getUserId());
//						 pklist.setLasttimeupdate(new Date());
//					 }
//					 invoiced.setGrossweight(grossweight);
//					 invoiced.setNetweight(netweight);
//					 invoiced.setYds(yds);
//				 });
				 //totalgrossweight +=invoiced.getGrossweight();
				// totalnetweight +=invoiced.getNetweight();
				// totalpackage = invoiced.getTotalpackage();
		    	});
			// invoice.setTotalgrossweight(totalgrossweight);
			// invoice.setTotalnetweight(totalnetweight);
			// invoice.setTotalm3(totalm3);
			// invoice.setTotalpackage(totalpackage);
			 Invoice _invoice = invoiceService.save(invoice);
			 
			 //update lai stocking 
			if(isNew) {
				Stocking_UniqueCode unique = stockingService.getby_type(1);
				unique.setStocking_max(unique.getStocking_max()+ 1);
				stockingService.save(unique);
			}
			 
			 response.id = _invoice.getId();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/invoice_getbyid",method = RequestMethod.POST)
	public ResponseEntity<?> InvoiceGetById(@RequestBody invoice_getbyid_request entity,HttpServletRequest request ) {
		invoice_getbyid_response response = new invoice_getbyid_response();
		try {
			response.data = invoiceService.findOne(entity.id);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<invoice_getbyid_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/invoice_insertsku",method = RequestMethod.POST)
	public ResponseEntity<?> InsertSKU(@RequestBody invoice_insertsku_request entity,HttpServletRequest request ) {
		invoice_insertsku_response response = new invoice_insertsku_response();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			long orgrootid_link = user.getRootorgid_link();
			long invoiceid_link = entity.invoiceid_link;
			Invoice invoice = invoiceService.findOne(entity.invoiceid_link);
			
			for(Long skuid_link : entity.list_skuid_link) {
				//kiem tra trong invoice-d co sku chua chua co thi them vao
				List<InvoiceD> list_invoiced = invoiceDService.get_invoiced_bysku(invoiceid_link, skuid_link);
				
				if(list_invoiced.size() == 0) {
					
					invoice.setLasttimeupdate(new Date());
					invoice.setLastuserupdateid_link(user.getUserId());
					
					InvoiceD invoice_d = new InvoiceD();
//					invoice_d.setColorid_link(sku.getColor_id());
					invoice_d.setFoc((float)0);
					invoice_d.setGrossweight((float)0);
					invoice_d.setId(null);
					invoice_d.setInvoiceid_link(invoiceid_link);
					invoice_d.setM3((float)0);
					invoice_d.setNetweight((float)0);
					invoice_d.setOrgrootid_link(orgrootid_link);
//					invoice_d.setSizeid_link(sku.getSize_id());
					invoice_d.setSkuid_link(skuid_link);
					invoice_d.setTimecreate(new Date());
					invoice_d.setTotalamount((float)0);
					invoice_d.setTotalpackage(0);
//					invoice_d.setUnitid_link(sku.getUnitid_link());
					invoice_d.setUnitprice((float)0);
					invoice_d.setUsercreateid_link(user.getUserId());
					invoice_d.setYds((float)0);
					
					invoice.getInvoice_d().add(invoice_d);
					
				}
			}

			invoiceService.save(invoice);
			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<invoice_insertsku_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/invoice_insertpkl",method = RequestMethod.POST)
	public ResponseEntity<?> InsertPKL(@RequestBody invoice_insertpkl_request entity,HttpServletRequest request ) {
		invoice_insertsku_response response = new invoice_insertsku_response();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			long orgrootid_link = user.getRootorgid_link();
			long invoiceid_link = entity.data.getInvoiceid_link();
			Invoice invoice = invoiceService.findOne(invoiceid_link);
			
			//Cập nhật lại invoice
			invoice.setLasttimeupdate(new Date());
			invoice.setLastuserupdateid_link(user.getUserId());

			invoiceService.save(invoice);
			
			//Thêm vào Packinglist
			SKU sku = skuService.findOne(entity.data.getSkuid_link());
			
			PackingList pkl = entity.data;
			
			
			if(pkl.getId() == null) {
				pkl.setColorid_link(sku.getColor_id());
				pkl.setTimecreate(new Date());
				pkl.setUsercreateid_link(user.getUserId());
				pkl.setOrgrootid_link(orgrootid_link);
			}
			else {
				pkl.setLasttimeupdate(new Date());
				pkl.setLastuserupdateid_link(user.getUserId());
			}
			
			packingListService.save(pkl);
			
			//Cap nhat lai invoiced
			
			InvoiceD invoiced = invoiceDService.findOne(pkl.getInvoicedid_link());
			Float m3 = (float)0;
			Float gw = (float)0;
			Float nw = (float)0;
			Float yds = (float)0;
			Integer amount = 0;
			
			List<PackingList> list_pkl = packingListService.getPKL_by_invoiced(pkl.getInvoicedid_link());
			for(PackingList packinglist : list_pkl) {
				m3 += packinglist.getM3();
				gw += packinglist.getGrossweight();
				nw += packinglist.getNetweight();
				yds += packinglist.getYdsorigin();
				amount ++;
			}
			
			invoiced.setM3(m3);
			invoiced.setGrossweight(gw);
			invoiced.setNetweight(nw);
			invoiced.setYds(yds);
			invoiced.setTotalpackage(amount);
			
			invoiceDService.save(invoiced);
			
			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<invoice_insertsku_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getlist_bypage",method = RequestMethod.POST)
	public ResponseEntity<?> InvoiceGetList_bypage(@RequestBody invoice_getlist_request entity,HttpServletRequest request ) {
		invoice_getlist_response response = new invoice_getlist_response();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			long orgrootid_link = user.getRootorgid_link();
			String invoicenumber = entity.invoicenumber;
			String custom_declaration = entity.custom_declaration;
			Date invociedate_from = entity.invociedate_from;
			Date invoicedate_to = entity.invoicedate_to;
			long org_prodviderid_link = entity.org_prodviderid_link;
			int status = entity.status;
			int page = entity.page;
			int limit = entity.limit;
			
			Page<Invoice> pageInvcoie = invoiceService.getlist_bypage(orgrootid_link, invoicenumber, custom_declaration, invociedate_from, 
					invoicedate_to, org_prodviderid_link, status, page, limit);
			
			response.data = pageInvcoie.getContent();
			response.totalCount = pageInvcoie.getTotalElements();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<invoice_getlist_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getlotnumber",method = RequestMethod.POST)
	public ResponseEntity<?> InvoiceGetLotNumber(@RequestBody invoice_getlotnumber_request entity,HttpServletRequest request ) {
		invoice_getlotnumber_response response = new invoice_getlotnumber_response();
		try {
			long invoicedid_link = entity.invoicedid_link;
			response.data = packingListService.getLotNumber_byinvoiced(invoicedid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<invoice_getlotnumber_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getpkl_bylotnumber",method = RequestMethod.POST)
	public ResponseEntity<?> InvoiceGetPKL_byLotNumber(@RequestBody invoice_getpkl_bylotnumber_request entity,HttpServletRequest request ) {
		invoice_getpkl_bylotnumber_response response = new invoice_getpkl_bylotnumber_response();
		try {
			long invoicedid_link = entity.invoicedid_link;
			String lotnumber = entity.lotnumber;
			
			response.data = packingListService.getbylotnumber(lotnumber, invoicedid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<invoice_getpkl_bylotnumber_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/invoice_list_comming",method = RequestMethod.POST)
	public ResponseEntity<?> InvoiceListComming(@RequestBody InvoiceListCommingRequest entity,HttpServletRequest request ) {
		InvoiceResponse response = new InvoiceResponse();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			response.data = invoiceService.InvoiceListComming(user.getRootorgid_link(),entity.stockcode, entity.orgfrom_code, entity.invoicenumber, entity.shipdateto_from, entity.shipdateto_to,entity.status);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<InvoiceResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/invoice_deletebyid",method = RequestMethod.POST)
	public ResponseEntity<?> DeleteInvoiceById(@RequestBody GetInvoiceByID entity, HttpServletRequest request ) {
		InvoiceByIDResponse response = new InvoiceByIDResponse();
		try {
			invoiceService.deleteById(entity.invoiceid);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/invoiced_deletebyid",method = RequestMethod.POST)
	public ResponseEntity<?> DeleteInvoiceDById(@RequestBody GetInvoiceByID entity, HttpServletRequest request ) {
		InvoiceByIDResponse response = new InvoiceByIDResponse();
		try {
			invoiceDService.deleteById(entity.invoiceid);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	//cũ
	@RequestMapping(value = "/getInvoice",method = RequestMethod.POST)
	public ResponseEntity<?> getInvoice(HttpServletRequest request ) {
		InvoiceResponse output = new InvoiceResponse();
		try {
			output.data = invoiceService.findAll();
			output.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			output.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<InvoiceResponse>(output,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/getInvoiceByID",method = RequestMethod.POST)
	public ResponseEntity<?> getInvoiceById(@RequestBody GetInvoiceByID entity, HttpServletRequest request ) {
		InvoiceByIDResponse output = new InvoiceByIDResponse();
		try {
			output.data = invoiceService.findOne(entity.invoiceid);
			output.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			output.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<InvoiceByIDResponse>(output,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getInvoiceByCode",method = RequestMethod.POST)
	public ResponseEntity<?> GetStockinByCode( @RequestBody GetInvoiceByCode entity,HttpServletRequest request ) {
		InvoiceResponse output = new InvoiceResponse();
		try {
			List<Invoice> listinv = invoiceService.findByInvoicenumber(entity.invoicecode);
			output.data = listinv;
			if(listinv!=null && listinv.size()>0) {
				packingListService.inv_getbyid(listinv.get(0).getId());
			}
			output.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			output.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<InvoiceResponse>(output,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getInvoiceDBySkuCode",method = RequestMethod.POST)
	public ResponseEntity<?> getInvoiceDBySkuCode( @RequestBody GetInvoiceDBySkuCodeRequest entity,HttpServletRequest request ) {
		InvoiceDResponse response = new InvoiceDResponse();
		try {
			if(entity.invoicenumber!=null) {
				response.data = invoiceService.findInvoiceDBySkuCode(entity.invoicenumber, entity.skucode);
			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<InvoiceDResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
