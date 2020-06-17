package vn.gpay.gsmart.core.api.Report;

import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
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
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/report")
public class ReportAPI {
	@Autowired PContractProductService pcontractproductService;
	
	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public ResponseEntity<report_test_response> Product_GetOne(HttpServletRequest request, @RequestBody report_test_request entity) {
		report_test_response response = new report_test_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long productid_link = 0;
			long pcontractid_link = entity.pcontractid_link;
			
			String FILE_NAME = "report/MyFirstExcel.xlsx"; // đường dẫn file template
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Sheet 1");
			List<PContractProduct> listproduct = pcontractproductService.get_by_product_and_pcontract
					(orgrootid_link, productid_link, pcontractid_link);
			
			int rowNum = 0;
			
			Row row1 = sheet.createRow(rowNum);
			
			sheet.addMergedRegion(new CellRangeAddress(rowNum , rowNum  , 0, 3));
			Cell cell = row1.createCell(0);
			cell.setCellValue("Danh sách sản phẩm");
			CellStyle cellStyle = cell.getCellStyle();
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
			
			rowNum++;
			Row row2 = sheet.createRow(rowNum);
			Cell cell2_1 = row2.createCell(0);
			cell2_1.setCellValue("Tên sản phẩm");
			Cell cell2_2 = row2.createCell(1);
			cell2_2.setCellValue("Mã sản phẩm");
			Cell cell2_3 = row2.createCell(2);
			cell2_3.setCellValue("Ảnh");
			
			for(PContractProduct product : listproduct) {
				rowNum++;
				Row row = sheet.createRow(rowNum);
				int colNum = 0;
				Cell cell1 = row.createCell(colNum++);
				cell1.setCellValue(product.getProductName());
				
				Cell cell2 = row.createCell(colNum++);
				cell2.setCellValue(product.getProductCode());
				
				Cell cell3 = row.createCell(colNum++);
				cell3.setCellValue(product.getProductName());
			}
			String FolderPath = request.getServletContext().getRealPath(FILE_NAME);
			FileOutputStream outputStream = new FileOutputStream(FolderPath);
            workbook.write(outputStream);
            workbook.close();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<report_test_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<report_test_response>(response, HttpStatus.OK);
		}
	}
}
