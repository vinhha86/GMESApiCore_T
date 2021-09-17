package vn.gpay.gsmart.core.api.product_sewingcost;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.category.ILaborLevelService;
import vn.gpay.gsmart.core.category.LaborLevel;
import vn.gpay.gsmart.core.devices_type.Devices_Type;
import vn.gpay.gsmart.core.devices_type.IDevices_TypeService;
import vn.gpay.gsmart.core.product.IProductService;
import vn.gpay.gsmart.core.product.Product;
import vn.gpay.gsmart.core.product_balance.IProductBalanceService;
import vn.gpay.gsmart.core.product_balance.ProductBalance;
import vn.gpay.gsmart.core.product_balance_process.IProductBalanceProcessService;
import vn.gpay.gsmart.core.product_balance_process.ProductBalanceProcess;
import vn.gpay.gsmart.core.product_sewingcost.IProductSewingCostService;
import vn.gpay.gsmart.core.product_sewingcost.ProductSewingCost;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ColumnPorderSewingCost;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.ResponseMessage;
import vn.gpay.gsmart.core.workingprocess.IWorkingProcess_Service;
import vn.gpay.gsmart.core.workingprocess.WorkingProcess;

@RestController
@RequestMapping("/api/v1/productsewingcost")
public class ProductSewingCostAPI {
	@Autowired
	Common commonService;
	@Autowired IProductSewingCostService productSewingCostService;
	@Autowired IProductBalanceService productBalanceService;
	@Autowired IProductBalanceProcessService productBalanceProcessService;
	@Autowired IDevices_TypeService devicesTypeService;
	@Autowired ILaborLevelService laborLevelService;
	@Autowired IWorkingProcess_Service workingProcessService;
	@Autowired IProductService productService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<create_productsewingcost_response> Create(HttpServletRequest request,
			@RequestBody create_productsewingcost_request entity) {
		create_productsewingcost_response response = new create_productsewingcost_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long productid_link = entity.productid_link;
			long orgrootid_link = user.getRootorgid_link();
			List<Long> list_id = entity.list_working;

			for (Long workingprocessid_link : list_id) {
				WorkingProcess wp = workingProcessService.findOne(workingprocessid_link);

				List<ProductSewingCost> list_sewing = productSewingCostService.getby_product_and_workingprocess(productid_link,
						workingprocessid_link);
				if (list_sewing.size() == 0) {
					ProductSewingCost productSewing = new ProductSewingCost();
					productSewing.setAmount(0);
					productSewing.setCost((float) 0);
					productSewing.setDatecreated(new Date());
					productSewing.setId(null);
					productSewing.setOrgrootid_link(orgrootid_link);
					productSewing.setProductid_link(productid_link);
					productSewing.setTotalcost((float) 0);
					productSewing.setUsercreatedid_link(user.getId());
					productSewing.setWorkingprocessid_link(workingprocessid_link);
					productSewing.setTechcomment(wp.getTechcomment());
					productSewing.setLaborrequiredid_link(wp.getLaborrequiredid_link());
					productSewing.setDevicerequiredid_link(wp.getDevicerequiredid_link());
					productSewing.setTimespent_standard(wp.getTimespent_standard());
					productSewing.setDevicerequiredid_link(wp.getDevicerequiredid_link());
					productSewing.setLaborrequiredid_link(wp.getLaborrequiredid_link());

					productSewingCostService.save(productSewing);
				}
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<create_productsewingcost_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<create_productsewingcost_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/getby_product", method = RequestMethod.POST)
	public ResponseEntity<getby_product_response> GetByProduct(HttpServletRequest request,
			@RequestBody getby_product_request entity) {
		getby_product_response response = new getby_product_response();
		try {
			long productid_link = entity.productid_link;

			long workingprocessid_link = 0; // 0 : lay theo product
			List<ProductSewingCost> productSewingCost_list = productSewingCostService.getby_product_and_workingprocess(productid_link, workingprocessid_link);
			
			response.data = productSewingCost_list;

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getby_product_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<getby_product_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<update_productsewingcost_response> Update(HttpServletRequest request,
			@RequestBody update_productsewingcost_request entity) {
		update_productsewingcost_response response = new update_productsewingcost_response();
		try {
			ProductSewingCost productsewingcost = productSewingCostService.findOne(entity.data.getId());
			productSewingCostService.save(entity.data);

			// Cap nhat gia moi nhat cho san pham vao bang workingprocess
			WorkingProcess wp = workingProcessService.findOne(entity.data.getWorkingprocessid_link());
			wp.setLastcost(entity.data.getCost());
			workingProcessService.save(wp);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<update_productsewingcost_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<update_productsewingcost_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<delete_productsewingcost_response> Delete(HttpServletRequest request,
			@RequestBody delete_productsewingcost_request entity) {
		delete_productsewingcost_response response = new delete_productsewingcost_response();
		try {
			// xoá trong bảng product_balance_process (danh sách công đoạn trong cụm công
			// đoạn)
			List<ProductBalanceProcess> productBalanceProcess_list = productBalanceProcessService
					.getByProductSewingcost(entity.id);
			if (productBalanceProcess_list.size() > 0) {
				for (ProductBalanceProcess item : productBalanceProcess_list) {
					productBalanceProcessService.deleteById(item.getId());
				}
			}
			// xoá trong bảng product_sewingcost (danh sách công đoạn lệnh)
			productSewingCostService.deleteById(entity.id);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<delete_productsewingcost_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<delete_productsewingcost_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/delete_multi", method = RequestMethod.POST)
	public ResponseEntity<delete_productsewingcost_response> delete_multi(HttpServletRequest request,
			@RequestBody delete_productsewingcost_request entity) {
		delete_productsewingcost_response response = new delete_productsewingcost_response();
		try {
			List<Long> idList = entity.idList;
			for(Long id : idList) {
				// xoá trong bảng product_balance_process (danh sách công đoạn trong cụm công đoạn)
				List<ProductBalanceProcess> productBalanceProcess_list = productBalanceProcessService
						.getByProductSewingcost(id);
				if (productBalanceProcess_list.size() > 0) {
					for (ProductBalanceProcess item : productBalanceProcess_list) {
						productBalanceProcessService.deleteById(item.getId());
					}
				}
				// xoá trong bảng product_sewingcost (danh sách công đoạn lệnh)
				productSewingCostService.deleteById(id);
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<delete_productsewingcost_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<delete_productsewingcost_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/getby_product_notin_product_balance", method = RequestMethod.POST)
	public ResponseEntity<getby_product_response> getByProductNotInProductBalance(HttpServletRequest request,
			@RequestBody getby_product_request entity) {
		getby_product_response response = new getby_product_response();
		try {
			Long productid_link = entity.productid_link;
//				productid_link = 268L;

			List<Long> listProductBalanceProcessId = productBalanceProcessService
					.getProductBalanceProcessIdByProduct(productid_link);
//				System.out.println(listProductBalanceProcessId);
			if (listProductBalanceProcessId.size() > 0)
				response.data = productSewingCostService.getByProductUnused(productid_link, listProductBalanceProcessId);
			else
				response.data = productSewingCostService.getByProductUnused(productid_link);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getby_product_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<getby_product_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/upload_product_sewingcost", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> upload_product_sewingcost(HttpServletRequest request, @RequestParam("file") MultipartFile file,
			@RequestParam("productid_link") Long productid_link
			) {
		ResponseBase response = new ResponseBase();

		Date current_time = new Date();
		String name = "";
		String mes_err = "";
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			String FolderPath = "upload/product_sewingcost";
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

				// kiem tra xem co upload nham loai file hay khong
				Row row0 = sheet.getRow(0);
				String file_type = commonService.getStringValue(row0.getCell(ColumnPorderSewingCost.STT));
				if (!file_type.equals("STT (SewingCost)")) {
					mes_err = "Bạn upload nhầm loại file! Bạn phải tải file mẫu trước khi upload!";
				} else {
					// Kiem tra header
					Integer rowNum = 1; // index bắt đầu từ 0 (header)
					Integer colNum = 1;

					Row row = sheet.getRow(rowNum);
//					Row rowheader = sheet.getRow(0);
					
					try {
						String STT = "";
						STT = commonService.getStringValue(row.getCell(ColumnPorderSewingCost.STT));
						STT = STT.equals("0") ? "" : STT;
						
						Product product = productService.findOne(productid_link);
//						Long productid_link = product.getId();
						
						// kiểm tra các dòng có lỗi hay không (loop qua toàn bộ file excel)
						while(!STT.equals("")) {
//							System.out.println("- TenCongDoan");
							// TenCongDoan
							colNum = ColumnPorderSewingCost.TenCongDoan;
							String tenCongDoan = commonService.getStringValue(row.getCell(ColumnPorderSewingCost.TenCongDoan));
							tenCongDoan = tenCongDoan.toLowerCase().trim();
							if(tenCongDoan.equals("")) {
								mes_err = "Tên công đoạn không được để trống. Ở dòng " + (rowNum + 1) + " và cột "
										+ (colNum + 1);
								break;
							}
							
							// MaCongDoan
							colNum = ColumnPorderSewingCost.MaCongDoan;
							String maCongDoan = commonService.getStringValue(row.getCell(ColumnPorderSewingCost.MaCongDoan));
							maCongDoan = maCongDoan.toLowerCase().trim();
							if(maCongDoan.equals("")) {
								mes_err = "Mã công đoạn không được để trống. Ở dòng " + (rowNum + 1) + " và cột "
										+ (colNum + 1);
								break;
							}
							
//							System.out.println("- CumCongDoan");
							// CumCongDoan
							colNum = ColumnPorderSewingCost.CumCongDoan;
							String cumCongDoan = commonService.getStringValue(row.getCell(ColumnPorderSewingCost.CumCongDoan));
							cumCongDoan = cumCongDoan.toLowerCase().trim();
							
//							System.out.println("- ThietBi");
							// ThietBi
							colNum = ColumnPorderSewingCost.ThietBi;
							String thietBi = commonService.getStringValue(row.getCell(ColumnPorderSewingCost.ThietBi));
							thietBi = thietBi.toLowerCase().trim();
							if(!thietBi.equals("")) {
								List<Devices_Type> devices_type_list = devicesTypeService.loadDevicesTypeByCode(thietBi);
								if(devices_type_list.size() == 0) {
									mes_err = "Thiết bị không tồn tại. Ở dòng " + (rowNum + 1) + " và cột "
											+ (colNum + 1);
									break;
								}
							}
							
//							System.out.println("- BacTho");
							// BacTho
							colNum = ColumnPorderSewingCost.BacTho;
							String bacTho = commonService.getStringValue(row.getCell(ColumnPorderSewingCost.BacTho));
							bacTho = bacTho.toLowerCase().trim();
							if(!bacTho.equals("")) {
								List<LaborLevel> laborLevel_list = laborLevelService.findByCode(bacTho);
								if(laborLevel_list.size() == 0) {
									mes_err = "Bậc thợ không tồn tại. Ở dòng " + (rowNum + 1) + " và cột "
											+ (colNum + 1);
									break;
								}
							}
							
//							System.out.println("- ThoiGian");
							// ThoiGian
							colNum = ColumnPorderSewingCost.ThoiGian;
							String thoiGian = commonService.getStringValue(row.getCell(ColumnPorderSewingCost.ThoiGian));
							thoiGian = thoiGian.toLowerCase().trim();
							if(!thoiGian.equals("")) {
								try {
								    Integer.parseInt(thoiGian);
								} catch (NumberFormatException e) {
									mes_err = "Thời gian phải là số. Ở dòng " + (rowNum + 1) + " và cột "
											+ (colNum + 1);
									break;
								}
							}else {
								mes_err = "Thời gian phải là số. Ở dòng " + (rowNum + 1) + " và cột "
										+ (colNum + 1);
								break;
							}
							
//							System.out.println("- DonGia");
							// DonGia
							colNum = ColumnPorderSewingCost.DonGia;
							String donGia = commonService.getStringValue(row.getCell(ColumnPorderSewingCost.DonGia));
							donGia = donGia.toLowerCase().trim();
							if(!donGia.equals("")) {
								try {
								    Float.parseFloat(donGia);
								} catch (NumberFormatException e) {
									mes_err = "Đơn giá phải là số. Ở dòng " + (rowNum + 1) + " và cột "
											+ (colNum + 1);
									break;
								}
							}
							
//							System.out.println("- SoLuong");
							// SoLuong
							colNum = ColumnPorderSewingCost.SoLuong;
							String soLuong = commonService.getStringValue(row.getCell(ColumnPorderSewingCost.SoLuong));
							soLuong = soLuong.toLowerCase().trim();
							if(!soLuong.equals("")) {
								try {
								    Integer.parseInt(soLuong);
								} catch (NumberFormatException e) {
									mes_err = "Số lượng phải là số. Ở dòng " + (rowNum + 1) + " và cột "
											+ (colNum + 1);
									break;
								}
							}
							
							// Chuyen sang row tiep theo neu con du lieu thi xu ly tiep khong thi dung lai
							rowNum++;
							row = sheet.getRow(rowNum);
							if (row == null) {
								break;
							}
							STT = commonService.getStringValue(row.getCell(ColumnPorderSewingCost.STT));
							STT = STT.equals("0") ? "" : STT;
						}
						
						// nếu không có lỗi -> xử lý db
						if(mes_err.equals("")) {
							rowNum = 1;
							colNum = 1;
							row = sheet.getRow(rowNum);
							
							STT = commonService.getStringValue(row.getCell(ColumnPorderSewingCost.STT));
							STT = STT.equals("0") ? "" : STT;
							
							while (!STT.equals("")) {
								colNum = ColumnPorderSewingCost.TenCongDoan;
								String tenCongDoan = commonService.getStringValue(row.getCell(ColumnPorderSewingCost.TenCongDoan)).toLowerCase().trim();
								colNum = ColumnPorderSewingCost.MaCongDoan;
								String maCongDoan = commonService.getStringValue(row.getCell(ColumnPorderSewingCost.MaCongDoan)).toLowerCase().trim();
								colNum = ColumnPorderSewingCost.CumCongDoan;
								String cumCongDoan = commonService.getStringValue(row.getCell(ColumnPorderSewingCost.CumCongDoan)).toLowerCase().trim();
								colNum = ColumnPorderSewingCost.ThietBi;
								String thietBi = commonService.getStringValue(row.getCell(ColumnPorderSewingCost.ThietBi)).toLowerCase().trim();
								colNum = ColumnPorderSewingCost.BacTho;
								String bacTho = commonService.getStringValue(row.getCell(ColumnPorderSewingCost.BacTho)).toLowerCase().trim();
								colNum = ColumnPorderSewingCost.ThoiGian;
								String thoiGian = commonService.getStringValue(row.getCell(ColumnPorderSewingCost.ThoiGian)).toLowerCase().trim();
								colNum = ColumnPorderSewingCost.ChuThich;
								String chuThich = commonService.getStringValue(row.getCell(ColumnPorderSewingCost.ChuThich)).trim();
								colNum = ColumnPorderSewingCost.DonGia;
								String donGia = commonService.getStringValue(row.getCell(ColumnPorderSewingCost.DonGia)).toLowerCase().trim();
								colNum = ColumnPorderSewingCost.SoLuong;
								String soLuong = commonService.getStringValue(row.getCell(ColumnPorderSewingCost.SoLuong)).toLowerCase().trim();
								
								Long thietBiId = null;
								Long bacThoId = null;
								String bacThoComment = null;
								Integer intValueThoiGian = Integer.parseInt(thoiGian);
								Float floatValueDonGia = null;
								Integer intValueSoLuong = null;
								Float floatValueTongGia = null;
								
								// ThietBi
								if(!thietBi.equals("")) {
									List<Devices_Type> devices_type_list = devicesTypeService.loadDevicesTypeByCode(thietBi);
									if(devices_type_list.size() > 0) {
										Devices_Type devices_Type = devices_type_list.get(0);
										thietBiId = devices_Type.getId();
									}
								}
								// BacTho
								if(!bacTho.equals("")) {
									List<LaborLevel> laborLevel_list = laborLevelService.findByCode(bacTho);
									if(laborLevel_list.size() > 0) {
										LaborLevel laborLevel = laborLevel_list.get(0);
										bacThoId = laborLevel.getId();
										bacThoComment = laborLevel.getComment();
									}
								}
								// ChuThich
								if(chuThich.equals("")) {
									chuThich = null;
								}
								// DonGia
								if(!donGia.equals("")) {
									floatValueDonGia = Float.parseFloat(donGia);
								}
								// SoLuong
								if(!soLuong.equals("")) {
									intValueSoLuong = Integer.parseInt(soLuong);
								}
								// TongGia
								if(floatValueDonGia != null && intValueSoLuong != null) {
									floatValueTongGia = floatValueDonGia * intValueSoLuong;
								}
								// CumCongDoan
								if(cumCongDoan.equals("")) {
									cumCongDoan = null;
								}
								
								// product workingprocess
								WorkingProcess workingProcess = new WorkingProcess();
//								List<WorkingProcess> workingProcess_list = workingProcessService.getByName_Product(tenCongDoan, productid_link);
								List<WorkingProcess> workingProcess_list = workingProcessService.getByCode(maCongDoan, productid_link);
								if(workingProcess_list.size() > 0) { // công đoạn đã tồn tại
									workingProcess = workingProcess_list.get(0);
									workingProcess.setName(commonService.getStringValue(row.getCell(ColumnPorderSewingCost.TenCongDoan)).trim());
									workingProcess.setDevicerequiredid_link(thietBiId);
									workingProcess.setLaborrequiredid_link(bacThoId);
									workingProcess.setLaborrequired_desc(bacThoComment);
									workingProcess.setTimespent_standard(intValueThoiGian);
									workingProcess.setTechcomment(chuThich);
									workingProcess.setProductid_link(productid_link);
									workingProcess = workingProcessService.save(workingProcess);
								} else { // công đoạn chưa tồn tại -> create
									workingProcess.setId(null);
									workingProcess.setOrgrootid_link(orgrootid_link);
									workingProcess.setName(commonService.getStringValue(row.getCell(ColumnPorderSewingCost.TenCongDoan)).trim());
									workingProcess.setCode(commonService.getStringValue(row.getCell(ColumnPorderSewingCost.MaCongDoan)).trim());
									workingProcess.setProductid_link(productid_link);
									workingProcess.setTimespent_standard(intValueThoiGian);
									workingProcess.setDevicerequiredid_link(thietBiId);
									workingProcess.setLaborrequiredid_link(bacThoId);
									workingProcess.setLaborrequired_desc(bacThoComment);
									workingProcess.setTechcomment(chuThich);
									workingProcess.setProductid_link(productid_link);
									workingProcess.setProcess_type(1);
									workingProcess.setUsercreatedid_link(user.getId());
									workingProcess.setTimecreated(current_time);
									workingProcess = workingProcessService.save(workingProcess);
								}
								// product_sewingcost
								ProductSewingCost productSewingCost = new ProductSewingCost();
								List<ProductSewingCost> productSewingCost_list = productSewingCostService.getby_product_and_workingprocess(productid_link, workingProcess.getId());
								if(productSewingCost_list.size() > 0) { // công đoạn đã tồn tại trong lệnh sx
									productSewingCost = productSewingCost_list.get(0);
									productSewingCost.setDevicerequiredid_link(thietBiId);
									productSewingCost.setLaborrequiredid_link(bacThoId);
									productSewingCost.setTechcomment(chuThich);
									productSewingCost.setTimespent_standard(intValueThoiGian);
									productSewingCost.setCost(floatValueDonGia);
									productSewingCost.setAmount(intValueSoLuong);
									productSewingCost.setTotalcost(floatValueTongGia);
									productSewingCost.setWorkingprocessid_link(workingProcess.getId());
									productSewingCost = productSewingCostService.save(productSewingCost);
								}else { // công đoạn chưa tồn tại trong lệnh sx -> create
									productSewingCost.setId(null);
									productSewingCost.setOrgrootid_link(orgrootid_link);
									productSewingCost.setProductid_link(productid_link);
									productSewingCost.setWorkingprocessid_link(workingProcess.getId());
									productSewingCost.setCost(floatValueDonGia);
									productSewingCost.setAmount(intValueSoLuong);
									productSewingCost.setTotalcost(floatValueTongGia);
									productSewingCost.setUsercreatedid_link(user.getId());
									productSewingCost.setDatecreated(current_time);
									productSewingCost.setTimespent_standard(intValueThoiGian);
									productSewingCost.setDevicerequiredid_link(thietBiId);
									productSewingCost.setLaborrequiredid_link(bacThoId);
									productSewingCost.setTechcomment(chuThich);
									productSewingCost.setWorkingprocessid_link(workingProcess.getId());
									productSewingCost = productSewingCostService.save(productSewingCost);
								}
								
								// product_balance
								if(cumCongDoan != null) {
									ProductBalance productBalance = new ProductBalance();
									List<ProductBalance> productBalance_list = productBalanceService.getByBalanceName_Product(cumCongDoan, productid_link);
									List<ProductBalance> productBalance_list_by_productid_link = productBalanceService.getByProduct(productid_link);
									if(productBalance_list.size() == 0) {
										productBalance.setId(null);
										productBalance.setOrgrootid_link(orgrootid_link);
										productBalance.setProductid_link(productid_link);
										productBalance.setBalance_name(commonService.getStringValue(row.getCell(ColumnPorderSewingCost.CumCongDoan)).trim());
										productBalance.setSortvalue(productBalance_list_by_productid_link.size());
										productBalance = productBalanceService.save(productBalance);
									}
								}
								
								// product_balance_process
								ProductBalanceProcess productBalanceProcess = new ProductBalanceProcess();
								if(cumCongDoan != null) {
									List<ProductBalance> productBalance_list = productBalanceService.getByBalanceName_Product(cumCongDoan, productid_link);
									ProductBalance productBalance = new ProductBalance();
									if(productBalance_list.size() > 0) {
										productBalance = productBalance_list.get(0);
										List<ProductBalanceProcess> productBalanceProcess_list = productBalanceProcessService.getByProductSewingcost(productSewingCost.getId());
										if(productBalanceProcess_list.size() > 0) { // công đoạn đã có trong 1 cụm công đoạn
											productBalanceProcess =  productBalanceProcess_list.get(0);
											productBalanceProcess.setProductbalanceid_link(productBalance.getId());
											productBalanceProcess.setProductsewingcostid_link(productSewingCost.getId());
											productBalanceProcess = productBalanceProcessService.save(productBalanceProcess);
										}else { // công đoạn chưa có trong 1 cụm công đoạn
											productBalanceProcess.setId(null);
											productBalanceProcess.setOrgrootid_link(orgrootid_link);
											productBalanceProcess.setProductbalanceid_link(productBalance.getId());
											productBalanceProcess.setProductsewingcostid_link(productSewingCost.getId());
											productBalanceProcess = productBalanceProcessService.save(productBalanceProcess);
										}
									}
								}else {
									List<ProductBalanceProcess> productBalanceProcess_list = productBalanceProcessService.getByProductSewingcost(productSewingCost.getId());
									if(productBalanceProcess_list.size() > 0) { // công đoạn đã có trong 1 cụm công đoạn
										productBalanceProcess =  productBalanceProcess_list.get(0);
										productBalanceProcessService.delete(productBalanceProcess);
									}
								}
								
								// Chuyen sang row tiep theo neu con du lieu thi xu ly tiep khong thi dung lai
								rowNum++;
								row = sheet.getRow(rowNum);
								if (row == null) {
									break;
								}
								STT = commonService.getStringValue(row.getCell(ColumnPorderSewingCost.STT));
								STT = STT.equals("0") ? "" : STT;
							}
						}
						
					} catch (Exception e) {
//						System.out.println("- last catch");
						mes_err = "Có lỗi ở dòng " + (rowNum + 1) + " và cột " + (colNum + 1) + ": " + mes_err;
					} finally {
//						System.out.println("- finally");
						workbook.close();
						serverFile.delete();
					}
				}
//				System.out.println("- outside");
				if (mes_err == "") {
//					System.out.println("- no error");
					response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				} else {
//					System.out.println("- error");
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
	
	@RequestMapping(value = "/download_temp_productsewingcost", method = RequestMethod.POST)
	public ResponseEntity<download_temp_productsewingcost_response> download_temp_productsewingcost(HttpServletRequest request) {

		download_temp_productsewingcost_response response = new download_temp_productsewingcost_response();
		try {
			String FolderPath = "TemplateUpload";

			// Thư mục gốc upload file.
			String uploadRootPath = request.getServletContext().getRealPath(FolderPath);

			String filePath = uploadRootPath + "/" + "Template_ProductSewingCost_New.xlsx";
			Path path = Paths.get(filePath);
			byte[] data = Files.readAllBytes(path);
			response.data = data;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<download_temp_productsewingcost_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<download_temp_productsewingcost_response>(response, HttpStatus.OK);
		}
	}
}
