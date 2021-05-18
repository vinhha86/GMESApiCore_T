package vn.gpay.gsmart.core.api.Upload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.gpay.gsmart.core.attribute.Attribute;
import vn.gpay.gsmart.core.attribute.IAttributeService;
import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.category.IShipModeService;
import vn.gpay.gsmart.core.category.ShipMode;
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.pcontract.IPContractService;
import vn.gpay.gsmart.core.pcontract.PContract;
import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.pcontract_po_productivity.IPContract_PO_Productivity_Service;
import vn.gpay.gsmart.core.pcontract_po_productivity.PContract_PO_Productivity;
import vn.gpay.gsmart.core.pcontract_price.IPContract_Price_DService;
import vn.gpay.gsmart.core.pcontract_price.IPContract_Price_Service;
import vn.gpay.gsmart.core.pcontract_price.PContract_Price;
import vn.gpay.gsmart.core.pcontract_price.PContract_Price_D;
import vn.gpay.gsmart.core.pcontractproduct.IPContractProductService;
import vn.gpay.gsmart.core.pcontractproduct.PContractProduct;
import vn.gpay.gsmart.core.pcontractproductpairing.IPContractProductPairingService;
import vn.gpay.gsmart.core.pcontractproductpairing.PContractProductPairing;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder_req.IPOrder_Req_Service;
import vn.gpay.gsmart.core.porder_req.POrder_Req;
import vn.gpay.gsmart.core.product.IProductService;
import vn.gpay.gsmart.core.product.Product;
import vn.gpay.gsmart.core.productattributevalue.IProductAttributeService;
import vn.gpay.gsmart.core.productattributevalue.ProductAttributeValue;
import vn.gpay.gsmart.core.productpairing.IProductPairingService;
import vn.gpay.gsmart.core.productpairing.ProductPairing;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sizeset.ISizeSetService;
import vn.gpay.gsmart.core.sku.ISKU_AttributeValue_Service;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.sku.SKU;
import vn.gpay.gsmart.core.sku.SKU_Attribute_Value;
import vn.gpay.gsmart.core.utils.AtributeFixValues;
import vn.gpay.gsmart.core.utils.ColumnTempNew;
import vn.gpay.gsmart.core.utils.ColumnTemplate;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.POStatus;
import vn.gpay.gsmart.core.utils.POType;
import vn.gpay.gsmart.core.utils.POrderReqStatus;
import vn.gpay.gsmart.core.utils.ProductType;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/upload")
public class UploadAPI {
	@Autowired Common commonService;
	@Autowired
	IProductService productService; 
	@Autowired
	IAttributeService attrService;
	@Autowired IProductAttributeService pavService;
	@Autowired ISKU_Service skuService;
	@Autowired ISKU_AttributeValue_Service skuattService;
	@Autowired
	IProductPairingService productpairService;
	@Autowired IPContract_Price_DService pricedetailService;
	@Autowired
	IPContractProductService pcontractproductService;
	@Autowired
	IPContractProductPairingService pcontractpairService;
	@Autowired
	IShipModeService shipmodeService;
	@Autowired IOrgService orgService;
	@Autowired
	IPContract_POService pcontract_POService;
	@Autowired
	IPContract_Price_Service priceService;
	@Autowired
	IPContract_PO_Productivity_Service productivityService;
	@Autowired ISizeSetService sizesetService;
	@Autowired IPOrder_Req_Service reqService;
	@Autowired IPOrder_Service porderService;
	@Autowired IPContractService pcontractService;
	
	@RequestMapping(value = "/offers", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> UploadTemplate(HttpServletRequest request,
			@RequestParam("file") MultipartFile file, @RequestParam("pcontractid_link") long pcontractid_link) {
		ResponseBase response = new ResponseBase();

		Date current_time = new Date();
		String name = "";
		String mes_err = "";
		try {
			PContract pcontract = pcontractService.findOne(pcontractid_link);
			if(pcontract == null) {
				mes_err = "Đơn hàng không tồn tại trong hệ thống! Bạn hãy kiểm tra lại";
			}
			
			if(mes_err == "") {
				GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				long orgrootid_link = user.getRootorgid_link();
				String FolderPath = "upload/pcontract_po";

				String uploadRootPath = request.getServletContext().getRealPath(FolderPath);

				File uploadRootDir = new File(uploadRootPath);
				// Tạo thư mục gốc upload nếu nó không tồn tại.
				if (!uploadRootDir.exists()) {
					uploadRootDir.mkdirs();
				}

				name = file.getOriginalFilename();
				if (name != null && name.length() > 0) {
					String[] str = name.split("\\.");
					String extend = str[str.length - 1];
					name = current_time.getTime() + "." + extend;
					File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);

					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(file.getBytes());
					stream.close();

					// doc file upload
					XSSFWorkbook workbook = new XSSFWorkbook(serverFile);
					XSSFSheet sheet = workbook.getSheetAt(0);

					// Kiem tra header
					int rowNum = 2;
					int colNum = 1, col_phancach1 = 13, col_phancach2 = 0;
					
					Row row = sheet.getRow(rowNum);
					Row rowheader = sheet.getRow(0);				
					
					try {
						String STT = "";
						STT = commonService.getStringValue(row.getCell(ColumnTempNew.STT));
						STT = STT.equals("0") ? "" : STT;
						
						while (!STT.equals("")) {
							//Lay thong tin PO kiem tra xem PO da ton tai trong he thong hay chua
							//Neu la san pham don chiec thi kiem tra masp, ngay giao, vendor target
							//Neu la san pham bo thi kiem tra masp bo, ngay giao
							
							//Kiem tra san pham co chua thi them san pham vao trong he thong
							long productid_link = 0;
							colNum = ColumnTempNew.MaSP + 1;
							String product_code = commonService.getStringValue(row.getCell(ColumnTempNew.MaSP));
							
							colNum = ColumnTempNew.TenSP + 1;
							String stylename = commonService.getStringValue(row.getCell(ColumnTempNew.TenSP));
							
							if(product_code=="") {
								mes_err = "Mã Sản phẩm không được để trống";
								break;
							}
							
							List<Product> products = productService.getone_by_code(orgrootid_link, product_code, (long) 0,
									ProductType.SKU_TYPE_COMPLETEPRODUCT);
							if (products.size() == 0) {
								Product p = new Product();
								p.setBuyercode(product_code);
								p.setBuyername(stylename);
								p.setId(null);
								p.setOrgrootid_link(orgrootid_link);
								p.setStatus(1);
								p.setUsercreateid_link(user.getId());
								p.setTimecreate(current_time);
								p.setProducttypeid_link(ProductType.SKU_TYPE_COMPLETEPRODUCT);
								p = productService.save(p);

								productid_link = p.getId();
								
								//Sinh thuoc tinh mac dinh cho san pham
								List<Attribute> lstAttr = attrService.getList_attribute_forproduct(ProductType.SKU_TYPE_COMPLETEPRODUCT,
										user.getRootorgid_link());
								for (Attribute attribute : lstAttr) {
									ProductAttributeValue pav = new ProductAttributeValue();
									long value = 0;
									
									if(attribute.getId() == AtributeFixValues.ATTR_COLOR) {
										value = AtributeFixValues.value_color_all;
									}
									else if(attribute.getId() == AtributeFixValues.ATTR_SIZE) {
										value = AtributeFixValues.value_size_all;
									} else if(attribute.getId() == AtributeFixValues.ATTR_SIZEWIDTH) {
										value = AtributeFixValues.value_sizewidth_all;
									}
									
									pav.setId((long) 0);
									pav.setProductid_link(productid_link);
									pav.setAttributeid_link(attribute.getId());
									pav.setAttributevalueid_link(value);
									pav.setOrgrootid_link(user.getRootorgid_link());
									pavService.save(pav);
								}
								
								//Sinh SKU cho mau all va co all
								long skuid_link = 0;
								
								SKU sku = new SKU();
								sku.setId(skuid_link);
								sku.setCode(genCodeSKU(p));
								sku.setName(p.getBuyername());
								sku.setProductid_link(productid_link);
								sku.setOrgrootid_link(user.getRootorgid_link());
								sku.setSkutypeid_link(ProductType.SKU_TYPE_COMPLETEPRODUCT);

								sku = skuService.save(sku);
								skuid_link = sku.getId();
								
								// Them vao bang sku_attribute_value
								SKU_Attribute_Value savMau = new SKU_Attribute_Value();
								savMau.setId((long) 0);
								savMau.setAttributevalueid_link(AtributeFixValues.value_color_all);
								savMau.setAttributeid_link(AtributeFixValues.ATTR_COLOR);
								savMau.setOrgrootid_link(user.getRootorgid_link());
								savMau.setSkuid_link(skuid_link);
								savMau.setUsercreateid_link(user.getId());
								savMau.setTimecreate(new Date());

								skuattService.save(savMau);

								SKU_Attribute_Value savCo = new SKU_Attribute_Value();
								savCo.setId((long) 0);
								savCo.setAttributevalueid_link(AtributeFixValues.value_size_all);
								savCo.setAttributeid_link(AtributeFixValues.ATTR_SIZE);
								savCo.setOrgrootid_link(user.getRootorgid_link());
								savCo.setSkuid_link(skuid_link);
								savCo.setUsercreateid_link(user.getId());
								savCo.setTimecreate(new Date());

								skuattService.save(savCo);
								
							} else {
								productid_link = products.get(0).getId();
							}
							
							//Kiem tra xem PO co phai la hang bo hay khong
							long product_set_id_link = 0;
							
							colNum = ColumnTempNew.Style_Set + 1;
							String product_set_code = commonService.getStringValue(row.getCell(ColumnTempNew.Style_Set));
							product_set_code = product_set_code.equals("0") ? "" : product_set_code;
							
							colNum = ColumnTemplate.amount_style + 1;
							String s_amount = commonService.getStringValue(row.getCell(ColumnTemplate.amount_style));
							s_amount = s_amount.replace(",", "");
							int amount = (int) row.getCell(ColumnTemplate.amount_style).getNumericCellValue() == 0 ? 1 : (int) row.getCell(ColumnTemplate.amount_style).getNumericCellValue();
							
							
							if (!product_set_code.equals(null) && !product_set_code.equals("")) {
								List<Product> product_set = productService.getone_by_code(orgrootid_link, product_set_code,
										(long) 0, ProductType.SKU_TYPE_PRODUCT_PAIR);
								if (product_set.size() == 0) {
									Product set = new Product();
									set.setId(null);
									set.setBuyercode(product_set_code);
									set.setBuyername("");
									set.setDescription("");
									set.setOrgrootid_link(orgrootid_link);
									set.setStatus(1);
									set.setUsercreateid_link(user.getId());
									set.setTimecreate(current_time);
									set.setProducttypeid_link(ProductType.SKU_TYPE_PRODUCT_PAIR);
									set = productService.save(set);

									product_set_id_link = set.getId();
								} else {
									product_set_id_link = product_set.get(0).getId();
								}
							}
							
							// kiem tra trong bang productpair co chua thi them bo vao
							if (product_set_id_link > 0) {
								ProductPairing pair = productpairService.getproduct_pairing_bykey(productid_link,
										product_set_id_link);
								if (pair == null) {
									ProductPairing newpair = new ProductPairing();
									newpair.setAmount(amount);
									newpair.setId(null);
									newpair.setOrgrootid_link(orgrootid_link);
									newpair.setProductid_link(productid_link);
									newpair.setProductpairid_link(product_set_id_link);
									productpairService.save(newpair);
									
									Product set = productService.findOne(product_set_id_link);
									String name_old = set.getBuyername();
									name_old = name_old.equals("") ? "": name_old+ "; ";
									String name_new = name_old+amount+"-"+product_code;
									set.setBuyername(name_new);
								}
							}
							
							// Them san pham vao trong pcontract
							List<PContractProduct> list_product = pcontractproductService.get_by_product_and_pcontract(
									orgrootid_link, productid_link, pcontractid_link);
							if (list_product.size() == 0) {
								PContractProduct product = new PContractProduct();
								product.setIs_breakdown_done(false);
								product.setIsbom2done(false);
								product.setIsbomdone(false);
								product.setOrgrootid_link(orgrootid_link);
								product.setPcontractid_link(pcontractid_link);
								product.setProductid_link(productid_link);
								product.setId(null);
								pcontractproductService.save(product);
							}
							
							// them bo vao trong pcontract
							if (product_set_id_link > 0) {
								List<PContractProductPairing> list_pair = pcontractpairService
										.getdetail_bypcontract_and_productpair(orgrootid_link, pcontractid_link,
												product_set_id_link);
								if (list_pair.size() == 0) {
									PContractProductPairing pair = new PContractProductPairing();
									pair.setId(null);
									pair.setOrgrootid_link(orgrootid_link);
									pair.setPcontractid_link(pcontractid_link);
									pair.setProductpairid_link(product_set_id_link);
									pcontractpairService.save(pair);
								}
							}
							
							long shipmodeid_link = 0;
							colNum = ColumnTempNew.shipmode + 1; 
							String shipmode_name = commonService.getStringValue(row.getCell(ColumnTempNew.shipmode));
							List<ShipMode> shipmode = shipmodeService.getbyname(shipmode_name);
							if (shipmode.size() > 0) {
								shipmodeid_link = shipmode.get(0).getId();
							}
							
							//Lay ngay giao hang lon nhat

							List<Date> list_ngaygiao = new ArrayList<Date>();
							List<Integer> list_soluong = new ArrayList<Integer>();
							
							Date ShipDate = null;
							colNum = 14;
							col_phancach2 = col_phancach1 + 3;
							String s_header_phancach2 = commonService.getStringValue(rowheader.getCell(col_phancach2));
							
							try {
								String s_shipdate = commonService.getStringValue(row.getCell(colNum));
								if(s_shipdate.contains("/")) {
									String[] s_date = s_shipdate.split("/");
									if(Integer.parseInt(s_date[1].toString()) < 13 && Integer.parseInt(s_date[0].toString()) < 32) {
										ShipDate = new SimpleDateFormat("dd/MM/yyyy").parse(s_shipdate);
									}
									else {
										mes_err = "Định dạng ngày không đúng dd/MM/yyyy! ";
									}
									
								}
								else if (s_shipdate != ""){
									if(DateUtil.isCellDateFormatted(row.getCell(colNum))) {
										ShipDate = row.getCell(colNum).getDateCellValue();
									}
								}
								
							}
							catch (Exception e) {
								if(DateUtil.isCellDateFormatted(row.getCell(colNum))) {
									ShipDate = row.getCell(colNum).getDateCellValue();
								}
							}
							
							if(ShipDate != null) {
								list_ngaygiao.add(ShipDate);
							}
							
							colNum = col_phancach1 + 3;
							String s_po_quantity = commonService.getStringValue(row.getCell(col_phancach1 + 2));
							s_po_quantity = s_po_quantity.replace(",", "");
							Float f_po_quantity = s_po_quantity.equals("") ? 0 : Float.parseFloat(s_po_quantity);
							int po_quantity = f_po_quantity.intValue();
							if(po_quantity>0)
								list_soluong.add(po_quantity);
							
							while (!s_header_phancach2.equals("xxx")) {
								col_phancach2 += 2;
								s_header_phancach2 = commonService.getStringValue(rowheader.getCell(col_phancach2));
								
								Date shipdate2 = null;
								try {
									String s_shipdate2 = commonService.getStringValue(row.getCell(colNum));
									if(s_shipdate2.contains("/")) {
										String[] s_date = s_shipdate2.split("/");
										if(Integer.parseInt(s_date[1].toString()) < 13 && Integer.parseInt(s_date[0].toString()) < 32) {
											shipdate2 = new SimpleDateFormat("dd/MM/yyyy").parse(s_shipdate2);
										}
										else {
											mes_err = "Định dạng ngày không đúng dd/MM/yyyy! ";
										}
										
									}
									else if (s_shipdate2 != "") {
										if(DateUtil.isCellDateFormatted(row.getCell(colNum))) {
											shipdate2 = row.getCell(colNum).getDateCellValue();
										}
									}
									
								}
								catch (Exception e) {
									if(DateUtil.isCellDateFormatted(row.getCell(colNum))) {
										shipdate2 = row.getCell(colNum).getDateCellValue();
									}
								}
								
								if(shipdate2 != null) {
									list_ngaygiao.add(shipdate2);
									//Lay so luong
									String s_sub_quantity = commonService.getStringValue(row.getCell(colNum+1));
									s_sub_quantity = s_sub_quantity.replace(",", "");
									Float f_sub_quantity = s_sub_quantity.equals("") ? 0 : Float.parseFloat(s_sub_quantity);
									po_quantity += f_sub_quantity.intValue();
									
									list_soluong.add(f_sub_quantity.intValue() * amount);
									
									if(ShipDate ==null)
										ShipDate = shipdate2;
									else if(ShipDate.before(shipdate2)) {
										ShipDate = shipdate2;
									}
								}

								colNum +=2;
							}
							
							
							//Kiem tra chao gia da ton tai hay chua
							long pcontractpo_id_link = 0;
							PContract_PO po_tong = new PContract_PO();
							long po_productid_link = product_set_id_link > 0 ? product_set_id_link : productid_link;
							
							colNum = ColumnTempNew.fob + 1;
							String s_price_fob = commonService.getStringValue(row.getCell(ColumnTempNew.fob));
							s_price_fob = s_price_fob.replace(",", "");
							float price_fob = s_price_fob.equals("") ? 0 : Float.parseFloat(s_price_fob);
							
//							colNum = ColumnTempNew.amount_style + 1;
//							String s_amount_style = commonService.getStringValue(row.getCell(ColumnTempNew.amount_style));
//							s_amount_style = s_amount_style.replace(",", "");
//							Float amount_style = s_amount_style == "" ? 0 : Float.parseFloat(s_amount_style);
													
							colNum = ColumnTempNew.vendor_target + 1;
							String s_vendor_target = commonService.getStringValue(row.getCell(ColumnTempNew.vendor_target));
							s_vendor_target = s_vendor_target.replace(",", "");
							float vendor_target = s_vendor_target == "" ? 0 : Float.parseFloat(s_vendor_target);
							
							colNum = ColumnTempNew.ns_target + 1;
							String s_ns_target = commonService.getStringValue(row.getCell(ColumnTempNew.ns_target));
							s_ns_target = s_ns_target.replace(",", "");
							Float ns_target = s_ns_target == "" ? 0 : Float.parseFloat(s_ns_target);
							
							colNum = ColumnTempNew.org + 1;
							String s_org_code = commonService.getStringValue(row.getCell(ColumnTempNew.org));
							s_org_code = s_org_code.replace(",", "");
							Long orgid_link = null;
							List<Org> list_org = orgService.getbycode(s_org_code, orgrootid_link);
							if(list_org.size() > 0) {
								orgid_link = list_org.get(0).getId();
							}
							
							int productiondays_ns = ns_target == 0 ? 0 : po_quantity/(ns_target.intValue()); 
							
							colNum = ColumnTemplate.matdate + 1;
							Date matdate = null;
							
							try {
								String s_matdate = commonService.getStringValue(row.getCell(ColumnTempNew.matdate));
								if(s_matdate.contains("/")) {
									String[] s_date = s_matdate.split("/");
									if(Integer.parseInt(s_date[1].toString()) < 13 && Integer.parseInt(s_date[0].toString()) < 32) {
										matdate = new SimpleDateFormat("dd/MM/yyyy").parse(s_matdate);
									}
									else {
										mes_err = "Định dạng ngày không đúng dd/MM/yyyy! ";
									}
									
								}
								else {
									if(DateUtil.isCellDateFormatted(row.getCell(ColumnTempNew.matdate))) {
										matdate = row.getCell(ColumnTempNew.matdate).getDateCellValue();
									}
								}
								
							}
							catch (Exception e) {
								if(DateUtil.isCellDateFormatted(row.getCell(ColumnTempNew.matdate))) {
									matdate = row.getCell(ColumnTempNew.matdate).getDateCellValue();
								}
							}
							Date production_date = null;
							int production_day = 0;
							float plan_linerequired = 0;
							if(matdate != null) {
								production_date = Common.Date_Add(matdate, 7);
								production_day = commonService.getDuration(production_date, ShipDate, orgrootid_link);
								plan_linerequired = (float)productiondays_ns/(float)production_day;
								if(plan_linerequired < 1) plan_linerequired = 1;
								
								DecimalFormat df = new DecimalFormat("#.##"); 
								String formatted = df.format(plan_linerequired);
								plan_linerequired = Float.parseFloat(formatted);
							}
							
							colNum = ColumnTempNew.PO + 1;
							String PO_No = commonService.getStringValue(row.getCell(ColumnTempNew.PO));
							if (PO_No == "" || PO_No.equals("0")) {
								PO_No = "TBD";
							}
							
							colNum = ColumnTempNew.status + 1;
							String s_status = commonService.getStringValue(row.getCell(ColumnTempNew.status));
							Integer status = POStatus.PO_STATUS_UNCONFIRM;
							if(!s_status.equals("")) {
								status = POStatus.PO_STATUS_CONFIRMED;
							}
							
							//Neu khong cot ngay giao hang nao co gia tri thi ko xu ly nua chuyen den dong tiep theo
							if(ShipDate !=null) {
								List<PContract_PO> listpo = new ArrayList<PContract_PO>();
								if(product_set_id_link == 0) {
									listpo = pcontract_POService.check_exist_po(ShipDate,
											po_productid_link, shipmodeid_link, pcontractid_link, PO_No);
								}
								else {
									listpo = pcontract_POService.check_exist_po(ShipDate,
											po_productid_link, shipmodeid_link, pcontractid_link, "");
								}
								if(listpo.size() == 0) {
									po_tong.setId(null);
									po_tong.setCurrencyid_link((long) 1);
									po_tong.setDatecreated(current_time);
									po_tong.setIsauto_calculate(true);
									po_tong.setOrgrootid_link(orgrootid_link);
									po_tong.setPcontractid_link(pcontractid_link);
									po_tong.setProductid_link(po_productid_link);
									po_tong.setUsercreatedid_link(user.getId());
									po_tong.setDate_importdata(current_time);
									po_tong.setPo_typeid_link(POType.PO_CMP);
									po_tong.setSewtarget_percent((float)20);
									po_tong.setParentpoid_link(null);
									
									if(status == POStatus.PO_STATUS_CONFIRMED) {
										po_tong.setOrgmerchandiseid_link(orgid_link);
									}

								}
								else {
									po_tong = listpo.get(0);
								}

								po_tong.setProductiondays_ns(productiondays_ns);
								po_tong.setShipmodeid_link(shipmodeid_link);
								po_tong.setShipdate(ShipDate);
								po_tong.setMatdate(matdate);
								po_tong.setProductiondate(production_date);
								po_tong.setProductiondays(production_day);
								po_tong.setStatus(status);
								po_tong.setPo_buyer(PO_No);
								po_tong.setPo_vendor(PO_No);
								po_tong.setPo_quantity(po_quantity);
								po_tong.setIs_tbd(PO_No == "TBD" ? true : false);
								po_tong.setCode(PO_No);
								po_tong.setProductid_link(po_productid_link);
								
								po_tong = pcontract_POService.save(po_tong);
								pcontractpo_id_link = po_tong.getId();
								
								//Kiem tra ns cua san pham 
								List<PContract_PO_Productivity> list_productivity = productivityService.getbypo_and_product(pcontractpo_id_link, productid_link);
								PContract_PO_Productivity po_productivity = new PContract_PO_Productivity();
								if(list_productivity.size() == 0) {
									po_productivity.setId(null);
									po_productivity.setOrgrootid_link(orgrootid_link);
									po_productivity.setPcontract_poid_link(pcontractpo_id_link);
									po_productivity.setProductid_link(productid_link);							
								}
								else {
									po_productivity = list_productivity.get(0);
								}
								
								po_productivity.setAmount(po_quantity*amount);
								po_productivity.setPlan_linerequired(plan_linerequired);
								po_productivity.setPlan_productivity(ns_target.intValue());
								po_productivity.setProductiondays_ns(productiondays_ns);
								productivityService.save(po_productivity);

								//Xoa cac dai co di roi insert lai vao san pham con hoac san pham don
								List<PContract_Price> list_price = priceService.getPrice_by_product(pcontractpo_id_link, productid_link);
								for(PContract_Price price : list_price) {
									List<PContract_Price_D> list_price_d = pricedetailService.getPrice_D_ByPContractPrice(price.getId());
									for(PContract_Price_D price_d : list_price_d) {
										pricedetailService.delete(price_d);
									}
									priceService.delete(price);
								}
								
								//Sinh cac dai co vao trong san pham con 
								
								//Sinh cac dai co khac all
								float total_price_amount = 0;
								float total_amount = 0;
								float total_price = 0;
								int count = 0;
								
								for(int i= col_phancach2+1; i<=col_phancach2+6;i++) {
									colNum = i+1;
									Row row2 = sheet.getRow(1);
									String sizesetname = commonService.getStringValue(row2.getCell(i));
									String s_price_sizeset = commonService.getStringValue(row.getCell(i));
									s_price_sizeset = s_price_sizeset.replace(",", "");
									Float price_sizeset = s_price_sizeset.equals("") ? 0 : Float.parseFloat(s_price_sizeset);
									
									String s_amount_sizeset = commonService.getStringValue(row.getCell(i+7));
									s_amount_sizeset = s_amount_sizeset.replace(",", "");
									Float amount_sizeset = s_amount_sizeset.equals("") ? 0 : Float.parseFloat(s_amount_sizeset);
									
									if (price_sizeset > 0 || amount_sizeset > 0) {
										count++;
										total_price_amount += price_sizeset * amount_sizeset;
										total_amount += amount_sizeset;
										total_price += price_sizeset;
										
										Long sizesetid_link = sizesetService.getbyname(sizesetname);
										
										PContract_Price price = new PContract_Price();
										price.setId(null);
										price.setIs_fix(true);
										price.setOrgrootid_link(orgrootid_link);
										price.setPcontract_poid_link(pcontractpo_id_link);
										price.setPcontractid_link(pcontractid_link);
										price.setProductid_link(productid_link);
										price.setSizesetid_link(sizesetid_link == null ? 0 : sizesetid_link);
										price.setDate_importdata(current_time);
										price.setPrice_cmp(price_sizeset);
										price.setTotalprice(price_sizeset);
										price.setQuantity(amount_sizeset.intValue());
										price = priceService.save(price);
										
										//Them detail
										PContract_Price_D price_sizeset_d = new PContract_Price_D();
										price_sizeset_d.setOrgrootid_link(orgrootid_link);
										price_sizeset_d.setFobpriceid_link((long)1);
										price_sizeset_d.setPrice(price_sizeset);
										price_sizeset_d.setIsfob(false);
										price_sizeset_d.setId(null);
										price_sizeset_d.setSizesetid_link(sizesetid_link == null ? 0 : sizesetid_link);
										price_sizeset_d.setPcontract_poid_link(pcontractpo_id_link);
										price_sizeset_d.setPcontractid_link(pcontractid_link);
										price_sizeset_d.setPcontractpriceid_link(price.getId());
										price_sizeset_d.setProductid_link(productid_link);
										pricedetailService.save(price_sizeset_d);
										
									}
								}
								float price_cmp = 0;
								
								if(total_amount == 0) {
									price_cmp = count == 0 ? 0 : total_price /count;
								}
								else {
									price_cmp = total_price_amount/ total_amount;
								}

								DecimalFormat df = new DecimalFormat("#.###"); 
								String formatted = df.format(price_cmp);
								price_cmp = Float.parseFloat(formatted);
								
								//Sinh dai co all vao san pham con 
								PContract_Price price_all = new PContract_Price();
								price_all.setId(null);
								price_all.setIs_fix(true);
								price_all.setOrgrootid_link(orgrootid_link);
								price_all.setPcontract_poid_link(pcontractpo_id_link);
								price_all.setPcontractid_link(pcontractid_link);
								price_all.setProductid_link(productid_link);
								price_all.setSizesetid_link((long)1);
								price_all.setDate_importdata(current_time);
								price_all.setPrice_cmp(price_cmp);
								price_all.setTotalprice(price_cmp + price_fob);
								price_all.setPrice_fob(price_fob);
								price_all.setPrice_vendortarget(vendor_target);
								price_all.setQuantity(po_quantity);
								price_all = priceService.save(price_all);
								
								//Them detail
								PContract_Price_D price_detail_all = new PContract_Price_D();
								price_detail_all.setOrgrootid_link(orgrootid_link);
								price_detail_all.setFobpriceid_link((long)1);
								price_detail_all.setPrice(price_cmp);
								price_detail_all.setIsfob(false);
								price_detail_all.setId(null);
								price_detail_all.setSizesetid_link((long)1);
								price_detail_all.setPcontract_poid_link(pcontractpo_id_link);
								price_detail_all.setPcontractid_link(pcontractid_link);
								price_detail_all.setPcontractpriceid_link(price_all.getId());
								price_detail_all.setProductid_link(productid_link);
								pricedetailService.save(price_detail_all);
								
								//kiem tra xem co phai san pham bo khong thi cap nhat cac dai co vao trong sản phẩm bộ
								if(product_set_id_link > 0) {
									//Kiem tra ns cua san pham bo
									List<PContract_PO_Productivity> list_productivity_set = productivityService.getbypo_and_product(pcontractpo_id_link, product_set_id_link);
									PContract_PO_Productivity po_productivity_set = new PContract_PO_Productivity();
									if(list_productivity_set.size() == 0) {
										po_productivity_set.setId(null);
										po_productivity_set.setOrgrootid_link(orgrootid_link);
										po_productivity_set.setPcontract_poid_link(pcontractpo_id_link);
										po_productivity_set.setProductid_link(product_set_id_link);							
									}
									else {
										po_productivity_set = list_productivity_set.get(0);
									}

									po_productivity_set.setAmount(po_quantity);
									po_productivity_set.setPlan_linerequired(plan_linerequired);
									po_productivity_set.setPlan_productivity(ns_target.intValue());
									po_productivity_set.setProductiondays_ns(productiondays_ns);
									productivityService.save(po_productivity_set);
									
									//Xoa cac dai co di roi insert lai vao san pham con hoac san pham bo
									List<PContract_Price> list_price_set = priceService.getPrice_by_product(pcontractpo_id_link, product_set_id_link);
									for(PContract_Price price : list_price_set) {
										List<PContract_Price_D> list_price_d = pricedetailService.getPrice_D_ByPContractPrice(price.getId());
										for(PContract_Price_D price_d : list_price_d) {
											pricedetailService.delete(price_d);
										}
										priceService.delete(price);
									}
									
									//them gia va so luong cac dai co khac all vao trong bo
									List<Long> list_sizeset = new ArrayList<Long>();
									list_sizeset.add((long)1);
									for(int i= col_phancach2+1; i<=col_phancach2+6;i++) {
										Row row2 = sheet.getRow(1);
										String sizesetname = commonService.getStringValue(row2.getCell(i));
										Long sizesetid_link = sizesetService.getbyname(sizesetname);
										list_sizeset.add(sizesetid_link);
									}
									
									for(Long sizeid : list_sizeset) {
										//Lay gia cua cac san pham con cua dai co do
										//Lay ds cac san pham con cua san pham bo
										List<ProductPairing> list_pair = productpairService.getproduct_pairing_detail_bycontract(orgrootid_link, pcontractid_link, product_set_id_link);
										Float price_set = (float)0;
										int amount_set = 0;
										for(ProductPairing pair : list_pair) {
											List<PContract_Price> list_price_chil = priceService.getPrice_by_product_and_sizeset(pcontractpo_id_link, pair.getProductid_link(), sizeid);
											if(list_price_chil.size() > 0) {
												amount_set = list_price_chil.get(0).getQuantity();
												price_set += list_price_chil.get(0).getPrice_cmp();
											}
										}
										
										if(amount_set > 0 || price_set>0) {
											//them gia va so luong vao trong san pham bo
											PContract_Price price_sizeset = new PContract_Price();
											price_sizeset.setId(null);
											price_sizeset.setIs_fix(true);
											price_sizeset.setOrgrootid_link(orgrootid_link);
											price_sizeset.setPcontract_poid_link(pcontractpo_id_link);
											price_sizeset.setPcontractid_link(pcontractid_link);
											price_sizeset.setProductid_link(product_set_id_link);
											price_sizeset.setSizesetid_link(sizeid == null ? 0 : sizeid);
											price_sizeset.setDate_importdata(current_time);
											price_sizeset.setPrice_cmp(price_set);
											price_sizeset.setTotalprice(price_set);
											price_sizeset.setQuantity(amount_set);
											price_sizeset = priceService.save(price_sizeset);
											
											//Them detail
											PContract_Price_D price_sizeset_d = new PContract_Price_D();
											price_sizeset_d.setOrgrootid_link(orgrootid_link);
											price_sizeset_d.setFobpriceid_link((long)1);
											price_sizeset_d.setPrice(price_set);
											price_sizeset_d.setIsfob(false);
											price_sizeset_d.setId(null);
											price_sizeset_d.setSizesetid_link(sizeid == null ? 0 : sizeid);
											price_sizeset_d.setPcontract_poid_link(pcontractpo_id_link);
											price_sizeset_d.setPcontractid_link(pcontractid_link);
											price_sizeset_d.setPcontractpriceid_link(price_sizeset.getId());
											price_sizeset_d.setProductid_link(productid_link);
											pricedetailService.save(price_sizeset_d);
										}
									}
								}
								
								//Kiem tra line giao hang va sinh line giao hang
								for(int i=0; i<list_ngaygiao.size(); i++) {
									Date ngaygiao = list_ngaygiao.get(i);
									int soluong = list_soluong.get(i);
									
									List<PContract_PO> list_line_gh = new ArrayList<PContract_PO>();
									list_line_gh = pcontract_POService.check_exist_line(ngaygiao, po_productid_link, pcontractid_link, pcontractpo_id_link);
									PContract_PO po_line = new PContract_PO();
									
									Date production_date_line = null;
									int production_day_line = 0;
									float plan_linerequired_line = 0;
									int productiondays_ns_line = ns_target == 0 ? 0 : soluong/ns_target.intValue(); 
									if(i==0) {
										if(matdate != null) {
											production_date_line = Common.Date_Add(matdate, 7);
											production_day_line = commonService.getDuration(production_date_line, ngaygiao, orgrootid_link);
											plan_linerequired_line = (float)productiondays_ns_line/(float)production_day_line;
											if (plan_linerequired_line < 1) plan_linerequired_line = 1;
											
											DecimalFormat df_line = new DecimalFormat("#.##"); 
											String formatted_line = df_line.format(plan_linerequired_line);
											plan_linerequired_line = Float.parseFloat(formatted_line);
										}
									}
									else {
										production_date_line = commonService.Date_Add_with_holiday(list_ngaygiao.get(i-1), -2, orgrootid_link);
										production_day_line = commonService.getDuration(production_date_line, ngaygiao, orgrootid_link);
										plan_linerequired_line = (float)productiondays_ns_line/(float)production_day_line;
										if (plan_linerequired_line < 1) plan_linerequired_line = 1;
										
										DecimalFormat df_line = new DecimalFormat("#.##"); 
										String formatted_line = df_line.format(plan_linerequired_line);
										plan_linerequired_line = Float.parseFloat(formatted_line);
									}
									
									
									if(list_line_gh.size() == 0) {
										po_line.setId(null);
										po_line.setCurrencyid_link((long) 1);
										po_line.setDatecreated(current_time);
										po_line.setIsauto_calculate(true);
										po_line.setOrgrootid_link(orgrootid_link);
										po_line.setPcontractid_link(pcontractid_link);
										po_line.setProductid_link(po_productid_link);
										po_line.setUsercreatedid_link(user.getId());
										po_line.setDate_importdata(current_time);
										po_line.setPo_typeid_link(POType.PO_LINE_PLAN);
										po_line.setSewtarget_percent((float)20);
										po_line.setParentpoid_link(pcontractpo_id_link);
										
										if(status == POStatus.PO_STATUS_CONFIRMED) {
											po_line.setOrgmerchandiseid_link(orgid_link);
										}

									}
									else {
										po_line = list_line_gh.get(0);
									}

									po_line.setProductiondays_ns(productiondays_ns_line);
									po_line.setShipmodeid_link(shipmodeid_link);
									po_line.setShipdate(ngaygiao);
									po_line.setMatdate(matdate);
									po_line.setProductiondate(production_date_line);
									po_line.setProductiondays(production_day_line);
									po_line.setStatus(po_tong.getStatus());
									po_line.setPo_buyer(PO_No);
									po_line.setPo_vendor(PO_No);
									po_line.setPo_quantity(soluong);
									po_line.setIs_tbd(PO_No == "TBD" ? true : false);
									po_line.setCode(PO_No);
									po_line.setProductid_link(po_productid_link);
									
									po_line = pcontract_POService.save(po_line);
									
									Long pcontract_po_line_id = po_line.getId();
									
									//Kiem tra ns cua san pham trong line
									List<PContract_PO_Productivity> list_productivity_line = productivityService.getbypo_and_product(pcontract_po_line_id, productid_link);
									PContract_PO_Productivity po_productivity_line = new PContract_PO_Productivity();
									if(list_productivity_line.size() == 0) {
										po_productivity_line.setId(null);
										po_productivity_line.setOrgrootid_link(orgrootid_link);
										po_productivity_line.setPcontract_poid_link(pcontract_po_line_id);
										po_productivity_line.setProductid_link(productid_link);							
									}
									else {
										po_productivity_line = list_productivity_line.get(0);
									}
									
									po_productivity_line.setAmount(soluong*amount);
									po_productivity_line.setPlan_linerequired(plan_linerequired_line);
									po_productivity_line.setPlan_productivity(ns_target.intValue());
									po_productivity_line.setProductiondays_ns(productiondays_ns_line);
									productivityService.save(po_productivity_line);
									
									if(product_set_id_link > 0) {
										//Kiem tra ns cua san pham trong line
										List<PContract_PO_Productivity> list_productivity_line_set = productivityService.getbypo_and_product(pcontract_po_line_id, product_set_id_link);
										PContract_PO_Productivity po_productivity_line_set = new PContract_PO_Productivity();
										if(list_productivity_line_set.size() == 0) {
											po_productivity_line_set.setId(null);
											po_productivity_line_set.setOrgrootid_link(orgrootid_link);
											po_productivity_line_set.setPcontract_poid_link(pcontract_po_line_id);
											po_productivity_line_set.setProductid_link(product_set_id_link);							
										}
										else {
											po_productivity_line_set = list_productivity_line_set.get(0);
										}
										
										po_productivity_line_set.setAmount(soluong);
										po_productivity_line_set.setPlan_linerequired(plan_linerequired_line);
										po_productivity_line_set.setPlan_productivity(ns_target.intValue());
										po_productivity_line_set.setProductiondays_ns(productiondays_ns_line);
										productivityService.save(po_productivity_line_set);
									}
									
									//kiem tra porder_req ton tai chua thi them vao
									if(orgid_link != null) {
										List<POrder_Req> list_req = reqService.getByOrg_PO_Product(pcontract_po_line_id, productid_link, orgid_link);
										if(list_req.size() == 0) {
											POrder_Req porder_req = new POrder_Req();
											porder_req.setAmount_inset(amount);
											porder_req.setGranttoorgid_link(orgid_link);
											porder_req.setId(null);
											porder_req.setIs_calculate(true);
											porder_req.setOrgrootid_link(orgrootid_link);
											porder_req.setPcontract_poid_link(pcontract_po_line_id);
											porder_req.setPcontractid_link(pcontractid_link);
											porder_req.setProductid_link(productid_link);
											porder_req.setStatus(status == POStatus.PO_STATUS_CONFIRMED ? POrderReqStatus.STATUS_POCONFFIRMED : POrderReqStatus.STATUS_FREE);
											porder_req.setTotalorder(soluong);
											porder_req = reqService.save(porder_req);
											
											if(po_tong.getStatus() == POStatus.PO_STATUS_CONFIRMED)
												porderService.createPOrder(porder_req, user);
										}
										
									}
								}
							}
							
							//Chuyen sang row tiep theo neu con du lieu thi xu ly tiep khong thi dung lai
							rowNum++;
							row = sheet.getRow(rowNum);
							if(row == null) break;
							
							STT = commonService.getStringValue(row.getCell(ColumnTemplate.STT));
							STT = STT.equals("0") ? "" : STT;
						}
					}
					catch (Exception e) {
						mes_err = "Có lỗi ở dòng " +(rowNum+1)+" và cột "+ colNum +": "+ mes_err; 
					}
					finally {
						workbook.close();
						serverFile.delete();
					}

					if (mes_err == "") {
						response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
						response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
					} else {
						response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
						response.setMessage(mes_err);
					}
				} else {
					response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
					response.setMessage("Có lỗi trong quá trình upload! Bạn hãy thử lại");
				}
			}
			else {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage(mes_err);
			}
			
			
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
	
	private String genCodeSKU(Product product) {
		List<SKU> lstSKU = skuService.getlist_byProduct(product.getId());
		if (lstSKU.size() == 0) {
			return product.getBuyercode().trim() + "_" + "1";
		}
		String old_code = lstSKU.get(0).getCode().trim();
		String[] obj = old_code.split("_");
		int a = Integer.parseInt(obj[obj.length-1]);
		String code = product.getBuyercode() + "_" + (a + 1);
		return code;
	}
}
