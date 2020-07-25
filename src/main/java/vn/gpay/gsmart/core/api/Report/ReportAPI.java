package vn.gpay.gsmart.core.api.Report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
//import org.apache.poi.xssf.usermodel.XSSFPicture;
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

import vn.gpay.gsmart.core.pcontractproduct.PContractProduct;
import vn.gpay.gsmart.core.pcontractproduct.PContractProductService;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/report")
public class ReportAPI {
	@Autowired PContractProductService pcontractproductService;
	@Autowired Common commonService;
	
	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public ResponseEntity<report_test_response> Product_GetOne(HttpServletRequest request, @RequestBody report_test_request entity) throws IOException, InvalidFormatException {
		report_test_response response = new report_test_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();
		long productid_link = 0;
		long pcontractid_link = entity.pcontractid_link;
		
//		String FILE_NAME = request.getServletContext().getRealPath("report/Template/MyFirstExcel.xlsx"); // đường dẫn file template
		
		String uploadRoot = request.getServletContext().getRealPath("report/Export");
		File uploadRootDir = new File(uploadRoot);
		// Tạo thư mục gốc upload nếu không tồn tại.
		if (!uploadRootDir.exists()) {
			uploadRootDir.mkdirs();
		}
		
		
//		File tempFile = new File(FILE_NAME);
		File FileExport = new File(uploadRoot+"\\Test.xlsx");
//		commonService.copyFile(tempFile, FileExport);
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		
		XSSFSheet sheet = workbook.createSheet("Sheet 1");
		List<PContractProduct> listproduct = pcontractproductService.get_by_product_and_pcontract
				(orgrootid_link, productid_link, pcontractid_link);
		sheet.setColumnWidth(0, 30*256);
		sheet.setColumnWidth(1, 30*256);
		sheet.setColumnWidth(2, 10*256);
		
		int rowNum = 0;
		
		Row row1 = sheet.createRow(rowNum);
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum , rowNum  , 0, 2));
		Cell cell = row1.createCell(0);
		cell.setCellValue("Danh sách sản phẩm");
		
		CellStyle cellStyle_aligncenter = workbook.createCellStyle();
		cellStyle_aligncenter.setAlignment(HorizontalAlignment.CENTER);
		cellStyle_aligncenter.setVerticalAlignment(VerticalAlignment.CENTER);
		
		cell.setCellStyle(cellStyle_aligncenter);
		
		rowNum++;
		Row row2 = sheet.createRow(rowNum);
		Cell cell2_1 = row2.createCell(0);
		cell2_1.setCellValue("Tên sản phẩm");
		cell2_1.setCellStyle(cellStyle_aligncenter);
		
		Cell cell2_2 = row2.createCell(1);
		cell2_2.setCellValue("Mã sản phẩm");
		cell2_2.setCellStyle(cellStyle_aligncenter);
		
		Cell cell2_3 = row2.createCell(2);
		cell2_3.setCellValue("Ảnh");
		cell2_3.setCellStyle(cellStyle_aligncenter);
		
		for(PContractProduct product : listproduct) {
			rowNum++;
			Row row = sheet.createRow(rowNum);
			row.setHeight((short)(40*20));
			int colNum = 0;			

			CellStyle cellStyle_alignleft = workbook.createCellStyle();
			cellStyle_alignleft.setAlignment(HorizontalAlignment.LEFT);
			cellStyle_alignleft.setVerticalAlignment(VerticalAlignment.CENTER);
			
			Cell cell1 = row.createCell(colNum++);
			cell1.setCellValue(product.getProductName());
			cell1.setCellStyle(cellStyle_alignleft);
			
			Cell cell2 = row.createCell(colNum++);
			cell2.setCellValue(product.getProductCode());
			cell2.setCellStyle(cellStyle_alignleft);
			
			if(product.getImgurl1() != null) {
				String FolderPath = commonService.getFolderPath(product.getProducttypeid_link());
				String uploadRootPath = request.getServletContext().getRealPath(FolderPath);
				
				String filePath = uploadRootPath+"\\"+ product.getImgurl1();
				InputStream isimg = new FileInputStream(filePath);
				byte[] img = IOUtils.toByteArray(isimg);
				int pictureIdx = workbook.addPicture(img, Workbook.PICTURE_TYPE_JPEG);
				isimg.close();
				
				XSSFDrawing  drawing = sheet.createDrawingPatriarch();
				
				// add a picture shape
				XSSFClientAnchor  anchor = new XSSFClientAnchor();
				
				// set top-left corner of the picture,
				// subsequent call of Picture#resize() will operate relative to it
				anchor.setCol1(2);
				anchor.setRow1(rowNum);
				anchor.setCol2(3);
				anchor.setRow2(rowNum+1);
				drawing.createPicture(anchor, pictureIdx);

			}
		}
		
		try {
			OutputStream outputstream = new FileOutputStream(FileExport);
			workbook.write(outputstream);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		} 
		finally {
			workbook.close();
		}

		return new ResponseEntity<report_test_response>(response, HttpStatus.OK);
	}
}
