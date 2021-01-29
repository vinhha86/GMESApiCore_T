package vn.gpay.gsmart.core.api.cutplan;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import vn.gpay.gsmart.core.pcontractbomsku.IPContractBOM2SKUService;
import vn.gpay.gsmart.core.pcontractproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKU;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.CutPlanRowType;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/cutplan")
public class CutPlanAPI {
	@Autowired ICutPlan_Service cutplanService;
	@Autowired ICutPlan_Row_Service cutplanrowService;
	@Autowired IPContractProductSKUService pskuservice;
	@Autowired IPContractBOM2SKUService ppbom2skuservice;
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<create_cutplan_response> CreateCutPlan(HttpServletRequest request,
			@RequestBody create_cutplan_request entity) {
		create_cutplan_response response = new create_cutplan_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Long skuid_link = entity.material_skuid_link;
			Long porderid_link = entity.porderid_link;
			Long orgrootid_link = user.getRootorgid_link();
			Long productid_link = entity.productid_link;
			Long pcontractid_link = entity.pcontractid_link;
			Date current = new Date();
			
			//Kiem tra xem npl da co so do hay chua
			List<CutPlan> list_cutplan = cutplanService.getby_sku_and_porder(skuid_link, porderid_link, orgrootid_link);
			if(list_cutplan.size() == 0) {
				//them vao plan
				//Lay danh sach sku cua san pham trong don hang
				List<PContractProductSKU> list_sku = pskuservice.getlistsku_byproduct_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
				for (PContractProductSKU pContractProductSKU : list_sku) {
					//tao 2 row mac dinh de them vao plan
					CutPlan_Row row_yeucau = new CutPlan_Row();
					row_yeucau.setCode("SL yêu cầu");
					row_yeucau.setId(null);
					row_yeucau.setName("SL yêu cầu");
					row_yeucau.setType(CutPlanRowType.yeucau);
					row_yeucau.setNgay(current);
					row_yeucau.setProduct_skuid_link(pContractProductSKU.getSkuid_link());
					row_yeucau.setAmount(pContractProductSKU.getPquantity_total());
					
					row_yeucau = cutplanrowService.save(row_yeucau);
					
					CutPlan_Row row_catdu = new CutPlan_Row();
					row_catdu.setCode("SL cắt dư");
					row_catdu.setId(null);
					row_catdu.setName("SL cắt dư");
					row_catdu.setType(CutPlanRowType.catdu);
					row_catdu.setNgay(current);
					row_catdu.setProduct_skuid_link(pContractProductSKU.getSkuid_link());
					row_catdu.setAmount(0 - pContractProductSKU.getPquantity_total());
					
					row_catdu = cutplanrowService.save(row_catdu);
					
					CutPlan plan_yc = new CutPlan();
					plan_yc.setCreateddate(current);
					plan_yc.setCreateduserid_link(user.getId());
					plan_yc.setCutplanrowid_link(row_yeucau.getId());
					plan_yc.setId(null);
					plan_yc.setOrgrootid_link(orgrootid_link);
					plan_yc.setPorderid_link(porderid_link);
					plan_yc.setSkuid_link(skuid_link);
					
					cutplanService.save(plan_yc);
					
					CutPlan plan_catdu = new CutPlan();
					plan_catdu.setCreateddate(current);
					plan_catdu.setCreateduserid_link(user.getId());
					plan_catdu.setCutplanrowid_link(row_catdu.getId());
					plan_catdu.setId(null);
					plan_catdu.setOrgrootid_link(orgrootid_link);
					plan_catdu.setPorderid_link(porderid_link);
					plan_catdu.setSkuid_link(skuid_link);
					
					cutplanService.save(plan_catdu);
				}
				
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
	
	@RequestMapping(value = "/getby_color", method = RequestMethod.POST)
	public ResponseEntity<getplanby_color_response> GetPlanByColor(HttpServletRequest request,
			@RequestBody getplan_by_color_request entity) {
		getplanby_color_response response = new getplanby_color_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Long skuid_link = entity.material_skuid_link;
			Long porderid_link = entity.porderid_link;
			Long orgrootid_link = user.getRootorgid_link();
			Long colorid_link = entity.colorid_link;
			Long productid_link = entity.productid_link;
			Long pcontractid_link = entity.pcontractid_link;
			
			List<CutPlan_Row> list_row = cutplanrowService.getby_color(porderid_link, skuid_link, colorid_link, orgrootid_link);
			List<String> list_name = new ArrayList<String>();
			
			//lay nhung ten ke hoach ra 
			for (CutPlan_Row row : list_row) {
				if(!list_name.contains(row.getName()))
					list_name.add(row.getName());
			}
			
			//Lay cac co trong don hang
			List<Long> List_size = pskuservice.getlistvalue_by_product(pcontractid_link, productid_link, (long)30);
			
			List<Map<String, String>> listdata = new ArrayList<Map<String, String>>();	
			
			for(String name : list_name) {
				List<CutPlan_Row> list_row_clone = new ArrayList<CutPlan_Row>(list_row);
				//loc het nhung so do khong trung ten ra
				list_row_clone.removeIf(c->!c.getName().equals(name));
				if(list_row_clone.size() > 0) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", name);
					map.put("la_vai", ""+list_row_clone.get(0).getLa_vai());
					map.put("dai_so_do", ""+list_row_clone.get(0).getDai_so_do());
					map.put("sl_vai", ""+list_row_clone.get(0).getSl_vai());
					map.put("kho", ""+list_row_clone.get(0).getKho());
					map.put("so_cay", ""+list_row_clone.get(0).getSo_cay());
					
					Date date = list_row_clone.get(0).getNgay();  
					DateFormat dateFormat = new SimpleDateFormat("dd-mm-yy");  
					String strDate = dateFormat.format(date); 
					map.put("ngay", strDate);
					map.put("type", ""+list_row_clone.get(0).getType());
					
					//lay gia tri cua cac cot co
					for(long sizeid_link : List_size) {
						long product_skuid_link = ppbom2skuservice.getskuid_link_by_color_and_size(colorid_link, sizeid_link, productid_link);
						List<CutPlan_Row> list_size_clone = new ArrayList<CutPlan_Row>(list_row_clone);
						list_size_clone.removeIf(c-> !c.getProduct_skuid_link().equals(product_skuid_link));
						Integer amount_size = 0;
						if(list_size_clone.size() > 0) {
							amount_size = list_size_clone.get(0).getAmount();
						}
						map.put(""+sizeid_link, amount_size.toString());
					}
					
					listdata.add(map);
				}
				
				
			}
			
			response.data = listdata;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getplanby_color_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<getplanby_color_response>(response, HttpStatus.OK);
		}
	}
}
