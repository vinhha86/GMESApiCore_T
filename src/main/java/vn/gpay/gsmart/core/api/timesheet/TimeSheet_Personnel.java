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

public class TimeSheet_Personnel implements Runnable{
	private ITimeSheet_Service timesheetService;
	
	
	private Thread t;
	private Personel personnel;
	private Integer year;
	private Integer month;
	private long orgid_link;
	
	CountDownLatch latch;
	
	TimeSheet_Personnel(Personel myPersonnel, int myyear, int mymonth, Long myorgid_link,
			ITimeSheet_Service timesheetService,
			CountDownLatch latch){
		this.personnel = myPersonnel;
		this.year = myyear;
		this.month = mymonth;
		this.orgid_link = myorgid_link;
		this.timesheetService = timesheetService;
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
			List<TimeSheet> myTimeSheet = timesheetService.getByTime(personnel.getRegister_code(), dateStart, dateEnd);
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
