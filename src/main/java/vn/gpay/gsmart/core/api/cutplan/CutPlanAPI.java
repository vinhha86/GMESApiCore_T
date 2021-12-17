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

import vn.gpay.gsmart.core.cutplan.CutPlan_Row;
import vn.gpay.gsmart.core.cutplan.CutPlan_Size;
import vn.gpay.gsmart.core.cutplan.ICutPlan_Row_Service;
import vn.gpay.gsmart.core.cutplan.ICutPlan_Size_Service;
import vn.gpay.gsmart.core.cutplan.LoaiPhoi;
import vn.gpay.gsmart.core.pcontractbomsku.IPContractBOM2SKUService;
import vn.gpay.gsmart.core.pcontractproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKU;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder_bom_sku.IPOrderBOMSKU_Service;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sku.ISKU_AttributeValue_Service;
import vn.gpay.gsmart.core.sku.SKU_Attribute_Value;
import vn.gpay.gsmart.core.utils.AtributeFixValues;
import vn.gpay.gsmart.core.utils.CutPlanRowType;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/cutplan")
public class CutPlanAPI {
	@Autowired
	ICutPlan_Size_Service cutplan_size_Service;
	@Autowired
	ICutPlan_Row_Service cutplanrowService;
	@Autowired
	IPContractProductSKUService pskuservice;
	@Autowired
	IPContractBOM2SKUService ppbom2skuservice;
	@Autowired
	IPOrder_Service porderService;
	@Autowired
	ISKU_AttributeValue_Service skuavService;
	@Autowired
	IPOrderBOMSKU_Service porderbomskuService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<create_cutplan_response> CreateCutPlan(HttpServletRequest request,
			@RequestBody create_cutplan_request entity) {
		create_cutplan_response response = new create_cutplan_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long material_skuid_link = entity.material_skuid_link;
//			Long porderid_link = entity.porderid_link;
			Long orgrootid_link = user.getRootorgid_link();
			Long productid_link = entity.productid_link;
			Long pcontractid_link = entity.pcontractid_link;
			Long colorid_link = entity.colorid_link;
			String loaiphoi = entity.loaiphoi;

			Date current = new Date();

			// Kiem tra xem npl da co so do hay chua
//			List<CutPlan_Size> list_cutplan = cutplan_size_Service.getby_sku_and_pcontract_product(material_skuid_link,
//					pcontractid_link, productid_link, orgrootid_link);

			List<CutPlan_Row> list_row = cutplanrowService.getby_loaiphoi(pcontractid_link, productid_link,
					material_skuid_link, colorid_link, orgrootid_link, loaiphoi);

			if (list_row.size() == 0) {
				// them vao plan
				// Lay danh sach sku cua san pham trong don hang
				List<PContractProductSKU> list_sku = pskuservice.getlistsku_byproduct_and_pcontract(orgrootid_link,
						productid_link, pcontractid_link);

				// Tao row cho cac mau truoc
				List<Long> list_mau = new ArrayList<Long>();
				for (PContractProductSKU pContractProductSKU : list_sku) {
					if (list_mau.contains(pContractProductSKU.getColor_id()) || list_mau.contains(colorid_link))
						continue;

					list_mau.add(colorid_link);
					CutPlan_Row row_yeucau = new CutPlan_Row();
					row_yeucau.setCode("SL yêu cầu");
					row_yeucau.setId(null);
					row_yeucau.setName("SL yêu cầu");
					row_yeucau.setType(CutPlanRowType.yeucau);
					row_yeucau.setNgay(current);
					row_yeucau.setPorderid_link(null);
					row_yeucau.setMaterial_skuid_link(material_skuid_link);
					row_yeucau.setCreateduserid_link(user.getId());
					row_yeucau.setLa_vai(0);
					row_yeucau.setDai_so_do((float) 0);
					row_yeucau.setSl_vai((float) 0);
					row_yeucau.setKho("");
					row_yeucau.setSo_cay(0);
					row_yeucau.setPcontractid_link(pcontractid_link);
					row_yeucau.setProductid_link(productid_link);
					row_yeucau.setLoaiphoimau(loaiphoi);

					row_yeucau = cutplanrowService.save(row_yeucau);

					CutPlan_Row row_catdu = new CutPlan_Row();
					row_catdu.setCode("SL cắt dư");
					row_catdu.setId(null);
					row_catdu.setName("SL cắt dư");
					row_catdu.setType(CutPlanRowType.catdu);
					row_catdu.setNgay(current);
					row_catdu.setPorderid_link(null);
					row_catdu.setMaterial_skuid_link(material_skuid_link);
					row_catdu.setCreateduserid_link(user.getId());
					row_catdu.setLa_vai(0);
					row_catdu.setDai_so_do((float) 0);
					row_catdu.setSl_vai((float) 0);
					row_catdu.setKho("");
					row_catdu.setSo_cay(0);
					row_catdu.setPcontractid_link(pcontractid_link);
					row_catdu.setProductid_link(productid_link);
					row_catdu.setLoaiphoimau(loaiphoi);

					row_catdu = cutplanrowService.save(row_catdu);

					// Tao size cho cac row
					List<PContractProductSKU> list_sku_clone = new ArrayList<PContractProductSKU>(list_sku);
					list_sku_clone.removeIf(c -> !c.getColor_id().equals(colorid_link));
					List<Long> listsku = new ArrayList<Long>();
					for (PContractProductSKU sku : list_sku_clone) {
						if (listsku.contains(sku.getSkuid_link()))
							continue;

						listsku.add(sku.getSkuid_link());
						CutPlan_Size plan_yc = new CutPlan_Size();
						plan_yc.setCutplanrowid_link(row_yeucau.getId());
						plan_yc.setId(null);
						plan_yc.setOrgrootid_link(orgrootid_link);
						plan_yc.setAmount(sku.getPquantity_total());
						plan_yc.setProduct_skuid_link(sku.getSkuid_link());

						cutplan_size_Service.save(plan_yc);

						CutPlan_Size plan_catdu = new CutPlan_Size();
						plan_catdu.setCutplanrowid_link(row_catdu.getId());
						plan_catdu.setId(null);
						plan_catdu.setOrgrootid_link(orgrootid_link);
						plan_catdu.setAmount(0 - sku.getPquantity_total());
						plan_catdu.setProduct_skuid_link(sku.getSkuid_link());

						cutplan_size_Service.save(plan_catdu);
					}
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
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long material_skuid_link = entity.material_skuid_link;
//			Long porderid_link = entity.porderid_link;
			Long orgrootid_link = user.getRootorgid_link();
			Long colorid_link = entity.colorid_link;
			Long productid_link = entity.productid_link;
			Long pcontractid_link = entity.pcontractid_link;
			String loaiphoi = entity.loaiphoi;

			List<CutPlan_Row> list_row = cutplanrowService.getby_loaiphoi(pcontractid_link, productid_link,
					material_skuid_link, colorid_link, orgrootid_link, loaiphoi);
//			List<String> list_name = new ArrayList<String>();

			// lay nhung ten ke hoach ra
//			for (CutPlan_Row row : list_row) {
//				if(!list_name.contains(row.getName()))
//					list_name.add(row.getName());
//			}

			// Lay cac co trong don hang
			List<Long> List_size = pskuservice.getlistvalue_by_product(pcontractid_link, productid_link, (long) 30);

			List<Map<String, String>> listdata = new ArrayList<Map<String, String>>();

			for (CutPlan_Row row : list_row) {
//				List<CutPlan_Row> list_row_clone = new ArrayList<CutPlan_Row>(list_row);
//				//loc het nhung so do khong trung ten ra
//				list_row_clone.removeIf(c->!c.getName().equals(name));
				if (list_row.size() > 0) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", row.getName());
					map.put("la_vai", "" + row.getLa_vai());
					map.put("dai_so_do", "" + row.getDai_so_do());
					map.put("sl_vai", "" + row.getSl_vai());
					map.put("kho", "" + row.getKho());
					map.put("so_cay", "" + row.getSo_cay());
					map.put("id", row.getId().toString());
					map.put("code", row.getCode());
					map.put("pcontractid_link", row.getPcontractid_link().toString());
					map.put("productid_link", row.getProductid_link().toString());
					map.put("material_skuid_link", row.getMaterial_skuid_link().toString());
					map.put("porderid_link", row.getPorderid_link() == null ? "" : row.getPorderid_link().toString());
					map.put("createduserid_link", row.getCreateduserid_link().toString());

					Date date = row.getNgay();
					DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
					String strDate = dateFormat.format(date);
					map.put("ngay", strDate);
					map.put("type", "" + row.getType());

					// lay gia tri cua cac cot co
					for (long sizeid_link : List_size) {
						long product_skuid_link = ppbom2skuservice.getskuid_link_by_color_and_size(colorid_link,
								sizeid_link, productid_link);

						List<CutPlan_Size> list_size_clone = cutplan_size_Service
								.getby_row_and_productsku(orgrootid_link, row.getId(), product_skuid_link);

						Integer amount_size = 0;
						if (list_size_clone.size() > 0) {
							amount_size = list_size_clone.get(0).getAmount();
						}
						map.put("" + sizeid_link, amount_size.toString());
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

	@RequestMapping(value = "/getall_loaiphoimau", method = RequestMethod.POST)
	public ResponseEntity<getall_loaiphoimau_response> GetAllLoaiPhoiMau(HttpServletRequest request,
			@RequestBody getall_loaiphoimau_request entity) {
		getall_loaiphoimau_response response = new getall_loaiphoimau_response();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long pcontractid_link = entity.pcontractid_link;
			Long productid_link = entity.productid_link;
			Long material_skuid_link = entity.material_skuid_link;

			List<String> list_loaiPhoi = cutplanrowService.getAllLoaiPhoiMau(pcontractid_link, productid_link,
					material_skuid_link);
			List<LoaiPhoi> list = new ArrayList<LoaiPhoi>();
			for (String loaiphoi : list_loaiPhoi) {
				LoaiPhoi new_loaiphoi = new LoaiPhoi();
				new_loaiphoi.name = loaiphoi;

				list.add(new_loaiphoi);
			}

			response.data = list;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getall_loaiphoimau_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<getall_loaiphoimau_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/add_row", method = RequestMethod.POST)
	public ResponseEntity<add_row_response> AddRow(HttpServletRequest request, @RequestBody add_row_request entity) {
		add_row_response response = new add_row_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long material_skuid_link = entity.material_skuid_link;
//			Long porderid_link = entity.porderid_link;
			Long orgrootid_link = user.getRootorgid_link();
			Long productid_link = entity.productid_link;
			Long pcontractid_link = entity.pcontractid_link;
			Long colorid_link = entity.colorid_link;
			String loaiphoi = entity.loaiphoi;

			Date current = new Date();
			DateFormat dateFormat = new SimpleDateFormat("hh_mm_ss");
			String strDate = dateFormat.format(current);

			List<Long> list_sku = pskuservice.getsku_bycolor(pcontractid_link, productid_link, colorid_link);

			// Kiem tra sinh yeu cau va cat du hay chua
			List<CutPlan_Size> list_cutplan = cutplan_size_Service.getby_sku_and_pcontract_product_and_color_loaiphoi(
					material_skuid_link, pcontractid_link, productid_link, orgrootid_link, colorid_link, loaiphoi);
			if (list_cutplan.size() == 0) {
				CutPlan_Row row_yeucau = new CutPlan_Row();
				row_yeucau.setCode("SL yêu cầu");
				row_yeucau.setId(null);
				row_yeucau.setName("SL yêu cầu");
				row_yeucau.setType(CutPlanRowType.yeucau);
				row_yeucau.setNgay(current);
				row_yeucau.setPorderid_link(null);
				row_yeucau.setMaterial_skuid_link(material_skuid_link);
				row_yeucau.setCreateduserid_link(user.getId());
				row_yeucau.setLa_vai(0);
				row_yeucau.setDai_so_do((float) 0);
				row_yeucau.setSl_vai((float) 0);
				row_yeucau.setKho("");
				row_yeucau.setSo_cay(0);
				row_yeucau.setPcontractid_link(pcontractid_link);
				row_yeucau.setProductid_link(productid_link);
				row_yeucau.setLoaiphoimau(loaiphoi);

				row_yeucau = cutplanrowService.save(row_yeucau);

				CutPlan_Row row_catdu = new CutPlan_Row();
				row_catdu.setCode("SL cắt dư");
				row_catdu.setId(null);
				row_catdu.setName("SL cắt dư");
				row_catdu.setType(CutPlanRowType.catdu);
				row_catdu.setNgay(current);
				row_catdu.setPorderid_link(null);
				row_catdu.setMaterial_skuid_link(material_skuid_link);
				row_catdu.setCreateduserid_link(user.getId());
				row_catdu.setLa_vai(0);
				row_catdu.setDai_so_do((float) 0);
				row_catdu.setSl_vai((float) 0);
				row_catdu.setKho("");
				row_catdu.setSo_cay(0);
				row_catdu.setLoaiphoimau(loaiphoi);

				row_catdu = cutplanrowService.save(row_catdu);

				// Tao size cho cac row
				List<PContractProductSKU> list_sku_clone = pskuservice
						.getlistsku_byproduct_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
				list_sku_clone.removeIf(c -> !c.getColor_id().equals(colorid_link));
				Map<Long, Integer> map_sku = new HashMap<Long, Integer>();
				List<Long> list_sku_id = new ArrayList<Long>();
				for (PContractProductSKU sku : list_sku_clone) {
					if (!map_sku.containsKey(sku.getSkuid_link())) {
						map_sku.put(sku.getSkuid_link(), sku.getPquantity_total());
						list_sku_id.add(sku.getSkuid_link());
					} else {
						map_sku.put(sku.getSkuid_link(), sku.getPquantity_total() + map_sku.get(sku.getSkuid_link()));
					}
				}

				for (Long skuid_link : list_sku_id) {
					CutPlan_Size plan_yc = new CutPlan_Size();
					plan_yc.setCutplanrowid_link(row_yeucau.getId());
					plan_yc.setId(null);
					plan_yc.setOrgrootid_link(orgrootid_link);
					plan_yc.setAmount(map_sku.get(skuid_link));
					plan_yc.setProduct_skuid_link(skuid_link);

					cutplan_size_Service.save(plan_yc);

					CutPlan_Size plan_catdu = new CutPlan_Size();
					plan_catdu.setCutplanrowid_link(row_catdu.getId());
					plan_catdu.setId(null);
					plan_catdu.setOrgrootid_link(orgrootid_link);
					plan_catdu.setAmount(0 - map_sku.get(skuid_link));
					plan_catdu.setProduct_skuid_link(skuid_link);

					cutplan_size_Service.save(plan_catdu);
				}
			}

			CutPlan_Row row_new = new CutPlan_Row();
			row_new.setCode("Sđ " + strDate);
			row_new.setId(null);
			row_new.setName("Sđ " + strDate);
			row_new.setNgay(current);
			row_new.setType(CutPlanRowType.sodocat);
			row_new.setCreateduserid_link(user.getId());
			row_new.setPorderid_link(null);
			row_new.setMaterial_skuid_link(material_skuid_link);
			row_new.setLa_vai(0);
			row_new.setDai_so_do((float) 0);
			row_new.setSl_vai((float) 0);
			row_new.setKho("");
			row_new.setSo_cay(0);
			row_new.setPcontractid_link(pcontractid_link);
			row_new.setProductid_link(productid_link);
			row_new.setLoaiphoimau(loaiphoi);

			row_new = cutplanrowService.save(row_new);

			for (Long product_skuid_link : list_sku) {

				CutPlan_Size plan = new CutPlan_Size();
				plan.setCutplanrowid_link(row_new.getId());
				plan.setId(null);
				plan.setOrgrootid_link(orgrootid_link);
				plan.setProduct_skuid_link(product_skuid_link);
				plan.setAmount(0);

				cutplan_size_Service.save(plan);
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
	public ResponseEntity<update_size_amount_response> UpdateSizeAmount(HttpServletRequest request,
			@RequestBody update_size_amount_request entity) {
		update_size_amount_response response = new update_size_amount_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long cutplanrowid_link = entity.cutplanrowid_link;
			Long colorid_link = entity.colorid_link;
			Long sizeid_link = entity.sizeid_link;
			Long productid_link = entity.productid_link;
//			Long porderid_link = entity.porderid_link;
			Long material_skuid_link = entity.material_skuid_link;
			Long pcontractid_link = entity.pcontractid_link;
			String loaiphoi = entity.loaiphoi;

			Long orgrootid_link = user.getRootorgid_link();

			long product_skuid_link = ppbom2skuservice.getskuid_link_by_color_and_size(colorid_link, sizeid_link,
					productid_link);

			CutPlan_Row row = cutplanrowService.findOne(cutplanrowid_link);
			// tinh so la vai

			int la_vai = 0;
			int sl_catdu = 0;

			List<CutPlan_Size> list_sodo = cutplan_size_Service.getby_row_loaiphoi(orgrootid_link, cutplanrowid_link,
					loaiphoi);

			List<CutPlan_Size> list_yeucau = cutplan_size_Service.getby_pcontract_product_matsku_productsku_loaiphoi(
					pcontractid_link, productid_link, material_skuid_link, (long) 0, CutPlanRowType.yeucau, "",
					loaiphoi);
			List<CutPlan_Size> list_sodo_all = cutplan_size_Service.getby_pcontract_product_matsku_productsku_loaiphoi(
					pcontractid_link, productid_link, material_skuid_link, (long) 0, CutPlanRowType.sodocat, "",
					loaiphoi);
//			List<CutPlan_Size> list_sodo_catdu = cutplan_size_Service.getby_pcontract_product_matsku_productsku(pcontractid_link, productid_link,
//					material_skuid_link, (long) 0, CutPlanRowType.catdu, "");

//			for (CutPlan_Size catdu : list_sodo_catdu) {
//				sl_catdu = catdu.getAmount();
//				
//			}

			for (CutPlan_Size yeucau : list_yeucau) {
				List<CutPlan_Size> list_sodo_clone = new ArrayList<CutPlan_Size>(list_sodo);
				list_sodo_clone.removeIf(c -> !c.getProduct_skuid_link().equals(yeucau.getProduct_skuid_link()));

				// tinh so luong cat du
				int sodo_all = 0;
				List<CutPlan_Size> list_sodo_all_clone = new ArrayList<CutPlan_Size>(list_sodo_all);
				// bo nhung size khong nam trong row va size dang sua
				list_sodo_all_clone.removeIf(c -> !c.getProduct_skuid_link().equals(yeucau.getProduct_skuid_link())
						|| (c.getProduct_skuid_link().equals(yeucau.getProduct_skuid_link())
								&& c.getCutplanrowid_link().equals(cutplanrowid_link)));
				for (CutPlan_Size sodo_size_all : list_sodo_all_clone) {
					sodo_all += (sodo_size_all.getAmount() == null || sodo_size_all.getLaVai() == null) ? 0
							: sodo_size_all.getAmount() * sodo_size_all.getLaVai();
				}

				sl_catdu = yeucau.getAmount() - sodo_all;

				if (list_sodo_clone.size() > 0) {
					CutPlan_Size sodo = list_sodo_clone.get(0);
					if (entity.amount > 0) {
						if (sodo.getProduct_skuid_link().equals(product_skuid_link)) {
							if (la_vai == 0) {
								la_vai = sl_catdu / entity.amount;
							} else {
								la_vai = (sl_catdu / entity.amount) > la_vai ? la_vai : (sl_catdu / entity.amount);
							}
						} else {
							if (!sodo.getAmount().equals(0)) {
								if (la_vai == 0) {
									la_vai = sl_catdu / sodo.getAmount();
								} else {
									la_vai = (sl_catdu / sodo.getAmount()) > la_vai ? la_vai
											: (sl_catdu / sodo.getAmount());
								}
							}
						}
					}
				}
			}

			List<CutPlan_Size> list_size = cutplan_size_Service.getby_row_and_productsku(orgrootid_link,
					cutplanrowid_link, product_skuid_link);

			response.catdu = null;
			if (list_size.size() > 0) {

				CutPlan_Size size = list_size.get(0);
				size.setAmount(entity.amount);

				cutplan_size_Service.save(size);
			}

			// sinh ten so do theo co
			String name = "";

			list_sodo = cutplan_size_Service.getby_row(orgrootid_link, cutplanrowid_link);
			for (CutPlan_Size size : list_sodo) {
				if (size.getAmount() > 0) {
					if (name == "") {
						name = size.getAmount() + "(" + size.getSizeName() + ")";
					} else {
						name += " - " + size.getAmount() + "(" + size.getSizeName() + ")";
					}
				}

			}

			// Cap nhat lai row
			row.setLa_vai(la_vai);
			row.setName(name);
			cutplanrowService.save(row);

			// Cap nhat lai so cat du

			for (CutPlan_Size size_yc : list_yeucau) {
				int yeucau = size_yc.getAmount();
				int sodo = 0;

				List<CutPlan_Size> listsize_sodo_clone = new ArrayList<CutPlan_Size>(list_sodo);
				listsize_sodo_clone.removeIf(c -> !c.getProduct_skuid_link().equals(size_yc.getProduct_skuid_link()));

				List<CutPlan_Size> list_size_sodo_all = cutplan_size_Service.getby_pcontract_product_matsku_productsku(
						pcontractid_link, productid_link, material_skuid_link, size_yc.getProduct_skuid_link(),
						CutPlanRowType.sodocat, "");
				for (CutPlan_Size sizesd : list_size_sodo_all) {
					int amount_sd = sizesd.getAmount() == null ? 0 : sizesd.getAmount();
					int lavai_sd = sizesd.getLaVai() == null ? 0 : sizesd.getLaVai();
					sodo += amount_sd * lavai_sd;
				}

				List<CutPlan_Size> listsize_catdu = cutplan_size_Service.getby_pcontract_product_matsku_productsku(
						pcontractid_link, productid_link, material_skuid_link, size_yc.getProduct_skuid_link(),
						CutPlanRowType.catdu, "");
				listsize_catdu.removeIf(c -> !c.getProduct_skuid_link().equals(size_yc.getProduct_skuid_link()));

				CutPlan_Size size_catdu = listsize_catdu.get(0);
				int amount = sodo - yeucau;
				size_catdu.setAmount(amount);
				cutplan_size_Service.save(size_catdu);
			}

			// dong bo dinh muc
//			POrder porder = porderService.findOne(porderid_link);
			cutplanrowService.sync_porder_bom(material_skuid_link, pcontractid_link, productid_link, colorid_link,
					user.getId(), orgrootid_link);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<update_size_amount_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<update_size_amount_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/delete_row", method = RequestMethod.POST)
	public ResponseEntity<delete_row_response> DeleteRow(HttpServletRequest request,
			@RequestBody delete_row_request entity) {
		delete_row_response response = new delete_row_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long cutplanrowid_link = entity.cutplanrowid_link;
			Long orgrootid_link = user.getRootorgid_link();
//			Long porderid_link = entity.porderid_link;
			Long material_skuid_link = entity.material_skuid_link;
			Long pcontractid_link = entity.pcontractid_link;
			Long productid_link = entity.productid_link;

			// delete row
			cutplanrowService.deleteById(cutplanrowid_link);

//			POrder porder = porderService.findOne(porderid_link);
			// delete size in row
			List<CutPlan_Size> list_size = cutplan_size_Service.getby_row(orgrootid_link, cutplanrowid_link);
			for (CutPlan_Size cutPlan_Size : list_size) {
				cutplan_size_Service.delete(cutPlan_Size);

				long product_skuid_link = cutPlan_Size.getProduct_skuid_link();

				// Cap nhat lai so cat du
				List<CutPlan_Size> listsize_yeucau = cutplan_size_Service.getby_pcontract_product_matsku_productsku(
						pcontractid_link, productid_link, material_skuid_link, product_skuid_link,
						CutPlanRowType.yeucau, "");
				List<CutPlan_Size> listsize_catdu = cutplan_size_Service.getby_pcontract_product_matsku_productsku(
						pcontractid_link, productid_link, material_skuid_link, product_skuid_link, CutPlanRowType.catdu,
						"");
				List<CutPlan_Size> listsize_sodo = cutplan_size_Service.getby_pcontract_product_matsku_productsku(
						pcontractid_link, productid_link, material_skuid_link, product_skuid_link,
						CutPlanRowType.sodocat, "");
				int yeucau = listsize_yeucau.get(0).getAmount();
				int sodo = 0;

				for (CutPlan_Size cutPlanSize : listsize_sodo) {
					CutPlan_Row cut_row = cutplanrowService.findOne(cutPlanSize.getCutplanrowid_link());
					int la_vai = cut_row.getLa_vai() == null ? 0 : cut_row.getLa_vai();
					sodo += (cutPlanSize.getAmount() == null ? 0 : cutPlanSize.getAmount()) * la_vai;
				}

				CutPlan_Size size_catdu = listsize_catdu.get(0);
				size_catdu.setAmount(sodo - yeucau);
				cutplan_size_Service.save(size_catdu);

				// Dong bo sang dinh muc san xuat
				List<SKU_Attribute_Value> list_value = skuavService.getlist_bysku(product_skuid_link);
				long colorid_link = 0;
				for (SKU_Attribute_Value value : list_value) {
					if (value.getAttributeid_link().equals(AtributeFixValues.ATTR_COLOR)
							|| value.getAttributeid_link() == AtributeFixValues.ATTR_COLOR) {
						colorid_link = value.getAttributevalueid_link();
					}
				}
				cutplanrowService.sync_porder_bom(material_skuid_link, pcontractid_link, productid_link, colorid_link,
						user.getId(), orgrootid_link);
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<delete_row_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<delete_row_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/update_row", method = RequestMethod.POST)
	public ResponseEntity<update_row_response> UpdateRow(HttpServletRequest request,
			@RequestBody update_row_request entity) {
		update_row_response response = new update_row_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long cutplanrowid_link = entity.data.getId();
			Long orgrootid_link = user.getRootorgid_link();
//			Long porderid_link = entity.data.getPorderid_link();
			Long material_skuid_link = entity.data.getMaterial_skuid_link();
			Long pcontractid_link = entity.data.getPcontractid_link();
			Long productid_link = entity.data.getProductid_link();
			String loaiphoi = entity.data.getLoaiphoimau();

			cutplanrowService.save(entity.data);

//			POrder porder = porderService.findOne(porderid_link);

			// Cap nhat lai cat du trong truong hop nhap lai so la
			List<CutPlan_Size> list_size = cutplan_size_Service.getby_row_loaiphoi(orgrootid_link, cutplanrowid_link,
					loaiphoi);
			for (CutPlan_Size cutPlan_Size : list_size) {

				long product_skuid_link = cutPlan_Size.getProduct_skuid_link();

				// Cap nhat lai so cat du
				List<CutPlan_Size> listsize_yeucau = cutplan_size_Service
						.getby_pcontract_product_matsku_productsku_loaiphoi(pcontractid_link, productid_link,
								material_skuid_link, product_skuid_link, CutPlanRowType.yeucau, "", loaiphoi);
				List<CutPlan_Size> listsize_catdu = cutplan_size_Service
						.getby_pcontract_product_matsku_productsku_loaiphoi(pcontractid_link, productid_link,
								material_skuid_link, product_skuid_link, CutPlanRowType.catdu, "", loaiphoi);
				List<CutPlan_Size> listsize_sodo = cutplan_size_Service
						.getby_pcontract_product_matsku_productsku_loaiphoi(pcontractid_link, productid_link,
								material_skuid_link, product_skuid_link, CutPlanRowType.sodocat, "", loaiphoi);
				int yeucau = listsize_yeucau.get(0).getAmount();
				int sodo = 0;

				for (CutPlan_Size cutPlanSize : listsize_sodo) {
					CutPlan_Row cut_row = cutplanrowService.findOne(cutPlanSize.getCutplanrowid_link());
					int la_vai = cut_row.getLa_vai();
					sodo += (cutPlanSize.getAmount() == null ? 0 : cutPlanSize.getAmount()) * la_vai;
				}

				CutPlan_Size size_catdu = listsize_catdu.get(0);
				size_catdu.setAmount(sodo - yeucau);
				cutplan_size_Service.save(size_catdu);

				// Dong bo sang dinh muc san xuat
				List<SKU_Attribute_Value> list_value = skuavService.getlist_bysku(product_skuid_link);
				long colorid_link = 0;
				for (SKU_Attribute_Value value : list_value) {
					if (value.getAttributeid_link().equals(AtributeFixValues.ATTR_COLOR)
							|| value.getAttributeid_link() == AtributeFixValues.ATTR_COLOR) {
						colorid_link = value.getAttributevalueid_link();
					}
				}
				cutplanrowService.sync_porder_bom(material_skuid_link, pcontractid_link, productid_link, colorid_link,
						user.getId(), orgrootid_link);
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<update_row_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<update_row_response>(response, HttpStatus.OK);
		}
	}

}
