package vn.gpay.gsmart.core.api.timesheet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;

import vn.gpay.gsmart.core.personel.Personel;
import vn.gpay.gsmart.core.porderprocessingns.IPorderProcessingNsService;
import vn.gpay.gsmart.core.salary.IOrgSal_BasicService;
import vn.gpay.gsmart.core.salary.IOrgSal_ComService;
import vn.gpay.gsmart.core.salary.IOrgSal_TypeService;
import vn.gpay.gsmart.core.salary.IOrgSal_Type_LevelService;
import vn.gpay.gsmart.core.salary.ISalary_SumService;
import vn.gpay.gsmart.core.salary.ISalary_Sum_POrdersService;
import vn.gpay.gsmart.core.salary.OrgSal_Basic;
import vn.gpay.gsmart.core.salary.OrgSal_Com;
import vn.gpay.gsmart.core.salary.OrgSal_Type;
import vn.gpay.gsmart.core.salary.OrgSal_Type_Level;
import vn.gpay.gsmart.core.salary.Salary_Sum;
import vn.gpay.gsmart.core.salary.Salary_Sum_POrders;
import vn.gpay.gsmart.core.timesheet.ITimeSheet_Service;
import vn.gpay.gsmart.core.timesheet.TimeSheet;
import vn.gpay.gsmart.core.timesheet_lunch.ITimeSheetLunchService;

public class TimeSheet_Personnel implements Runnable{
	private ITimeSheet_Service timesheetService;
	private ITimeSheetLunchService timesheet_lunchService;
	
	
	private Thread t;
	private Personel personnel;
	private Integer year;
	private Integer month;
	private long orgid_link;
	
	CountDownLatch latch;
	
	TimeSheet_Personnel(Personel myPersonnel, int myyear, int mymonth, Long myorgid_link,
			ITimeSheet_Service timesheetService,
			ITimeSheetLunchService timesheet_lunchService,
			CountDownLatch latch){
		this.personnel = myPersonnel;
		this.year = myyear;
		this.month = mymonth;
		this.orgid_link = myorgid_link;
		this.timesheetService = timesheetService;
		this.timesheet_lunchService = timesheet_lunchService;
		this.latch = latch;
	}
	@Override
	public void run() {
		try {
			if (null != personnel){
				cal_timesheet_grid();
			}
			latch.countDown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void start () {
		if (t == null) {
			t = new Thread (this, personnel.getCode());
			t.start ();
		}
	}
	private void cal_timesheet_grid(){
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String dateStartString = "07-" + month.toString() + "-" + year.toString() + " 00:00:00";
		Integer month_next = month + 1;
		Integer year_next = year;
		if (month_next == 13){
			month_next = 1;
			year_next = year + 1;
		}
		String dateEndString = "06-" + month_next.toString() + "-" + year_next.toString() + " 23:59:59";
		try {
			Date dateStart = sdf.parse(dateStartString);
			Date dateEnd = sdf.parse(dateEndString);
			
			//I. Lấy danh sách các ca đi làm của nhân sự được khai báo trong tháng
			
			//II. Duyệt từng ngày, từng ca --> Lấy danh sách Timerecorded trong ca
			//Tính thời gian bắt đầy và kết thúc ca - Sai so 30 phut truoc va sau
			
			List<TimeSheet> lsTimeSheet = timesheetService.getByTime(personnel.getRegister_code(), dateStart, dateEnd);
			//Duyet tu dau den cuoi theo danh sach sap xep thứ tự thời gian tăng dần
			for(TimeSheet theInOut:lsTimeSheet){
				//2.1 Xac dinh xem Timerecorded la vao hay ra
				//+ Xuất hiện lần đầu trong ca, tính từ đầu ca - sai số --> Vào
				//+ Xuất hiện cuối cùng trong ca, tính từ cuối ca + sai số --> Ra
				//+ Các lần Timerecorded ở giữa ca --> Không tính
				
				//2.2 Nếu trong khoảng sai số --> Cộng tròn công giờ trong ca 
				//2.3 Nếu Vào muộn hơn sai số --> Lấy giờ vào thực tế; Ra sớm hơn sai số -- Lấy giờ ra thực tế
				//--> Tính công giờ thực tê
				
				//2.4 Tính hệ số tăng ca (ca đêm/nghỉ/lễ) vào công ca và cộng dồn vào tổng công ngày
				
				//2.5 Ghi nhận tổng công ngày
			}
			
			//III. Lay danh sach cac ngay nghi co dang ky của nhân sự trong khoảng thời gian
			//3.1 Duyệt và xác định loại nghỉ, thời gian nghỉ
			//3.2 Tính công theo hệ số công của ngày nghỉ (theo ca mặc định,ca ngày)
			
			
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
