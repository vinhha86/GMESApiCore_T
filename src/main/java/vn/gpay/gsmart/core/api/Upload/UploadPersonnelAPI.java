package vn.gpay.gsmart.core.api.Upload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.personel.IPersonnel_Service;
import vn.gpay.gsmart.core.personel.Personel;
import vn.gpay.gsmart.core.utils.ColumnPersonnel;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/upload_personnel")
public class UploadPersonnelAPI {
	@Autowired Common commonService;
	@Autowired
	IPersonnel_Service personnel_service;

	@RequestMapping(value = "/personnel", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> UploadPersonnel(HttpServletRequest request,
			@RequestParam("file") MultipartFile file) {
		ResponseBase response = new ResponseBase();
		Date current_time = new Date();
		String name = "";
		String mes_err = "";

		try {
			String FolderPath = "upload/personnel";
			String uploadRootPath = request.getServletContext().getRealPath(FolderPath);
			File uploadRootDir = new File(uploadRootPath);
			name = file.getOriginalFilename();

			System.out.print(name);

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
				int colNum = 1;

				Row row = sheet.getRow(rowNum);
				try {
				
					String STT = "";
					STT = commonService.getStringValue(row.getCell(ColumnPersonnel.STT));
					STT = STT.equals("0") ? "" : STT;
					
					String MaSoMoi = commonService.getStringValue(row.getCell(ColumnPersonnel.MaSoMoi));
					String TinhTrang = commonService.getStringValue(row.getCell(ColumnPersonnel.TinhTrang));
					String Ho = commonService.getStringValue(row.getCell(ColumnPersonnel.Ho));
					String Ten =commonService.getStringValue(row.getCell(ColumnPersonnel.Ten));
					String HoVaTen =commonService.getStringValue(row.getCell(ColumnPersonnel.HoVaTen));
					String GioTinh = commonService.getStringValue(row.getCell(ColumnPersonnel.GT)) ;
					String NgaySinh = commonService.getStringValue(row.getCell(ColumnPersonnel.NgaySinh));
					String Tuoi = commonService.getStringValue(row.getCell(ColumnPersonnel.Tuoi));
					String BoPhan = commonService.getStringValue(row.getCell(ColumnPersonnel.BoPhan));
					String ChucVu= commonService.getStringValue(row.getCell(ColumnPersonnel.ChucVu));
					String ChucVuBH =commonService.getStringValue(row.getCell(ColumnPersonnel.ChucVutrongBH));
					String Bac = commonService.getStringValue(row.getCell(ColumnPersonnel.Bac));
					String MaBacLuongBH = commonService.getStringValue(row.getCell(ColumnPersonnel.MaBacluongBH));
					String HeSoLuong = commonService.getStringValue(row.getCell(ColumnPersonnel.HeSoLuongBH));
					String MucLuongBH = commonService.getStringValue(row.getCell(ColumnPersonnel.MucLuongDongBH));
					String HeSoCV = commonService.getStringValue(row.getCell(ColumnPersonnel.HeSoCV));
					String ThangGiamBH = commonService.getStringValue(row.getCell(ColumnPersonnel.ThangGiamBH));
					String NgayVaoCT = commonService.getStringValue(row.getCell(ColumnPersonnel.NgayVaoCT));
					String NgayThoiViec = commonService.getStringValue(row.getCell(ColumnPersonnel.NgayThoiViec));
					String LyDo = commonService.getStringValue(row.getCell(ColumnPersonnel.LyDo));
					String NgayKiHDTV = commonService.getStringValue(row.getCell(ColumnPersonnel.NgayKiHDTV));
					String NgayKiHDCTH = commonService.getStringValue(row.getCell(ColumnPersonnel.NgayKiHDCTH));
					String NgayKiHDVTH = commonService.getStringValue(row.getCell(ColumnPersonnel.NgayKiHDVTH));
					String NgayDongBH = commonService.getStringValue(row.getCell(ColumnPersonnel.NgayDongBH));
					String TGCT = commonService.getStringValue(row.getCell(ColumnPersonnel.TGCongTac));
					String Thon = commonService.getStringValue(row.getCell(ColumnPersonnel.Thon));
					String Xa = commonService.getStringValue(row.getCell(ColumnPersonnel.Xa));
					String Huyen = commonService.getStringValue(row.getCell(ColumnPersonnel.Huyen));
					String Tinh = commonService.getStringValue(row.getCell(ColumnPersonnel.Tinh));
					String DiaChi = commonService.getStringValue(row.getCell(ColumnPersonnel.DiaChi));
					String DT = commonService.getStringValue(row.getCell(ColumnPersonnel.DienThoai));
					String CMND = commonService.getStringValue(row.getCell(ColumnPersonnel.CMND));
					String NgayCap = commonService.getStringValue(row.getCell(ColumnPersonnel.NgayCap));
					String NoiCap = commonService.getStringValue(row.getCell(ColumnPersonnel.NoiCap));
					String CMTM = commonService.getStringValue(row.getCell(ColumnPersonnel.CMTM));
					String NgayCapMoi = commonService.getStringValue(row.getCell(ColumnPersonnel.NgayCapMoi));
					String NoiCapMoi = commonService.getStringValue(row.getCell(ColumnPersonnel.NoiCapMoi));
					String SK = commonService.getStringValue(row.getCell(ColumnPersonnel.SucKhoe));
					String SoSBH = commonService.getStringValue(row.getCell(ColumnPersonnel.SoSBH));
					
					
					response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
					
				} catch (Exception e) {
					mes_err = "Có lỗi ở dòng " + (rowNum + 1) + " và cột " + colNum + ": " + mes_err;
				}
			}

		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
	
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
}
