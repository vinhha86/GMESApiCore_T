package vn.gpay.gsmart.core.api.Report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.io.Files;

import vn.gpay.gsmart.core.api.timesheetinout.TimeSheetInOutAPI;
import vn.gpay.gsmart.core.api.timesheetinout.getDailyRequest;
import vn.gpay.gsmart.core.api.timesheetinout.getDailyResponse;

import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.timesheetinout.TimeSheetDaily;
import vn.gpay.gsmart.core.utils.ColumnExcel;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/timesheet_report")
public class TimeSheetReportAPI {
	@Autowired TimeSheetInOutAPI timesheetAPI;
	@Autowired Common commonService;
	@Autowired IOrgService orgService;

	@RequestMapping(value = "/daily", method = RequestMethod.POST)
	public ResponseEntity<porder_report_response> PorderReport(HttpServletRequest request,
			@RequestBody timesheet_daily_request entity) throws IOException {
		porder_report_response response = new porder_report_response();
		String uploadRoot = request.getServletContext().getRealPath("report/Export/TimeSheet");
		File uploadRootDir = new File(uploadRoot);
		// Tạo thư mục gốc upload nếu không tồn tại.
		if (!uploadRootDir.exists()) {
			uploadRootDir.mkdirs();
		}
		Date current_time = new Date();
		File FileExport = new File(uploadRoot + "/Template_timesheet_daily.xlsx");
		File FileCopy = new File(uploadRoot + "/Template_timesheet_daily_" + current_time.getTime() + ".xlsx"); // copy cua
																											// template
																											// de chinh
																											// sua tren
																											// do
		File file = new File(uploadRoot + "/Bangcong_T" + entity.month + "_" + entity.orgid_link + "_" + current_time.getTime() + ".xlsx"); // file de export
		// tao file copy cua template
		Files.copy(FileExport, FileCopy);
		FileInputStream is_filecopy = new FileInputStream(FileCopy);

		XSSFWorkbook workbook = new XSSFWorkbook(is_filecopy);
		XSSFSheet sheet = workbook.getSheetAt(0);


		// tao font de cho vao style

		XSSFFont font_Calibri = workbook.createFont();
		font_Calibri.setFontName("Calibri");
		font_Calibri.setFontHeightInPoints((short) 9.5);
		
		XSSFFont font_Calibri_bold = workbook.createFont();
		font_Calibri_bold.setFontName("Calibri");
		font_Calibri_bold.setFontHeightInPoints((short) 10);
		font_Calibri_bold.setBold(true);
		
		XSSFFont font_Calibri_Italic = workbook.createFont();
		font_Calibri_Italic.setFontName("Calibri");
		font_Calibri_Italic.setFontHeightInPoints((short) 9.5);
		font_Calibri_Italic.setItalic(true);

		// Tao cac style cho cac cell
		XSSFCellStyle cellStyle_row0 = workbook.createCellStyle();
		cellStyle_row0.setAlignment(HorizontalAlignment.CENTER);
		cellStyle_row0.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle_row0.setFont(font_Calibri_bold);
		cellStyle_row0.setFillForegroundColor(IndexedColors.RED.index);
		cellStyle_row0.setFillBackgroundColor(IndexedColors.RED.index);
		cellStyle_row0.setBorderTop(BorderStyle.THIN);
		cellStyle_row0.setBorderBottom(BorderStyle.THIN);
		cellStyle_row0.setBorderLeft(BorderStyle.THIN);
		cellStyle_row0.setBorderRight(BorderStyle.THIN);
		
		XSSFCellStyle cellStyle_row0_borderless = workbook.createCellStyle();
		cellStyle_row0_borderless.setAlignment(HorizontalAlignment.CENTER);
		cellStyle_row0_borderless.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle_row0_borderless.setFont(font_Calibri_bold);
		cellStyle_row0_borderless.setFillForegroundColor(IndexedColors.RED.index);
		cellStyle_row0_borderless.setFillBackgroundColor(IndexedColors.RED.index);

		
		XSSFCellStyle cellStyle_row_inout = workbook.createCellStyle();
		cellStyle_row_inout.setAlignment(HorizontalAlignment.CENTER);
		cellStyle_row_inout.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle_row_inout.setFont(font_Calibri);
		cellStyle_row_inout.setBorderTop(BorderStyle.THIN);
		cellStyle_row_inout.setBorderBottom(BorderStyle.THIN);
		cellStyle_row_inout.setBorderLeft(BorderStyle.THIN);
		cellStyle_row_inout.setBorderRight(BorderStyle.THIN);
		
		XSSFCellStyle cellStyle_row_inout_borderless = workbook.createCellStyle();
		cellStyle_row_inout_borderless.setAlignment(HorizontalAlignment.CENTER);
		cellStyle_row_inout_borderless.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle_row_inout_borderless.setFont(font_Calibri);

		
		XSSFCellStyle cellStyle_row_lunch = workbook.createCellStyle();
		cellStyle_row_lunch.setAlignment(HorizontalAlignment.CENTER);
		cellStyle_row_lunch.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle_row_lunch.setFont(font_Calibri_Italic);
		cellStyle_row_lunch.setBorderTop(BorderStyle.THIN);
		cellStyle_row_lunch.setBorderBottom(BorderStyle.THIN);
		cellStyle_row_lunch.setBorderLeft(BorderStyle.THIN);
		cellStyle_row_lunch.setBorderRight(BorderStyle.THIN);

		XSSFCellStyle cellStyle_row_lunch_borderless = workbook.createCellStyle();
		cellStyle_row_lunch_borderless.setAlignment(HorizontalAlignment.CENTER);
		cellStyle_row_lunch_borderless.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle_row_lunch_borderless.setFont(font_Calibri_Italic);

		try {
			getDailyRequest timesheet_req = new getDailyRequest();
			timesheet_req.month = entity.month;
			timesheet_req.year = entity.year;
			timesheet_req.orgid_link = entity.orgid_link;
			timesheet_req.grantid_link = entity.grantid_link;
			
			ResponseEntity<getDailyResponse> timesheet_daily = timesheetAPI.getDaily(timesheet_req);
			
			//Lay ten phan xuong - to chuyen
			Org org_Tochuyen = orgService.findOne(entity.grantid_link);
			String name_tochuyen = org_Tochuyen.getName();
			String name_phanxuong = org_Tochuyen.getParentname();
			String name_thangtinhcong = entity.month + "/" + entity.year;
			
			//Ghi ten phan xuong
			Row row_1 = sheet.getRow(0);
			Cell cell_A1 = row_1.getCell(ColumnExcel.A);
			cell_A1.setCellValue(name_phanxuong);
			
			
			//Ghi ten to chuyen
			Row row_3 = sheet.getRow(2);
			Cell cell_C3 = row_3.getCell(ColumnExcel.C);
			cell_C3.setCellValue(name_tochuyen);
			
			//Ghi thang cong
			Cell cell_AG3 = row_3.getCell(ColumnExcel.AG);
			cell_AG3.setCellValue(name_thangtinhcong);
			
			if (null != timesheet_daily) {
				int iRowID = 4;
				Integer iSTT = 0;
				Integer iRow = 1;
				List<TimeSheetDaily> timesheet_data = timesheet_daily.getBody().data;
//				System.out.println("rows:" + timesheet_data.size());
				for (TimeSheetDaily oneTSRow:  timesheet_data) {
					Row tsRow = sheet.createRow(iRowID++);
					XSSFCellStyle theCellStyle;
					//STT
					if (oneTSRow.getSortvalue() == 0) {
						theCellStyle = cellStyle_row0;
						Cell cell_STT = tsRow.createCell(ColumnExcel.A);
						iSTT++;
						cell_STT.setCellValue(iSTT.toString());
						cell_STT.setCellStyle(theCellStyle);
						
						//Ma nhan vien
						Cell cell_MaNV = tsRow.createCell(ColumnExcel.B);
						cell_MaNV.setCellValue(oneTSRow.getPersonnel_code());
						cell_MaNV.setCellStyle(theCellStyle);
						
						//Ten nhan vien
						Cell cell_TenNV = tsRow.createCell(ColumnExcel.C);
						cell_TenNV.setCellValue(oneTSRow.getFullname());
						cell_TenNV.setCellStyle(theCellStyle);
						
						//Ngat trang
						if (iSTT % 12 == 0) {
							System.out.println(iSTT);
							sheet.setRowBreak(iRowID-2);
						}
					}  else {
						if (oneTSRow.getSortvalue() > 1 && oneTSRow.getSortvalue() <=3)
							theCellStyle = cellStyle_row_lunch;
						else
							theCellStyle = cellStyle_row_inout;
						
						//3 cot dau de trong
						Cell cell_A = tsRow.createCell(ColumnExcel.A);
						cell_A.setCellValue("");
						cell_A.setCellStyle(theCellStyle);
						Cell cell_B = tsRow.createCell(ColumnExcel.B);
						cell_B.setCellValue("");
						cell_B.setCellStyle(theCellStyle);
						Cell cell_C = tsRow.createCell(ColumnExcel.C);
						cell_C.setCellValue("");
						cell_C.setCellStyle(theCellStyle);
					}
					int iStartCol = ColumnExcel.D;
					for (int i =1; i<=31;i++) {
						Cell cell_Day = tsRow.createCell(iStartCol);
						cell_Day.setCellStyle(theCellStyle);
						switch (i) {
							case 1:
								cell_Day.setCellValue(oneTSRow.getDay1());
								break;
							case 2:
								cell_Day.setCellValue(oneTSRow.getDay2());
								break;
							case 3:
								cell_Day.setCellValue(oneTSRow.getDay3());
								break;
							case 4:
								cell_Day.setCellValue(oneTSRow.getDay4());
								break;
							case 5:
								cell_Day.setCellValue(oneTSRow.getDay5());
								break;
							case 6:
								cell_Day.setCellValue(oneTSRow.getDay6());
								break;
							case 7:
								cell_Day.setCellValue(oneTSRow.getDay7());
								break;
							case 8:
								cell_Day.setCellValue(oneTSRow.getDay8());
								break;
							case 9:
								cell_Day.setCellValue(oneTSRow.getDay9());
								break;
							case 10:
								cell_Day.setCellValue(oneTSRow.getDay10());
								break;
							case 11:
								cell_Day.setCellValue(oneTSRow.getDay11());
								break;
							case 12:
								cell_Day.setCellValue(oneTSRow.getDay12());
								break;
							case 13:
								cell_Day.setCellValue(oneTSRow.getDay13());
								break;
							case 14:
								cell_Day.setCellValue(oneTSRow.getDay14());
								break;
							case 15:
								cell_Day.setCellValue(oneTSRow.getDay15());
								break;
							case 16:
								cell_Day.setCellValue(oneTSRow.getDay16());
								break;
							case 17:
								cell_Day.setCellValue(oneTSRow.getDay17());
								break;
							case 18:
								cell_Day.setCellValue(oneTSRow.getDay18());
								break;
							case 19:
								cell_Day.setCellValue(oneTSRow.getDay19());
								break;
							case 20:
								cell_Day.setCellValue(oneTSRow.getDay20());
								break;
							case 21:
								cell_Day.setCellValue(oneTSRow.getDay21());
								break;
							case 22:
								cell_Day.setCellValue(oneTSRow.getDay22());
								break;
							case 23:
								cell_Day.setCellValue(oneTSRow.getDay23());
								break;
							case 24:
								cell_Day.setCellValue(oneTSRow.getDay24());
								break;
							case 25:
								cell_Day.setCellValue(oneTSRow.getDay25());
								break;
							case 26:
								cell_Day.setCellValue(oneTSRow.getDay26());
								break;
							case 27:
								cell_Day.setCellValue(oneTSRow.getDay27());
								break;
							case 28:
								cell_Day.setCellValue(oneTSRow.getDay28());
								break;
							case 29:
								cell_Day.setCellValue(oneTSRow.getDay29());
								break;
							case 30:
								cell_Day.setCellValue(oneTSRow.getDay30());
								break;
							case 31:
								cell_Day.setCellValue(oneTSRow.getDay31());
								break;
						}

						iStartCol++;
					}
					
					//Set Style cho phan ky ten
					Cell cell_Sign = tsRow.createCell(iStartCol);
					cell_Sign.setCellStyle(theCellStyle);
					
					//Merge cells
					if (iRow > 5 && iRow % 5 == 1) {
						int row_start = iRowID - 6;
						int row_end = iRowID - 2;
						sheet.addMergedRegion(new CellRangeAddress(row_start, row_end, ColumnExcel.A, ColumnExcel.A));
						sheet.addMergedRegion(new CellRangeAddress(row_start, row_end, ColumnExcel.B, ColumnExcel.B));
						sheet.addMergedRegion(new CellRangeAddress(row_start, row_end, ColumnExcel.C, ColumnExcel.C));
						sheet.addMergedRegion(new CellRangeAddress(row_start, row_end, ColumnExcel.AI, ColumnExcel.AI));
					}
					iRow++;
				}
				//Merge cells nguoi cuoi cung
				int row_start = iRowID - 5;
				int row_end = iRowID - 1;
				sheet.addMergedRegion(new CellRangeAddress(row_start, row_end, ColumnExcel.A, ColumnExcel.A));
				sheet.addMergedRegion(new CellRangeAddress(row_start, row_end, ColumnExcel.B, ColumnExcel.B));
				sheet.addMergedRegion(new CellRangeAddress(row_start, row_end, ColumnExcel.C, ColumnExcel.C));
				sheet.addMergedRegion(new CellRangeAddress(row_start, row_end, ColumnExcel.AI, ColumnExcel.AI));
				
				//Dong footer
				iRowID = iRowID+1;
				Row tsRow_f1 = sheet.createRow(iRowID++);
				Cell cell_f_B1 = tsRow_f1.createCell(ColumnExcel.B);
				cell_f_B1.setCellValue("Ghi chú:");
				cell_f_B1.setCellStyle(cellStyle_row0_borderless);
				Cell cell_f_C1 = tsRow_f1.createCell(ColumnExcel.C);
				cell_f_C1.setCellValue("Ô: Nghỉ ốm");
				Cell cell_f_F1 = tsRow_f1.createCell(ColumnExcel.F);
				cell_f_F1.setCellValue("L: Nghỉ lẻ");
				Cell cell_f_I1 = tsRow_f1.createCell(ColumnExcel.I);
				cell_f_I1.setCellValue("H: Học tập, Hội họp");
				Cell cell_f_M1 = tsRow_f1.createCell(ColumnExcel.M);
				cell_f_M1.setCellValue("Ro: Nghỉ không hưởng lương");
				Cell cell_f_AD1 = tsRow_f1.createCell(ColumnExcel.AD);
				cell_f_AD1.setCellValue("00:00 : Tổng thời gian");
				cell_f_AD1.setCellStyle(cellStyle_row0_borderless);
				
				Row tsRow_f2 = sheet.createRow(iRowID++);
				Cell cell_f_C2 = tsRow_f2.createCell(ColumnExcel.C);
				cell_f_C2.setCellValue("CO: Nghỉ con ốm");
				Cell cell_f_F2 = tsRow_f2.createCell(ColumnExcel.F);
				cell_f_F2.setCellValue("CT: Công tác");
				Cell cell_f_I2 = tsRow_f2.createCell(ColumnExcel.I);
				cell_f_I2.setCellValue("R: Nghỉ, Hiếu, Hỉ");
				Cell cell_f_M2 = tsRow_f2.createCell(ColumnExcel.M);
				cell_f_M2.setCellValue("DC: Đình chỉ");
				Cell cell_f_AD2 = tsRow_f2.createCell(ColumnExcel.AD);
				cell_f_AD2.setCellValue("00:00 : Thời gian ăn ca");
				cell_f_AD2.setCellStyle(cellStyle_row_lunch_borderless);
				
				Row tsRow_f3 = sheet.createRow(iRowID++);
				Cell cell_f_C3 = tsRow_f3.createCell(ColumnExcel.C);
				cell_f_C3.setCellValue("CO: Nghỉ con ốm");
				Cell cell_f_F3 = tsRow_f3.createCell(ColumnExcel.F);
				cell_f_F3.setCellValue("CT: Công tác");
				Cell cell_f_I3 = tsRow_f3.createCell(ColumnExcel.I);
				cell_f_I3.setCellValue("R: Nghỉ, Hiếu, Hỉ");
				Cell cell_f_M3 = tsRow_f3.createCell(ColumnExcel.M);
				cell_f_M3.setCellValue("DC: Đình chỉ");
				Cell cell_f_AD3 = tsRow_f3.createCell(ColumnExcel.AD);
				cell_f_AD3.setCellValue("00:00 : Thời gian vào ra");
				cell_f_AD3.setCellStyle(cellStyle_row_inout_borderless);
			}


			// tra file ve dang byte[]
			OutputStream outputstream = new FileOutputStream(file);
			workbook.write(outputstream);
			workbook.close();

			outputstream.close();

			InputStream file_download = new FileInputStream(file);
			response.data = IOUtils.toByteArray(file_download);
			file_download.close();

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		} finally {
			workbook.close();
			FileCopy.delete();
			file.delete();
		}

		return new ResponseEntity<porder_report_response>(response, HttpStatus.OK);
	}
}
