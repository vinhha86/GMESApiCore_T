package vn.gpay.gsmart.core.api.pcontractproductbom;

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
import vn.gpay.gsmart.core.pcontractattributevalue.IPContractProductAtrributeValueService;
import vn.gpay.gsmart.core.pcontractbomcolor.IPContractBOMColorService;
import vn.gpay.gsmart.core.pcontractbomcolor.IPContractBom2ColorService;
import vn.gpay.gsmart.core.pcontractbomcolor.PContractBOMColor;
import vn.gpay.gsmart.core.pcontractbomsku.IPContractBOM2SKUService;
import vn.gpay.gsmart.core.pcontractbomsku.IPContractBOMSKUService;
import vn.gpay.gsmart.core.pcontractbomsku.PContractBOMSKU;
import vn.gpay.gsmart.core.pcontractproduct.IPContractProductService;
import vn.gpay.gsmart.core.pcontractproduct.PContractProduct;
import vn.gpay.gsmart.core.pcontractproductbom.IPContractProductBom2Service;
import vn.gpay.gsmart.core.pcontractproductbom.IPContractProductBomService;
import vn.gpay.gsmart.core.pcontractproductbom.PContractProductBom;
import vn.gpay.gsmart.core.pcontractproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.sku.SKU;
import vn.gpay.gsmart.core.task.ITask_Service;
import vn.gpay.gsmart.core.task.Task;
import vn.gpay.gsmart.core.task_checklist.ITask_CheckList_Service;
import vn.gpay.gsmart.core.task_checklist.Task_CheckList;
import vn.gpay.gsmart.core.task_flow.ITask_Flow_Service;
import vn.gpay.gsmart.core.task_flow.Task_Flow;
import vn.gpay.gsmart.core.task_object.ITask_Object_Service;
import vn.gpay.gsmart.core.utils.AtributeFixValues;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/pcontractproductbom")
public class PContractProductBomAPI {
	@Autowired
	IPContractProductBomService ppbomservice;
	@Autowired
	IPContractProductBom2Service ppbom2service;
	@Autowired
	IPContractBOMColorService ppbomcolorservice;
	@Autowired
	IPContractBom2ColorService ppbom2colorservice;
	@Autowired
	IPContractBOMSKUService ppbomskuservice;
	@Autowired
	IPContractBOM2SKUService ppbom2skuservice;
	@Autowired
	IPContractProductAtrributeValueService ppatt_service;
	@Autowired
	IPContractProductSKUService ppskuService;
	@Autowired
	IPContractProductService ppService;
	@Autowired
	ITask_Object_Service taskobjectService;
	@Autowired
	ITask_CheckList_Service checklistService;
	@Autowired
	ITask_Service taskService;
	@Autowired
	ITask_Flow_Service commentService;
	@Autowired
	ISKU_Service skuService;

	@RequestMapping(value = "/create_pcontract_productbom", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> CreateProductBom(HttpServletRequest request,
			@RequestBody PContractProductBom_create_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long productid_link = entity.productid_link;
			long pcontractid_link = entity.pcontractid_link;

			for (Long materialid_link : entity.listnpl) {
				// them vao bang product_bom
				SKU sku = skuService.findOne(materialid_link);
				List<PContractProductBom> listBom = ppbomservice.getby_pcontract_product_material(productid_link,
						pcontractid_link, materialid_link);
				if (listBom.size() == 0) {
					PContractProductBom productbom = new PContractProductBom();
					productbom.setProductid_link(productid_link);
					productbom.setMaterialid_link(materialid_link);
					productbom.setAmount((float) 0);
					productbom.setLost_ratio((float) 0);
					productbom.setDescription("");
					productbom.setCreateduserid_link(user.getId());
					productbom.setCreateddate(new Date());
					productbom.setOrgrootid_link(user.getRootorgid_link());
					productbom.setPcontractid_link(pcontractid_link);
					productbom.setForothercontract(false);
					productbom.setUnitid_link(sku.getUnitid_link());

					ppbomservice.save(productbom);

//					PContractProductBom2 productbom2 = new PContractProductBom2(); 
//					productbom2.setProductid_link(productid_link);
//					productbom2.setMaterialid_link(materialid_link);
//					productbom2.setAmount((float)0);
//					productbom2.setLost_ratio((float)0);
//					productbom2.setDescription("");
//					productbom2.setCreateduserid_link(user.getId());
//					productbom2.setCreateddate(new Date());
//					productbom2.setOrgrootid_link(user.getRootorgid_link());
//					productbom2.setPcontractid_link(pcontractid_link);
//					
//					ppbom2service.save(productbom2);
				}

			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/getlist_pcontract_productbom", method = RequestMethod.POST)
	public ResponseEntity<PContractProductBOM_getbyproduct_response> GetListProductBom(HttpServletRequest request,
			@RequestBody PContractProductBOM_getbyproduct_request entity) {
		PContractProductBOM_getbyproduct_response response = new PContractProductBOM_getbyproduct_response();
		try {
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;

			response.data = ppbomservice.get_pcontract_productBOMbyid(productid_link, pcontractid_link);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<PContractProductBOM_getbyproduct_response>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/getlist_npl_by_pcontract", method = RequestMethod.POST)
	public ResponseEntity<getNPL_by_pcontract_response> GetListProductBom(HttpServletRequest request,
			@RequestBody getNPL_by_pcontract_request entity) {
		getNPL_by_pcontract_response response = new getNPL_by_pcontract_response();
		try {
			long pcontractid_link = entity.pcontractid_link;
			int producttypeid_link = entity.producttypeid_link;

			response.data = skuService.getnpl_by_pcontract(producttypeid_link, pcontractid_link);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<getNPL_by_pcontract_response>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/getlist_npl_by_pcontract_product", method = RequestMethod.POST)
	public ResponseEntity<getNPL_by_pcontract_response> GetListNPL(HttpServletRequest request,
			@RequestBody getNPL_bycontract_and_prodyuct_request entity) {
		getNPL_by_pcontract_response response = new getNPL_by_pcontract_response();
		try {
			long pcontractid_link = entity.pcontractid_link;
			int producttypeid_link = entity.producttypeid_link;
			long productid_link = entity.productid_link;

			response.data = skuService.getnpl_by_pcontract_product(producttypeid_link, pcontractid_link,
					productid_link);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<getNPL_by_pcontract_response>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/getlist_npl_by_pcontract_product_color", method = RequestMethod.POST)
	public ResponseEntity<getNPL_by_pcontract_response> GetListNPLColor(HttpServletRequest request,
			@RequestBody getNPL_byPContract_Product_Color_request entity) {
		getNPL_by_pcontract_response response = new getNPL_by_pcontract_response();
		try {
			long pcontractid_link = entity.pcontractid_link;
			int producttypeid_link = entity.producttypeid_link;
			long productid_link = entity.productid_link;
			long colorid_link = entity.colorid_link;

			response.data = skuService.getnpl_by_pcontract_product_and_color(producttypeid_link, pcontractid_link,
					productid_link, colorid_link);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<getNPL_by_pcontract_response>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/confim_bom1", method = RequestMethod.POST)
	public ResponseEntity<confim_bom1_response> ConfimBom1(HttpServletRequest request,
			@RequestBody confim_bom1_request entity) {
		confim_bom1_response response = new confim_bom1_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;

			List<PContractProduct> list_pp = ppService.get_by_product_and_pcontract(orgrootid_link, productid_link,
					pcontractid_link);
			if (list_pp.size() > 0) {
				PContractProduct pp = list_pp.get(0);
				pp.setIsbomdone(true);
				ppService.save(pp);
			}

			// Danh dau cong viec da xong
			List<Long> list_task = taskobjectService.getby_pcontract_and_product(pcontractid_link, productid_link,
					null);
			for (Long taskid_link : list_task) {
				// Lay checklist cua task

				long tasktype_checklits_id_link = 6;
				List<Task_CheckList> list_sub = checklistService.getby_taskid_link_and_typechecklist(taskid_link,
						tasktype_checklits_id_link);
				if (list_sub.size() > 0) {

					Task task = taskService.findOne(taskid_link);
					task.setDatefinished(new Date());
					task.setStatusid_link(2);
					taskService.save(task);

					Task_Flow flow = new Task_Flow();
					flow.setDatecreated(new Date());
					flow.setDescription("Đã xác nhận định mức");
					flow.setFlowstatusid_link(3);
					flow.setFromuserid_link(user.getId());
					flow.setId(null);
					flow.setOrgrootid_link(orgrootid_link);
					flow.setTaskid_link(taskid_link);
					flow.setTaskstatusid_link(2);
					flow.setTouserid_link(task.getUsercreatedid_link());
					commentService.save(flow);
				}

				for (Task_CheckList checklist : list_sub) {
					checklist.setDone(true);
					checklistService.save(checklist);
				}
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<confim_bom1_response>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/update_pcontract_productbom", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> UpdateProductBom(HttpServletRequest request,
			@RequestBody PContractProductBom_update_request entity) {
		ResponseBase response = new ResponseBase();
		try {

			ppbomservice.save(entity.data);
			// Xóa trong bom_color và bom_sku
			long pcontractid_link = entity.data.getPcontractid_link();
			long productid_link = entity.data.getProductid_link();
			long materialid_link = entity.data.getMaterialid_link();

			if (entity.isUpdateBOM) {
				List<PContractBOMColor> listcolor = ppbomcolorservice
						.getcolor_bymaterial_in_productBOMColor(pcontractid_link, productid_link, materialid_link);
				for (PContractBOMColor pContractBOMColor : listcolor) {
					ppbomcolorservice.delete(pContractBOMColor);
				}

				List<PContractBOMSKU> listsku = ppbomskuservice.getcolor_bymaterial_in_productBOMSKU(pcontractid_link,
						productid_link, materialid_link);
				for (PContractBOMSKU pContractBOMSKU : listsku) {
					ppbomskuservice.delete(pContractBOMSKU);
				}
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/update_pcontract_productbomcolor", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> UpdateProductBomColor(HttpServletRequest request,
			@RequestBody PContractBOMColor_update_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long pcontractid_link = entity.data.getPcontractid_link();
			long productid_link = entity.data.getProductid_link();
			long materialid_link = entity.data.getMaterialid_link();
			long colorid_link = entity.colorid_link;

			List<PContractBOMColor> listcolor = ppbomcolorservice.getall_material_in_productBOMColor(pcontractid_link,
					productid_link, colorid_link, materialid_link);

			PContractBOMColor pContractBOMColor = new PContractBOMColor();
			if (listcolor.size() > 0) {
				pContractBOMColor = listcolor.get(0);
				pContractBOMColor.setAmount(entity.data.getAmount_color());
			} else {
				pContractBOMColor.setAmount(entity.data.getAmount_color());
				pContractBOMColor.setColorid_link(colorid_link);
				pContractBOMColor.setCreateddate(new Date());
				pContractBOMColor.setCreateduserid_link(user.getId());
				pContractBOMColor.setDescription(entity.data.getDescription());
				pContractBOMColor.setId((long) 0);
				pContractBOMColor.setMaterialid_link(materialid_link);
				pContractBOMColor.setOrgrootid_link(user.getRootorgid_link());
				pContractBOMColor.setPcontractid_link(pcontractid_link);
				pContractBOMColor.setProductid_link(productid_link);
			}
			ppbomcolorservice.save(pContractBOMColor);

			// update lại các màu khác từ chung về chung màu
			PContractProductBom pContractProductBom = ppbomservice.findOne(entity.data.getId());
			float amount_color = pContractProductBom.getAmount();

			if (amount_color > 0) {
				// Lấy các màu trong sản phẩm của đơn hàng
				List<Long> list_color = ppskuService.getlistvalue_by_product(pcontractid_link, productid_link,
						AtributeFixValues.ATTR_COLOR);
				list_color.remove(colorid_link);

				for (Long colorid : list_color) {
					PContractBOMColor color = new PContractBOMColor();

					color.setAmount(amount_color);
					color.setColorid_link(colorid);
					color.setCreateddate(new Date());
					color.setCreateduserid_link(user.getId());
					color.setDescription(entity.data.getDescription());
					color.setId((long) 0);
					color.setMaterialid_link(materialid_link);
					color.setOrgrootid_link(user.getRootorgid_link());
					color.setPcontractid_link(pcontractid_link);
					color.setProductid_link(productid_link);

					ppbomcolorservice.save(color);
				}
			}

			// update lai bang bom amount = 0
			pContractProductBom.setAmount((float) 0);
			ppbomservice.update(pContractProductBom);

			// update lai bang sku bom
			List<PContractBOMSKU> listsku = ppbomskuservice.getmaterial_bycolorid_link(pcontractid_link, productid_link,
					colorid_link, materialid_link);
			for (PContractBOMSKU pContractBOMSKU : listsku) {
				ppbomskuservice.delete(pContractBOMSKU);
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/update_pcontract_productbomsku", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> UpdateProductBomSKU(HttpServletRequest request,
			@RequestBody PContractBOMSKU_update_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long pcontractid_link = entity.data.getPcontractid_link();
			long productid_link = entity.data.getProductid_link();
			long materialid_link = entity.data.getMaterialid_link();
			long sizeid_link = entity.sizeid_link;
			long colorid_link = entity.colorid_link;
			long skuid_link = ppbomskuservice.getskuid_link_by_color_and_size(colorid_link, sizeid_link,
					productid_link);

			// Kiem tra neu chua co thi insert neu co roi thi update
			List<PContractBOMSKU> list_sku = ppbomskuservice.getall_material_in_productBOMSKU(pcontractid_link,
					productid_link, sizeid_link, colorid_link, materialid_link);

			PContractBOMSKU pContractBOMSKU = new PContractBOMSKU();
			if (list_sku.size() > 0) {
				pContractBOMSKU = list_sku.get(0);
				pContractBOMSKU.setAmount(entity.value);
			} else {
				pContractBOMSKU.setAmount(entity.value);
				pContractBOMSKU.setCreateddate(new Date());
				pContractBOMSKU.setCreateduserid_link(user.getId());
				pContractBOMSKU.setDescription(entity.data.getDescription());
				pContractBOMSKU.setId((long) 0);
				pContractBOMSKU.setMaterialid_link(materialid_link);
				pContractBOMSKU.setOrgrootid_link(user.getRootorgid_link());
				pContractBOMSKU.setPcontractid_link(pcontractid_link);
				pContractBOMSKU.setProductid_link(productid_link);
				pContractBOMSKU.setLost_ratio(entity.data.getLost_ratio());
				pContractBOMSKU.setSkuid_link(skuid_link);
			}
			ppbomskuservice.save(pContractBOMSKU);

			// update lai bang bom amount = 0
			PContractProductBom pContractProductBom = ppbomservice.findOne(entity.data.getId());
			pContractProductBom.setAmount((float) 0);
			ppbomservice.update(pContractProductBom);

			// update lai bang sku bom
			List<PContractBOMColor> listcolor = ppbomcolorservice.getall_material_in_productBOMColor(pcontractid_link,
					productid_link, colorid_link, materialid_link);

			for (PContractBOMColor pColor : listcolor) {
				ppbomcolorservice.delete(pColor);
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/getlist_pcontract_productbomcolor", method = RequestMethod.POST)
	public ResponseEntity<PContractProductBOM_getbomcolor_response> GetListProductBomColor(HttpServletRequest request,
			@RequestBody PContractProductBOM_getbomcolor_request entity) {
		PContractProductBOM_getbomcolor_response response = new PContractProductBOM_getbomcolor_response();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
//					.getPrincipal();
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			long colorid_link = entity.colorid_link;
			List<Map<String, String>> listdata = new ArrayList<Map<String, String>>();

			List<PContractProductBom> listbom = ppbomservice.get_pcontract_productBOMbyid(productid_link,
					pcontractid_link);
			List<PContractBOMColor> listbomcolor = ppbomcolorservice
					.getall_material_in_productBOMColor(pcontractid_link, productid_link, colorid_link, (long) 0);
			List<PContractBOMSKU> listbomsku = ppbomskuservice.getmaterial_bycolorid_link(pcontractid_link,
					productid_link, colorid_link, 0);

			List<Long> List_size = ppskuService.getlistvalue_by_product(pcontractid_link, productid_link, (long) 30);
//			List<Long> List_size = ppatt_service.getvalueid_by_product_and_pcontract_and_attribute(orgrootid_link, pcontractid_link, productid_link, (long) 30);
			// ppbomskuservice.getsize_bycolor(pcontractid_link, productid_link,
			// colorid_link);

			for (PContractProductBom pContractProductBom : listbom) {
				Map<String, String> map = new HashMap<String, String>();
				List<PContractBOMColor> listbomcolorclone = new ArrayList<PContractBOMColor>(listbomcolor);
				listbomcolorclone
						.removeIf(c -> !c.getMaterialid_link().equals(pContractProductBom.getMaterialid_link()));

				Float amount_color = (float) 0;
				if (listbomcolorclone.size() > 0)
					amount_color = listbomcolorclone.get(0).getAmount();

				map.put("amount", pContractProductBom.getAmount() + "");

				map.put("forothercontract_name", pContractProductBom.getForothercontract() + "");

				map.put("amount_color", amount_color + "");

				map.put("coKho", pContractProductBom.getCoKho() + "");

				map.put("createddate", pContractProductBom.getCreateddate() + "");

				map.put("createduserid_link", pContractProductBom.getCreateduserid_link() + "");

				map.put("description", pContractProductBom.getDescription() + "");

				map.put("id", pContractProductBom.getId() + "");

				map.put("lost_ratio", pContractProductBom.getLost_ratio() + "");

				map.put("materialid_link", pContractProductBom.getMaterialid_link() + "");

				map.put("materialName", pContractProductBom.getMaterialName() + "");

				map.put("materialCode", pContractProductBom.getMaterialCode() + "");

				map.put("orgrootid_link", pContractProductBom.getOrgrootid_link() + "");

				map.put("pcontractid_link", pContractProductBom.getPcontractid_link() + "");

				map.put("product_type", pContractProductBom.getProduct_type() + "");

				map.put("product_typename", pContractProductBom.getProduct_typeName() + "");

				map.put("productid_link", pContractProductBom.getProductid_link() + "");

				map.put("tenMauNPL", pContractProductBom.getTenMauNPL() + "");

				map.put("thanhPhanVai", pContractProductBom.getThanhPhanVai() + "");

				map.put("unitName", pContractProductBom.getUnitName() + "");

				for (Long size : List_size) {
					List<PContractBOMSKU> listbomsku_clone = new ArrayList<PContractBOMSKU>(listbomsku);
					long skuid_link = ppbomskuservice.getskuid_link_by_color_and_size(colorid_link, size,
							productid_link);
					listbomsku_clone
							.removeIf(c -> !c.getMaterialid_link().equals(pContractProductBom.getMaterialid_link())
									|| !c.getSkuid_link().equals(skuid_link));
					Float amount_size = (float) 0;
					if (listbomsku_clone.size() > 0)
						amount_size = listbomsku_clone.get(0).getAmount();
					map.put("" + size, amount_size + "");
				}

				listdata.add(map);
			}

			response.data = listdata;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<PContractProductBOM_getbomcolor_response>(response, HttpStatus.OK);
	}

	public List<Map<String, String>> GetListProductBomColor(PContractProductBOM_getbomcolor_request entity) {
		try {
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			long colorid_link = entity.colorid_link;
			List<Map<String, String>> listdata = new ArrayList<Map<String, String>>();

			List<PContractProductBom> listbom = ppbomservice.get_pcontract_productBOMbyid(productid_link,
					pcontractid_link);
			List<PContractBOMColor> listbomcolor = ppbomcolorservice
					.getall_material_in_productBOMColor(pcontractid_link, productid_link, colorid_link, (long) 0);
			List<PContractBOMSKU> listbomsku = ppbomskuservice.getmaterial_bycolorid_link(pcontractid_link,
					productid_link, colorid_link, 0);

			List<Long> List_size = ppskuService.getlistvalue_by_product(pcontractid_link, productid_link, (long) 30);

			for (PContractProductBom pContractProductBom : listbom) {
				Map<String, String> map = new HashMap<String, String>();
				List<PContractBOMColor> listbomcolorclone = new ArrayList<PContractBOMColor>(listbomcolor);
				listbomcolorclone
						.removeIf(c -> !c.getMaterialid_link().equals(pContractProductBom.getMaterialid_link()));

				Float amount_color = (float) 0;
				if (listbomcolorclone.size() > 0)
					amount_color = listbomcolorclone.get(0).getAmount();

				map.put("amount", pContractProductBom.getAmount() + "");

				map.put("amount_color", amount_color + "");

				map.put("id", pContractProductBom.getId() + "");

				map.put("lost_ratio", pContractProductBom.getLost_ratio() + "");

				map.put("materialid_link", pContractProductBom.getMaterialid_link() + "");

				map.put("materialName", pContractProductBom.getMaterialName() + "");

				map.put("materialCode", pContractProductBom.getMaterialCode() + "");

				map.put("unitName", pContractProductBom.getUnitName() + "");

				for (Long size : List_size) {
					List<PContractBOMSKU> listbomsku_clone = new ArrayList<PContractBOMSKU>(listbomsku);
					long skuid_link = ppbomskuservice.getskuid_link_by_color_and_size(colorid_link, size,
							productid_link);
					listbomsku_clone
							.removeIf(c -> !c.getMaterialid_link().equals(pContractProductBom.getMaterialid_link())
									|| !c.getSkuid_link().equals(skuid_link));
					Float amount_size = (float) 0;
					if (listbomsku_clone.size() > 0)
						amount_size = listbomsku_clone.get(0).getAmount();
					map.put("" + size, amount_size + "");
				}

				listdata.add(map);
			}

			return listdata;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/deletematerial", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> DeleteMaterial(HttpServletRequest request,
			@RequestBody PContractProductBom_delete_material_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			long materialid_link = entity.materialid_link;

			// xoa trong bang pcontract_product_bom
			List<PContractProductBom> list_bom = ppbomservice.getby_pcontract_product_material(productid_link,
					pcontractid_link, materialid_link);
			for (PContractProductBom bom : list_bom) {
				ppbomservice.delete(bom);
			}

			// Xoa trong bang pcontract_product_color_bom
			List<PContractBOMColor> list_bom_color = ppbomcolorservice
					.getcolor_bymaterial_in_productBOMColor(pcontractid_link, productid_link, materialid_link);
			for (PContractBOMColor color : list_bom_color) {
				ppbomcolorservice.delete(color);
			}

			// Xoa trong bang pcontract_sku_bom
			List<PContractBOMSKU> list_bom_sku = ppbomskuservice.getcolor_bymaterial_in_productBOMSKU(pcontractid_link,
					productid_link, materialid_link);
			for (PContractBOMSKU sku : list_bom_sku) {
				ppbomskuservice.delete(sku);
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
}
