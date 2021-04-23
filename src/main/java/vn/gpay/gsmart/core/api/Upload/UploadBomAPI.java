package vn.gpay.gsmart.core.api.Upload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import vn.gpay.gsmart.core.attributevalue.Attributevalue;
import vn.gpay.gsmart.core.attributevalue.IAttributeValueService;
import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.pcontractbomsku.IPContractBOM2SKUService;
import vn.gpay.gsmart.core.pcontractbomsku.PContractBOM2SKU;
import vn.gpay.gsmart.core.pcontractproductbom.IPContractProductBom2Service;
import vn.gpay.gsmart.core.pcontractproductbom.PContractProductBom2;
import vn.gpay.gsmart.core.pcontractproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.product.IProductService;
import vn.gpay.gsmart.core.product.Product;
import vn.gpay.gsmart.core.productattributevalue.IProductAttributeService;
import vn.gpay.gsmart.core.productattributevalue.ProductAttributeValue;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sku.ISKU_AttributeValue_Service;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.sku.SKU;
import vn.gpay.gsmart.core.sku.SKU_Attribute_Value;
import vn.gpay.gsmart.core.utils.AtributeFixValues;
import vn.gpay.gsmart.core.utils.ColumnTempBom;
import vn.gpay.gsmart.core.utils.ColumnTemplate;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/uploadbom")
public class UploadBomAPI {
	@Autowired Common commonService;
	@Autowired
	IAttributeService attrService;
	@Autowired IProductService productService;
	@Autowired IProductAttributeService pavService;
	@Autowired ISKU_Service skuService;
	@Autowired ISKU_AttributeValue_Service skuattService;
	@Autowired IPContractProductBom2Service bomproductService;
	@Autowired IPContractBOM2SKUService bomskuService;
	@Autowired IAttributeValueService attributevalueService;
	@Autowired IPContractProductSKUService pcontractskuService;
	
	@RequestMapping(value = "/bom_candoi", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> BomCanDoi(HttpServletRequest request,
			@RequestParam("file") MultipartFile file, @RequestParam("pcontractid_link") long pcontractid_link, @RequestParam("productid_link") long productid_link) {
		ResponseBase response = new ResponseBase();

		Date current_time = new Date();
		String name = "";
		String mes_err = "";
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			String FolderPath = "upload/bom";

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
				int colNum = 1;
				
				Row row = sheet.getRow(rowNum);
				Row rowheader = sheet.getRow(1);
				
				try {
					String STT = "";
					STT = commonService.getStringValue(row.getCell(ColumnTempBom.STT));
					STT = STT.equals("0") ? "" : STT;
					//kiem tra ten mau xem co ton tai trong chi tiet po hay chua roi moi xu ly
					while (!STT.equals("")) {
						colNum = ColumnTempBom.TenMauSP;
						String ColorName = commonService.getStringValue(row.getCell(ColumnTempBom.TenMauSP));
						
						colNum = ColumnTempBom.MaMauSP;
						String ColorCode = commonService.getStringValue(row.getCell(ColumnTempBom.MaMauSP));
						
						if(!ColorName.toLowerCase().equals("All".toLowerCase())) {
							Long colorid_link = null;
							List<Attributevalue> listAttributevalue = attributevalueService.getByValue(ColorName+"("+ColorCode+")", AtributeFixValues.ATTR_COLOR);
							if(listAttributevalue.size() == 0) {
								mes_err = "Màu sản phẩm "+ ColorName + " chưa có trong chi tiết PO! Bạn hãy thêm vào chi tiết PO trước khi upload định mức";
								break;
							}
							
							colorid_link = listAttributevalue.get(0).getId();
							
							List<Long> list_sku = pcontractskuService.getsku_bycolor(pcontractid_link, productid_link, colorid_link);
							if(list_sku.size() == 0) {
								mes_err = "Màu sản phẩm '"+ ColorName + "' chưa có trong chi tiết PO! Bạn hãy thêm vào chi tiết PO trước khi upload định mức";
								break;
							}
							
						}
						
						//Chuyen sang row tiep theo neu con du lieu thi xu ly tiep khong thi dung lai
						rowNum++;
						row = sheet.getRow(rowNum);
						if(row == null) break;
						
						STT = commonService.getStringValue(row.getCell(ColumnTemplate.STT));
						STT = STT.equals("0") ? "" : STT;
					}
					
					//Nếu không có lỗi thì mới xử lý vào trong DB
					if(mes_err == "") {
						rowNum = 2;
						colNum = 1;
						row = sheet.getRow(rowNum);
						
						STT = commonService.getStringValue(row.getCell(ColumnTempBom.STT));
						STT = STT.equals("0") ? "" : STT;
						
						
						while (!STT.equals("")) {
							colNum = ColumnTempBom.Type;
							String type_npl_name = commonService.getStringValue(row.getCell(ColumnTempBom.Type));
							int type_npl = commonService.gettype_npl_byname(type_npl_name);
							
							colNum = ColumnTempBom.TenNPL;
							String name_npl = commonService.getStringValue(row.getCell(ColumnTempBom.TenNPL));
							
							colNum = ColumnTempBom.MaNPL;
							String ma_npl = commonService.getStringValue(row.getCell(ColumnTempBom.MaNPL));
							
							colNum = ColumnTempBom.Description;
							String description = commonService.getStringValue(row.getCell(ColumnTempBom.Description));
							
							colNum = ColumnTempBom.HaoHut;
							double lost_ratio = row.getCell(ColumnTempBom.HaoHut).getNumericCellValue();
							
							//kiem tra npl co chua thi sinh moi va them vao san pham
							Long npl_id = null;

							long material_skuid_link = 0;
							
							List<Product> list_npl = productService.getby_code_type_description(orgrootid_link, ma_npl, type_npl, description);
							if(list_npl.size() == 0) {
								Product new_npl = new Product();
								new_npl.setBuyercode(ma_npl);
								new_npl.setBuyername(name_npl);
								new_npl.setCode(ma_npl);
								new_npl.setDescription(description);
								new_npl.setId(null);
								new_npl.setName(name_npl);
								new_npl.setOrgrootid_link(orgrootid_link);
								new_npl.setProducttypeid_link(type_npl);
								new_npl.setStatus(1);
								new_npl.setTimecreate(current_time);
								new_npl.setUsercreateid_link(user.getId());
								
								new_npl = productService.save(new_npl);
								npl_id = new_npl.getId();
								//sinh sku all cho npl
								//Sinh thuoc tinh mac dinh cho san pham
								List<Attribute> lstAttr = attrService.getList_attribute_forproduct(type_npl,orgrootid_link);
								for (Attribute attribute : lstAttr) {
									ProductAttributeValue pav = new ProductAttributeValue();
									long value = 0;
									
									if(attribute.getId() == AtributeFixValues.ATTR_COLOR) {
										value = AtributeFixValues.value_color_all;
									}
									else if(attribute.getId() == AtributeFixValues.ATTR_SIZEWIDTH) {
										value = AtributeFixValues.value_sizewidth_all;
									}
									
									pav.setId((long) 0);
									pav.setProductid_link(npl_id);
									pav.setAttributeid_link(attribute.getId());
									pav.setAttributevalueid_link(value);
									pav.setOrgrootid_link(user.getRootorgid_link());
									pavService.save(pav);
								}
								
								//Sinh SKU cho mau all va co all
								
								SKU sku = new SKU();
								sku.setId(null);
								sku.setCode(genCodeSKU(new_npl));
								sku.setName(new_npl.getBuyername());
								sku.setProductid_link(npl_id);
								sku.setOrgrootid_link(user.getRootorgid_link());
								sku.setSkutypeid_link(type_npl);

								sku = skuService.save(sku);
								material_skuid_link = sku.getId();
								
								// Them vao bang sku_attribute_value
								SKU_Attribute_Value savMau = new SKU_Attribute_Value();
								savMau.setId((long) 0);
								savMau.setAttributevalueid_link(AtributeFixValues.value_color_all);
								savMau.setAttributeid_link(AtributeFixValues.ATTR_COLOR);
								savMau.setOrgrootid_link(orgrootid_link);
								savMau.setSkuid_link(material_skuid_link);
								savMau.setUsercreateid_link(user.getId());
								savMau.setTimecreate(new Date());

								skuattService.save(savMau);

								SKU_Attribute_Value savCo = new SKU_Attribute_Value();
								savCo.setId((long) 0);
								savCo.setAttributevalueid_link(AtributeFixValues.value_size_all);
								savCo.setAttributeid_link(AtributeFixValues.ATTR_SIZE);
								savCo.setOrgrootid_link(orgrootid_link);
								savCo.setSkuid_link(material_skuid_link);
								savCo.setUsercreateid_link(user.getId());
								savCo.setTimecreate(new Date());

								skuattService.save(savCo);
							}
							else {
								npl_id = list_npl.get(0).getId();
								List<SKU> list_sku_npl = skuService.getlist_byProduct(npl_id);
								material_skuid_link = list_sku_npl.get(0).getId();
							}
							
							//them npl vao trong bang pcontract_product_bom2
							List<PContractProductBom2> list_bom = bomproductService.getby_pcontract_product_material(productid_link, pcontractid_link, material_skuid_link);
							if(list_bom.size() == 0) {
								PContractProductBom2 bom_new  = new PContractProductBom2();
								bom_new.setCreateddate(current_time);
								bom_new.setCreateduserid_link(user.getId());
								bom_new.setDescription("");
								bom_new.setId(null);
								bom_new.setLost_ratio((float)lost_ratio);
								bom_new.setMaterialid_link(material_skuid_link);
								bom_new.setOrgrootid_link(orgrootid_link);
								bom_new.setPcontractid_link(pcontractid_link);
								bom_new.setProductid_link(productid_link);
								
								bomproductService.save(bom_new);
							}
							
							//them vao trong bang pcontract-bom2_sku
							int columnsize = ColumnTempBom.HaoHut + 1;
							String s_sizename = commonService.getStringValue(rowheader.getCell(columnsize));
							s_sizename = s_sizename.equals("0") ? "" : s_sizename;
							while (!s_sizename.equals("")) {
								colNum = columnsize;
								//lay gia tri dinh muc
								Double amount = row.getCell(columnsize).getNumericCellValue();
								if(amount != null && amount != 0) {
									//kiem tra co co trong db chua chua co thi sinh moi
									Long sizeid_link = null;
									String sizename = commonService.getStringValue(rowheader.getCell(columnsize));
									List<Attributevalue> list_size = attributevalueService.getByValue(sizename, AtributeFixValues.ATTR_SIZE);
									
									if(list_size.size() == 0) {
										Attributevalue av = new Attributevalue();
										av.setAttributeid_link(AtributeFixValues.ATTR_SIZE);
										av.setId(null);
										av.setIsdefault(false);
										av.setOrgrootid_link(orgrootid_link);
										av.setSortvalue(attributevalueService.getMaxSortValue(AtributeFixValues.ATTR_SIZE));
										av.setTimecreate(new Date());
										av.setUsercreateid_link(user.getId());
										av.setValue(sizename);
										
										av = attributevalueService.save(av);
										sizeid_link = av.getId();
									}
									else {
										sizeid_link = list_size.get(0).getId();
									}
									
									//kiem tra mau co trong db chua
									colNum = ColumnTempBom.TenMauSP;
									String ColorName = commonService.getStringValue(row.getCell(ColumnTempBom.TenMauSP));
									
									colNum = ColumnTempBom.MaMauSP;
									String ColorCode = commonService.getStringValue(row.getCell(ColumnTempBom.MaMauSP));
									
									Long colorid_link = null;
									List<Attributevalue> listAttributevalue = attributevalueService.getByValue(ColorName+"("+ColorCode+")", AtributeFixValues.ATTR_COLOR);
									if(listAttributevalue.size() == 0) {
										Attributevalue av = new Attributevalue();
										av.setAttributeid_link(AtributeFixValues.ATTR_COLOR);
										av.setId(null);
										av.setIsdefault(false);
										av.setOrgrootid_link(orgrootid_link);
										av.setSortvalue(attributevalueService.getMaxSortValue(AtributeFixValues.ATTR_COLOR));
										av.setTimecreate(new Date());
										av.setUsercreateid_link(user.getId());
										av.setValue(ColorName+"("+ColorCode+")");
										
										av = attributevalueService.save(av);
										colorid_link = av.getId();
									}
									else {
										colorid_link = listAttributevalue.get(0).getId();
									}
									
									
									//get sku cua san pham theo mau va co
									long product_skuid_link = skuattService.getsku_byproduct_and_valuemau_valueco(productid_link, colorid_link, sizeid_link);
									if(product_skuid_link == 0) {
										Product product = productService.findOne(productid_link);
										
										SKU sku = new SKU();
										sku.setId(null);
										sku.setCode(genCodeSKU(product));
										sku.setName(product.getBuyername());
										sku.setProductid_link(productid_link);
										sku.setOrgrootid_link(user.getRootorgid_link());
										sku.setSkutypeid_link(vn.gpay.gsmart.core.utils.ProductType.SKU_TYPE_COMPLETEPRODUCT);

										sku = skuService.save(sku);
										product_skuid_link = sku.getId();
										
										// Them vao bang sku_attribute_value
										SKU_Attribute_Value savMau = new SKU_Attribute_Value();
										savMau.setId(null);
										savMau.setAttributevalueid_link(colorid_link);
										savMau.setAttributeid_link(AtributeFixValues.ATTR_COLOR);
										savMau.setOrgrootid_link(orgrootid_link);
										savMau.setSkuid_link(product_skuid_link);
										savMau.setUsercreateid_link(user.getId());
										savMau.setTimecreate(new Date());

										skuattService.save(savMau);

										SKU_Attribute_Value savCo = new SKU_Attribute_Value();
										savCo.setId(null);
										savCo.setAttributevalueid_link(sizeid_link);
										savCo.setAttributeid_link(AtributeFixValues.ATTR_SIZE);
										savCo.setOrgrootid_link(orgrootid_link);
										savCo.setSkuid_link(product_skuid_link);
										savCo.setUsercreateid_link(user.getId());
										savCo.setTimecreate(new Date());

										skuattService.save(savCo);
									}
									
									List<PContractBOM2SKU> list_bom_sku = bomskuService.getall_material_in_productBOMSKU(pcontractid_link, productid_link, sizeid_link, colorid_link, material_skuid_link);
									if(list_bom_sku.size() == 0) {
										PContractBOM2SKU bom_sku_new = new PContractBOM2SKU();
										bom_sku_new.setAmount(Float.parseFloat(amount.toString()));
										bom_sku_new.setCreateddate(current_time);
										bom_sku_new.setCreateduserid_link(user.getId());
										bom_sku_new.setId(null);
										bom_sku_new.setLost_ratio((float)lost_ratio);
										bom_sku_new.setMaterial_skuid_link(material_skuid_link);
										bom_sku_new.setOrgrootid_link(orgrootid_link);
										bom_sku_new.setPcontractid_link(pcontractid_link);
										bom_sku_new.setProduct_skuid_link(product_skuid_link);
										bom_sku_new.setProductid_link(productid_link);
										
										bomskuService.save(bom_sku_new);
									}
								}
								
								columnsize++;
								
								s_sizename = commonService.getStringValue(rowheader.getCell(columnsize));
								s_sizename = s_sizename.equals("0") ? "" : s_sizename;
							}
							
							//Chuyen sang row tiep theo neu con du lieu thi xu ly tiep khong thi dung lai
							rowNum++;
							row = sheet.getRow(rowNum);
							if(row == null) break;
							
							STT = commonService.getStringValue(row.getCell(ColumnTemplate.STT));
							STT = STT.equals("0") ? "" : STT;
						}
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
