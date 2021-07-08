package vn.gpay.gsmart.core.api.PorderPOline;

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

import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.pcontractproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKU;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_product_sku.IPOrder_Product_SKU_Service;
import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU;
import vn.gpay.gsmart.core.porders_poline.IPOrder_POLine_Service;
import vn.gpay.gsmart.core.porders_poline.POrder_POLine;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/porderpoline")
public class POrderPOLineAPI {
	@Autowired IPOrder_POLine_Service porder_line_Service;
	@Autowired IPOrder_Service porderService;
	@Autowired IPContract_POService poService;
	@Autowired IPOrder_Product_SKU_Service porderskuService;
	@Autowired IPContractProductSKUService pcontractskuService;
	
	@RequestMapping(value = "/add_porder",method = RequestMethod.POST)
	public ResponseEntity<add_porder_response> AddPorder(@RequestBody add_porder_request entity,HttpServletRequest request ) {
		add_porder_response response = new add_porder_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			Long pcontract_poid_link = entity.pcontract_poid_link;
			
			for(POrder porder : entity.data) {
				POrder_POLine porder_line = new POrder_POLine();
				porder_line.setId(null);
				porder_line.setPcontract_poid_link(pcontract_poid_link);
				porder_line.setPorderid_link(porder.getId());
				
				porder_line_Service.save(porder_line);
				
				//Cap nhat trang thai po sang da map voi lenh
				PContract_PO po = poService.findOne(pcontract_poid_link);
				po.setIsmap(true);
				poService.save(po);
				
				//Cap nhat lai so luong mau co trong porder_sku
				List<POrder_Product_SKU> list_porder_sku = porderskuService.getby_porder(porder.getId());
				for(POrder_Product_SKU pordersku : list_porder_sku) {
					porderskuService.delete(pordersku);
				}
				
				List<PContractProductSKU> list_pcontract_sku = pcontractskuService.getlistsku_bypo(pcontract_poid_link);
				int total = 0;
				for(PContractProductSKU pcontractsku : list_pcontract_sku) {
					POrder_Product_SKU pordersku = new POrder_Product_SKU();
					pordersku.setId(null);
					pordersku.setOrgrootid_link(orgrootid_link);
					pordersku.setPcontract_poid_link(pcontract_poid_link);
					pordersku.setPorderid_link(porder.getId());
					pordersku.setPquantity_granted(0);
					pordersku.setPquantity_porder(pcontractsku.getPquantity_porder());
					pordersku.setPquantity_production(pcontractsku.getPquantity_production());
					pordersku.setPquantity_sample(pcontractsku.getPquantity_sample());
					pordersku.setPquantity_total(pcontractsku.getPquantity_total());
					pordersku.setProductid_link(pcontractsku.getProductid_link());
					pordersku.setSkuid_link(pcontractsku.getSkuid_link());
					
					total += pcontractsku.getPquantity_total() == null ? 0 : pcontractsku.getPquantity_total();
				}
				
				//Cap nhat trang thai porder sang da map voi poline
				porder.setIsMap(true);
				porder.setTotalorder(total);
				porder.setGolivedate(po.getShipdate());
				porderService.save(porder);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
		    return new ResponseEntity<add_porder_response>(response, HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<add_porder_response>(response, HttpStatus.OK);
		}
	}  
	
	@RequestMapping(value = "/delete_porder",method = RequestMethod.POST)
	public ResponseEntity<delete_porder_response> DeletePorder(@RequestBody delete_porder_request entity,HttpServletRequest request ) {
		delete_porder_response response = new delete_porder_response();
		try {
			Long pcontract_poid_link = entity.pcontract_poid_link;
			List<POrder_POLine> list_porder = porder_line_Service.get_porderline_by_po(pcontract_poid_link);
			for(POrder_POLine porder_line : list_porder) {
				
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
		    return new ResponseEntity<delete_porder_response>(response, HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<delete_porder_response>(response, HttpStatus.OK);
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
