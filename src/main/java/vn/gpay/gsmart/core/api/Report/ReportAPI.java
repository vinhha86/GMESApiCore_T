package vn.gpay.gsmart.core.api.Report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
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

import vn.gpay.gsmart.core.packingtype.IPackingTypeService;
import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.pcontract_price.IPContract_Price_DService;
import vn.gpay.gsmart.core.pcontract_price.IPContract_Price_Service;
import vn.gpay.gsmart.core.pcontract_price.PContract_Price;
import vn.gpay.gsmart.core.pcontract_price.PContract_Price_D;
import vn.gpay.gsmart.core.pcontractproduct.PContractProduct;
import vn.gpay.gsmart.core.pcontractproduct.PContractProductService;
import vn.gpay.gsmart.core.pcontractproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.product.IProductService;
import vn.gpay.gsmart.core.product.Product;
import vn.gpay.gsmart.core.productpairing.IProductPairingService;
import vn.gpay.gsmart.core.productpairing.ProductPairing;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.AtributeFixValues;
import vn.gpay.gsmart.core.utils.ColumnExcel;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/report")
public class ReportAPI {
	@Autowired PContractProductService pcontractproductService;
	@Autowired Common commonService;
	@Autowired IPContract_POService poService;
	@Autowired IProductPairingService pairService;
	@Autowired IProductService productService;
	@Autowired IPContract_Price_Service priceService;
	@Autowired IPContract_Price_DService fobpriceService;
	@Autowired IPackingTypeService packingService;
	@Autowired IPContractProductSKUService ppskuService;
	
	@RequestMapping(value = "/quatation", method = RequestMethod.POST)
	public ResponseEntity<report_quotation_response> Quotation(HttpServletRequest request, @RequestBody report_quotation_request entity) throws IOException {
		report_quotation_response response = new report_quotation_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();		
		Date curent = new Date();
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
		String s_current = dateFormat.format(curent);  
		//Tao hashmap de sinh cac cot trong report
		Map<String, Integer> size_set_name = new HashMap<String, Integer>();
		int idx = 0;
		size_set_name.put("DateOffer", idx++);
		size_set_name.put("Buyer", idx++);
		size_set_name.put("Vendor", idx++);
		size_set_name.put("PO", idx++);
		size_set_name.put("Style", idx++);
		size_set_name.put("Detail", idx++);
		size_set_name.put("Description", idx++);
		
		size_set_name.put("Picture", idx++);
		
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		for(long pcontractpoid_link : entity.listidpo) {
			PContract_PO po = poService.findOne(pcontractpoid_link);
			long pcontractid_link = po.getPcontractid_link();
			
			List<ProductPairing> list_product = new ArrayList<ProductPairing>();
			//Neu PO la bo thi lay tung san pham
			Product product = productService.findOne(po.getProductid_link());
			if(product.getProducttypeid_link() == 10) {
				Map<String, String> map = new HashMap<String, String>();
				
				map.put("DateOffer", s_current);
				map.put("Buyer", po.getBuyerName());
				map.put("Vendor", po.getVendorName());
				map.put("PO", po.getPo_buyer());
				map.put("Style", product.getBuyercode());
				map.put("Detail", product.getBuyercode());
				map.put("Description", product.getDescription());
				map.put("Picture", product.getImgurl1());
				map.put("Packing", packingService.getNameby_listid(po.getPackingnotice(), orgrootid_link));
				
				List<PContract_Price> list_price = po.getPcontract_price();
				Comparator<PContract_Price> compareBySortValue = (PContract_Price a1, PContract_Price a2) -> a1.getSortvalue().compareTo( a2.getSortvalue());
				Collections.sort(list_price, compareBySortValue);
				
				for(PContract_Price price : list_price) {
					map.put(price.getSizesetname(), price.getTotalprice()+" ");
					
					//kiem tra dai co co trong hashmap sinh cot chua thi them vao
					if(!size_set_name.containsKey(price.getSizesetname())) {
						size_set_name.put(price.getSizesetname(), idx++);
					}
				}
				
				map.put("Quantity", commonService.FormatNumber(po.getPo_quantity().intValue()));
				map.put("Shipdate", dateFormat.format(po.getShipdate()));
				map.put("Material", dateFormat.format(po.getMatdate()));
				
				if(!size_set_name.containsKey("Quantity")) {
					size_set_name.put("Quantity", idx++);
				}
				if(!size_set_name.containsKey("Shipdate")) {
					size_set_name.put("Shipdate", idx++);
				}
				if(!size_set_name.containsKey("Material")) {
					size_set_name.put("Material", idx++);
				}
				
				//fob price
				List<PContract_Price_D> list_fob_price = fobpriceService.getbypo_product(po.getId(), po.getProductid_link());
				if(list_fob_price.size() > 0)
					list_fob_price.removeIf(c->!c.getIsfob());
				for(PContract_Price_D fob_price : list_fob_price) {
					if(map.get(fob_price.getFobprice_name()) != null) {
						Float price_old = Float.parseFloat(map.get(fob_price.getFobprice_name()));
						map.put(fob_price.getFobprice_name(), (fob_price.getPrice() +price_old) +" ");
					}
					else {
						map.put(fob_price.getFobprice_name(), fob_price.getPrice() +" ");
					}
					
					
					//kiem tra gia co trong hashmap sinh cot chua thi them vao
					if(!size_set_name.containsKey(fob_price.getFobprice_name())) {
						size_set_name.put(fob_price.getFobprice_name(), idx++);
					}
				}
				
				if(!size_set_name.containsKey("Packing")) {
					size_set_name.put("Packing", idx++);
				}
				
				if(!size_set_name.containsKey("Note")) {
					size_set_name.put("Note", idx++);
				}
								
				list.add(map);
			}
			else {
				list_product = pairService.getproduct_pairing_detail_bycontract(orgrootid_link, pcontractid_link, po.getProductid_link());
				for(ProductPairing pair : list_product) {
					
					Map<String, String> map = new HashMap<String, String>();
					map.put("DateOffer", s_current);
					map.put("Buyer", po.getBuyerName());
					map.put("Vendor", po.getVendorName());
					map.put("PO", po.getPo_buyer());
					map.put("Style", pair.getSetCode());
					map.put("Detail", pair.getProductBuyerCode());
					map.put("Description", product.getDescription());
					map.put("Picture", pair.getImgurl1());
					map.put("Packing", ""+packingService.getNameby_listid(po.getPackingnotice(), orgrootid_link));
					
					//Lay danh sach dai co theo san pham con
					List<PContract_Price> list_price = priceService.getPrice_by_product(pcontractpoid_link, pair.getProductid_link());
					Comparator<PContract_Price> compareBySortValue = (PContract_Price a1, PContract_Price a2) -> a1.getSortvalue().compareTo( a2.getSortvalue());
					Collections.sort(list_price, compareBySortValue);
					
					for(PContract_Price price : list_price) {
						map.put(price.getSizesetname(), price.getTotalprice()+" ");
						
						//kiem tra dai co co trong hashmap sinh cot chua thi them vao
						if(!size_set_name.containsKey(price.getSizesetname())) {
							size_set_name.put(price.getSizesetname(), idx++);
						}
					}
					
					map.put("Quantity", commonService.FormatNumber(po.getPo_quantity().intValue()));
					map.put("Shipdate", dateFormat.format(po.getShipdate()));
					map.put("Material", dateFormat.format(po.getMatdate()));
					
					if(!size_set_name.containsKey("Quantity")) {
						size_set_name.put("Quantity", idx++);
					}
					if(!size_set_name.containsKey("Shipdate")) {
						size_set_name.put("Shipdate", idx++);
					}
					if(!size_set_name.containsKey("Material")) {
						size_set_name.put("Material", idx++);
					}
					
					//fob price
					List<PContract_Price_D> list_fob_price = fobpriceService.getbypo_product(po.getId(), pair.getProductid_link());
					if(list_fob_price.size() > 0)
						list_fob_price.removeIf(c->!c.getIsfob());
					for(PContract_Price_D fob_price : list_fob_price) {
						if(map.get(fob_price.getFobprice_name()) != null) {
							Float price_old = Float.parseFloat(map.get(fob_price.getFobprice_name()));
							map.put(fob_price.getFobprice_name(), (fob_price.getPrice() +price_old) +" ");
						}
						else {
							map.put(fob_price.getFobprice_name(), fob_price.getPrice() +" ");
						}
						
						
						//kiem tra gia co trong hashmap sinh cot chua thi them vao
						if(!size_set_name.containsKey(fob_price.getFobprice_name())) {
							size_set_name.put(fob_price.getFobprice_name(), idx++);
						}
					}
					
					if(!size_set_name.containsKey("Packing")) {
						size_set_name.put("Packing", idx++);
					}
					
					if(!size_set_name.containsKey("Note")) {
						size_set_name.put("Note", idx++);
					}
									
					list.add(map);
				}
			}
		}
		
		// Ghi ra File
		
		String uploadRoot = request.getServletContext().getRealPath("report/Export/Quotation");
		File uploadRootDir = new File(uploadRoot);
		// Tạo thư mục gốc upload nếu không tồn tại.
		if (!uploadRootDir.exists()) {
			uploadRootDir.mkdirs();
		}
		
		
		File FileExport = new File(uploadRoot+"/Quotation.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook();
		
		XSSFSheet sheet = workbook.createSheet("Sheet 1");
		
		XSSFFont font= workbook.createFont();
		font.setFontHeightInPoints((short)12);
		font.setBold(true);
		
		DataFormat format = workbook.createDataFormat();
		
		XSSFCellStyle cellStyle_aligncenter_fontBold = workbook.createCellStyle();
		cellStyle_aligncenter_fontBold.setAlignment(HorizontalAlignment.CENTER);
		cellStyle_aligncenter_fontBold.setVerticalAlignment(VerticalAlignment.CENTER);
//		cellStyle_aligncenter_fontBold.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
//		cellStyle_aligncenter_fontBold.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle_aligncenter_fontBold.setBorderTop(BorderStyle.THIN);
		cellStyle_aligncenter_fontBold.setBorderBottom(BorderStyle.THIN);
		cellStyle_aligncenter_fontBold.setBorderLeft(BorderStyle.THIN);
		cellStyle_aligncenter_fontBold.setBorderRight(BorderStyle.THIN);
		cellStyle_aligncenter_fontBold.setFont(font);
		
		XSSFCellStyle cellStyle_aligncenter = workbook.createCellStyle();
		cellStyle_aligncenter.setAlignment(HorizontalAlignment.CENTER);
		cellStyle_aligncenter.setVerticalAlignment(VerticalAlignment.CENTER);
//		cellStyle_aligncenter.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
//		cellStyle_aligncenter.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle_aligncenter.setBorderTop(BorderStyle.THIN);
		cellStyle_aligncenter.setBorderBottom(BorderStyle.THIN);
		cellStyle_aligncenter.setBorderLeft(BorderStyle.THIN);
		cellStyle_aligncenter.setBorderRight(BorderStyle.THIN);
		
		XSSFCellStyle cellStyle_align_right = workbook.createCellStyle();
		cellStyle_align_right.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle_align_right.setVerticalAlignment(VerticalAlignment.CENTER);
//		cellStyle_align_right.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
//		cellStyle_align_right.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle_align_right.setBorderTop(BorderStyle.THIN);
		cellStyle_align_right.setBorderBottom(BorderStyle.THIN);
		cellStyle_align_right.setBorderLeft(BorderStyle.THIN);
		cellStyle_align_right.setBorderRight(BorderStyle.THIN);
		cellStyle_align_right.setDataFormat(format.getFormat("#,###"));
		
		XSSFCellStyle cellStyle_align_right_currency = workbook.createCellStyle();
		cellStyle_align_right_currency.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle_align_right_currency.setVerticalAlignment(VerticalAlignment.CENTER);
//		cellStyle_align_right.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
//		cellStyle_align_right.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle_align_right_currency.setBorderTop(BorderStyle.THIN);
		cellStyle_align_right_currency.setBorderBottom(BorderStyle.THIN);
		cellStyle_align_right_currency.setBorderLeft(BorderStyle.THIN);
		cellStyle_align_right_currency.setBorderRight(BorderStyle.THIN);
		cellStyle_align_right_currency.setDataFormat(format.getFormat("_($* #,##0.00_);_($* (#,##0.00);_($* \"-\"??_);_(@_)"));
		
		XSSFCellStyle cellStyle_align_left = workbook.createCellStyle();
		cellStyle_align_left.setAlignment(HorizontalAlignment.LEFT);
		cellStyle_align_left.setVerticalAlignment(VerticalAlignment.CENTER);
//		cellStyle_align_left.setFillBackgroundColor(HSSFColor.SKY_BLUE.index);
//		cellStyle_align_left.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle_align_left.setBorderTop(BorderStyle.THIN);
		cellStyle_align_left.setBorderBottom(BorderStyle.THIN);
		cellStyle_align_left.setBorderLeft(BorderStyle.THIN);
		cellStyle_align_left.setBorderRight(BorderStyle.THIN);
		
		CellStyle cellStyle_wraptext = workbook.createCellStyle();
		cellStyle_wraptext.setAlignment(HorizontalAlignment.LEFT);
		cellStyle_wraptext.setVerticalAlignment(VerticalAlignment.TOP);
		cellStyle_wraptext.setWrapText(true);
		cellStyle_wraptext.setBorderTop(BorderStyle.THIN);
		cellStyle_wraptext.setBorderBottom(BorderStyle.THIN);
		cellStyle_wraptext.setBorderLeft(BorderStyle.THIN);
		cellStyle_wraptext.setBorderRight(BorderStyle.THIN);
//		
//		CellStyle cellStyle_wraptext_left = workbook.createCellStyle();
//		cellStyle_wraptext_left.setAlignment(HorizontalAlignment.LEFT);
//		cellStyle_wraptext_left.setVerticalAlignment(VerticalAlignment.CENTER);
//		cellStyle_wraptext_left.setWrapText(true);
//		cellStyle_wraptext_left.setFillBackgroundColor(HSSFColor.SKY_BLUE.index);
//		cellStyle_wraptext_left.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		int rowNum = 0, start = 0, end = 0;
		//sinh header
		Row row_header = sheet.createRow(rowNum);
		for (String key : size_set_name.keySet()) {
			int col_idx = size_set_name.get(key);
			
			sheet.setColumnWidth(col_idx, 13*256);
			
			Cell cell = row_header.createCell(col_idx);
			cell.setCellValue(key);
			cell.setCellStyle(cellStyle_aligncenter_fontBold);
	    }
		
		//sinh du lieu
		Row row_old = null;
		for(Map<String, String> map : list) {
			rowNum++;
			Row row = sheet.createRow(rowNum);
			
			row.setHeight((short)(80*20));
			
			if(row_old!= null) {
				String old_value = row_old.getCell(4).getStringCellValue();
				String new_val = map.get("Style");
				if(old_value == new_val) {
					end = rowNum;
					if(rowNum == list.size() )
						sheet.addMergedRegion(new CellRangeAddress(start , end  , 4, 4));
				}
				else {
					if(start < end)
						sheet.addMergedRegion(new CellRangeAddress(start , end, 4, 4));
					start = rowNum;
					end = rowNum;
				}
			}
			else {
				start = rowNum;
				end = rowNum;
			}
			
			
			for (String key : size_set_name.keySet()) {
				int col_idx = size_set_name.get(key);
								
				Cell cell = row.createCell(col_idx);
				if(key != "Picture") {
					if(key == "Shipdate" || key == "Material") {
						cell.setCellValue(map.get(key));
						cell.setCellStyle(cellStyle_aligncenter);
					}
					else if (key == "Description") {
						cell.setCellValue(map.get(key));
						cell.setCellStyle(cellStyle_wraptext);
					}
					else if (key == "Detail" || key == "DateOffer" ||
							key == "Buyer" || key == "Vendor" || key == "PO") {
						cell.setCellValue(map.get(key));
						cell.setCellStyle(cellStyle_aligncenter);
					}
					else if (key == "Style" ) {
						cell.setCellValue(map.get(key));
						cell.setCellStyle(cellStyle_aligncenter_fontBold);
					}
					else if(col_idx > size_set_name.get("Picture") && col_idx < size_set_name.get("Quantity")) {
						cell.setCellValue(map.get(key) == null ? 0 : Float.parseFloat(map.get(key)));
						cell.setCellStyle(cellStyle_align_right_currency);
					}
					else if(col_idx > size_set_name.get("Material") && col_idx < size_set_name.get("Packing")) {
						cell.setCellValue((map.get(key) == null ) ? 0 : Float.parseFloat(map.get(key)));
						cell.setCellStyle(cellStyle_align_right_currency);
					}
					else {
						cell.setCellValue(map.get(key));
						cell.setCellStyle(cellStyle_align_right);
					}
				}
				else {
					
					cell.setCellStyle(cellStyle_aligncenter);
					String FolderPath = commonService.getFolderPath(10);
					String uploadRootPath = request.getServletContext().getRealPath("");
//					String uploadRootPath = request.getServletContext().getRealPath(FolderPath);
					
					String filename = map.get(key);
					if(filename!=null) {
//						String filePath = uploadRootPath+"/"+ filename;
						File uploadRootDir_img = new File(uploadRootPath);
						String parent = uploadRootDir_img.getParent();
						String filePath = parent+"/"+FolderPath+"/"+ filename;
						
						File img_tmp = new File(filePath);
						if(img_tmp.exists() && !img_tmp.isDirectory()) {
							InputStream isimg = new FileInputStream(filePath);
							byte[] img = IOUtils.toByteArray(isimg);
							
							int pictureIdx = workbook.addPicture(img, Workbook.PICTURE_TYPE_JPEG);
							isimg.close();
							
							XSSFDrawing  drawing = sheet.createDrawingPatriarch();
							
							// add a picture shape
							XSSFClientAnchor  anchor = new XSSFClientAnchor();
							
							// set top-left corner of the picture,
							// subsequent call of Picture#resize() will operate relative to it
							anchor.setCol1(col_idx);
							anchor.setRow1(rowNum);
							anchor.setCol2(col_idx+1);
							anchor.setRow2(rowNum+1);
							drawing.createPicture(anchor, pictureIdx);
						}
						
					}
					
				}
		    }

			row_old = row;
		}
		
		try {
			OutputStream outputstream = new FileOutputStream(FileExport);
			workbook.write(outputstream);
			
			InputStream isimg = new FileInputStream(FileExport);
			response.data = IOUtils.toByteArray(isimg);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		} 
		finally {
			workbook.close();
		}
		
		
		return new ResponseEntity<report_quotation_response>(response, HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value = "/download_temp_chaogia", method = RequestMethod.POST)
	public ResponseEntity<download_template_chaogia_response> DownloadChaoGia(HttpServletRequest request)
	{

		download_template_chaogia_response response = new download_template_chaogia_response();
		try {
			String FolderPath = "TemplateUpload";
			
			// Thư mục gốc upload file.			
			String uploadRootPath = request.getServletContext().getRealPath(FolderPath);
			
			String filePath = uploadRootPath+"/"+ "Template_ChaoGia_New.xlsx";
			Path path = Paths.get(filePath);
			byte[] data = Files.readAllBytes(path);
			response.data = data;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<download_template_chaogia_response>(response, HttpStatus.OK);			
		}
		catch(Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<download_template_chaogia_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/download_temp_po", method = RequestMethod.POST)
	public ResponseEntity<download_template_chaogia_response> DownloadPO(HttpServletRequest request)
	{

		download_template_chaogia_response response = new download_template_chaogia_response();
		try {
			String FolderPath = "TemplateUpload";
			
			// Thư mục gốc upload file.			
			String uploadRootPath = request.getServletContext().getRealPath(FolderPath);
			
			String filePath = uploadRootPath+"/"+ "Template_Upload_PO_Line.xlsx";
			Path path = Paths.get(filePath);
			byte[] data = Files.readAllBytes(path);
			response.data = data;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<download_template_chaogia_response>(response, HttpStatus.OK);			
		}
		catch(Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<download_template_chaogia_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/download_temp_bom_candoi", method = RequestMethod.POST)
	public ResponseEntity<down_temp_bom_response> DownloadBomCanDoi(HttpServletRequest request, @RequestBody down_temp_bom_request entity) throws IOException
	{

		down_temp_bom_response response = new down_temp_bom_response();
		try {
			String FolderPath = "TemplateUpload";
			
			// Thư mục gốc upload file.			
			String uploadRootPath = request.getServletContext().getRealPath(FolderPath);
			
			Date current_time = new Date();
			File FileExport = new File(uploadRootPath+"/Template_Bom_CanDoi.xlsx");
			File FileCopy = new File(uploadRootPath+"/Template_Bom_CanDoi_"+current_time.getTime()+".xlsx");
			File file = new File(uploadRootPath+"/Bom_CanDoi_"+current_time.getTime()+".xlsx");
			
			com.google.common.io.Files.copy(FileExport, FileCopy);
			FileInputStream is_filecopy = new FileInputStream(FileCopy);
			
			XSSFWorkbook workbook = new XSSFWorkbook(is_filecopy);
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			try {
				//get tat ca co cua san pham trong don hang
				Long pcontractid_link = entity.pcontractid_link;
				Long productid_link = entity.productid_link;
				List<String> list_size = ppskuService.getlistnamevalue_by_product(pcontractid_link, productid_link, AtributeFixValues.ATTR_SIZE);
				
				Row row_1 = sheet.getRow(0);
				
				Cell cellstyle_row1 = row_1.getCell(ColumnExcel.B);
				CellStyle style_row1 = workbook.createCellStyle();
				style_row1.cloneStyleFrom(cellstyle_row1.getCellStyle());
				
				int start = ColumnExcel.L;
				int end = start+ list_size.size() - 1;
				sheet.addMergedRegion(new CellRangeAddress( 0, 0,start , end));	
								
				Cell cell_ds = row_1.createCell(ColumnExcel.L);
				cell_ds.setCellValue("Danh sách cỡ và định mức sử dụng NPL cho từng cỡ");
				cell_ds.setCellStyle(style_row1);				

				Row row_2 = sheet.getRow(1);
				Cell cellstyle_row2 = row_2.getCell(ColumnExcel.B);
				CellStyle style_row2 = workbook.createCellStyle();
				style_row2.cloneStyleFrom(cellstyle_row2.getCellStyle());
				
				for(int i =0; i<list_size.size(); i++) {
					Cell cell_size = row_2.createCell(ColumnExcel.L+ i);
					cell_size.setCellValue(list_size.get(i));
					cell_size.setCellStyle(style_row2);
				}
				
				
				//tra file ve dang byte[]
				OutputStream outputstream = new FileOutputStream(file);
				workbook.write(outputstream);
				workbook.close();

				outputstream.close();
				
				InputStream isimg = new FileInputStream(file);
				response.data = IOUtils.toByteArray(isimg);
				isimg.close();
				
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			}
			catch (Exception e) {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage(e.getMessage());
			}
			finally {
				workbook.close();
				FileCopy.delete();
				file.delete();
			}
			
			return new ResponseEntity<down_temp_bom_response>(response, HttpStatus.OK);			
		}
		catch(Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<down_temp_bom_response>(response, HttpStatus.OK);
		}
	}
	
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
