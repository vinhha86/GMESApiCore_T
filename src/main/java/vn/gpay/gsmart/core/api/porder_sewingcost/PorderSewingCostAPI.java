package vn.gpay.gsmart.core.api.porder_sewingcost;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import vn.gpay.gsmart.core.pcontract_price.IPContract_Price_Service;
import vn.gpay.gsmart.core.pcontract_price.PContract_Price;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_balance.IPOrderBalanceService;
import vn.gpay.gsmart.core.porder_balance.POrderBalance;
import vn.gpay.gsmart.core.porder_balance_process.IPOrderBalanceProcessService;
import vn.gpay.gsmart.core.porder_balance_process.POrderBalanceProcess;
import vn.gpay.gsmart.core.porder_sewingcost.IPorderSewingCost_Service;
import vn.gpay.gsmart.core.porder_sewingcost.POrderSewingCost;
import vn.gpay.gsmart.core.porder_sewingcost.POrderSewingCostBinding;
import vn.gpay.gsmart.core.porder_workingprocess.IPOrderWorkingProcess_Service;
import vn.gpay.gsmart.core.porder_workingprocess.POrderWorkingProcess;
import vn.gpay.gsmart.core.porder_workingprocess.POrderWorkingProcess_Service;
import vn.gpay.gsmart.core.porderprocessingns.IPorderProcessingNsService;
import vn.gpay.gsmart.core.porderprocessingns.PorderProcessingNs;
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
//import vn.gpay.gsmart.core.workingprocess.WorkingProcess;
import vn.gpay.gsmart.core.workingprocess.WorkingProcess;

@RestController
@RequestMapping("/api/v1/pordersewingcost")
public class PorderSewingCostAPI {
	@Autowired
	Common commonService;
	@Autowired IPorderSewingCost_Service pordersewingService;
	@Autowired IPOrder_Service porderService;
	@Autowired IPContract_Price_Service pcontractpriceService;
	@Autowired IPOrderBalanceService porderBalanceService;
	@Autowired IPOrderBalanceProcessService porderBalanceProcessService;
	@Autowired IPorderProcessingNsService porderProcessingNsService;
	@Autowired IDevices_TypeService devicesTypeService;
	@Autowired ILaborLevelService laborLevelService;
//	@Autowired IWorkingProcess_Service workingProcessService;
	@Autowired IPOrderWorkingProcess_Service porderWorkingProcessService;

	@Autowired IWorkingProcess_Service workingprocessService;
	@Autowired IProductSewingCostService productSewingCostService;
	@Autowired IProductBalanceService productBalanceService;
	@Autowired IProductBalanceProcessService productBalanceProcessService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<create_pordersewingcost_response> Create(HttpServletRequest request,
			@RequestBody create_pordersewingcost_request entity) {
		create_pordersewingcost_response response = new create_pordersewingcost_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long porderid_link = entity.porderid_link;
			long orgrootid_link = user.getRootorgid_link();
			List<Long> list_id = entity.list_working;

			for (Long workingprocessid_link : list_id) {
				POrderWorkingProcess wp = porderWorkingProcessService.findOne(workingprocessid_link);

				List<POrderSewingCost> list_sewing = pordersewingService.getby_porder_and_workingprocess(porderid_link,
						workingprocessid_link);
				if (list_sewing.size() == 0) {
					POrderSewingCost porderSewing = new POrderSewingCost();
					porderSewing.setAmount(0);
					porderSewing.setCost((float) 0);
					porderSewing.setDatecreated(new Date());
					porderSewing.setId(null);
					porderSewing.setOrgrootid_link(orgrootid_link);
					porderSewing.setPorderid_link(porderid_link);
					porderSewing.setTotalcost((float) 0);
					porderSewing.setUsercreatedid_link(user.getId());
					porderSewing.setWorkingprocessid_link(workingprocessid_link);
					porderSewing.setTechcomment(wp.getTechcomment());
					porderSewing.setLaborrequiredid_link(wp.getLaborrequiredid_link());
					porderSewing.setDevicerequiredid_link(wp.getDevicerequiredid_link());
					porderSewing.setTimespent_standard(wp.getTimespent_standard());
					porderSewing.setDevicerequiredid_link(wp.getDevicerequiredid_link());
					porderSewing.setLaborrequiredid_link(wp.getLaborrequiredid_link());

					pordersewingService.save(porderSewing);
				}
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<create_pordersewingcost_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<create_pordersewingcost_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/getby_porder", method = RequestMethod.POST)
	public ResponseEntity<getby_porder_response> GetByPorder(HttpServletRequest request,
			@RequestBody getby_porder_request entity) {
		getby_porder_response response = new getby_porder_response();
		try {
			long porderid_link = entity.porderid_link;

			long workingprocessid_link = 0; // 0 : lay theo porder
			List<POrderSewingCost> porderSewingCost_list = pordersewingService.getby_porder_and_workingprocess(porderid_link, workingprocessid_link);
			
			response.data = porderSewingCost_list;

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getby_porder_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<getby_porder_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<update_pordersewingcost_response> Update(HttpServletRequest request,
			@RequestBody update_pordersewingcost_request entity) {
		update_pordersewingcost_response response = new update_pordersewingcost_response();
		try {
			POrderSewingCost pordersewingcost = pordersewingService.findOne(entity.data.getId());
			float cost_old = pordersewingcost.getTotalcost();
			pordersewingService.save(entity.data);

			// Cap nhat gia moi nhat cho san pham vao bang workingprocess
			POrderWorkingProcess wp = porderWorkingProcessService.findOne(entity.data.getWorkingprocessid_link());
			wp.setLastcost(entity.data.getCost());
			porderWorkingProcessService.save(wp);

			// Cap nhat gia len PContract_PO
			POrder porder = porderService.findOne(entity.data.getPorderid_link());
			long pcontract_poid_link = porder.getPcontract_poid_link();
			long productid_link = porder.getProductid_link();
			PContract_Price price = pcontractpriceService.getPrice_CMP(pcontract_poid_link, productid_link);
			float price_cost_old = 0;
			if (price == null) {
				price = new PContract_Price();
				price.setPcontract_poid_link(pcontract_poid_link);
				price.setProductid_link(productid_link);
			} else {
				price_cost_old = price.getPrice_sewingcost();
			}
			price.setPrice_sewingcost(price_cost_old - cost_old + entity.data.getTotalcost());
			pcontractpriceService.save(price);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<update_pordersewingcost_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<update_pordersewingcost_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<delete_porersewingcost_response> Delete(HttpServletRequest request,
			@RequestBody delete_pordersewingcost_request entity) {
		delete_porersewingcost_response response = new delete_porersewingcost_response();
		try {
			// xoá trong bảng porders_balance_process (danh sách công đoạn trong cụm công
			// đoạn)
			List<POrderBalanceProcess> porderBalanceProcess_list = porderBalanceProcessService
					.getByPorderSewingcost(entity.id);
			if (porderBalanceProcess_list.size() > 0) {
				for (POrderBalanceProcess item : porderBalanceProcess_list) {
					porderBalanceProcessService.deleteById(item.getId());
				}
			}
			// xoá trong bảng porders_sewingcost (danh sách công đoạn lệnh)
			pordersewingService.deleteById(entity.id);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<delete_porersewingcost_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<delete_porersewingcost_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/delete_multi", method = RequestMethod.POST)
	public ResponseEntity<delete_porersewingcost_response> delete_multi(HttpServletRequest request,
			@RequestBody delete_pordersewingcost_request entity) {
		delete_porersewingcost_response response = new delete_porersewingcost_response();
		try {
			List<Long> idList = entity.idList;
			for(Long id : idList) {
				// xoá trong bảng porders_balance_process (danh sách công đoạn trong cụm công đoạn)
				List<POrderBalanceProcess> porderBalanceProcess_list = porderBalanceProcessService
						.getByPorderSewingcost(id);
				if (porderBalanceProcess_list.size() > 0) {
					for (POrderBalanceProcess item : porderBalanceProcess_list) {
						porderBalanceProcessService.deleteById(item.getId());
					}
				}
				// xoá trong bảng porders_sewingcost (danh sách công đoạn lệnh)
				pordersewingService.deleteById(id);
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<delete_porersewingcost_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<delete_porersewingcost_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/getby_porder_notin_porder_balance", method = RequestMethod.POST)
	public ResponseEntity<getby_porder_response> getByPorderNotInPorderBalance(HttpServletRequest request,
			@RequestBody getby_porder_request entity) {
		getby_porder_response response = new getby_porder_response();
		try {
			Long porderid_link = entity.porderid_link;
//				porderid_link = 268L;

			List<Long> listPorderBalanceProcessId = porderBalanceProcessService
					.getPOrderBalanceProcessIdByPorder(porderid_link);
//				System.out.println(listPorderBalanceProcessId);
			if (listPorderBalanceProcessId.size() > 0)
				response.data = pordersewingService.getByPorderUnused(porderid_link, listPorderBalanceProcessId);
			else
				response.data = pordersewingService.getByPorderUnused(porderid_link);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getby_porder_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<getby_porder_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/getForPProcessProductivity", method = RequestMethod.POST)
	public ResponseEntity<getForPProcessProductivity_response> getForPProcessProductivity(HttpServletRequest request,
			@RequestBody getForPProcessProductivity_request entity) {
		getForPProcessProductivity_response response = new getForPProcessProductivity_response();
		try {
			Long personnelid_link = entity.personnelid_link;
			Long porderid_link = entity.porderid_link;
			Long pordergrantid_link = entity.pordergrantid_link;
			Integer shifttypeid_link = entity.shifttypeid_link;
			Date processingdate = entity.processingdate;

			List<POrderSewingCost> listPOrderSewingCost = pordersewingService
					.getForPProcessProductivity(personnelid_link);
			Map<Long, POrderSewingCostBinding> mapTmp = new HashMap<Long, POrderSewingCostBinding>();
			List<PorderProcessingNs> listPorderProcessingNs = porderProcessingNsService.getByPersonnelDateAndShift(
					porderid_link, pordergrantid_link, personnelid_link, processingdate, shifttypeid_link);

			for (POrderSewingCost porderSewingCost : listPOrderSewingCost) {
				POrderSewingCostBinding temp = new POrderSewingCostBinding();
				temp.setId(porderSewingCost.getId());
				temp.setWorkingprocess_name(porderSewingCost.getWorkingprocess_name());
				temp.setAmount_complete(0);
				mapTmp.put(temp.getId(), temp);
			}

			for (PorderProcessingNs porderProcessingNs : listPorderProcessingNs) {
				Long pordersewingcostid_link = porderProcessingNs.getPordersewingcostid_link();
				POrderSewingCostBinding temp = mapTmp.get(pordersewingcostid_link);
				temp.setAmount_complete(porderProcessingNs.getAmount_complete());
				mapTmp.put(temp.getId(), temp);
			}

			response.data = new ArrayList<>(mapTmp.values());

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getForPProcessProductivity_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<getForPProcessProductivity_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/upload_porders_sewingcost", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> upload_porders_sewingcost(HttpServletRequest request, @RequestParam("file") MultipartFile file,
			@RequestParam("porderid_link") Long porderid_link
			) {
		ResponseBase response = new ResponseBase();

		Date current_time = new Date();
		String name = "";
		String mes_err = "";
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			String FolderPath = "upload/porders_sewingcost";
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
						
						POrder porder = porderService.findOne(porderid_link);
						Long productid_link = porder.getProductid_link();
						
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
								
								// porder workingprocess
								POrderWorkingProcess workingProcess = new POrderWorkingProcess();
//								List<POrderWorkingProcess> workingProcess_list = porderWorkingProcessService.getByName_Product(tenCongDoan, productid_link);
								List<POrderWorkingProcess> workingProcess_list = porderWorkingProcessService.getByCode(maCongDoan, productid_link, porderid_link);
								if(workingProcess_list.size() > 0) { // công đoạn đã tồn tại
									workingProcess = workingProcess_list.get(0);
									workingProcess.setName(commonService.getStringValue(row.getCell(ColumnPorderSewingCost.TenCongDoan)).trim());
									workingProcess.setDevicerequiredid_link(thietBiId);
									workingProcess.setLaborrequiredid_link(bacThoId);
									workingProcess.setLaborrequired_desc(bacThoComment);
									workingProcess.setTimespent_standard(intValueThoiGian);
									workingProcess.setTechcomment(chuThich);
									workingProcess.setPorderid_link(porderid_link);
									workingProcess = porderWorkingProcessService.save(workingProcess);
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
									workingProcess.setPorderid_link(porderid_link);
									workingProcess.setProcess_type(1);
									workingProcess.setUsercreatedid_link(user.getId());
									workingProcess.setTimecreated(current_time);
									workingProcess = porderWorkingProcessService.save(workingProcess);
								}
								// porders_sewingcost
								POrderSewingCost porderSewingCost = new POrderSewingCost();
								List<POrderSewingCost> porderSewingCost_list = pordersewingService.getby_porder_and_workingprocess(porderid_link, workingProcess.getId());
								if(porderSewingCost_list.size() > 0) { // công đoạn đã tồn tại trong lệnh sx
									porderSewingCost = porderSewingCost_list.get(0);
									porderSewingCost.setDevicerequiredid_link(thietBiId);
									porderSewingCost.setLaborrequiredid_link(bacThoId);
									porderSewingCost.setTechcomment(chuThich);
									porderSewingCost.setTimespent_standard(intValueThoiGian);
									porderSewingCost.setCost(floatValueDonGia);
									porderSewingCost.setAmount(intValueSoLuong);
									porderSewingCost.setTotalcost(floatValueTongGia);
									porderSewingCost.setWorkingprocessid_link(workingProcess.getId());
									porderSewingCost = pordersewingService.save(porderSewingCost);
								}else { // công đoạn chưa tồn tại trong lệnh sx -> create
									porderSewingCost.setId(null);
									porderSewingCost.setOrgrootid_link(orgrootid_link);
									porderSewingCost.setPorderid_link(porderid_link);
									porderSewingCost.setWorkingprocessid_link(workingProcess.getId());
									porderSewingCost.setCost(floatValueDonGia);
									porderSewingCost.setAmount(intValueSoLuong);
									porderSewingCost.setTotalcost(floatValueTongGia);
									porderSewingCost.setUsercreatedid_link(user.getId());
									porderSewingCost.setDatecreated(current_time);
									porderSewingCost.setTimespent_standard(intValueThoiGian);
									porderSewingCost.setDevicerequiredid_link(thietBiId);
									porderSewingCost.setLaborrequiredid_link(bacThoId);
									porderSewingCost.setTechcomment(chuThich);
									porderSewingCost.setWorkingprocessid_link(workingProcess.getId());
									porderSewingCost = pordersewingService.save(porderSewingCost);
								}
								
								// porders_balance
								if(cumCongDoan != null) {
									POrderBalance porderBalance = new POrderBalance();
									List<POrderBalance> porderBalance_list = porderBalanceService.getByBalanceName_POrder(cumCongDoan, porderid_link);
									List<POrderBalance> porderBalance_list_by_porderid_link = porderBalanceService.getByPorder(porderid_link);
									if(porderBalance_list.size() == 0) {
										porderBalance.setId(null);
										porderBalance.setOrgrootid_link(orgrootid_link);
										porderBalance.setPorderid_link(porderid_link);
										porderBalance.setBalance_name(commonService.getStringValue(row.getCell(ColumnPorderSewingCost.CumCongDoan)).trim());
										porderBalance.setSortvalue(porderBalance_list_by_porderid_link.size());
										porderBalance = porderBalanceService.save(porderBalance);
									}
								}
								
								// porders_balance_process
								POrderBalanceProcess porderBalanceProcess = new POrderBalanceProcess();
								if(cumCongDoan != null) {
									List<POrderBalance> porderBalance_list = porderBalanceService.getByBalanceName_POrder(cumCongDoan, porderid_link);
									POrderBalance porderBalance = new POrderBalance();
									if(porderBalance_list.size() > 0) {
										porderBalance = porderBalance_list.get(0);
										List<POrderBalanceProcess> porderBalanceProcess_list = porderBalanceProcessService.getByPorderSewingcost(porderSewingCost.getId());
										if(porderBalanceProcess_list.size() > 0) { // công đoạn đã có trong 1 cụm công đoạn
											porderBalanceProcess =  porderBalanceProcess_list.get(0);
											porderBalanceProcess.setPorderbalanceid_link(porderBalance.getId());
											porderBalanceProcess.setPordersewingcostid_link(porderSewingCost.getId());
											porderBalanceProcess = porderBalanceProcessService.save(porderBalanceProcess);
										}else { // công đoạn chưa có trong 1 cụm công đoạn
											porderBalanceProcess.setId(null);
											porderBalanceProcess.setOrgrootid_link(orgrootid_link);
											porderBalanceProcess.setPorderbalanceid_link(porderBalance.getId());
											porderBalanceProcess.setPordersewingcostid_link(porderSewingCost.getId());
											porderBalanceProcess = porderBalanceProcessService.save(porderBalanceProcess);
										}
									}
								}else {
									List<POrderBalanceProcess> porderBalanceProcess_list = porderBalanceProcessService.getByPorderSewingcost(porderSewingCost.getId());
									if(porderBalanceProcess_list.size() > 0) { // công đoạn đã có trong 1 cụm công đoạn
										porderBalanceProcess =  porderBalanceProcess_list.get(0);
										porderBalanceProcessService.delete(porderBalanceProcess);
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
	
	// update_copy_productsewingcost_default_request
	@RequestMapping(value = "/copy_productsewingcost_default", method = RequestMethod.POST)
	public ResponseEntity<create_pordersewingcost_response> copy_productsewingcost_default(HttpServletRequest request,
			@RequestBody copy_productsewingcost_default_request entity) {
		create_pordersewingcost_response response = new create_pordersewingcost_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Date date = new Date();
			Long porderid_link = entity.porderid_link;
			Long productid_link = entity.productid_link;
			
			// delete old
			List<POrderWorkingProcess> porderWorkingProcess_list = 
					porderWorkingProcessService.getby_porder(porderid_link);
			List<POrderSewingCost> porderSewingCost_list =
					pordersewingService.getby_porder_and_workingprocess(porderid_link, (long) 0);
			List<POrderBalance> porderBalance_list =
					porderBalanceService.getByPorder(porderid_link);
			
			for(POrderWorkingProcess porderWorkingProcess : porderWorkingProcess_list) {
				porderWorkingProcessService.deleteById(porderWorkingProcess.getId());
			}
			for(POrderSewingCost porderSewingCost : porderSewingCost_list) {
				pordersewingService.deleteById(porderSewingCost.getId());
			}
			for(POrderBalance porderBalance : porderBalance_list) {
				List<POrderBalanceProcess> porderBalanceProcess_list = porderBalance.getPorderBalanceProcesses();
				for(POrderBalanceProcess porderBalanceProcess : porderBalanceProcess_list) {
					porderBalanceProcessService.deleteById(porderBalanceProcess.getId());
				}
				porderBalanceService.deleteById(porderBalance.getId());
			}
			
			// create new
			List<WorkingProcess> workingProcess_list = 
					workingprocessService.getby_product(productid_link);
			List<ProductSewingCost> productSewingCost_list = 
					productSewingCostService.getby_product_and_workingprocess(productid_link, (long)0);
			List<ProductBalance> productBalance_list = 
					productBalanceService.getByProduct(productid_link);
			
			for(WorkingProcess workingProcess : workingProcess_list) {
				POrderWorkingProcess newPOrderWorkingProcess = new POrderWorkingProcess();
				newPOrderWorkingProcess.setId(null);
				newPOrderWorkingProcess.setOrgrootid_link(workingProcess.getOrgrootid_link());
				newPOrderWorkingProcess.setName(workingProcess.getName());
				newPOrderWorkingProcess.setCode(workingProcess.getCode());
				newPOrderWorkingProcess.setProductid_link(workingProcess.getProductid_link());
				newPOrderWorkingProcess.setPorderid_link(porderid_link);
				newPOrderWorkingProcess.setTimespent_standard(workingProcess.getTimespent_standard());
				newPOrderWorkingProcess.setDevicerequiredid_link(workingProcess.getDevicerequiredid_link());
				newPOrderWorkingProcess.setDevicerequired_desc(workingProcess.getDevicerequired_desc());
				newPOrderWorkingProcess.setLaborrequiredid_link(workingProcess.getLaborrequiredid_link());
				newPOrderWorkingProcess.setLaborrequired_desc(workingProcess.getLaborrequired_desc());
				newPOrderWorkingProcess.setTechcomment(workingProcess.getTechcomment());
				newPOrderWorkingProcess.setProcess_type(workingProcess.getProcess_type());
				newPOrderWorkingProcess.setStatus(workingProcess.getStatus());
				newPOrderWorkingProcess.setUsercreatedid_link(user.getId());
				newPOrderWorkingProcess.setTimecreated(date);
				newPOrderWorkingProcess.setIsselected(workingProcess.getIsselected());
				newPOrderWorkingProcess.setLastcost(workingProcess.getLastcost());
				newPOrderWorkingProcess = porderWorkingProcessService.save(newPOrderWorkingProcess);
				
				for(ProductSewingCost productSewingCost : productSewingCost_list) {
					if(productSewingCost.getWorkingprocessid_link().equals(workingProcess.getId())) {
						POrderSewingCost newPOrderSewingCost = new POrderSewingCost();
						newPOrderSewingCost.setId(null);
						newPOrderSewingCost.setOrgrootid_link(productSewingCost.getOrgrootid_link());
						newPOrderSewingCost.setPorderid_link(porderid_link);
						newPOrderSewingCost.setWorkingprocessid_link(newPOrderWorkingProcess.getId());
						newPOrderSewingCost.setCost(productSewingCost.getCost());
						newPOrderSewingCost.setAmount(productSewingCost.getAmount());
						newPOrderSewingCost.setTotalcost(productSewingCost.getTotalcost());
						newPOrderSewingCost.setUsercreatedid_link(user.getId());
						newPOrderSewingCost.setDatecreated(date);
						newPOrderSewingCost.setTimespent_standard(productSewingCost.getTimespent_standard());
						newPOrderSewingCost.setDevicerequiredid_link(productSewingCost.getDevicerequiredid_link());
						newPOrderSewingCost.setLaborrequiredid_link(productSewingCost.getLaborrequiredid_link());
						newPOrderSewingCost.setTechcomment(productSewingCost.getTechcomment());
						newPOrderSewingCost = pordersewingService.save(newPOrderSewingCost);
					}
				}
			}
			
			for(ProductBalance productBalance : productBalance_list) {
				List<ProductBalanceProcess> productBalanceProcess_list = productBalance.getProductBalanceProcesses();
				POrderBalance newPOrderBalance = new POrderBalance();
				newPOrderBalance.setId(null);
				newPOrderBalance.setPorderid_link(porderid_link);
				newPOrderBalance.setOrgrootid_link(productBalance.getOrgrootid_link());
				newPOrderBalance.setBalance_name(productBalance.getBalance_name());
				newPOrderBalance.setSortvalue(productBalance.getSortvalue());
				newPOrderBalance = porderBalanceService.save(newPOrderBalance);
				for(ProductBalanceProcess productBalanceProcess : productBalanceProcess_list) {
					POrderBalanceProcess newPorderBalanceProcess = new POrderBalanceProcess();
					newPorderBalanceProcess.setId(null);
					newPorderBalanceProcess.setOrgrootid_link(productBalanceProcess.getOrgrootid_link());
					newPorderBalanceProcess.setPorderbalanceid_link(newPOrderBalance.getId());
					
					Long productsewingcostid_link = productBalanceProcess.getProductsewingcostid_link();
					ProductSewingCost productSewingCost = productSewingCostService.findOne(productsewingcostid_link);
					WorkingProcess workingProcess = workingprocessService.findOne(productSewingCost.getWorkingprocessid_link());
					String code = workingProcess.getCode();
//					System.out.println("_____________");
//					System.out.println(code);
					List<POrderWorkingProcess> porderWorkingProcess__list = 
							porderWorkingProcessService.getByCode(code, productid_link, porderid_link);
					if(porderWorkingProcess__list.size() > 0) {
//						System.out.println("if 1");
						POrderWorkingProcess porderWorkingProcess = porderWorkingProcess__list.get(0);
						List<POrderSewingCost> porderSewingCost__list = 
								pordersewingService.getby_porder_and_workingprocess(porderid_link, porderWorkingProcess.getId());
						if(porderSewingCost__list.size() > 0) {
//							System.out.println("if 2");
							POrderSewingCost porderSewingCost = porderSewingCost__list.get(0);
							newPorderBalanceProcess.setPordersewingcostid_link(porderSewingCost.getId());
						}
					}
//					System.out.println("_____________");
					newPorderBalanceProcess = porderBalanceProcessService.save(newPorderBalanceProcess);
//					List<POrderSewingCost> porderSewing_list = pordersewingService.f
				}
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<create_pordersewingcost_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<create_pordersewingcost_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/download_temp_pordersewingcost", method = RequestMethod.POST)
	public ResponseEntity<download_temp_pordersewingcost_response> download_temp_pordersewingcost(HttpServletRequest request) {

		download_temp_pordersewingcost_response response = new download_temp_pordersewingcost_response();
		try {
			String FolderPath = "TemplateUpload";

			// Thư mục gốc upload file.
			String uploadRootPath = request.getServletContext().getRealPath(FolderPath);

			String filePath = uploadRootPath + "/" + "Template_PorderSewingCost_New.xlsx";
			Path path = Paths.get(filePath);
			byte[] data = Files.readAllBytes(path);
			response.data = data;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<download_temp_pordersewingcost_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<download_temp_pordersewingcost_response>(response, HttpStatus.OK);
		}
	}
}
