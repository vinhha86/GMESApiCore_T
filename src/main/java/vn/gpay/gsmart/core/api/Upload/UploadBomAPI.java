package vn.gpay.gsmart.core.api.Upload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
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
import vn.gpay.gsmart.core.pcontract_bom2_npl_poline.IPContract_bom2_npl_poline_Service;
import vn.gpay.gsmart.core.pcontract_bom2_npl_poline.PContract_bom2_npl_poline;
import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.pcontract_product_bom_log.IPContract_bom2_sku_log_Service;
import vn.gpay.gsmart.core.pcontract_product_bom_log.PContract_bom2_sku_log;
import vn.gpay.gsmart.core.pcontractbomsku.IPContractBOM2SKUService;
import vn.gpay.gsmart.core.pcontractbomsku.PContractBOM2SKU;
import vn.gpay.gsmart.core.pcontractproduct.IPContractProductService;
import vn.gpay.gsmart.core.pcontractproduct.PContractProduct;
import vn.gpay.gsmart.core.pcontractproductbom.IPContractProductBom2Service;
import vn.gpay.gsmart.core.pcontractproductbom.PContractProductBom2;
import vn.gpay.gsmart.core.pcontractproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.product.IProductService;
import vn.gpay.gsmart.core.product.Product;
import vn.gpay.gsmart.core.productattributevalue.IProductAttributeService;
import vn.gpay.gsmart.core.productattributevalue.ProductAttributeValue;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sizeset.ISizeSetService;
import vn.gpay.gsmart.core.sku.ISKU_AttributeValue_Service;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.sku.SKU;
import vn.gpay.gsmart.core.sku.SKU_Attribute_Value;
import vn.gpay.gsmart.core.utils.AtributeFixValues;
import vn.gpay.gsmart.core.utils.ColumnTempBom;
import vn.gpay.gsmart.core.utils.ColumnTemplate;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.POType;
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
	@Autowired IPContract_POService poService;
	@Autowired IPContract_bom2_npl_poline_Service po_npl_Service;
	@Autowired IPContractProductService ppService;
	@Autowired IPContract_bom2_sku_log_Service bomlogService;
	@Autowired ISizeSetService sizesetService;
	
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
				
				//kiem tra xem co upload nham loai file hay khong 
				Row row0 = sheet.getRow(0);
				String file_type = commonService.getStringValue(row0.getCell(ColumnTempBom.STT));
				if(!file_type.equals("Size")) {
					mes_err = "Bạn upload nhầm loại file! Bạn phải xuất file mẫu trước khi upload!";
				}
				else {
					// Kiem tra header
					int rowNum = 2;
					int colNum = 1;
					
					Row row = sheet.getRow(rowNum);
					Row rowheader = sheet.getRow(1);

					List<Long> list_colorid_link = new ArrayList<Long>();
					String ColorName = "", ColorCode = "";
					String list_po_no = "";
					try {					
						String STT = "";
						STT = commonService.getStringValue(row.getCell(ColumnTempBom.STT));
						STT = STT.equals("0") ? "" : STT;
						//kiem tra ten mau xem co ton tai trong chi tiet po hay chua roi moi xu ly
						while (!STT.equals("")) {
							colNum = ColumnTempBom.TenMauSP;
							ColorName = commonService.getStringValue(row.getCell(ColumnTempBom.TenMauSP));
							
							colNum = ColumnTempBom.MaMauSP;
							ColorCode = commonService.getStringValue(row.getCell(ColumnTempBom.MaMauSP));

							if(!ColorName.toLowerCase().equals("All".toLowerCase())) {
								List<Attributevalue> listAttributevalue = attributevalueService.getByValue(ColorName+"("+ColorCode+")", AtributeFixValues.ATTR_COLOR);
								if(listAttributevalue.size() == 0) {
									mes_err = "Màu sản phẩm '"+ ColorName + "("+ColorCode+")"+ "' chưa có trong chi tiết PO! Bạn hãy thêm vào chi tiết PO trước khi upload định mức";
									break;
								}
								
								list_colorid_link.add(listAttributevalue.get(0).getId());
								
								List<Long> list_sku = pcontractskuService.getsku_bycolor(pcontractid_link, productid_link, listAttributevalue.get(0).getId());
								if(list_sku.size() == 0) {
									mes_err = "Màu sản phẩm '"+ ColorName + "' chưa có trong chi tiết PO! Bạn hãy thêm vào chi tiết PO trước khi upload định mức";
									break;
								}
							}
							else {
								list_colorid_link = pcontractskuService.getlistvalue_by_product(pcontractid_link, productid_link, AtributeFixValues.ATTR_COLOR);
							}
							
							//Kiem tra xem so PO co dung voi trong he thong hay khong
							colNum = ColumnTempBom.POLine;
							list_po_no = commonService.getStringValue(row.getCell(ColumnTempBom.POLine));
							list_po_no = list_po_no.toLowerCase();
							//List_str_po mà trống là dùng cho tất cả các line
							if(!list_po_no.equals("")) {
								String[] lst_po = list_po_no.split(",");
								for(String po_no : lst_po) {
									if(po_no.toLowerCase().equals("tbd")) {
										mes_err = "Số PO không được để là TBD. Ở dòng "+(rowNum+1) +" và cột "+ (colNum+1);
										break;
									}
									List<PContract_PO> listpo = poService.getbycode_and_type_and_product(po_no, POType.PO_LINE_CONFIRMED, pcontractid_link, productid_link);
									if(listpo.size() == 0) {
										mes_err = "Số PO '"+po_no+"' ở dòng "+(rowNum+1) +" và cột "+ (colNum+1)+" không tồn tại trong hệ thống! Bạn hãy kiểm tra lại trong hệ thống trước khi upload!";
										break;
									}
								}
							}
							
							if(!mes_err.equals("")) break;
							
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
								list_colorid_link =  new ArrayList<Long>();
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
								
								colNum = ColumnTempBom.NhaCungCap;
								String nhacungcap = commonService.getStringValue(row.getCell(ColumnTempBom.NhaCungCap));
								
								colNum = ColumnTempBom.CoKho;
								String str_CoKho = commonService.getStringValue(row.getCell(ColumnTempBom.CoKho));
								
								colNum = ColumnTempBom.TenMauSP;
								ColorName = commonService.getStringValue(row.getCell(ColumnTempBom.TenMauSP));
								
								colNum = ColumnTempBom.MaMauSP;
								ColorCode = commonService.getStringValue(row.getCell(ColumnTempBom.MaMauSP));
								
								colNum = ColumnTempBom.TenMauNPL;
								String TenMauNPL = commonService.getStringValue(row.getCell(ColumnTempBom.TenMauNPL));								
								
								colNum = ColumnTempBom.TenMauNPL;
								String MaMauNPL = commonService.getStringValue(row.getCell(ColumnTempBom.MaMauNPL));
								
								//kiem tra maunpl co trong db chua thi them vao
								Long npl_colorid_link = AtributeFixValues.value_color_all;
								
								String code_color_npl = TenMauNPL+"("+MaMauNPL+")";
								if(!code_color_npl.equals("()")) {
									List<Attributevalue> listAttributevalue_npl = attributevalueService.getByValue(code_color_npl, AtributeFixValues.ATTR_COLOR);
									if(listAttributevalue_npl.size() > 0) {
										npl_colorid_link = listAttributevalue_npl.get(0).getId();
									}
									else {
										Attributevalue av_new = new Attributevalue();
										av_new.setAttributeid_link(AtributeFixValues.ATTR_COLOR);
										av_new.setDatatype(0);
										av_new.setId(null);
										av_new.setIsdefault(false);
										av_new.setOrgrootid_link(orgrootid_link);
										av_new.setTimecreate(current_time);
										av_new.setUsercreateid_link(user.getId());
										av_new.setValue(code_color_npl);
										av_new.setSortvalue(0);
										
										av_new = attributevalueService.save(av_new);
										npl_colorid_link = av_new.getId();
									}
								}
								
								//Kiem tra co kho co trong db chua thi them vao bang attribute_value
								long sizewidthid_link = AtributeFixValues.value_sizewidth_all;
								if(!str_CoKho.trim().equals("")) {
									List<Attributevalue> list_av = attributevalueService.getByValue(str_CoKho.trim(), AtributeFixValues.ATTR_SIZEWIDTH);
									if(list_av.size() > 0) {
										sizewidthid_link = list_av.get(0).getId();
									}
									else {
										Attributevalue av_new = new Attributevalue();
										av_new.setAttributeid_link(AtributeFixValues.ATTR_SIZEWIDTH);
										av_new.setDatatype(0);
										av_new.setId(null);
										av_new.setIsdefault(false);
										av_new.setOrgrootid_link(orgrootid_link);
										av_new.setTimecreate(current_time);
										av_new.setUsercreateid_link(user.getId());
										av_new.setValue(str_CoKho.trim());
										av_new.setSortvalue(0);
										
										av_new = attributevalueService.save(av_new);
										sizewidthid_link = av_new.getId();
									}
								}
								
								
								if(!ColorName.toLowerCase().equals("All".toLowerCase())) {
									String code_color = ColorName+"("+ColorCode+")";
									List<Attributevalue> listAttributevalue = attributevalueService.getByValue(code_color, AtributeFixValues.ATTR_COLOR);								
									list_colorid_link.add(listAttributevalue.get(0).getId());
								}
								else {
									list_colorid_link = pcontractskuService.getlistvalue_by_product(pcontractid_link, productid_link, AtributeFixValues.ATTR_COLOR);
								}
								
								//kiem tra npl co chua thi sinh moi va them vao san pham
								//Chay tung mau san pham de sinh nguyen lieu va day vao trong db
								for(Long colorid_link : list_colorid_link) {
									Long npl_id = null;
//									String color_code_npl = attributevalueService.findOne(colorid_link).getValue();
//									String[] str_mau= color_code_npl.split("\\(");
//									String color_code = str_mau[1].replace(")", "");
									
									long material_skuid_link = 0;
									String ma_npl_code = MaMauNPL.equals("") ? ma_npl : ma_npl+"("+MaMauNPL+")";
									List<Product> list_npl = productService.getby_code_type_description_and_color_and_size(orgrootid_link, ma_npl_code, 
											type_npl, description, npl_colorid_link, sizewidthid_link);
									
									if(list_npl.size() == 0) {
										Product new_npl = new Product();
										new_npl.setBuyercode(ma_npl_code);
										new_npl.setBuyername(name_npl);
										new_npl.setCode(ma_npl_code);
										new_npl.setDescription(description);
										new_npl.setId(null);
										new_npl.setName(name_npl);
										new_npl.setOrgrootid_link(orgrootid_link);
										new_npl.setProducttypeid_link(type_npl);
										new_npl.setStatus(1);
										new_npl.setTimecreate(current_time);
										new_npl.setUsercreateid_link(user.getId());
										new_npl.setPartnercode(nhacungcap);
										
										new_npl = productService.save(new_npl);
										npl_id = new_npl.getId();
										
										//Them vao bang product_attribute_value cho mau va co 
										
										//sinh sku all cho npl
										//Sinh thuoc tinh mac dinh cho npl
										List<Attribute> lstAttr = attrService.getList_attribute_forproduct(type_npl,orgrootid_link);
										for (Attribute attribute : lstAttr) {
											ProductAttributeValue pav = new ProductAttributeValue();
											
											if(attribute.getId() == AtributeFixValues.ATTR_COLOR) {
												//them mau npl
												if(npl_colorid_link != AtributeFixValues.value_color_all) {
													pav.setId(null);
													pav.setProductid_link(npl_id);
													pav.setAttributeid_link(attribute.getId());
													pav.setAttributevalueid_link(npl_colorid_link);
													pav.setOrgrootid_link(user.getRootorgid_link());
													pavService.save(pav);
												}
												
												//them mau all
												ProductAttributeValue pav_mauall = new ProductAttributeValue();
												pav_mauall.setId(null);
												pav_mauall.setProductid_link(npl_id);
												pav_mauall.setAttributeid_link(attribute.getId());
												pav_mauall.setAttributevalueid_link(AtributeFixValues.value_color_all);
												pav_mauall.setOrgrootid_link(user.getRootorgid_link());
												pavService.save(pav_mauall);
											}
											else if(attribute.getId() == AtributeFixValues.ATTR_SIZEWIDTH) {
												//them co kho npl
												if(sizewidthid_link != AtributeFixValues.value_sizewidth_all) {
													pav.setId(null);
													pav.setProductid_link(npl_id);
													pav.setAttributeid_link(attribute.getId());
													pav.setAttributevalueid_link(sizewidthid_link);
													pav.setOrgrootid_link(user.getRootorgid_link());
													pavService.save(pav);
												}
												
												//them co kho all
												ProductAttributeValue pav_coall = new ProductAttributeValue();
												pav_coall.setId(null);
												pav_coall.setProductid_link(npl_id);
												pav_coall.setAttributeid_link(attribute.getId());
												pav_coall.setAttributevalueid_link(AtributeFixValues.value_sizewidth_all);
												pav_coall.setOrgrootid_link(user.getRootorgid_link());
												pavService.save(pav_coall);
											}
											else {
												pav.setId((long) 0);
												pav.setProductid_link(npl_id);
												pav.setAttributeid_link(attribute.getId());
												pav.setAttributevalueid_link((long)0);
												pav.setOrgrootid_link(user.getRootorgid_link());
												pavService.save(pav);
											}
										}
										
										//Sinh SKU cho mau all va co all
										
										SKU sku_all = new SKU();
										sku_all.setId(null);
										sku_all.setCode(genCodeSKU(new_npl));
										sku_all.setName(new_npl.getBuyername());
										sku_all.setProductid_link(npl_id);
										sku_all.setOrgrootid_link(user.getRootorgid_link());
										sku_all.setSkutypeid_link(type_npl);

										sku_all = skuService.save(sku_all);
										material_skuid_link = sku_all.getId();
										
										// Them vao bang sku_attribute_value
										SKU_Attribute_Value savMau_all = new SKU_Attribute_Value();
										savMau_all.setId(null);
										savMau_all.setAttributevalueid_link(AtributeFixValues.value_color_all);
										savMau_all.setAttributeid_link(AtributeFixValues.ATTR_COLOR);
										savMau_all.setOrgrootid_link(orgrootid_link);
										savMau_all.setSkuid_link(material_skuid_link);
										savMau_all.setUsercreateid_link(user.getId());
										savMau_all.setTimecreate(new Date());

										skuattService.save(savMau_all);

										SKU_Attribute_Value savCo_all = new SKU_Attribute_Value();
										savCo_all.setId(null);
										savCo_all.setAttributevalueid_link(AtributeFixValues.value_sizewidth_all);
										savCo_all.setAttributeid_link(AtributeFixValues.ATTR_SIZEWIDTH);
										savCo_all.setOrgrootid_link(orgrootid_link);
										savCo_all.setSkuid_link(material_skuid_link);
										savCo_all.setUsercreateid_link(user.getId());
										savCo_all.setTimecreate(new Date());

										skuattService.save(savCo_all);
										
										//sinh sku cho mau va co kho
										if(npl_colorid_link != AtributeFixValues.value_color_all || sizewidthid_link != AtributeFixValues.value_sizewidth_all) {
											SKU sku_npl = new SKU();
											sku_npl.setId(null);
											sku_npl.setCode(genCodeSKU(new_npl));
											sku_npl.setName(new_npl.getBuyername());
											sku_npl.setProductid_link(npl_id);
											sku_npl.setOrgrootid_link(user.getRootorgid_link());
											sku_npl.setSkutypeid_link(type_npl);

											sku_npl = skuService.save(sku_npl);
											material_skuid_link = sku_npl.getId();
											
											// Them vao bang sku_attribute_value
											SKU_Attribute_Value savMau = new SKU_Attribute_Value();
											savMau.setId(null);
											savMau.setAttributevalueid_link(npl_colorid_link);
											savMau.setAttributeid_link(AtributeFixValues.ATTR_COLOR);
											savMau.setOrgrootid_link(orgrootid_link);
											savMau.setSkuid_link(material_skuid_link);
											savMau.setUsercreateid_link(user.getId());
											savMau.setTimecreate(new Date());

											skuattService.save(savMau);

											SKU_Attribute_Value savCo = new SKU_Attribute_Value();
											savCo.setId(null);
											savCo.setAttributevalueid_link(sizewidthid_link);
											savCo.setAttributeid_link(AtributeFixValues.ATTR_SIZEWIDTH);
											savCo.setOrgrootid_link(orgrootid_link);
											savCo.setSkuid_link(material_skuid_link);
											savCo.setUsercreateid_link(user.getId());
											savCo.setTimecreate(new Date());

											skuattService.save(savCo);
										}
										
									}
									else {
										npl_id = list_npl.get(0).getId();
										List<ProductAttributeValue> pav_npl_mau = pavService.getOne_byproduct_and_value(productid_link, AtributeFixValues.ATTR_COLOR, npl_colorid_link);
										if(pav_npl_mau.size()  == 0) {
											ProductAttributeValue pav_mau = new ProductAttributeValue();
											pav_mau.setAttributeid_link(AtributeFixValues.ATTR_COLOR);
											pav_mau.setAttributevalueid_link(npl_colorid_link);
											pav_mau.setId(null);
											pav_mau.setOrgrootid_link(orgrootid_link);
											pav_mau.setProductid_link(productid_link);
											pavService.save(pav_mau);
										}
										
										material_skuid_link = skuattService.get_npl_sku_byproduct_and_valuemau_valueco(npl_id, npl_colorid_link, sizewidthid_link);
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
									else {
										PContractProductBom2 bom = list_bom.get(0);
										bom.setLost_ratio((float)lost_ratio);
										bomproductService.save(bom);
									}
									
									//kiem tra va them vao bang pcontractpo_npl
									colNum = ColumnTempBom.POLine;
//									String list_po_no = commonService.getStringValue(row.getCell(ColumnTempBom.POLine));
									if(!list_po_no.equals("")) {
										String[] lst_po = list_po_no.split(",");
										List<String> list_po_npl = new ArrayList<String>();
										for(String po_no : lst_po) {
											list_po_npl.add(po_no);
											List<PContract_PO> listpo = poService.getbycode_and_type_and_product(po_no, POType.PO_LINE_CONFIRMED, pcontractid_link, productid_link);
											if(listpo.size() == 0) {
												PContract_bom2_npl_poline po_npl = new PContract_bom2_npl_poline();
												po_npl.setId(null);
												po_npl.setNpl_skuid_link(material_skuid_link);
												po_npl.setPcontract_poid_link(listpo.get(0).getId());
												po_npl.setPcontractid_link(pcontractid_link);
												
												po_npl_Service.save(po_npl);
											}
										}
										
										//xoa cac po khoong co trong danh sach
										List<PContract_PO> listpo_del = poService.getpo_notin_list(list_po_npl, POType.PO_LINE_CONFIRMED, pcontractid_link);
										for(PContract_PO po : listpo_del) {
											List<PContract_bom2_npl_poline> po_npls = po_npl_Service.getby_po_and_npl(po.getId(), material_skuid_link);
											if(po_npls.size() > 0) {
												po_npl_Service.delete(po_npls.get(0));
											}
										}
									}
									else {
//										List<Integer> type = new ArrayList<Integer>();
//										type.add(POType.PO_LINE_CONFIRMED);
//										
//										List<PContract_PO> list_po = poService.getby_pcontract_and_type_andproduct(pcontractid_link, type, productid_link);
//										for(PContract_PO po : list_po) {
//											List<PContract_bom2_npl_poline> po_npls = po_npl_Service.getby_po_and_npl(po.getId(), material_skuid_link);
//											if(po_npls.size() == 0) {
//												PContract_bom2_npl_poline po_npl = new PContract_bom2_npl_poline();
//												po_npl.setId(null);
//												po_npl.setNpl_skuid_link(material_skuid_link);
//												po_npl.setPcontract_poid_link(po.getId());
//												po_npl.setPcontractid_link(pcontractid_link);
//												
//												po_npl_Service.save(po_npl);
//											}
//										}
									}
									
									
									//them vao trong bang pcontract-bom2_sku
									int columnsize = ColumnTempBom.HaoHut + 1;
									String s_sizename = commonService.getStringValue(rowheader.getCell(columnsize));
									s_sizename = s_sizename.equals("0") ? "" : s_sizename;
									
									while (!s_sizename.equals("")) {
										colNum = columnsize;
										//lay gia tri dinh muc
										double amount = 0;
										try {
											amount = row.getCell(columnsize).getNumericCellValue();
										}
										catch (Exception e) {
											amount = 0;
										}
										if(amount != 0) {
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
											
											Float amount_old = (float)-1;
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
												bom_sku_new.setAmount(Float.parseFloat("0"+amount));
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
											else {
												PContractBOM2SKU bom_sku = list_bom_sku.get(0);
												//luu lai gia tri cu truoc khi update
												amount_old = bom_sku.getAmount();
												
												bom_sku.setAmount(Float.parseFloat("0"+amount));
												bomskuService.save(bom_sku);
											}
											
											//kiem tra dinh muc da chot chua thi them vao bang log
											List<PContractProduct> list_pp = ppService.get_by_product_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
											if(list_pp.size() > 0) {
												boolean check = list_pp.get(0).getIsbom2done() == null ? false: list_pp.get(0).getIsbom2done();
												if(check) {
													if(!amount_old.equals(Float.parseFloat("0"+amount))) {
														PContract_bom2_sku_log bom_log = new PContract_bom2_sku_log();
														bom_log.setAmount(Float.parseFloat("0"+amount));
														bom_log.setAmount_old(amount_old);
														bom_log.setId(null);
														bom_log.setMaterial_skuid_link(material_skuid_link);
														bom_log.setOrgrootid_link(orgrootid_link);
														bom_log.setPcontractid_link(pcontractid_link);
														bom_log.setProduct_skuid_link(product_skuid_link);
														bom_log.setProductid_link(productid_link);
														bom_log.setProductid_link(productid_link);
														bom_log.setTimeupdate(current_time);
														bom_log.setUserupdateid_link(user.getId());
														
														bomlogService.save(bom_log);
													}
//													
												}
											}
										}
										
										columnsize++;
										
										s_sizename = commonService.getStringValue(rowheader.getCell(columnsize));
										s_sizename = s_sizename.equals("0") ? "" : s_sizename;
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
					}
					catch (Exception e) {
						mes_err = "Có lỗi ở dòng " +(rowNum+1)+" và cột "+ colNum +": "+ mes_err; 
					}
					finally {
						workbook.close();
						serverFile.delete();
					}
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
	
	@RequestMapping(value = "/bom_candoi_sizeset", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> BomCanDoiSizeset(HttpServletRequest request,
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
				
				//kiem tra xem co upload nham loai file hay khong 
				Row row0 = sheet.getRow(0);
				String file_type = commonService.getStringValue(row0.getCell(ColumnTempBom.STT));
				if(!file_type.equals("Sizeset")) {
					mes_err = "Bạn upload nhầm loại file! Bạn phải xuất file mẫu trước khi upload!";
				}
				else {
					// Kiem tra header
					int rowNum = 2;
					int colNum = 1;
					
					Row row = sheet.getRow(rowNum);
					Row rowheader = sheet.getRow(1);

					List<Long> list_colorid_link = new ArrayList<Long>();
					String ColorName = "", ColorCode = "";
					String list_po_no = "";
					try {					
						String STT = "";
						STT = commonService.getStringValue(row.getCell(ColumnTempBom.STT));
						STT = STT.equals("0") ? "" : STT;
						//kiem tra ten mau xem co ton tai trong chi tiet po hay chua roi moi xu ly
						while (!STT.equals("")) {
							
							colNum = ColumnTempBom.TenMauSP;
							ColorName = commonService.getStringValue(row.getCell(ColumnTempBom.TenMauSP));
							
							colNum = ColumnTempBom.MaMauSP;
							ColorCode = commonService.getStringValue(row.getCell(ColumnTempBom.MaMauSP));

							if(!ColorName.toLowerCase().equals("All".toLowerCase())) {
								List<Attributevalue> listAttributevalue = attributevalueService.getByValue(ColorName+"("+ColorCode+")", AtributeFixValues.ATTR_COLOR);
								if(listAttributevalue.size() == 0) {
									mes_err = "Màu sản phẩm '"+ ColorName + "("+ColorCode+")"+ "' chưa có trong chi tiết PO! Bạn hãy thêm vào chi tiết PO trước khi upload định mức";
									break;
								}
								
								list_colorid_link.add(listAttributevalue.get(0).getId());
								
								List<Long> list_sku = pcontractskuService.getsku_bycolor(pcontractid_link, productid_link, listAttributevalue.get(0).getId());
								if(list_sku.size() == 0) {
									mes_err = "Màu sản phẩm '"+ ColorName + "' chưa có trong chi tiết PO! Bạn hãy thêm vào chi tiết PO trước khi upload định mức";
									break;
								}
							}
							else {
								list_colorid_link = pcontractskuService.getlistvalue_by_product(pcontractid_link, productid_link, AtributeFixValues.ATTR_COLOR);
							}
							
							//Kiem tra xem so PO co dung voi trong he thong hay khong
							colNum = ColumnTempBom.POLine;
							list_po_no = commonService.getStringValue(row.getCell(ColumnTempBom.POLine));
							list_po_no = list_po_no.toLowerCase();
							//List_str_po mà trống là dùng cho tất cả các line
							if(!list_po_no.equals("")) {
								String[] lst_po = list_po_no.split(",");
								for(String po_no : lst_po) {
									if(po_no.toLowerCase().equals("tbd")) {
										mes_err = "Số PO không được để là TBD. Ở dòng "+(rowNum+1) +" và cột "+ (colNum+1);
										break;
									}
									List<PContract_PO> listpo = poService.getbycode_and_type_and_product(po_no, POType.PO_LINE_CONFIRMED, pcontractid_link, productid_link);
									if(listpo.size() == 0) {
										mes_err = "Số PO '"+po_no+"' ở dòng "+(rowNum+1) +" và cột "+ (colNum+1)+" không tồn tại trong hệ thống! Bạn hãy kiểm tra lại trong hệ thống trước khi upload!";
										break;
									}
								}
							}
							
							//kiem tra dai co co trong chi tiet po hay chua
							colNum = ColumnTempBom.HaoHut + 1;
							String s_sizesetname = commonService.getStringValue(rowheader.getCell(colNum));
							s_sizesetname = s_sizesetname.equals("0") ? "" : s_sizesetname;
							while (!s_sizesetname.equals("")) {
								Long sizesetid_link = sizesetService.getbyname_and_po(s_sizesetname, pcontractid_link, productid_link);
								
								if(sizesetid_link.equals(0)) {
									mes_err = "Dải cỡ '"+s_sizesetname+" không tồn tại trong chi tiết PO line! Bạn hãy kiểm tra lại trong hệ thống trước khi upload!";
									break;
								}
								
								colNum++;							
								s_sizesetname = commonService.getStringValue(rowheader.getCell(colNum));
							}
							
							if(!mes_err.equals("")) break;
							
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
								list_colorid_link =  new ArrayList<Long>();
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
								
								colNum = ColumnTempBom.NhaCungCap;
								String nhacungcap = commonService.getStringValue(row.getCell(ColumnTempBom.NhaCungCap));
								
								colNum = ColumnTempBom.CoKho;
								String str_CoKho = commonService.getStringValue(row.getCell(ColumnTempBom.CoKho));
								
								colNum = ColumnTempBom.TenMauSP;
								ColorName = commonService.getStringValue(row.getCell(ColumnTempBom.TenMauSP));
								
								colNum = ColumnTempBom.MaMauSP;
								ColorCode = commonService.getStringValue(row.getCell(ColumnTempBom.MaMauSP));

								if(!ColorName.toLowerCase().equals("All".toLowerCase())) {
									List<Attributevalue> listAttributevalue = attributevalueService.getByValue(ColorName+"("+ColorCode+")", AtributeFixValues.ATTR_COLOR);								
									list_colorid_link.add(listAttributevalue.get(0).getId());
								}
								else {
									list_colorid_link = pcontractskuService.getlistvalue_by_product(pcontractid_link, productid_link, AtributeFixValues.ATTR_COLOR);
								}
								
								//Kiem tra co kho co trong db chua thi them vao bang attribute_value
								List<Attributevalue> list_av = attributevalueService.getByValue(str_CoKho.trim(), AtributeFixValues.ATTR_SIZEWIDTH);
								long sizewidthid_link = 0;
								if(list_av.size() > 0) {
									sizewidthid_link = list_av.get(0).getId();
								}
								else {
									Attributevalue av_new = new Attributevalue();
									av_new.setAttributeid_link(AtributeFixValues.ATTR_SIZEWIDTH);
									av_new.setDatatype(0);
									av_new.setId(null);
									av_new.setIsdefault(false);
									av_new.setOrgrootid_link(orgrootid_link);
									av_new.setTimecreate(current_time);
									av_new.setUsercreateid_link(user.getId());
									av_new.setValue(str_CoKho);
									
									av_new = attributevalueService.save(av_new);
									sizewidthid_link = av_new.getId();
								}
								
								//kiem tra npl co chua thi sinh moi va them vao san pham
								//Chay tung mau san pham de sinh nguyen lieu va day vao trong db
								for(Long colorid_link : list_colorid_link) {
									Long npl_id = null;
									String color_code_npl = attributevalueService.findOne(colorid_link).getValue();
									String[] str_mau= color_code_npl.split("\\(");
									String color_code = str_mau[1].replace(")", "");
									
									long material_skuid_link = 0;
									String ma_npl_code = ma_npl+"("+color_code+")";
									List<Product> list_npl = productService.getby_code_type_description_and_color_and_size(orgrootid_link, ma_npl_code, type_npl, description, colorid_link, sizewidthid_link);
									
									if(list_npl.size() == 0) {
										Product new_npl = new Product();
										new_npl.setBuyercode(ma_npl_code);
										new_npl.setBuyername(name_npl);
										new_npl.setCode(ma_npl_code);
										new_npl.setDescription(description);
										new_npl.setId(null);
										new_npl.setName(name_npl);
										new_npl.setOrgrootid_link(orgrootid_link);
										new_npl.setProducttypeid_link(type_npl);
										new_npl.setStatus(1);
										new_npl.setTimecreate(current_time);
										new_npl.setUsercreateid_link(user.getId());
										new_npl.setPartnercode(nhacungcap);
										
										new_npl = productService.save(new_npl);
										npl_id = new_npl.getId();
										
										//Them vao bang product_attribute_value cho mau va co 
										
										//sinh sku all cho npl
										//Sinh thuoc tinh mac dinh cho npl
										List<Attribute> lstAttr = attrService.getList_attribute_forproduct(type_npl,orgrootid_link);
										for (Attribute attribute : lstAttr) {
											ProductAttributeValue pav = new ProductAttributeValue();
											
											if(attribute.getId() == AtributeFixValues.ATTR_COLOR) {
												pav.setId((long) 0);
												pav.setProductid_link(npl_id);
												pav.setAttributeid_link(attribute.getId());
												pav.setAttributevalueid_link(colorid_link);
												pav.setOrgrootid_link(user.getRootorgid_link());
												pavService.save(pav);
											}
											else if(attribute.getId() == AtributeFixValues.ATTR_SIZEWIDTH) {
												pav.setId((long) 0);
												pav.setProductid_link(npl_id);
												pav.setAttributeid_link(attribute.getId());
												pav.setAttributevalueid_link(sizewidthid_link);
												pav.setOrgrootid_link(user.getRootorgid_link());
												pavService.save(pav);
											}
											else {
												pav.setId((long) 0);
												pav.setProductid_link(npl_id);
												pav.setAttributeid_link(attribute.getId());
												pav.setAttributevalueid_link((long)0);
												pav.setOrgrootid_link(user.getRootorgid_link());
												pavService.save(pav);
											}
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
										List<ProductAttributeValue> pav_npl_mau = pavService.getOne_byproduct_and_value(productid_link, AtributeFixValues.ATTR_COLOR, colorid_link);
										if(pav_npl_mau.size()  == 0) {
											ProductAttributeValue pav_mau = new ProductAttributeValue();
											pav_mau.setAttributeid_link(AtributeFixValues.ATTR_COLOR);
											pav_mau.setAttributevalueid_link(colorid_link);
											pav_mau.setId(null);
											pav_mau.setOrgrootid_link(orgrootid_link);
											pav_mau.setProductid_link(productid_link);
											pavService.save(pav_mau);
										}
										
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
									else {
										PContractProductBom2 bom = list_bom.get(0);
										bom.setLost_ratio((float)lost_ratio);
										bomproductService.save(bom);
									}
									
									//kiem tra va them vao bang pcontractpo_npl
									colNum = ColumnTempBom.POLine;
//									String list_po_no = commonService.getStringValue(row.getCell(ColumnTempBom.POLine));
									if(!list_po_no.equals("")) {
										list_po_no = list_po_no.toLowerCase();
										String[] lst_po = list_po_no.split(",");
										List<String> list_po_npl = new ArrayList<String>();
										for(String po_no : lst_po) {
											list_po_npl.add(po_no);
											List<PContract_PO> listpo = poService.getbycode_and_type_and_product(po_no, POType.PO_LINE_CONFIRMED, pcontractid_link, productid_link);
											if(listpo.size() == 0) {
												PContract_bom2_npl_poline po_npl = new PContract_bom2_npl_poline();
												po_npl.setId(null);
												po_npl.setNpl_skuid_link(material_skuid_link);
												po_npl.setPcontract_poid_link(listpo.get(0).getId());
												po_npl.setPcontractid_link(pcontractid_link);
												
												po_npl_Service.save(po_npl);
											}
										}
										
										//xoa cac po khoong co trong danh sach
										List<PContract_PO> listpo_del = poService.getpo_notin_list(list_po_npl, POType.PO_LINE_CONFIRMED, pcontractid_link);
										for(PContract_PO po : listpo_del) {
											List<PContract_bom2_npl_poline> po_npls = po_npl_Service.getby_po_and_npl(po.getId(), material_skuid_link);
											if(po_npls.size() > 0) {
												po_npl_Service.delete(po_npls.get(0));
											}
										}
									}
									else {
										List<Integer> type = new ArrayList<Integer>();
										type.add(POType.PO_LINE_CONFIRMED);
										
										List<PContract_PO> list_po = poService.getby_pcontract_and_type_andproduct(pcontractid_link, type, productid_link);
										for(PContract_PO po : list_po) {
											List<PContract_bom2_npl_poline> po_npls = po_npl_Service.getby_po_and_npl(po.getId(), material_skuid_link);
											if(po_npls.size() == 0) {
												PContract_bom2_npl_poline po_npl = new PContract_bom2_npl_poline();
												po_npl.setId(null);
												po_npl.setNpl_skuid_link(material_skuid_link);
												po_npl.setPcontract_poid_link(po.getId());
												po_npl.setPcontractid_link(pcontractid_link);
												
												po_npl_Service.save(po_npl);
											}
										}
									}
									
									
									//them vao trong bang pcontract-bom2_sku
									int columnsizeset = ColumnTempBom.HaoHut + 1;
									String s_sizesetname = commonService.getStringValue(rowheader.getCell(columnsizeset));
									s_sizesetname = s_sizesetname.equals("0") ? "" : s_sizesetname;
									
									while (!s_sizesetname.equals("")) {
										colNum = columnsizeset;
										//lay gia tri dinh muc
										double amount = 0;
										try {
											amount = row.getCell(columnsizeset).getNumericCellValue();
										}
										catch (Exception e) {
											amount = 0;
										}
										
										if( amount != 0) {
											//kiem tra co co trong db chua chua co thi sinh moi
											String sizesetname = commonService.getStringValue(rowheader.getCell(columnsizeset));
											Long sizesetid_link = sizesetService.getbyname(sizesetname);
											
											List<Long> list_size = pcontractskuService.getlist_size_by_product_and_sizeset(pcontractid_link, productid_link, sizesetid_link);
											
											for(Long sizeid_link : list_size) {
												Float amount_old = (float)-1;
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
													bom_sku_new.setAmount(Float.parseFloat("0"+amount));
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
												else {
													PContractBOM2SKU bom_sku = list_bom_sku.get(0);
													//luu lai gia tri cu truoc khi update
													amount_old = bom_sku.getAmount();
													
													bom_sku.setAmount(Float.parseFloat("0"+amount));
													bomskuService.save(bom_sku);
												}
												
												//kiem tra dinh muc da chot chua thi them vao bang log
												List<PContractProduct> list_pp = ppService.get_by_product_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
												if(list_pp.size() > 0) {
													boolean check = list_pp.get(0).getIsbom2done() == null ? false: list_pp.get(0).getIsbom2done();
													if(check) {
														if(!amount_old.equals(Float.parseFloat("0"+amount))) {
															PContract_bom2_sku_log bom_log = new PContract_bom2_sku_log();
															bom_log.setAmount(Float.parseFloat("0"+amount));
															bom_log.setAmount_old(amount_old);
															bom_log.setId(null);
															bom_log.setMaterial_skuid_link(material_skuid_link);
															bom_log.setOrgrootid_link(orgrootid_link);
															bom_log.setPcontractid_link(pcontractid_link);
															bom_log.setProduct_skuid_link(product_skuid_link);
															bom_log.setProductid_link(productid_link);
															bom_log.setProductid_link(productid_link);
															bom_log.setTimeupdate(current_time);
															bom_log.setUserupdateid_link(user.getId());
															
															bomlogService.save(bom_log);
														}
//														
													}
												}
											}
											
										}
										
										columnsizeset++;
										
										s_sizesetname = commonService.getStringValue(rowheader.getCell(columnsizeset));
										s_sizesetname = s_sizesetname.equals("0") ? "" : s_sizesetname;
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
					}
					catch (Exception e) {
						mes_err = "Có lỗi ở dòng " +(rowNum+1)+" và cột "+ colNum +": "+ mes_err; 
					}
					finally {
						workbook.close();
						serverFile.delete();
					}
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
