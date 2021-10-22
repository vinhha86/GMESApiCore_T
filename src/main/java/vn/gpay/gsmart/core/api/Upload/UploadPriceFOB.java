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
import vn.gpay.gsmart.core.security.GpayUser;
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
			@RequestParam("file") MultipartFile file, @RequestParam("pcontractpriceid_link") long pcontractpriceid_link,
			@RequestParam("currencyid_link") long currencyid_link) {
		upload_price_response response = new upload_price_response();
		response.data = new ArrayList<>();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			Currency currency = currencyServcice.findOne(currencyid_link);
			PContract_Price pcontract_price = priceService.findOne(pcontractpriceid_link);

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

						colNum = Column_Price_FOB.Mota;
						String MoTa = commonService.getStringValue(row.getCell(Column_Price_FOB.Mota));
						MoTa = MoTa.equals("0") ? "" : MoTa;

						colNum = Column_Price_FOB.NhaCungCap;
						String NhaCungCap = commonService.getStringValue(row.getCell(Column_Price_FOB.NhaCungCap));
						NhaCungCap = NhaCungCap.equals("0") ? "" : NhaCungCap;

						colNum = Column_Price_FOB.MauNPL;
						String MauNPL = commonService.getStringValue(row.getCell(Column_Price_FOB.MauNPL));
						MauNPL = MauNPL.equals("0") ? "" : MauNPL;

						colNum = Column_Price_FOB.CoKho;
						String CoKho = commonService.getStringValue(row.getCell(Column_Price_FOB.CoKho));
						CoKho = CoKho.equals("0") ? "" : CoKho;

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

						Integer product_type = 0;
						switch (Loai.trim()) {
						case "FABRIC":
							product_type = ProductType.SKU_TYPE_MAINMATERIAL;
							break;
						case "SEWING":
							product_type = ProductType.SKU_TYPE_SWEINGTRIM_MIN;
							break;
						case "PACKING":
							product_type = ProductType.SKU_TYPE_PACKINGTRIM_MIN;
							break;
						case "THREAD":
							product_type = ProductType.SKU_TYPE_SWEINGTHREAD_MIN;
							break;
						case "TICKET":
							product_type = ProductType.TICKET;
							break;
						default:
							break;
						}

						// Kiem tra xem ten gia co trong danh muc chua
						Long fobpriceid_link = null;
						List<FOBPrice> list_dm_pricefob = fobService.getByName(MaNPL);
						if (list_dm_pricefob.size() == 0) {
							FOBPrice price = new FOBPrice();
							price.setId(null);
							price.setIsdefault(false);
							price.setName(MaNPL);
							price.setOrgrootid_link(orgrootid_link);
							price.setPrice(Gia);
							price = fobService.save(price);
							fobpriceid_link = price.getId();
						} else {
							fobpriceid_link = list_dm_pricefob.get(0).getId();
						}

						// Kiem tra nha cung cap
						Long org_providerid_link = null;
						List<Org> list_org_provider = orgService.getByNameAndType(NhaCungCap, 5);
						if (list_org_provider.size() == 0) {
							Org org = new Org();
							org.setId(null);
							org.setOrgrootid_link(orgrootid_link);
							org.setName(NhaCungCap);
							org = orgService.save(org);
							org_providerid_link = org.getId();
						} else {
							org_providerid_link = list_org_provider.get(0).getId();
						}

						// kiem tra don vi tinh
						List<Unit> list_unit = unitService.getbyName(s_donvitinh);

						// Kiem tra xem price da co hay chua
						List<PContract_Price_D> list_price_d = priceDService
								.getPrice_D_ByFobPriceNameAndPContractPrice(pcontractpriceid_link, MaNPL);
						if (list_price_d.size() == 0) {
							PContract_Price_D price_d = new PContract_Price_D();
							price_d.setCost(Gia);
							price_d.setCurrencyid_link(currencyid_link);
							price_d.setDatecreated(new Date());
							price_d.setExchangerate(currency.getExchangerate().floatValue());
							price_d.setFobpriceid_link(fobpriceid_link);
							price_d.setId(null);
							price_d.setIsfob(true);
							price_d.setLost_ratio(TieuHao);
							price_d.setOrgrootid_link(orgrootid_link);
							price_d.setPcontract_poid_link(pcontract_price.getPcontract_poid_link());
							price_d.setPcontractid_link(pcontract_price.getPcontractid_link());
							price_d.setPcontractpriceid_link(pcontractpriceid_link);
							price_d.setPrice(Gia);
							price_d.setProductid_link(pcontract_price.getProductid_link());
							price_d.setProviderid_link(org_providerid_link);
							price_d.setSizesetid_link(pcontract_price.getSizesetid_link());
//							price_d.setUnitid_link(unitid_link);
						}
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
