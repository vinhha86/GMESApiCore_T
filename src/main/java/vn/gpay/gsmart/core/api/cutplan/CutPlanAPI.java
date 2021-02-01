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

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.cutplan.CutPlan_Row;
import vn.gpay.gsmart.core.cutplan.CutPlan_Size;
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
			List<CutPlan_Size> list_cutplan = cutplanService.getby_sku_and_porder(skuid_link, porderid_link, orgrootid_link);
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
					
					row_yeucau = cutplanrowService.save(row_yeucau);
					
					CutPlan_Row row_catdu = new CutPlan_Row();
					row_catdu.setCode("SL cắt dư");
					row_catdu.setId(null);
					row_catdu.setName("SL cắt dư");
					row_catdu.setType(CutPlanRowType.catdu);
					row_catdu.setNgay(current);
					
					row_catdu = cutplanrowService.save(row_catdu);
					
					CutPlan_Size plan_yc = new CutPlan_Size();
					plan_yc.setCutplanrowid_link(row_yeucau.getId());
					plan_yc.setId(null);
					plan_yc.setOrgrootid_link(orgrootid_link);
					plan_yc.setAmount(pContractProductSKU.getPquantity_total());
					plan_yc.setProduct_skuid_link(pContractProductSKU.getSkuid_link());					
					
					cutplanService.save(plan_yc);
					
					CutPlan_Size plan_catdu = new CutPlan_Size();
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
					map.put("id", list_row_clone.get(0).getId().toString());
					
					Date date = list_row_clone.get(0).getNgay();  
					DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");  
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
	
	@RequestMapping(value = "/add_row", method = RequestMethod.POST)
	public ResponseEntity<add_row_response> AddRow(HttpServletRequest request,
			@RequestBody add_row_request entity) {
		add_row_response response = new add_row_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Long skuid_link = entity.material_skuid_link;
			Long porderid_link = entity.porderid_link;
			Long orgrootid_link = user.getRootorgid_link();
			Long productid_link = entity.productid_link;
			Long pcontractid_link = entity.pcontractid_link;
			Long colorid_link = entity.colorid_link;
			
			Date current = new Date();
			DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");  
			String strDate = dateFormat.format(current);
			
			 
			
			List<Long> list_sku = pskuservice.getsku_bycolor(pcontractid_link, productid_link, colorid_link);
			
			for (Long product_skuid_link : list_sku) {
				
				CutPlan_Row row_new = new CutPlan_Row();
				row_new.setAmount(0);
				row_new.setCode("Sđ "+strDate);
				row_new.setId(null);
				row_new.setName("Sđ "+strDate);
				row_new.setProduct_skuid_link(product_skuid_link);
				row_new.setNgay(current);
				row_new.setType(CutPlanRowType.sodocat);
				
				row_new = cutplanrowService.save(row_new);
				
				CutPlan plan = new CutPlan();
				plan.setCreateddate(current);
				plan.setCreateduserid_link(user.getId());
				plan.setCutplanrowid_link(row_new.getId());
				plan.setId(null);
				plan.setOrgrootid_link(orgrootid_link);
				plan.setPorderid_link(porderid_link);
				plan.setSkuid_link(skuid_link);
				
				cutplanService.save(plan);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<add_row_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<add_row_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/update_size_amount", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> UpdateSizeAmount(HttpServletRequest request,
			@RequestBody update_size_amount_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			Long material_skuid_link = entity.material_skuid_link;
			Long porderid_link = entity.porderid_link;
			Long colorid_link = entity.colorid_link;
			Long sizeid_link = entity.sizeid_link;
			Long productid_link = entity.productid_link;
			String name = entity.name;
			
			long product_skuid_link = ppbom2skuservice.getskuid_link_by_color_and_size(colorid_link, sizeid_link, productid_link);
			
			List<CutPlan_Row> list_row = cutplanrowService.getby_porder_matsku_productsku(porderid_link, material_skuid_link, product_skuid_link, CutPlanRowType.sodocat, name);
			if(list_row.size() > 0) {
				CutPlan_Row row = list_row.get(0);
				row.setAmount(entity.amount);
				
				cutplanrowService.save(row);
				
				//Cap nhat lai so cat du
				List<CutPlan_Row> list_row_catdu = cutplanrowService.getby_porder_matsku_productsku(porderid_link, material_skuid_link, product_skuid_link, CutPlanRowType.catdu, "");
				if(list_row_catdu.size() > 0) {
					CutPlan_Row row_catdu = list_row_catdu.get(0);
					row_catdu.setAmount(row_catdu.getAmount() + entity.amount);
					cutplanrowService.save(row_catdu);
				}
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
}
