package vn.gpay.gsmart.core.api.Upload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

import vn.gpay.gsmart.core.category.IUnitService;
import vn.gpay.gsmart.core.category.Unit;
import vn.gpay.gsmart.core.currency.Currency;
import vn.gpay.gsmart.core.currency.ICurrencyService;
import vn.gpay.gsmart.core.fob_price.FOBPrice;
import vn.gpay.gsmart.core.fob_price.IFOBService;
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.pcontract_price.IPContract_Price_DService;
import vn.gpay.gsmart.core.pcontract_price.IPContract_Price_Service;
import vn.gpay.gsmart.core.pcontract_price.PContract_Price;
import vn.gpay.gsmart.core.pcontract_price.PContract_Price_D;
import vn.gpay.gsmart.core.product.IProductService;
import vn.gpay.gsmart.core.product.IProductTypeService;
import vn.gpay.gsmart.core.product.Product;
import vn.gpay.gsmart.core.productpairing.IProductPairingService;
import vn.gpay.gsmart.core.productpairing.ProductPairing;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.utils.Column_Price_FOB;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.ProductType;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/upload_price_fob")
public class UploadPriceFOB {
	@Autowired
	Common commonService;
	@Autowired
	IProductService productService;
	@Autowired
	IPContract_Price_DService priceDService;
	@Autowired
	ICurrencyService currencyServcice;
	@Autowired
	IFOBService fobService;
	@Autowired
	IPContract_Price_Service priceService;
	@Autowired
	IOrgService orgService;
	@Autowired
	IUnitService unitService;
	@Autowired
	ISKU_Service skuService;
	@Autowired
	IProductPairingService pairService;
	@Autowired
	IProductTypeService producttypeService;

	@RequestMapping(value = "/download_temp", method = RequestMethod.POST)
	public ResponseEntity<download_temp_price_fob_response> DownloadTemp(HttpServletRequest request) {

		download_temp_price_fob_response response = new download_temp_price_fob_response();
		try {
			String FolderPath = "TemplateUpload";

			// Thư mục gốc upload file.
			String uploadRootPath = request.getServletContext().getRealPath(FolderPath);

			String filePath = uploadRootPath + "/" + "Tempate_price_fob.xlsx";
			Path path = Paths.get(filePath);
			byte[] data = Files.readAllBytes(path);
			response.data = data;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<download_temp_price_fob_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<download_temp_price_fob_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/upload_price", method = RequestMethod.POST)
	public ResponseEntity<upload_price_response> UploadPrice_FOB(HttpServletRequest request,
			@RequestParam("file") MultipartFile file, @RequestParam("sizesetid_link") long sizesetid_link,
			@RequestParam("pcontract_poid_link") long pcontract_poid_link,
			@RequestParam("pcontractid_link") long pcontractid_link,
			@RequestParam("currencyid_link") long currencyid_link) {
		upload_price_response response = new upload_price_response();
		response.data = new ArrayList<>();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			Currency currency = currencyServcice.findOne(currencyid_link);

			Date current_time = new Date();
			String FolderPath = "upload/price_fob";
			String uploadRootPath = request.getServletContext().getRealPath(FolderPath);

			File uploadRootDir = new File(uploadRootPath);
			// Tạo thư mục gốc upload nếu nó không tồn tại.
			if (!uploadRootDir.exists()) {
				uploadRootDir.mkdirs();
			}

			String name = file.getOriginalFilename();
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

				int rowNum = 1;
				int colNum = 0;
				String mes_err = "";
				Row row = sheet.getRow(rowNum);
				try {
					String Loai = "";
					Loai = commonService.getStringValue(row.getCell(Column_Price_FOB.Loai));
					while (!Loai.equals("")) {
						colNum = Column_Price_FOB.MaNPL;
						String MaNPL = commonService.getStringValue(row.getCell(Column_Price_FOB.MaNPL));
						MaNPL = MaNPL.equals("0") ? "" : MaNPL;

						colNum = Column_Price_FOB.MaSPBo;
						String MaSPBo = commonService.getStringValue(row.getCell(Column_Price_FOB.MaSPBo));
						MaSPBo = MaSPBo.equals("0") ? "" : MaSPBo;

						colNum = Column_Price_FOB.MaSPCon;
						String MaSPCon = commonService.getStringValue(row.getCell(Column_Price_FOB.MaSPCon));
						MaSPCon = MaSPCon.equals("0") ? "" : MaSPCon;

						colNum = Column_Price_FOB.MauNPL;
						String MauNPL = commonService.getStringValue(row.getCell(Column_Price_FOB.MauNPL));
						MauNPL = MauNPL.equals("0") ? "" : MauNPL;
						
						colNum = Column_Price_FOB.Mota;
						String MoTa = commonService.getStringValue(row.getCell(Column_Price_FOB.Mota));
						MoTa = MoTa.equals("0") ? "" : MoTa;

						colNum = Column_Price_FOB.NhaCungCap;
						String NhaCungCap = commonService.getStringValue(row.getCell(Column_Price_FOB.NhaCungCap));
						NhaCungCap = NhaCungCap.equals("0") ? "" : NhaCungCap;

						colNum = Column_Price_FOB.DinhMuc;
						String s_dinhmuc = commonService.getStringValue(row.getCell(Column_Price_FOB.DinhMuc));
						s_dinhmuc = s_dinhmuc.equals("0") ? "" : s_dinhmuc;
						Float DinhMuc = Float.parseFloat(s_dinhmuc);

						colNum = Column_Price_FOB.TieuHao;
						String s_tieuhao = commonService.getStringValue(row.getCell(Column_Price_FOB.TieuHao));
						s_tieuhao = s_tieuhao.equals("0") ? "" : s_tieuhao;
						Float TieuHao = Float.parseFloat(s_tieuhao);

						colNum = Column_Price_FOB.DonViTinh;
						String s_donvitinh = commonService.getStringValue(row.getCell(Column_Price_FOB.DonViTinh));
						s_donvitinh = s_donvitinh.equals("0") ? "" : s_donvitinh;

						colNum = Column_Price_FOB.Gia;
						String s_gia = commonService.getStringValue(row.getCell(Column_Price_FOB.Gia));
						s_gia = s_gia.equals("0") ? "" : s_gia;
						Float Gia = Float.parseFloat(s_gia);

						// Kiem tra xem ten gia co trong danh muc chua
						Long fobpriceid_link = null;
						List<FOBPrice> list_dm_pricefob = fobService.getByName(MaNPL+"("+MauNPL+")");
						if (list_dm_pricefob.size() == 0) {
							FOBPrice price = new FOBPrice();
							price.setId(null);
							price.setIsdefault(false);
							price.setName(MaNPL+"("+MauNPL+")");
							price.setOrgrootid_link(orgrootid_link);
							price.setPrice(Gia);
							price = fobService.save(price);
							fobpriceid_link = price.getId();
						} else {
							fobpriceid_link = list_dm_pricefob.get(0).getId();
						}

						// kiem tra NPL
						List<vn.gpay.gsmart.core.product.ProductType> list_type = productService.getTypeByName(Loai.trim());
						Long product_type = null;
						if(list_type.size() > 0) {
							product_type = list_type.get(0).getId();
						}
						else {
							vn.gpay.gsmart.core.product.ProductType type = new vn.gpay.gsmart.core.product.ProductType();
							type.setId(null);
							type.setName(Loai.trim());
							
							type = producttypeService.save(type);
							product_type = type.getId();
						}
						
						Long npl_id_link = null;
						List<Product> list_npl = productService.getby_code_type_description_name(orgrootid_link, MaNPL,
								product_type.intValue(), MoTa, MaNPL);
						if (list_npl.size() == 0) {
							Product product = new Product();
							product.setBuyercode(MaNPL);
							product.setCode(MaNPL);
							product.setBuyername(MaNPL);
							product.setDescription(MoTa);
							product.setId(null);
							product.setOrgrootid_link(orgrootid_link);
							product.setTimecreate(new Date());
							product.setProducttypeid_link(product_type.intValue());
							product.setStatus(1);
							product = productService.save(product);
							npl_id_link = product.getId();
						} else {
							npl_id_link = list_npl.get(0).getId();
						}

//						List<SKU> list_sku = skuService.get

						// Kiem tra nha cung cap
						Long org_providerid_link = null;
						List<Org> list_org_provider = orgService.getByNameAndType(NhaCungCap, 5);
						if (list_org_provider.size() == 0) {
							Org org = new Org();
							org.setId(null);
							org.setOrgrootid_link(orgrootid_link);
							org.setName(NhaCungCap);
							org.setOrgtypeid_link(5);
							org = orgService.save(org);
							org_providerid_link = org.getId();
						} else {
							org_providerid_link = list_org_provider.get(0).getId();
						}

						// kiem tra don vi tinh
						Long unitid_link = null;
						List<Unit> list_unit = unitService.getbyName(s_donvitinh);
						if (list_unit.size() == 0) {
							Unit unit = new Unit();
							unit.setCode(s_donvitinh);
							unit.setId(null);
							unit.setName(s_donvitinh);
							unit.setOrgrootid_link(orgrootid_link);
//							unit.setUnittype(unittype);
							unit = unitService.save(unit);
							unitid_link = unit.getId();
						} else {
							unitid_link = list_unit.get(0).getId();
						}

						// kiem tra xem npl co dung cho ca bo hay khong
						Long productid_link = null, productpairid_link = null;
						String product_buyercode = MaSPCon.equals("") ? MaSPBo : MaSPCon;
						int producttype = !MaSPCon.equals("") ? ProductType.SKU_TYPE_COMPLETEPRODUCT
								: ProductType.SKU_TYPE_PRODUCT_PAIR;
						List<Product> list_product = productService.getByBuyerCodeAndType(product_buyercode,
								producttype);
						productid_link = list_product.get(0).getId();

						List<ProductPairing> list_pair = pairService.getbypcontract_product(pcontractid_link,
								productid_link, orgrootid_link);
						if (list_pair.size() > 0)
							productpairid_link = list_pair.get(0).getProductpairid_link();

						List<PContract_Price> list_pcontract_price = priceService
								.getPrice_by_product_and_sizeset(pcontract_poid_link, productid_link, sizesetid_link);

						Long pcontractpriceid_link = list_pcontract_price.get(0).getId();
						PContract_Price price_current = list_pcontract_price.get(0);
						float price_fob_current = price_current.getPrice_fob() == null ? 0
								: price_current.getPrice_fob();
						float price_total_curretn = price_current.getTotalprice() == null ? 0
								: price_current.getTotalprice();

						// Kiem tra xem price da co hay chua
						List<PContract_Price_D> list_price_d = priceDService
								.getPrice_D_ByFobPriceAndPContractPrice(pcontractpriceid_link, fobpriceid_link);
						Float price = Gia * DinhMuc * (1 + TieuHao / 100);
						
						if (list_price_d.size() == 0) {

							PContract_Price_D price_d = new PContract_Price_D();
							price_d.setCost(Gia);
							price_d.setCurrencyid_link(currencyid_link);
							price_d.setDatecreated(new Date());
							price_d.setExchangerate(currency.getExchangerate().floatValue());
							price_d.setFobpriceid_link(fobpriceid_link);
							price_d.setId(null);
							price_d.setIsfob(true);
							price_d.setOrgrootid_link(orgrootid_link);
							price_d.setPcontract_poid_link(pcontract_poid_link);
							price_d.setPcontractid_link(pcontractid_link);
							price_d.setPcontractpriceid_link(pcontractpriceid_link);
							price_d.setPrice(price);
							price_d.setProductid_link(productid_link);
							price_d.setProviderid_link(org_providerid_link);
							price_d.setSizesetid_link(sizesetid_link);
							price_d.setUnitid_link(unitid_link);
							price_d.setUsercreatedid_link(user.getId());
							price_d.setQuota(DinhMuc);
							price_d.setUnitprice(Gia);
							price_d.setLost_ratio(TieuHao);
							price_d.setMaterialid_link(npl_id_link);
							price_d = priceDService.save(price_d);

							// neu la san pham don thi cong len gia fob cua san pham va cong don len gia cua
							// bo
							price_current.setPrice_fob(price_fob_current + price);
							price_current.setTotalprice(price_total_curretn + price);
							priceService.save(price_current);

						} else {
							PContract_Price_D price_d = list_price_d.get(0);

							price_current.setPrice_fob(price_fob_current + price - price_d.getPrice());
							price_current.setTotalprice(price_total_curretn + price - price_d.getPrice());
							priceService.save(price_current);

							price_d.setProviderid_link(org_providerid_link);
							price_d.setQuota(DinhMuc);
							price_d.setUnitprice(Gia);
							price_d.setLost_ratio(TieuHao);
							price_d.setMaterialid_link(npl_id_link);
							price_d.setUnitid_link(unitid_link);
							price_d.setPrice(price);
							price_d = priceDService.save(price_d);

						}

						// tinh binh qua gia quyen ve dai co all cua san pham
						List<PContract_Price> list_price = priceService.getPrice_by_product(pcontract_poid_link,
								productid_link);
						PContract_Price price_all = null;
						double price_total = 0, fob_total = 0, cmp_total = 0;

						for (PContract_Price price_tb : list_price) {
							if (!price_tb.getSizesetid_link().equals((long) 1)) {
								int quantity = price_tb.getQuantity() == null ? 0 : price_tb.getQuantity();
								float totalprice = price_tb.getTotalprice() == null ? 0 : price_tb.getTotalprice();
								float fobprice = price_tb.getPrice_fob() == null ? 0 : price_tb.getPrice_fob();
								float cmpprice = price_tb.getPrice_cmp() == null ? 0 : price_tb.getPrice_cmp();

								price_total += quantity * totalprice;
								fob_total += quantity * fobprice;
								cmp_total += quantity * cmpprice;

							} else {
								price_all = price_tb;
							}
						}

						double db_quantity = (double) price_all.getQuantity();

						double price_binhquan_total = price_total / db_quantity;
						price_binhquan_total = Math.ceil(price_binhquan_total * 10000) / 10000;

						double price_binhquan_fob = fob_total / db_quantity;
						price_binhquan_fob = Math.ceil(price_binhquan_fob * 10000) / 10000;

						double price_binhquan_cmp = cmp_total / db_quantity;
						price_binhquan_cmp = Math.ceil(price_binhquan_cmp * 10000) / 10000;

						price_all.setPrice_cmp((float) price_binhquan_cmp);
						price_all.setPrice_fob((float) price_binhquan_fob);
						price_all.setTotalprice((float) price_binhquan_total);
						priceService.save(price_all);

						// tong hop gia tu cac san pham con len san pham bo
						// kiem tra xem san pham co nam trong bo cua don hang ko
						if (productpairid_link != null) {
							List<PContract_Price> list_pcontract_price_pair = priceService
									.getPrice_by_product_and_sizeset(pcontract_poid_link, productpairid_link,
											sizesetid_link);
							PContract_Price price_pair = list_pcontract_price_pair.get(0);

							// cong gia len san pham bo
							list_pair = pairService.getproduct_pairing_detail_bycontract(orgrootid_link,
									pcontractid_link, productpairid_link);
							float price_fob_pair = 0;
							float price_total_pair = 0;
							for (ProductPairing pair_detail : list_pair) {
								list_pcontract_price_pair = priceService.getPrice_by_product_and_sizeset(
										pcontract_poid_link, pair_detail.getProductid_link(), sizesetid_link);
								for (PContract_Price price_detail : list_pcontract_price_pair) {
									price_fob_pair += price_detail.getPrice_fob() == null ? 0
											: price_detail.getPrice_fob();
									price_total_pair += price_detail.getTotalprice() == null ? 0
											: price_detail.getTotalprice();
								}
							}

							List<PContract_Price_D> list_price_d_pair = priceDService
									.getPrice_D_ByPContractPrice(price_pair.getId());
							for (PContract_Price_D price_d_pair : list_price_d_pair) {
								if (price_d_pair.getIsfob())
									price_fob_pair += price_d_pair.getPrice() == null ? 0 : price_d_pair.getPrice();
								price_total_pair += price_d_pair.getPrice() == null ? 0 : price_d_pair.getPrice();

							}

							price_pair.setPrice_fob(price_fob_pair);
							price_pair.setTotalprice(price_total_pair);
							priceService.save(price_pair);

							// tinh binh quan gia quyen cho ca bo
							List<PContract_Price> list_price_pair = priceService
									.getPrice_by_product(pcontract_poid_link, productpairid_link);
							PContract_Price price_all_pair = null;

							price_total = 0;
							fob_total = 0;
							cmp_total = 0;
							for (PContract_Price price_tb : list_price_pair) {
								if (!price_tb.getSizesetid_link().equals((long) 1)) {
									int quantity = price_tb.getQuantity() == null ? 0 : price_tb.getQuantity();
									float totalprice = price_tb.getTotalprice() == null ? 0 : price_tb.getTotalprice();
									float fobprice = price_tb.getPrice_fob() == null ? 0 : price_tb.getPrice_fob();
									float cmpprice = price_tb.getPrice_cmp() == null ? 0 : price_tb.getPrice_cmp();

									price_total += quantity * totalprice;
									fob_total += quantity * fobprice;
									cmp_total += quantity * cmpprice;

								} else {
									price_all_pair = price_tb;
								}
							}

							db_quantity = (double) price_all.getQuantity();

							price_binhquan_total = price_total / db_quantity;
							price_binhquan_total = Math.ceil(price_binhquan_total * 10000) / 10000;

							price_binhquan_fob = fob_total / db_quantity;
							price_binhquan_fob = Math.ceil(price_binhquan_fob * 10000) / 10000;

							price_binhquan_cmp = cmp_total / db_quantity;
							price_binhquan_cmp = Math.ceil(price_binhquan_cmp * 10000) / 10000;

							price_all_pair.setPrice_cmp((float) price_binhquan_cmp);
							price_all_pair.setPrice_fob((float) price_binhquan_fob);
							price_all_pair.setTotalprice((float) price_binhquan_total);
							priceService.save(price_all_pair);
						}

						rowNum++;
						row = sheet.getRow(rowNum);
						if (row != null)
							Loai = commonService.getStringValue(row.getCell(Column_Price_FOB.Loai));
						else
							Loai = "";
					}

				} catch (Exception e) {
					mes_err += "Có lỗi ở dòng " + (rowNum + 1) + " và cột " + (colNum + 1);
				} finally {
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
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<upload_price_response>(response, HttpStatus.OK);
	}
}
