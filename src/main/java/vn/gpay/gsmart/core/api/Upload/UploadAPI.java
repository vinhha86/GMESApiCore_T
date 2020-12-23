package vn.gpay.gsmart.core.api.Upload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
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
import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.pcontractproduct.IPContractProductService;
import vn.gpay.gsmart.core.pcontractproduct.PContractProduct;
import vn.gpay.gsmart.core.pcontractproductpairing.IPContractProductPairingService;
import vn.gpay.gsmart.core.pcontractproductpairing.PContractProductPairing;
import vn.gpay.gsmart.core.product.IProductService;
import vn.gpay.gsmart.core.product.Product;
import vn.gpay.gsmart.core.productattributevalue.IProductAttributeService;
import vn.gpay.gsmart.core.productattributevalue.ProductAttributeValue;
import vn.gpay.gsmart.core.productpairing.IProductPairingService;
import vn.gpay.gsmart.core.productpairing.ProductPairing;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sku.ISKU_AttributeValue_Service;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.sku.SKU;
import vn.gpay.gsmart.core.sku.SKU_Attribute_Value;
import vn.gpay.gsmart.core.utils.AtributeFixValues;
import vn.gpay.gsmart.core.utils.ColumnTempNew;
import vn.gpay.gsmart.core.utils.ColumnTemplate;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.POStatus;
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
	@Autowired
	IPContractProductService pcontractproductService;
	@Autowired
	IPContractProductPairingService pcontractpairService;
	@Autowired
	IShipModeService shipmodeService;
	@Autowired IOrgService orgService;
	@Autowired
	IPContract_POService pcontract_POService;
	
	@RequestMapping(value = "/offers", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> UploadTemplate(HttpServletRequest request,
			@RequestParam("file") MultipartFile file, @RequestParam("pcontractid_link") long pcontractid_link) {
		ResponseBase response = new ResponseBase();

		Date current_time = new Date();
		String name = "";
		try {
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
				int colNum = 1, col_phancach1 = 12, col_phancach2 = 0, col_phancach3 = 0, col_phancach4 =0, col_phancach5 = 0;
				
				String mes_err = "";
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
						String shipmode_name = row.getCell(ColumnTempNew.shipmode).getStringCellValue();
						List<ShipMode> shipmode = shipmodeService.getbyname(shipmode_name);
						if (shipmode.size() > 0) {
							shipmodeid_link = shipmode.get(0).getId();
						}
						
						//Lay ngay giao hang lon nhat
						Date ShipDate = null;
						colNum = 13;
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
							else {
								if(HSSFDateUtil.isCellDateFormatted(row.getCell(colNum))) {
									ShipDate = row.getCell(colNum).getDateCellValue();
								}
							}
							
						}
						catch (Exception e) {
							if(HSSFDateUtil.isCellDateFormatted(row.getCell(colNum))) {
								ShipDate = row.getCell(colNum).getDateCellValue();
							}
						}
						
						if(ShipDate == null) {
							mes_err = "Định dạng ngày không đúng dd/MM/yyyy! ";
							throw new Exception();
						}
						
						while (!s_header_phancach2.equals("xxx")) {
							col_phancach2 += 2;
							colNum +=2;
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
								else {
									if(HSSFDateUtil.isCellDateFormatted(row.getCell(colNum))) {
										shipdate2 = row.getCell(colNum).getDateCellValue();
									}
								}
								
							}
							catch (Exception e) {
								if(HSSFDateUtil.isCellDateFormatted(row.getCell(colNum))) {
									shipdate2 = row.getCell(colNum).getDateCellValue();
								}
							}
							
							if(shipdate2 == null) {
								mes_err = "Định dạng ngày không đúng dd/MM/yyyy! ";
								throw new Exception();
							}
							else {
								if(ShipDate.before(shipdate2)) {
									ShipDate = shipdate2;
								}
							}
						}
						
						col_phancach5 = col_phancach2 + 17;
						
						//Kiem tra chao gia da ton tai hay chua
						long pcontractpo_id_link = 0;
						PContract_PO po_tong = new PContract_PO();
						long po_productid_link = product_set_id_link > 0 ? product_set_id_link : productid_link;
						
						colNum = ColumnTempNew.fob + 1;
						String s_price_fob = commonService.getStringValue(row.getCell(ColumnTempNew.fob));
						s_price_fob = s_price_fob.replace(",", "");
						float price_fob = s_price_fob.equals("") ? 0 : Float.parseFloat(s_price_fob);
						
						colNum = ColumnTempNew.vendor_target + 1;
						String s_vendor_target = commonService.getStringValue(row.getCell(ColumnTempNew.vendor_target));
						s_vendor_target = s_vendor_target.replace(",", "");
						float vendor_target = s_vendor_target == "" ? 0 : Float.parseFloat(s_vendor_target);
						
						colNum = col_phancach5 + 1;
						String s_po_quantity = commonService.getStringValue(row.getCell(col_phancach5 - 1));
						s_po_quantity = s_po_quantity.replace(",", "");
						int po_quantity = s_po_quantity == "" ? 0 : Integer.parseInt(s_po_quantity); 
						
						colNum = ColumnTempNew.org + 1;
						String s_org_code = commonService.getStringValue(row.getCell(ColumnTempNew.org));
						s_org_code = s_org_code.replace(",", "");
						Long orgid_link = null;
						List<Org> list_org = orgService.getbycode(s_org_code, orgrootid_link);
						if(list_org.size() > 0) {
							orgid_link = list_org.get(0).getId();
						}
						
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
								if(HSSFDateUtil.isCellDateFormatted(row.getCell(ColumnTempNew.matdate))) {
									matdate = row.getCell(ColumnTempNew.matdate).getDateCellValue();
								}
							}
							
						}
						catch (Exception e) {
							if(HSSFDateUtil.isCellDateFormatted(row.getCell(ColumnTempNew.matdate))) {
								matdate = row.getCell(ColumnTempNew.matdate).getDateCellValue();
							}
						}
						
						if(matdate == null) {
							mes_err = "Định dạng ngày không đúng dd/MM/yyyy! ";
							throw new Exception();
						}
						
						Date production_date = Common.Date_Add(matdate, 7);
						int production_day = commonService.getDuration(production_date, ShipDate, orgrootid_link);
						
						colNum = ColumnTempNew.PO + 1;
						String PO_No = commonService.getStringValue(row.getCell(ColumnTempNew.PO));
						if (PO_No == "" || PO_No.equals("0")) {
							PO_No = "TBD";
						}
						
						List<PContract_PO> listpo = new ArrayList<PContract_PO>();
						float target = product_set_id_link > 0 ? 0 : vendor_target;
						listpo = pcontract_POService.check_exist_po(ShipDate,
								po_productid_link, shipmodeid_link, pcontractid_link, target);
						if(list_org.size() == 0) {
							po_tong.setId(null);
							po_tong.setCode(PO_No);
							po_tong.setCurrencyid_link((long) 1);
							po_tong.setDatecreated(current_time);
							po_tong.setIs_tbd(PO_No == "TBD" ? true : false);
							po_tong.setIsauto_calculate(true);
							po_tong.setOrgrootid_link(orgrootid_link);
							po_tong.setPcontractid_link(pcontractid_link);
							po_tong.setPo_buyer(PO_No);
							po_tong.setPo_vendor(PO_No);
							po_tong.setPo_quantity(po_quantity);
							po_tong.setProductid_link(po_productid_link);
							po_tong.setShipdate(ShipDate);
							po_tong.setShipmodeid_link(shipmodeid_link);
							po_tong.setStatus(POStatus.PO_STATUS_PROBLEM);
							po_tong.setUsercreatedid_link(user.getId());
							po_tong.setDate_importdata(current_time);
							po_tong.setProductiondate(production_date);
							po_tong.setProductiondays(production_day);
							po_tong.setMatdate(matdate);

							po_tong = pcontract_POService.save(po_tong);
							pcontractpo_id_link = po_tong.getId();
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
