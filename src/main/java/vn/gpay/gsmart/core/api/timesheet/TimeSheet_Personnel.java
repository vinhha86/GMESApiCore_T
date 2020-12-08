package vn.gpay.gsmart.core.api.timesheet;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import vn.gpay.gsmart.core.personel.Personel;
import vn.gpay.gsmart.core.timesheet.ITimeSheet_Service;
import vn.gpay.gsmart.core.timesheet.TimeSheet;
import vn.gpay.gsmart.core.timesheet_lunch.ITimeSheetLunchService;
import vn.gpay.gsmart.core.timesheet_lunch.TimeSheetLunch;
import vn.gpay.gsmart.core.timesheet_sum.ITimeSheet_Sum_Service;
import vn.gpay.gsmart.core.timesheet_sum.TimeSheet_Sum;

public class TimeSheet_Personnel implements Runnable{
	private ITimeSheet_Service timesheetService;
	private ITimeSheetLunchService timesheet_lunchService;
	private ITimeSheet_Sum_Service timesheet_sumService;
	
	
	private Thread t;
	private Personel personnel;
	private Integer year;
	private Integer month;
	private long orgid_link;
	
	CountDownLatch latch;
	
	TimeSheet_Personnel(Personel myPersonnel, int myyear, int mymonth, Long myorgid_link,
			ITimeSheet_Service timesheetService,
			ITimeSheetLunchService timesheet_lunchService,
			ITimeSheet_Sum_Service timesheet_sumService,
			CountDownLatch latch){
		this.personnel = myPersonnel;
		this.year = myyear;
		this.month = mymonth;
		this.orgid_link = myorgid_link;
		this.timesheetService = timesheetService;
		this.timesheet_lunchService = timesheet_lunchService;
		this.timesheet_sumService = timesheet_sumService;
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
		int saiso_checkin = 30;
		int saiso_checkout = 30;
		
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
			
			System.out.println(personnel.getFullname());
			
			//I. Lấy danh sách các ca đi làm của nhân sự được khai báo trong tháng
			List<TimeSheetLunch> lsWorkingShift= timesheet_lunchService.getByPersonnelDate(personnel.getId(), dateStart, dateEnd);
			
			for (TimeSheetLunch theWorkingShift:lsWorkingShift){
				//II. Duyệt từng ngày, từng ca --> Lấy danh sách Timerecorded trong ca
				//Tính thời gian bắt đầy và kết thúc ca - Sai so 30 phut truoc va sau
				
				//Thoi gian bat dau ca
				Date shiftDate_Start = theWorkingShift.getWorkingdate();
				Calendar cal_Start = Calendar.getInstance();
				cal_Start.setTime(shiftDate_Start);
				cal_Start.set(Calendar.HOUR_OF_DAY,theWorkingShift.getShift_from_hour());
				cal_Start.set(Calendar.MINUTE,theWorkingShift.getShift_from_minute());
				cal_Start.set(Calendar.SECOND,0);
				cal_Start.set(Calendar.MILLISECOND,0);
				shiftDate_Start = cal_Start.getTime();
				//Cong sai so
				cal_Start.add(Calendar.MINUTE, -saiso_checkin);
				Date shiftDate_Start_ss = cal_Start.getTime();
				
				//Thoi gian ket thuc ca
				Date shiftDate_End = theWorkingShift.getWorkingdate();
				Date shiftDate_End_ss = theWorkingShift.getWorkingdate();
				if (theWorkingShift.getShift_to_hour() > 24){
					Calendar cal_End = Calendar.getInstance();
					cal_End.setTime(shiftDate_End);
					cal_End.add(Calendar.DAY_OF_MONTH, 1);
					cal_End.set(Calendar.HOUR_OF_DAY,theWorkingShift.getShift_to_hour()-24);
					cal_End.set(Calendar.MINUTE,theWorkingShift.getShift_to_minute());
					cal_End.set(Calendar.SECOND,0);
					cal_End.set(Calendar.MILLISECOND,0);	
					
					shiftDate_End = cal_End.getTime();
					//Cong sai so
					cal_End.add(Calendar.MINUTE, saiso_checkout);
					shiftDate_End_ss = cal_End.getTime();
				} else {
					Calendar cal_End = Calendar.getInstance();
					cal_End.setTime(shiftDate_End);
					cal_End.set(Calendar.HOUR_OF_DAY,theWorkingShift.getShift_to_hour());
					cal_End.set(Calendar.MINUTE,theWorkingShift.getShift_to_minute());
					cal_End.set(Calendar.SECOND,0);
					cal_End.set(Calendar.MILLISECOND,0);	
					
					shiftDate_End = cal_End.getTime();
					//Cong sai so
					cal_End.add(Calendar.MINUTE, saiso_checkout);
					shiftDate_End_ss = cal_End.getTime();
				}
				
				System.out.println(shiftDate_Start);
				System.out.println(shiftDate_Start);
				//Lay danh sach check in/out cua nhan su trong ca
				List<TimeSheet> lsTimeSheet = timesheetService.getByTime(personnel.getRegister_code(), shiftDate_Start_ss, shiftDate_End_ss);
				
				//Neu SL check >1 --> Co vao va co ra
				if (lsTimeSheet.size() > 1){
					//2.1 Xac dinh xem Timerecorded la vao hay ra
					//+ Xuất hiện lần đầu trong ca, tính từ đầu ca - sai số --> Vào
					//+ Xuất hiện cuối cùng trong ca, tính từ cuối ca + sai số --> Ra
					//+ Các lần Timerecorded ở giữa ca --> Không tính
					TimeSheet time_start = lsTimeSheet.get(0);
					Date checkin = time_start.getTimerecorded();
					TimeSheet time_end = lsTimeSheet.get(lsTimeSheet.size()-1);
					Date checkout = time_end.getTimerecorded();
					
					//2.2 Nếu trong khoảng sai số --> Cộng tròn công giờ trong ca, neu khong lay gio thuc te
					if (checkin.before(shiftDate_Start)) checkin = shiftDate_Start;
					if (checkout.after(shiftDate_End)) checkout = shiftDate_End;
					
					//2.3 Tinh so gio cong = checkout - checkin - lunch_minute
					long work_mili = checkout.getTime() - checkin.getTime() - theWorkingShift.getShift_lunch_minute()*3600;
					Float work_h =  (float) (work_mili/3600000);
					
					//2.4 Tính hệ số tăng ca (ca đêm/nghỉ/lễ) vào công ca và cộng dồn vào tổng công ngày
					
					//2.5 Ghi nhận tổng công ngày
					LocalDate localDate = theWorkingShift.getWorkingdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					int dayval = localDate.getDayOfMonth();
					List<TimeSheet_Sum> ls_TimeSheet_Sum = timesheet_sumService.getByKey(personnel.getId(), year, month, dayval);
					if (ls_TimeSheet_Sum.size() > 0){
						TimeSheet_Sum theSum = ls_TimeSheet_Sum.get(0);
						
						theSum.setSumvalue(work_h);
						timesheet_sumService.save(theSum);
					} else {
						TimeSheet_Sum theSum = new TimeSheet_Sum();
						theSum.setPersonnelid_link(personnel.getId());
						theSum.setYear(year);
						theSum.setMonth(month);
						theSum.setSumcolid_link(dayval);
						theSum.setSumcoltypeid_link(1);//Ngay trong thang
						
						theSum.setSumvalue(work_h);
						timesheet_sumService.save(theSum);
					}
				} else {
					//Moi check in 1 lan trong ca, khong co co so tinh toan
				}
			}
			//III. Lay danh sach cac ngay nghi co dang ky của nhân sự trong khoảng thời gian
			//3.1 Duyệt và xác định loại nghỉ, thời gian nghỉ
			//3.2 Tính công theo hệ số công của ngày nghỉ (theo ca mặc định,ca ngày)
			
			
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
