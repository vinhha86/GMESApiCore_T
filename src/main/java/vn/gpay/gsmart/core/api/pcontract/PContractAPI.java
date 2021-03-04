package vn.gpay.gsmart.core.api.pcontract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.pcontract.IPContractService;
import vn.gpay.gsmart.core.pcontract.IPContract_AutoID_Service;
import vn.gpay.gsmart.core.pcontract.PContract;
import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontractbomcolor.IPContractBOMColorService;
import vn.gpay.gsmart.core.pcontractbomcolor.IPContractBom2ColorService;
import vn.gpay.gsmart.core.pcontractbomcolor.PContractBOMColor;
import vn.gpay.gsmart.core.pcontractbomcolor.PContractBom2Color;
import vn.gpay.gsmart.core.pcontractbomsku.IPContractBOM2SKUService;
import vn.gpay.gsmart.core.pcontractbomsku.IPContractBOMSKUService;
import vn.gpay.gsmart.core.pcontractbomsku.PContractBOM2SKU;
import vn.gpay.gsmart.core.pcontractbomsku.PContractBOMSKU;
import vn.gpay.gsmart.core.pcontractproduct.IPContractProductService;
import vn.gpay.gsmart.core.pcontractproduct.PContractProduct;
import vn.gpay.gsmart.core.pcontractproductbom.IPContractProductBom2Service;
import vn.gpay.gsmart.core.pcontractproductbom.IPContractProductBomService;
import vn.gpay.gsmart.core.pcontractproductbom.PContractProductBom;
import vn.gpay.gsmart.core.pcontractproductbom.PContractProductBom2;
import vn.gpay.gsmart.core.pcontractproductdocument.IPContractProducDocumentService;
import vn.gpay.gsmart.core.pcontractproductdocument.PContractProductDocument;
import vn.gpay.gsmart.core.pcontractproductpairing.IPContractProductPairingService;
import vn.gpay.gsmart.core.pcontractproductpairing.PContractProductPairing;
import vn.gpay.gsmart.core.pcontractproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKU;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder_req.IPOrder_Req_Service;
import vn.gpay.gsmart.core.product.IProductService;
import vn.gpay.gsmart.core.product.Product;
import vn.gpay.gsmart.core.productpairing.IProductPairingService;
import vn.gpay.gsmart.core.productpairing.ProductPairing;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.security.GpayUserOrg;
import vn.gpay.gsmart.core.security.IGpayUserOrgService;
import vn.gpay.gsmart.core.utils.OrgType;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/pcontract")
public class PContractAPI {
	@Autowired
	IPContractService pcontractService;
	@Autowired
	IPContract_AutoID_Service pcontract_AutoID_Service;
	@Autowired
	IOrgService orgService;
	@Autowired
	IPContract_POService poService;
	@Autowired
	IPOrder_Service porderService;
	@Autowired
	IPContractProductSKUService pcontract_sku_Service;
	@Autowired
	IPContractProductService pcontract_product_Service;
	@Autowired
	IPContractProductPairingService pcontract_pairing_Service;
	@Autowired
	IPContractProducDocumentService pcontract_document_Service;
	@Autowired
	IPContractProductBomService pcontract_bom_Service;
	@Autowired
	IPContractProductBom2Service pcontract_bom2_Service;
	@Autowired
	IPContractBOMColorService pcontract_bom_color_Service;
	@Autowired
	IPContractBom2ColorService pcontract_bom2_color_Service;
	@Autowired
	IPContractBOMSKUService pcontract_bom_sku_Service;
	@Autowired
	IPContractBOM2SKUService pcontract_bom2_sku_Service;
	@Autowired
	IPOrder_Req_Service porderReqService;
	@Autowired
	IGpayUserOrgService userOrgService;
	@Autowired
	IProductService productService;
	@Autowired
	IProductPairingService pairService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<PContract_create_response> PContractCreate(@RequestBody PContract_create_request entity,
			HttpServletRequest request) {
		PContract_create_response response = new PContract_create_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long usercreatedid_link = user.getId();

			PContract pcontract = entity.data;
			if (pcontract.getId() == 0 || pcontract.getId() == null) {
				if (null == pcontract.getContractcode() || pcontract.getContractcode().length() == 0) {
					Org theBuyer = orgService.findOne(pcontract.getOrgbuyerid_link());
					if (null != theBuyer)
						pcontract.setContractcode(pcontract_AutoID_Service.getLastID(theBuyer.getCode()));
					else
						pcontract.setContractcode(pcontract_AutoID_Service.getLastID("UNKNOWN"));
				} else {
					String contractcode = pcontract.getContractcode();
					long pcontractid_link = pcontract.getId();

					List<PContract> lstcheck = pcontractService.getby_code(orgrootid_link, contractcode,
							pcontractid_link);
					if (lstcheck.size() > 0) {
						response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
						response.setMessage("Mã đã tồn tại trong hệ thống!");
						return new ResponseEntity<PContract_create_response>(response, HttpStatus.BAD_REQUEST);
					}
				}
				pcontract.setOrgrootid_link(orgrootid_link);
				pcontract.setUsercreatedid_link(usercreatedid_link);
				pcontract.setDatecreated(new Date());
			} else {
				PContract pc_old = pcontractService.findOne(pcontract.getId());
				pcontract.setOrgrootid_link(pc_old.getOrgrootid_link());
				pcontract.setUsercreatedid_link(pc_old.getUsercreatedid_link());
				pcontract.setDatecreated(pc_old.getDatecreated());
			}

			pcontract = pcontractService.save(pcontract);
			response.id = pcontract.getId();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContract_create_response>(response, HttpStatus.OK);

		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<PContract_create_response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/getbypaging", method = RequestMethod.POST)
	public ResponseEntity<PContract_getbypaging_response> PContractGetpage(
			@RequestBody PContract_getbypaging_request entity, HttpServletRequest request) {
		PContract_getbypaging_response response = new PContract_getbypaging_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();

			Page<PContract> pcontract = pcontractService.getall_by_orgrootid_paging(orgrootid_link, entity);

			response.data = pcontract.getContent();
			response.totalCount = pcontract.getTotalElements();

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContract_getbypaging_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<PContract_getbypaging_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/getlistbypaging", method = RequestMethod.POST)
	public ResponseEntity<PContract_getbypaging_response> PContractGetpageList(
			@RequestBody PContract_getbypaging_request entity, HttpServletRequest request) {
		PContract_getbypaging_response response = new PContract_getbypaging_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();

			List<PContract> pcontract = pcontractService.getalllist_by_orgrootid_paging(orgrootid_link, entity);

			response.data = new ArrayList<PContract>();

			for (PContract pc : pcontract) {
				String cc = pc.getContractcode().toLowerCase();
				String pl = pc.getProductlist().toLowerCase();
				String pol = pc.getPolist().toLowerCase();
				if (!cc.contains(entity.contractcode.toLowerCase()))
					continue;
				if (!pl.contains(entity.style.toLowerCase()))
					continue;
				if (!pol.contains(entity.po.toLowerCase()))
					continue;
				response.data.add(pc);
			}
			response.totalCount = response.data.size();

			PageRequest page = PageRequest.of(entity.page - 1, entity.limit);
			int start = (int) page.getOffset();
			int end = (start + page.getPageSize()) > response.data.size() ? response.data.size()
					: (start + page.getPageSize());
			Page<PContract> pageToReturn = new PageImpl<PContract>(response.data.subList(start, end), page,
					response.data.size());

			response.data = pageToReturn.getContent();
//			response.totalCount = pcontract.getTotalElements();

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContract_getbypaging_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<PContract_getbypaging_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/getone", method = RequestMethod.POST)
	public ResponseEntity<PContract_getone_response> PContractGetOne(@RequestBody PContract_getone_request entity,
			HttpServletRequest request) {
		PContract_getone_response response = new PContract_getone_response();
		try {

			response.data = pcontractService.findOne(entity.id);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContract_getone_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<PContract_getone_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> PContractDelete(@RequestBody PContract_delete_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			long orgrootid_link = user.getRootorgid_link();
			// Check if having PO? refuse deleting if have
			if (poService.getPOByContract(orgrootid_link, entity.id).size() > 0) {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage(
						"Hiện vẫn đang có đơn hàng (PO) trong hợp đồng! Cần xóa hết PO trước khi xóa hợp đồng");
				return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
			}
			// Check if having POrder? refuse deleting if have
			if (porderService.getByContract(entity.id).size() > 0) {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage(
						"Hiện vẫn đang có Lệnh SX trong hợp đồng! Cần xóa hết Lệnh SX trước khi xóa hợp đồng");
				return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);

			}
			// Delete products
			// pcontract_products;pcontract_product_sku;pcontract_product_pairing;pcontract_product_document
			for (PContractProduct theProduct : pcontract_product_Service.get_by_pcontract(orgrootid_link, entity.id)) {
				pcontract_product_Service.delete(theProduct);
			}
			for (PContractProductSKU theSku : pcontract_sku_Service.getlistsku_bypcontract(orgrootid_link, entity.id)) {
				pcontract_sku_Service.delete(theSku);
			}
			for (PContractProductPairing thePairing : pcontract_pairing_Service.getall_bypcontract(orgrootid_link,
					entity.id)) {
				pcontract_pairing_Service.delete(thePairing);
			}
			for (PContractProductDocument theDocument : pcontract_document_Service.getlist_bycontract(orgrootid_link,
					entity.id)) {
				pcontract_document_Service.delete(theDocument);
			}

			// Delete BOM
			// pcontract_bom_sku;pcontract_bom_product;pcontract_bom_color
			// pcontract_bom2_sku;pcontract_bom2_product;pcontract_bom2_color
			for (PContractProductBom theBom : pcontract_bom_Service.getall_bypcontract(orgrootid_link, entity.id)) {
				pcontract_bom_Service.delete(theBom);
			}
			for (PContractBOMColor theBomcolor : pcontract_bom_color_Service.getall_bypcontract(orgrootid_link,
					entity.id)) {
				pcontract_bom_color_Service.delete(theBomcolor);
			}
			for (PContractBOMSKU theBomsku : pcontract_bom_sku_Service.getall_bypcontract(orgrootid_link, entity.id)) {
				pcontract_bom_sku_Service.delete(theBomsku);
			}

			for (PContractProductBom2 theBom2 : pcontract_bom2_Service.getall_bypcontract(orgrootid_link, entity.id)) {
				pcontract_bom2_Service.delete(theBom2);
			}
			for (PContractBom2Color theBom2color : pcontract_bom2_color_Service.getall_bypcontract(orgrootid_link,
					entity.id)) {
				pcontract_bom2_color_Service.delete(theBom2color);
			}
			for (PContractBOM2SKU theBom2sku : pcontract_bom2_sku_Service.getall_bypcontract(orgrootid_link,
					entity.id)) {
				pcontract_bom2_sku_Service.delete(theBom2sku);
			}

			// Delete PContract
			pcontractService.deleteById(entity.id);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/getbysearch", method = RequestMethod.POST)
	public ResponseEntity<PContract_getbypaging_response> PContractGetBySearch(
			@RequestBody PContract_getbysearch_request entity, HttpServletRequest request) {
		PContract_getbypaging_response response = new PContract_getbypaging_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

//		//fix dieu kien tim kiem cho vendor cua DHA
//		if (user.getUsername().toLowerCase().trim().contains("hansoll")) 
//			entity.orgvendorid_link = 197;
//		else
//			if (user.getUsername().toLowerCase().trim().contains("paroman")) 
//				entity.orgvendorid_link = 200;
//		else
//			if (user.getUsername().toLowerCase().trim().contains("ekline")) 
//				entity.orgvendorid_link = 189;

		// Lay danh sach Vendor duoc phep quan ly
		List<GpayUserOrg> lsVendor = userOrgService.getall_byuser_andtype(user.getId(), OrgType.ORG_TYPE_VENDOR);
		List<GpayUserOrg> lsBuyer = userOrgService.getall_byuser_andtype(user.getId(), OrgType.ORG_TYPE_BUYER);

		try {
			List<Long> vendors = new ArrayList<Long>();
			for(GpayUserOrg vendor : lsVendor) {
				vendors.add(vendor.getOrgid_link());
			}
			
			List<Long> buyers = new ArrayList<Long>();
			for(GpayUserOrg buyer : lsBuyer) {
				buyers.add(buyer.getOrgid_link());
			}

			List<Long> orgs = new ArrayList<Long>();
			Long orgid_link = user.getOrgid_link();
			if (orgid_link != 0 && orgid_link != 1 && orgid_link != null) {
				// Lay danh sach cac phan xuong ma nguoi dung duoc phep quan ly
				for (GpayUserOrg userorg : userOrgService.getall_byuser_andtype(user.getId(),
						OrgType.ORG_TYPE_FACTORY)) {
					orgs.add(userorg.getOrgid_link());
				}
				// Them chinh don vi cua user
				orgs.add(orgid_link);
			}

			// Lay danh sach product thoa man dieu kien
			List<Long> products = new ArrayList<Long>();
			if (entity.productbuyer_code.length() > 0) {
				List<Product> product_lst = productService.getProductByLikeBuyercode(entity.productbuyer_code);
				for (Product theProduct : product_lst) {
					//kiem tra xem san pham co nam trong bo ko thì lay san pham bo vao
					List<ProductPairing> list_pair = pairService.getby_product(theProduct.getId());
					for(ProductPairing pair : list_pair) {
						products.add(pair.getProductpairid_link());
					}
					
					products.add(theProduct.getId());
				}
					
			}

			List<Long> pos = poService.getpcontract_BySearch(entity.po_code, orgs);
			// Lay danh sach PO thoa man dieu kien
//			List<PContract_PO> lstPO = poService.getBySearch(entity.po_code, orgs);
//			for (PContract_PO thePO : lstPO)
//				if(!pos.contains(thePO.getPcontractid_link()))
//					pos.add(thePO.getPcontractid_link());
			
			List<Long> product = new ArrayList<Long>();
			// Lay danh sach PO thoa man dieu kien
			List<Long> list_product = pcontract_product_Service.getby_product(products);
			for(Long p : list_product) {
				product.add(p);
			}

			List<PContract> list = pcontractService.getBySearch_PosList(entity, pos, product, vendors, buyers);
//			if (user.getOrgid_link() != 1) {
//				List<PContract> list_remove = new ArrayList<PContract>();
//				for (PContract thepcontract : list) {
//					if (!thepcontract.getIsHavingPO())
//						list_remove.add(thepcontract);
//				}
//				list.removeAll(list_remove);
//			}
			response.data = list;

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContract_getbypaging_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<PContract_getbypaging_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/findByContractcode", method = RequestMethod.POST)
	public ResponseEntity<PContract_getbypaging_response> findByContractcode(
			@RequestBody PContract_findByContractcode_request entity, HttpServletRequest request) {
		PContract_getbypaging_response response = new PContract_getbypaging_response();
//		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			response.data = pcontractService.findByContractcode(entity.contractcode);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContract_getbypaging_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<PContract_getbypaging_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/findByExactContractcode", method = RequestMethod.POST)
	public ResponseEntity<PContract_getbypaging_response> findByExactContractcode(
			@RequestBody PContract_findByContractcode_request entity, HttpServletRequest request) {
		PContract_getbypaging_response response = new PContract_getbypaging_response();
//		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			List<PContract> result = pcontractService.findByExactContractcode(entity.contractcode);
			if(result.size() > 0) {
				response.data = result;
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			}else {
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage("Mã đơn hàng không tồn tại");
			}
			return new ResponseEntity<PContract_getbypaging_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<PContract_getbypaging_response>(response, HttpStatus.OK);
		}
	}

}
