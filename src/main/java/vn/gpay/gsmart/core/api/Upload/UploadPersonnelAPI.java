package vn.gpay.gsmart.core.api.Upload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.DateUtil;
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

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.IOrgTypeService;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.org.OrgType;
import vn.gpay.gsmart.core.personel.IPersonnel_Service;
import vn.gpay.gsmart.core.personel.Personel;
import vn.gpay.gsmart.core.personnel_history.IPersonnel_His_Service;
import vn.gpay.gsmart.core.personnel_history.Personnel_His;
import vn.gpay.gsmart.core.personnel_position.IPersonnel_Position_Service;
import vn.gpay.gsmart.core.personnel_position.Personnel_Position;
import vn.gpay.gsmart.core.personnel_type.IPersonnelType_Service;
import vn.gpay.gsmart.core.personnel_type.PersonnelType;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ColumnPersonnel;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/upload_personnel")
public class UploadPersonnelAPI {
	@Autowired
	Common commonService;
	@Autowired
	IPersonnel_Service personnel_service;
	@Autowired
	IOrgService org_service;
	@Autowired
	IOrgTypeService org_type_service;
	@Autowired
	IPersonnel_His_Service personnel_his_service;
	@Autowired
	IPersonnel_Position_Service personnel_position_service;
	@Autowired
	IPersonnelType_Service personneltypeService;
	@Autowired
	IOrgTypeService orgtype_service;
	@RequestMapping(value = "/personnel", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> UploadPersonnel(HttpServletRequest request,
			@RequestParam("file") MultipartFile file, @RequestParam("orgmanageid_link") long orgmanageid_link) {
		ResponseBase response = new ResponseBase();
		Date current_time = new Date();
		String name = "";
		String mes_err = "";
		Personel person = new Personel();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long orgrootid_link = user.getRootorgid_link();
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
				// int colNum = 1;

				Row row = sheet.getRow(rowNum);

				try {
					String MaSoMoi = "";
					MaSoMoi = commonService.getStringValue(row.getCell(ColumnPersonnel.MaSoMoi));
					MaSoMoi = MaSoMoi.equals("0") ? "" : MaSoMoi;
					while (!MaSoMoi.equals("")) {

					
						// tim nhan vien theo ma so moi và đơn vị đang được chọn
						person = personnel_service.getPersonelBycode_orgmanageid_link(MaSoMoi, orgmanageid_link);
						// neu khong co nhan vien. thi tao nhan vien moi theo ma so moi
						if (person == null) {
							person = new Personel();
							person.setCode(MaSoMoi);
						}
						
						
						String ThoiVu = commonService.getStringValue(row.getCell(ColumnPersonnel.ThoiVu));
						Long LoaiNV = null;
						String ssThoiVu = ThoiVu.trim().toLowerCase();
						if(ssThoiVu.equals("c")  || ssThoiVu.equals("có")){
							//lấy id của loại nhân viên
							List<PersonnelType> lst_type=personneltypeService.getByName("Thời vụ");
							LoaiNV=lst_type.get(0).getId();
							
						}else {
							List<PersonnelType> lst_type=personneltypeService.getByName("Hợp đồng");
							LoaiNV=lst_type.get(0).getId();
						}
						
						String HoVaTen = commonService.getStringValue(row.getCell(ColumnPersonnel.HoVaTen));
						String gioiTinh = commonService.getStringValue(row.getCell(ColumnPersonnel.GT));
						int GioiTinh;
						if (gioiTinh.equals("Nam")) {
							GioiTinh = 1;
						} else {
							GioiTinh = 0;
						}
						// ngay sinh
						Date NgaySinh = null;
						try {
							String ngaySinh = commonService.getStringValue(row.getCell(ColumnPersonnel.NgaySinh));
							if (ngaySinh.contains("/")) {
								String[] s_date = ngaySinh.split("/");
								if (Integer.parseInt(s_date[1].toString()) < 13
										&& Integer.parseInt(s_date[0].toString()) < 32
										&& Integer.parseInt(s_date[1].toString()) > 0
										&& Integer.parseInt(s_date[0].toString()) > 0) {
									NgaySinh = new SimpleDateFormat("dd/MM/yyyy").parse(ngaySinh);
								} else {
									mes_err = " Định dạng ngày sinh không đúng dd/MM/yyyy! ở dòng  : " + (rowNum+1);
									break;
								}
							} else if (ngaySinh != "") {
								if (DateUtil.isCellDateFormatted(row.getCell(ColumnPersonnel.NgaySinh))) {
									NgaySinh = row.getCell(ColumnPersonnel.NgaySinh).getDateCellValue();
								}
							}
						} catch (Exception e) {
							if (DateUtil.isCellDateFormatted(row.getCell(ColumnPersonnel.NgaySinh))) {
								NgaySinh = row.getCell(ColumnPersonnel.NgaySinh).getDateCellValue();
							}
						}

						String BoPhan = commonService.getStringValue(row.getCell(ColumnPersonnel.BoPhan));

						String ChucVu = commonService.getStringValue(row.getCell(ColumnPersonnel.ChucVu));
						String ChucVuBH = commonService.getStringValue(row.getCell(ColumnPersonnel.ChucVutrongBH));
						// kiem tra chu vu trong DB, neu chua co thi them chuc vu vao DB
						List<Personnel_Position> lst_personnel_Position = personnel_position_service.getByName_Code(ChucVuBH,ChucVu);
						Long positionid_link;
						if (lst_personnel_Position.size() != 0) {
							positionid_link = lst_personnel_Position.get(0).getId();
						} else {
							Personnel_Position personnel_Position = new Personnel_Position();
							personnel_Position.setCode(ChucVu);
							personnel_Position.setName(ChucVuBH);
							Personnel_Position per_positionid = personnel_position_service.save(personnel_Position);
							positionid_link = per_positionid.getId();
						}
						// ngay ki hop dong co thoi han
						Date NgayKiHDCTH = null;
						try {
							String ngayKiHDCTH = commonService.getStringValue(row.getCell(ColumnPersonnel.NgayKiHDCTH));
							if (ngayKiHDCTH.contains("/")) {
								String[] s_date = ngayKiHDCTH.split("/");
								if (Integer.parseInt(s_date[1].toString()) < 13
										&& Integer.parseInt(s_date[0].toString()) < 32) {
									NgayKiHDCTH = new SimpleDateFormat("dd/MM/yyyy").parse(ngayKiHDCTH);
								} else {
									mes_err = " Định dạng ngày kí HDCTH không đúng dd/MM/yyyy! " + " ở dòng " + (rowNum+1);
									break;
								}

							} else if (ngayKiHDCTH != "") {
								if (DateUtil.isCellDateFormatted(row.getCell(ColumnPersonnel.NgayKiHDCTH))) {
									NgayKiHDCTH = row.getCell(ColumnPersonnel.NgayKiHDCTH).getDateCellValue();
								}
							}

						} catch (Exception e) {
							if (DateUtil.isCellDateFormatted(row.getCell(ColumnPersonnel.NgayKiHDCTH))) {
								NgayKiHDCTH = row.getCell(ColumnPersonnel.NgayKiHDCTH).getDateCellValue();
							}
						}

//						String Bac = commonService.getStringValue(row.getCell(ColumnPersonnel.Bac));
//						String MaBacLuongBH = commonService.getStringValue(row.getCell(ColumnPersonnel.MaBacluongBH));
//						String HeSoLuong = commonService.getStringValue(row.getCell(ColumnPersonnel.HeSoLuongBH));
//						String MucLuongBH = commonService.getStringValue(row.getCell(ColumnPersonnel.MucLuongDongBH));
//						String HeSoCV = commonService.getStringValue(row.getCell(ColumnPersonnel.HeSoCV));
//						String ThangGiamBH = commonService.getStringValue(row.getCell(ColumnPersonnel.ThangGiamBH));

						// kiem tra bo phan co trong DB khong
						// lay bo phan theo ma, don vi quan ly - code,parentid_link
						Long parentid_link = (long) orgmanageid_link;
						Long orgid_link = null;
						List<Org> lst_bp = org_service.getByCodeAndParentid_link(BoPhan, parentid_link);

						if (person.getId() != null) {
							// kiểm tra xem ngày quyết định mới có lơn hơn ngày quyết định cũ không, nếu nhỏ
							// hơn thì không được thêm mới quyết định
							List<Personnel_His> perhis = personnel_his_service.getHis_personByType_Id(person.getId(),
									3);
							// nếu ngày quyết định nhỏ hơn ngày đã tồn tại
							if (perhis.size() != 0 && perhis.get(0).getDecision_date()!=null) {
								if (NgayKiHDCTH.compareTo(perhis.get(0).getDecision_date()) < 0) {
									mes_err = " Kí hợp đồng có thời hạn mới không được nhỏ hơn ngày kí hợp đồng thời có thời hạn đã có "
											+ " ở dòng " + (rowNum+1);
									break;
								}
							}
						}
						//lấy mã bộ phận
						if (lst_bp.size() != 0) {
							// thêm
						//	orgid_link = lst_bp.get(0).getId();
							for(int i =0;i< lst_bp.size();i++) {
								
								//orgid_link = lst_bp.get(i).getId();
								//kiểm tra xem mã loại đơn vị của phòng ban có trong DB orgtype ko?
								//1.nếu có thì lấy
								OrgType orgtype = orgtype_service.findOne(lst_bp.get(i).getOrgtypeid_link());
								if(orgtype!=null) {
									orgid_link = lst_bp.get(i).getId();
									break;
								}else {
									continue;
								}
							}

						} else {

							// nếu chưa có thì thêm bộ phận vào DB
							 mes_err = " Bộ phận không tồn tại! " + " ở dòng " + (rowNum+1) + " cột Bộ Phận ";
							 break;
						}

						// ngay vao cong ty
						Date NgayVaoCT = null;
						try {
							String ngayVaoCT = commonService.getStringValue(row.getCell(ColumnPersonnel.NgayVaoCT));
							if (ngayVaoCT.contains("/")) {
								String[] s_date = ngayVaoCT.split("/");
								if (Integer.parseInt(s_date[1].toString()) < 13
										&& Integer.parseInt(s_date[0].toString()) < 32) {
									NgayVaoCT = new SimpleDateFormat("dd/MM/yyyy").parse(ngayVaoCT);
								} else {
									mes_err = " Định dạng ngày vào CT không đúng dd/MM/yyyy! " + " ở dòng : "
											+ (rowNum+1);
									break;
								}

							} else if (ngayVaoCT != "") {
								if (DateUtil.isCellDateFormatted(row.getCell(ColumnPersonnel.NgayVaoCT))) {
									NgayVaoCT = row.getCell(ColumnPersonnel.NgayVaoCT).getDateCellValue();
								}
							}

						} catch (Exception e) {
							if (DateUtil.isCellDateFormatted(row.getCell(ColumnPersonnel.NgayVaoCT))) {
								NgayVaoCT = row.getCell(ColumnPersonnel.NgayVaoCT).getDateCellValue();
							}
						}

						// ngay thoi viec
						Date NgayThoiViec = null;
						try {
							String ngayThoiViec = commonService
									.getStringValue(row.getCell(ColumnPersonnel.NgayThoiViec));
							if (ngayThoiViec.contains("/")) {
								String[] s_date = ngayThoiViec.split("/");
								if (Integer.parseInt(s_date[1].toString()) < 13
										&& Integer.parseInt(s_date[0].toString()) < 32) {
									NgayThoiViec = new SimpleDateFormat("dd/MM/yyyy").parse(ngayThoiViec);
								} else {
									mes_err = " Định dạng ngày thôi việc không đúng dd/MM/yyyy! " + " ở dòng " + (rowNum+1);
									break;
								}

							} else if (ngayThoiViec != "") {
								if (DateUtil.isCellDateFormatted(row.getCell(ColumnPersonnel.NgayThoiViec))) {
									NgayThoiViec = row.getCell(ColumnPersonnel.NgayThoiViec).getDateCellValue();
								}
							}

						} catch (Exception e) {
							if (DateUtil.isCellDateFormatted(row.getCell(ColumnPersonnel.NgayThoiViec))) {
								NgayThoiViec = row.getCell(ColumnPersonnel.NgayThoiViec).getDateCellValue();
							}
						}
						
						//loại nhân viên
						String tinhTrang = commonService.getStringValue(row.getCell(ColumnPersonnel.TinhTrang));
						int TinhTrang =0;
						if(tinhTrang==null) {
							//nghỉ việc
							if(NgayThoiViec!=null) {
								TinhTrang = 1;
							}
						}else {
							//nghỉ việc
							if (tinhTrang.equals("N")) {
								TinhTrang = 1;
//								rowNum++;
//								row = sheet.getRow(rowNum);
//								MaSoMoi = commonService.getStringValue(row.getCell(ColumnPersonnel.MaSoMoi));
//								MaSoMoi = MaSoMoi.equals("0") ? "" : MaSoMoi;
//								continue;
							}
						}
						
						
						// ngay ki hop dong thu viec
						Date NgayKiHDTV = null;
						try {
							String ngayKiHDTV = commonService.getStringValue(row.getCell(ColumnPersonnel.NgayKiHDTV));
							if (ngayKiHDTV.contains("/")) {
								String[] s_date = ngayKiHDTV.split("/");
								if (Integer.parseInt(s_date[1].toString()) < 13
										&& Integer.parseInt(s_date[0].toString()) < 32) {
									NgayKiHDTV = new SimpleDateFormat("dd/MM/yyyy").parse(ngayKiHDTV);
								} else {
									mes_err = " Định dạng ngày kí HDTV không đúng dd/MM/yyyy! " + " ở dòng " + (rowNum+1);
									break;
								}

							} else if (ngayKiHDTV != "") {
								if (DateUtil.isCellDateFormatted(row.getCell(ColumnPersonnel.NgayKiHDTV))) {
									NgayKiHDTV = row.getCell(ColumnPersonnel.NgayKiHDTV).getDateCellValue();
								}
							}

						} catch (Exception e) {
							if (DateUtil.isCellDateFormatted(row.getCell(ColumnPersonnel.NgayKiHDTV))) {
								NgayKiHDTV = row.getCell(ColumnPersonnel.NgayKiHDTV).getDateCellValue();
							}
						}

						// ngay ki hop dong vo thoi han
						Date NgayKiHDVTH = null;
						try {
							String ngayKiHDVTH = commonService.getStringValue(row.getCell(ColumnPersonnel.NgayKiHDVTH));
							if (ngayKiHDVTH.contains("/")) {
								String[] s_date = ngayKiHDVTH.split("/");
								if (Integer.parseInt(s_date[1].toString()) < 13
										&& Integer.parseInt(s_date[0].toString()) < 32) {
									NgayKiHDVTH = new SimpleDateFormat("dd/MM/yyyy").parse(ngayKiHDVTH);
								} else {
									mes_err = " Định dạng ngày kí HDVTH không đúng dd/MM/yyyy! " + " ở dòng "+ (rowNum+1);
									break;
								}

							} else if (ngayKiHDVTH != "") {
								if (DateUtil.isCellDateFormatted(row.getCell(ColumnPersonnel.NgayKiHDVTH))) {
									NgayKiHDVTH = row.getCell(ColumnPersonnel.NgayKiHDVTH).getDateCellValue();
								}
							}

						} catch (Exception e) {
							if (DateUtil.isCellDateFormatted(row.getCell(ColumnPersonnel.NgayKiHDVTH))) {
								NgayKiHDVTH = row.getCell(ColumnPersonnel.NgayKiHDVTH).getDateCellValue();
							}
						}

						// ngay dong bao hiem
						Date NgayDongBH = null;
						try {
							String ngayDongBH = commonService.getStringValue(row.getCell(ColumnPersonnel.NgayDongBH));
							if (ngayDongBH.contains("/")) {
								String[] s_date = ngayDongBH.split("/");
								if (Integer.parseInt(s_date[1].toString()) < 13
										&& Integer.parseInt(s_date[0].toString()) < 32) {
									NgayDongBH = new SimpleDateFormat("dd/MM/yyyy").parse(ngayDongBH);
								} else {
									mes_err = " Định dạng ngày đóng BH không đúng dd/MM/yyyy! " + " ở dòng " + (rowNum+1);
									break;
								}

							} else if (ngayDongBH != "") {
								if (DateUtil.isCellDateFormatted(row.getCell(ColumnPersonnel.NgayDongBH))) {
									NgayDongBH = row.getCell(ColumnPersonnel.NgayDongBH).getDateCellValue();
								}
							}

						} catch (Exception e) {
							if (DateUtil.isCellDateFormatted(row.getCell(ColumnPersonnel.NgayDongBH))) {
								NgayDongBH = row.getCell(ColumnPersonnel.NgayDongBH).getDateCellValue();
							}
						}

						// ly do nghi viec
						String LyDo = commonService.getStringValue(row.getCell(ColumnPersonnel.LyDo));
						String Thon = commonService.getStringValue(row.getCell(ColumnPersonnel.Thon));
						String Xa = commonService.getStringValue(row.getCell(ColumnPersonnel.Xa));
						String Huyen = commonService.getStringValue(row.getCell(ColumnPersonnel.Huyen));
						String Tinh = commonService.getStringValue(row.getCell(ColumnPersonnel.Tinh));
						String DiaChi = commonService.getStringValue(row.getCell(ColumnPersonnel.DiaChi));
						String DT = commonService.getStringValue(row.getCell(ColumnPersonnel.DienThoai));
						
						String SoSoHoKhau = commonService.getStringValue(row.getCell(ColumnPersonnel.SoSoHoKhau));
						String SoTaiKhoan = commonService.getStringValue(row.getCell(ColumnPersonnel.SoTaiKhoan));
						if (DT.equals("#N/A")) {
							DT = "";
						}
						String CMND = commonService.getStringValue(row.getCell(ColumnPersonnel.CMND));

						// kiem tra tinh trong danh sach orgtypeid_link = 25;
						Long tinh = null;

						//Long id_huyen = null;
						List<Org> lst_tinh = org_service.getByNameAndType(Tinh, 25);
						if (lst_tinh.size() != 0) {
							tinh = lst_tinh.get(0).getId();
						} else {
							Org org = new Org();
							org.setName(Tinh);
							org.setCode(Tinh.replaceAll(" ", ""));
							org.setStatus(1);
							org.setIs_manufacturer(0);
							org.setOrgtypeid_link(25);
							org.setOrgrootid_link(user.getRootorgid_link());
							Org id_org = org_service.save(org);

							// lấy id tỉnh thành vừa thêm
							tinh = id_org.getId();

						}

						// kiem tra huyen trong danh sach tinh orgtypeid_link = 26;
						Long huyen = null;
						Org lst_huyen = org_service.getByNameAndTypeAndParentid_link(Huyen, 26, tinh);

						if (lst_huyen != null) {
							huyen = lst_huyen.getId();
						} else {
							Org org = new Org();
							org.setName(Huyen);
							org.setCode(Huyen.replaceAll(" ", ""));
							org.setStatus(1);
							org.setIs_manufacturer(0);
							org.setParentid_link(tinh);
							org.setOrgrootid_link(user.getRootorgid_link());
							org.setOrgtypeid_link(26);

							// lấy id huyện vừa thêm
							Org id_org = org_service.save(org);
							huyen = id_org.getId();
//							
						}
						// kiem tra xa trong danh sach huyen orgtypeid_link = 27;
						Long xa = null;
						Org lst_xa = org_service.getByNameAndTypeAndParentid_link(Xa, 27, huyen);
						if (lst_xa != null) {
							xa = lst_xa.getId();
						} else {
							Org org = new Org();
							org.setName(Xa);
							org.setCode(Xa.replaceAll(" ", ""));
							org.setStatus(1);
							org.setIs_manufacturer(0);
							org.setParentid_link(huyen);
							org.setOrgtypeid_link(27);
							org.setOrgrootid_link(user.getRootorgid_link());
							// lấy id huyện vừa thêm
							Org id_org = org_service.save(org);
							xa = id_org.getId();

						}

						// String NgayCap =
						// commonService.getStringValue(row.getCell(ColumnPersonnel.NgayCap));
						// ngay caop cmnd
						Date NgayCap = null;
						try {
							String ngayCap = commonService.getStringValue(row.getCell(ColumnPersonnel.NgayCap));
							if (ngayCap.contains("/")) {
								String[] s_date = ngayCap.split("/");
								if(s_date.length > 3) {
									mes_err = " Định dạng ngày cấp không đúng dd/MM/yyyy! ở dòng " + (rowNum+1) +" cột Ngày cấp";
									break;
								}
								
								if (Integer.parseInt(s_date[1].toString()) < 13
										&& Integer.parseInt(s_date[0].toString()) < 32) {
									NgayCap = new SimpleDateFormat("dd/MM/yyyy").parse(ngayCap);
								} else {
									mes_err = " Định dạng ngày cấp không đúng dd/MM/yyyy! ở dòng " + (rowNum+1);
									break;
								}
							} else if (ngayCap != "") {
								if (DateUtil.isCellDateFormatted(row.getCell(ColumnPersonnel.NgayCap))) {
									NgayCap = row.getCell(ColumnPersonnel.NgayCap).getDateCellValue();
								}
							}
						} catch (Exception e) {
							if (DateUtil.isCellDateFormatted(row.getCell(ColumnPersonnel.NgayCap))) {
								NgayCap = row.getCell(ColumnPersonnel.NgayCap).getDateCellValue();
							}
						}

						String NoiCap = commonService.getStringValue(row.getCell(ColumnPersonnel.NoiCap));
						String CMTM = commonService.getStringValue(row.getCell(ColumnPersonnel.CMTM));
						// String NgayCapMoi =
						// commonService.getStringValue(row.getCell(ColumnPersonnel.NgayCapMoi));

						// ngay caop cmnd
						Date NgayCapMoi = null;
						try {
							String ngayCapMoi = commonService.getStringValue(row.getCell(ColumnPersonnel.NgayCapMoi));
							if (ngayCapMoi.contains("/")) {
								String[] s_date = ngayCapMoi.split("/");
								if (Integer.parseInt(s_date[1].toString()) < 13
										&& Integer.parseInt(s_date[0].toString()) < 32) {
									NgayCapMoi = new SimpleDateFormat("dd/MM/yyyy").parse(ngayCapMoi);
								} else {
									mes_err = " Định dạng ngày cấp mới không đúng dd/MM/yyyy! " + " ở dòng  "
											+ (rowNum+1);
									break;
								}
							} else if (ngayCapMoi != "") {
								if (DateUtil.isCellDateFormatted(row.getCell(ColumnPersonnel.NgayCapMoi))) {
									NgayCapMoi = row.getCell(ColumnPersonnel.NgayCapMoi).getDateCellValue();
								}
							}
						} catch (Exception e) {
							if (DateUtil.isCellDateFormatted(row.getCell(ColumnPersonnel.NgayCapMoi))) {
								NgayCapMoi = row.getCell(ColumnPersonnel.NgayCapMoi).getDateCellValue();
							}
						}

						String NoiCapMoi = commonService.getStringValue(row.getCell(ColumnPersonnel.NoiCapMoi));
						String SK = commonService.getStringValue(row.getCell(ColumnPersonnel.SucKhoe));
						String SoSBH = commonService.getStringValue(row.getCell(ColumnPersonnel.SoSBH));
						if (SoSBH.equals("#N/A")) {
							SoSBH = "";
						}

						// person.setCode(MaSoMoi);
						person.setOrgrootid_link(orgrootid_link);
						person.setStatus(TinhTrang);
						
						//loại nhân viên
						person.setPersonnel_typeid_link(LoaiNV);
						person.setFullname(HoVaTen);
						person.setGender(GioiTinh);
						person.setOrgid_link(orgid_link);
						person.setPositionid_link(positionid_link);
						person.setOrgmanagerid_link(orgmanageid_link);

						person.setBirthdate(NgaySinh);
						person.setDate_startworking(NgayVaoCT);
						person.setDate_endworking(NgayThoiViec);
						person.setReason(LyDo);
						person.setDate_probation_contract(NgayKiHDTV);
						person.setDate_limit_contract(NgayKiHDCTH);
						person.setDate_unlimit_contract(NgayKiHDVTH);
						person.setDate_insurance(NgayDongBH);

						person.setProvinceid_link(tinh);
						person.setDistrictid_link(huyen);
						person.setCommuneid_link(xa);
						person.setVillage(Thon);
						person.setAddress(DiaChi);
						person.setTel(DT);

						if (CMTM != "") {
							person.setIdnumber(CMTM);
						} else {
							person.setIdnumber(CMND);
						}
						if (NgayCapMoi != null) {
							person.setDateof_idnumber(NgayCapMoi);
						} else {
							person.setDateof_idnumber(NgayCap);
						}
						if (NoiCapMoi != "") {
							person.setPlace_idnumber(NoiCapMoi);
						} else {
							person.setPlace_idnumber(NoiCap);
						}

						person.setHealthinfo(SK);
						person.setInsurance_number(SoSBH);
						person.setAccount_number(SoTaiKhoan);
						person.setHousehold_number(SoSoHoKhau);

						// luu nhan vien
						Personel personnel = personnel_service.save(person);
						Long personnelid_link = personnel.getId();

						// luu chuc vu
						int type = 1;// chu vu type =1
						// Date decision_date = new SimpleDateFormat("dd/MM/yyyy").parse("01/08/2021");
						Personnel_His personnel_His = new Personnel_His();
						List<Personnel_His> lst_personnel_His = personnel_his_service
								.getHis_personByType_Id(personnelid_link, type);
						// neu da co trong danh sach roi-> update
						if (lst_personnel_His.size() != 0) {
							personnel_His.setId(lst_personnel_His.get(0).getId());
							personnel_His.setPositionid_link(positionid_link);
							personnel_His.setType(type);
							personnel_His.setDecision_date(NgayKiHDCTH);
							personnel_His.setPersonnelid_link(personnelid_link);
						} // neu khong co trong danh sach thi tao moi
						else {

							personnel_His.setPositionid_link(positionid_link);
							personnel_His.setType(type);
							personnel_His.setDecision_date(NgayKiHDCTH);
							personnel_His.setPersonnelid_link(personnelid_link);
						}
						personnel_his_service.save(personnel_His);

						// luu phòng ban orgid_link
						// chu vu type =1, phong ban type =3
						List<Personnel_His> lst_personnel_His_org = personnel_his_service
								.getHis_personByType_Id(personnelid_link, 3);
						personnel_His = new Personnel_His();
						// neu da co trong danh sach roi-> update
						if (lst_personnel_His_org.size() != 0) {
							personnel_His.setId(lst_personnel_His_org.get(0).getId());
							personnel_His.setOrgid_link(orgid_link);
							personnel_His.setType(3);
							personnel_His.setDecision_date(NgayKiHDCTH);
							personnel_His.setPersonnelid_link(personnelid_link);
						} // neu khong co trong danh sach thi tao moi
						else {

							personnel_His.setOrgid_link(orgid_link);
							personnel_His.setType(3);
							personnel_His.setDecision_date(NgayKiHDCTH);
							personnel_His.setPersonnelid_link(personnelid_link);
						}
						personnel_his_service.save(personnel_His);

						response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
						response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));

						// Chuyen sang row tiep theo neu con du lieu thi xu ly tiep khong thi dung lai
						rowNum++;
						row = sheet.getRow(rowNum);
						if (row == null)
							break;

						MaSoMoi = commonService.getStringValue(row.getCell(ColumnPersonnel.MaSoMoi));
						MaSoMoi = MaSoMoi.equals("0") ? "" : MaSoMoi;
					}

				} catch (Exception e) {
					System.out.println(e.getMessage());
					mes_err = "Có lỗi ở dòng " + (rowNum + 1) + " " + mes_err;
				} finally {
					workbook.close();
					serverFile.delete();
				}
				// neu co loi
				if (mes_err == "") {
					response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				} else {
					response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
					response.setMessage(mes_err);
				}
			}

		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}

		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
}
