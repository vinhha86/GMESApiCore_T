package vn.gpay.gsmart.core.api.Report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.io.Files;

import vn.gpay.gsmart.core.attributevalue.Attributevalue;
import vn.gpay.gsmart.core.attributevalue.IAttributeValueService;
import vn.gpay.gsmart.core.packingtype.IPackingTypeService;
import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_SKUService;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
import vn.gpay.gsmart.core.porder_grant.POrderGrant_SKU;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ColumnExcel;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/porder_report")
public class POrderReportAPI {
	@Autowired
	IPOrder_Service porderService;
	@Autowired
	Common commonService;
	@Autowired
	IPOrderGrant_Service grantService;
	@Autowired
	IPackingTypeService packingService;
	@Autowired
	IPContract_POService poService;
	@Autowired
	IPOrderGrant_SKUService grantskuService;
	@Autowired
	IAttributeValueService attvalueService;

	@RequestMapping(value = "/porder", method = RequestMethod.POST)
	public ResponseEntity<porder_report_response> PorderReport(HttpServletRequest request,
			@RequestBody porder_report_request entity) throws IOException {
		porder_report_response response = new porder_report_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();
		String uploadRoot = request.getServletContext().getRealPath("report/Export/LenhSanXuat");
		File uploadRootDir = new File(uploadRoot);
		// Tạo thư mục gốc upload nếu không tồn tại.
		if (!uploadRootDir.exists()) {
			uploadRootDir.mkdirs();
		}
		Date current_time = new Date();
		File FileExport = new File(uploadRoot + "/Template_LenhSanXuat.xlsx");
		File FileCopy = new File(uploadRoot + "/Template_LenhSanXuat_" + current_time.getTime() + ".xlsx"); // copy cua
																											// template
																											// de chinh
																											// sua tren
																											// do
		File file = new File(uploadRoot + "/LenhSanXuat_" + current_time.getTime() + ".xlsx"); //  file de export
		// tao file copy cua template
		Files.copy(FileExport, FileCopy);
		FileInputStream is_filecopy = new FileInputStream(FileCopy);

		XSSFWorkbook workbook = new XSSFWorkbook(is_filecopy);
		XSSFSheet sheet = workbook.getSheetAt(0);

		// tao font de cho vao style

		XSSFFont font_Timenewroman = workbook.createFont();
		font_Timenewroman.setFontName("TIMES NEW ROMAN");
		font_Timenewroman.setFontHeightInPoints((short) 11);

		XSSFFont font_Timenewroman_9 = workbook.createFont();
		font_Timenewroman_9.setFontName("TIMES NEW ROMAN");
		font_Timenewroman_9.setFontHeightInPoints((short) 9);

		// Tao cac style cho cac cell
		XSSFCellStyle cellStyle_align_left = workbook.createCellStyle();
		cellStyle_align_left.setAlignment(HorizontalAlignment.LEFT);
		cellStyle_align_left.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle_align_left.setFont(font_Timenewroman);

		XSSFCellStyle cellStyle_align_left_9 = workbook.createCellStyle();
		cellStyle_align_left_9.setAlignment(HorizontalAlignment.LEFT);
		cellStyle_align_left_9.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle_align_left_9.setBorderTop(BorderStyle.THIN);
		cellStyle_align_left_9.setBorderBottom(BorderStyle.THIN);
		cellStyle_align_left_9.setBorderLeft(BorderStyle.THIN);
		cellStyle_align_left_9.setBorderRight(BorderStyle.THIN);
		cellStyle_align_left_9.setFont(font_Timenewroman_9);

		DataFormat format = workbook.createDataFormat();
		XSSFCellStyle cellStyle_align_right = workbook.createCellStyle();
		cellStyle_align_right.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle_align_right.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle_align_right.setBorderTop(BorderStyle.THIN);
		cellStyle_align_right.setBorderBottom(BorderStyle.THIN);
		cellStyle_align_right.setBorderLeft(BorderStyle.THIN);
		cellStyle_align_right.setBorderRight(BorderStyle.THIN);
		cellStyle_align_right.setFont(font_Timenewroman_9);
		cellStyle_align_right.setDataFormat(format.getFormat("#,###"));

		XSSFCellStyle cellStyle_align_right_float = workbook.createCellStyle();
		cellStyle_align_right_float.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle_align_right_float.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle_align_right_float.setBorderTop(BorderStyle.THIN);
		cellStyle_align_right_float.setBorderBottom(BorderStyle.THIN);
		cellStyle_align_right_float.setBorderLeft(BorderStyle.THIN);
		cellStyle_align_right_float.setBorderRight(BorderStyle.THIN);
		cellStyle_align_right_float.setFont(font_Timenewroman_9);
		cellStyle_align_right_float.setDataFormat(format.getFormat("#.00"));

		XSSFCellStyle cellStyle_align_left_sizeheader = workbook.createCellStyle();
		cellStyle_align_left_sizeheader.setAlignment(HorizontalAlignment.CENTER);
		cellStyle_align_left_sizeheader.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle_align_left_sizeheader.setBorderTop(BorderStyle.THIN);
		cellStyle_align_left_sizeheader.setBorderBottom(BorderStyle.THIN);
		cellStyle_align_left_sizeheader.setBorderLeft(BorderStyle.THIN);
		cellStyle_align_left_sizeheader.setBorderRight(BorderStyle.THIN);
		cellStyle_align_left_sizeheader.setFont(font_Timenewroman_9);
		cellStyle_align_left_sizeheader.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
		cellStyle_align_left_sizeheader.setFillBackgroundColor(IndexedColors.LIGHT_YELLOW.index);

		try {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat dateFormat_short = new SimpleDateFormat("dd/MM/yy");
			POrder porder = porderService.findOne(entity.porderid_link);

			// Ghi thong tin chung
			Row row_2 = sheet.getRow(1);

			String masanpham = porder.getProductcode();
			Cell cell_B2 = row_2.getCell(ColumnExcel.B);
			cell_B2.setCellValue(masanpham);
			cell_B2.setCellStyle(cellStyle_align_left);

			String ngaygiao = dateFormat.format(porder.getGolivedate());
			Cell cell_G2 = row_2.getCell(ColumnExcel.G);
			cell_G2.setCellValue(ngaygiao);
			cell_G2.setCellStyle(cellStyle_align_left);

			String ngayduyet = dateFormat.format(porder.getOrderdate());
			Cell cell_L2 = row_2.getCell(ColumnExcel.L);
			cell_L2.setCellValue(ngayduyet);
			cell_L2.setCellStyle(cellStyle_align_left);

			Row row_3 = sheet.getRow(2);

			String khachhang = porder.getVendorname();
			Cell cell_B3 = row_3.getCell(ColumnExcel.B);
			cell_B3.setCellValue(khachhang);
			cell_B3.setCellStyle(cellStyle_align_left);

			String mer = porder.getMerName();
			Cell cell_G3 = row_3.getCell(ColumnExcel.G);
			cell_G3.setCellValue(mer);
			cell_G3.setCellStyle(cellStyle_align_left);

			String xuongsx = porder.getGranttoorgcode();
			Cell cell_L3 = row_3.getCell(ColumnExcel.L);
			cell_L3.setCellValue(xuongsx);
			cell_L3.setCellStyle(cellStyle_align_left);

			// add anh san pham
			try {
				String image_product = porder.getImageurl();
				String FolderPath = commonService.getFolderPath(10);
				String uploadRootPath = request.getServletContext().getRealPath("");
				File uploadRootDir_img = new File(uploadRootPath);
				InputStream is_img = new FileInputStream(
						uploadRootDir_img.getParent() + "/" + FolderPath + "/" + image_product);
				byte[] img = IOUtils.toByteArray(is_img);

				int pictureIdx = workbook.addPicture(img, Workbook.PICTURE_TYPE_JPEG);
				is_img.close();

				XSSFDrawing drawing = sheet.createDrawingPatriarch();

				// add a picture shape
				XSSFClientAnchor anchor = new XSSFClientAnchor();

				// set top-left corner of the picture,
				// subsequent call of Picture#resize() will operate relative to it
				anchor.setCol1(ColumnExcel.R);
				anchor.setRow1(0);
				anchor.setCol2(ColumnExcel.R + 2);
				anchor.setRow2(3);
				drawing.createPicture(anchor, pictureIdx);
			} catch (Exception e) {
				// TODO: handle exception
			}

			// Ghi danh sach

			Long porderid_link = entity.porderid_link;
			// Sinh cac co vao cac cot
			Map<String, Integer> map_size = new HashMap<String, Integer>();
			List<String> list_co = grantskuService.getlistco(porderid_link);
			int colsize = ColumnExcel.H;
			for (String co : list_co) {
				map_size.put(co, colsize);
				Row row_co = sheet.getRow(3);
				Cell cellstyle = row_co.getCell(ColumnExcel.M);
				CellStyle style = workbook.createCellStyle();
				style.cloneStyleFrom(cellstyle.getCellStyle());

				Cell cell_size = row_co.getCell(colsize);
				cell_size.setCellValue(co);
				cell_size.setCellStyle(style);

				colsize++;
			}

			// an nhung cot khong co co
			if (colsize < ColumnExcel.N)
				colsize = ColumnExcel.N;
			for (int i = colsize; i <= ColumnExcel.Q; i++) {
				sheet.setColumnWidth(i, 0 * 256);
			}

			// sinh cac dong po
			int rowNum = 5;
			// Lay cac PcontractPO da phan ve cho cac to
			List<PContract_PO> list_po = poService.getby_porder(porderid_link);
			for (PContract_PO po : list_po) {
				// Lay ds grant dang duoc nhan sx pcontract_po
				Long pcontract_poid_link = po.getId();
				List<POrderGrant> list_grant = grantService.getbyporder_andpo(porderid_link, pcontract_poid_link);
				for (POrderGrant grant : list_grant) {

					// Lay danh sach mau trong so luong duoc giao cho to
					Long pordergrantid_link = grant.getId();
					List<String> list_mau = grantskuService.getlistmau_by_grant(pordergrantid_link);

					for (String mau : list_mau) {
						Row row_detail = sheet.createRow(rowNum);
						Cell cell_po = row_detail.createCell(ColumnExcel.A);
						cell_po.setCellValue(po.getPo_buyer());
						cell_po.setCellStyle(cellStyle_align_left_9);

						Cell cell_shipdate = row_detail.createCell(ColumnExcel.B);
						String s_shipdate = dateFormat_short.format(po.getShipdate());
						cell_shipdate.setCellValue(s_shipdate);
						cell_shipdate.setCellStyle(cellStyle_align_left_9);

						Cell cell_shipmode = row_detail.createCell(ColumnExcel.C);
						cell_shipmode.setCellValue(po.getShipMode());
						cell_shipmode.setCellStyle(cellStyle_align_left_9);

						Cell cell_xuongto = row_detail.createCell(ColumnExcel.D);
						cell_xuongto.setCellValue(grant.getXuongTo());
						cell_xuongto.setCellStyle(cellStyle_align_left_9);

						Cell cell_cangden = row_detail.createCell(ColumnExcel.E);
						cell_cangden.setCellValue(po.getPortTo());
						cell_cangden.setCellStyle(cellStyle_align_left_9);

						Cell cell_ptdg = row_detail.createCell(ColumnExcel.F);
						String s_packingid = po.getPackingnotice();
						s_packingid = s_packingid.replace(";", ",");
						String s_packingname = packingService.getby_listid(s_packingid, orgrootid_link);
						cell_ptdg.setCellValue(s_packingname);
						cell_ptdg.setCellStyle(cellStyle_align_left_9);

						Cell cell_mau = row_detail.createCell(ColumnExcel.G);
						cell_mau.setCellValue(mau);
						cell_mau.setCellStyle(cellStyle_align_left_9);

						// Lay danh sach co theo mau
						long colorid_link = 0;
						List<Attributevalue> list_id_mau = attvalueService.getByValue(mau, (long) 4);
						if (list_id_mau.size() > 0)
							colorid_link = list_id_mau.get(0).getId();
						List<POrderGrant_SKU> list_sl_co = grantskuService.getlistco_by_grant_andmau(pordergrantid_link,
								colorid_link);
						int sum = 0;
						float sum_ave = 0;
						for (POrderGrant_SKU sl_co : list_sl_co) {
							int col_size = map_size.get(sl_co.getCoSanPham());
							Cell cell_size = row_detail.createCell(col_size);
							cell_size.setCellValue(sl_co.getGrantamount());
							sum += sl_co.getGrantamount();

							sum_ave += (col_size - ColumnExcel.G) * sl_co.getGrantamount();
						}

						Cell cell_sum = row_detail.createCell(ColumnExcel.R);
						cell_sum.setCellValue(sum);

						float ave = 0;
						float f_sum = (float) sum;
						if (sum > 0) {
							ave = sum_ave / f_sum;
						}

						Cell cell_ave = row_detail.createCell(ColumnExcel.S);
						cell_ave.setCellValue(ave);
						cell_ave.setCellStyle(cellStyle_align_right_float);

						// format lai cac cot so va cot tong
						for (int i = ColumnExcel.H; i < ColumnExcel.S; i++) {
							Cell cell = null;
							cell = row_detail.getCell(i);
							if (cell == null)
								cell = row_detail.createCell(i);

							cell.setCellStyle(cellStyle_align_right);
						}

						// xuong row tiep theo
						rowNum++;
					}
				}
			}

			// them dong tong cong duoi cung
			Row row_tong = sheet.createRow(rowNum);

			for (int i = ColumnExcel.A; i <= ColumnExcel.G; i++) {
				Cell cell = row_tong.createCell(i);
				cell.setCellStyle(cellStyle_align_left_9);
			}
			Cell cell_tong = row_tong.getCell(ColumnExcel.B);
			cell_tong.setCellValue("Tổng");

			for (int i = ColumnExcel.H; i < ColumnExcel.S; i++) {
				String strFormula = "";
				switch (i) {
				case 7:
					strFormula = String.format("SUM(%s6:%s%d)", "H", "H", rowNum);
					break;
				case 8:
					strFormula = String.format("SUM(%s6:%s%d)", "I", "I", rowNum);
					break;
				case 9:
					strFormula = String.format("SUM(%s6:%s%d)", "J", "J", rowNum);
					break;
				case 10:
					strFormula = String.format("SUM(%s6:%s%d)", "K", "K", rowNum);
					break;
				case 11:
					strFormula = String.format("SUM(%s6:%s%d)", "L", "L", rowNum);
					break;
				case 12:
					strFormula = String.format("SUM(%s6:%s%d)", "M", "M", rowNum);
					break;
				case 13:
					strFormula = String.format("SUM(%s6:%s%d)", "N", "N", rowNum);
					break;
				case 14:
					strFormula = String.format("SUM(%s6:%s%d)", "O", "O", rowNum);
					break;
				case 15:
					strFormula = String.format("SUM(%s6:%s%d)", "P", "P", rowNum);
					break;
				case 16:
					strFormula = String.format("SUM(%s6:%s%d)", "Q", "Q", rowNum);
					break;
				case 17:
					strFormula = String.format("SUM(%s6:%s%d)", "R", "R", rowNum);
					break;

				default:
					break;
				}

				Cell cell_tong_size = row_tong.createCell(i);
//				cell_tong_size.setCellType(CellType.FORMULA);
				cell_tong_size.setCellFormula(strFormula);
				cell_tong_size.setCellStyle(cellStyle_align_right);

			}

			Cell cell_end = row_tong.createCell(ColumnExcel.S);
			cell_end.setCellValue("");
			cell_end.setCellStyle(cellStyle_align_left_9);

			// tra file ve dang byte[]
			OutputStream outputstream = new FileOutputStream(file);
			workbook.write(outputstream);
			workbook.close();

			outputstream.close();

			InputStream isimg = new FileInputStream(file);
			response.data = IOUtils.toByteArray(isimg);
			isimg.close();

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
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
