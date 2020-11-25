package vn.gpay.gsmart.core.api.salary;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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
import vn.gpay.gsmart.core.utils.CONST_SALARY_SUM_COL;

public class Salary_Personnel implements Runnable{
	private IOrgSal_TypeService saltypeService;
	private IOrgSal_Type_LevelService saltype_levelService;
	private IOrgSal_ComService salcomService;
	private IOrgSal_BasicService salbasicService;
	private ISalary_SumService salarysumService;
	private ISalary_Sum_POrdersService salarysum_pordersService;
	private IPorderProcessingNsService porderprocessing_nsService;
	
	
	private Thread t;
	private Personel personnel;
	private Integer year;
	private Integer month;
	private OrgSal_Basic theSalBasic;
	private long orgid_link;
	
	CountDownLatch latch;
	
	Salary_Personnel(Personel myPersonnel, int myyear, int mymonth, OrgSal_Basic mySalBasic, Long myorgid_link,
			IOrgSal_TypeService my_saltypeService,
			IOrgSal_Type_LevelService my_saltype_levelService,
			IOrgSal_ComService my_salcomService,
			IOrgSal_BasicService salbasicService,
			ISalary_SumService my_salarysumService,
			ISalary_Sum_POrdersService salarysum_pordersService,
			IPorderProcessingNsService porderprocessing_nsService,
			CountDownLatch latch){
		this.personnel = myPersonnel;
		this.year = myyear;
		this.month = mymonth;
		this.theSalBasic = mySalBasic;
		this.orgid_link = myorgid_link;
		this.saltypeService = my_saltypeService;
		this.saltype_levelService = my_saltype_levelService;
		this.salcomService = my_salcomService;
		this.salbasicService = salbasicService;
		this.salarysumService = my_salarysumService;
		this.salarysum_pordersService = salarysum_pordersService;
		this.porderprocessing_nsService = porderprocessing_nsService;
		this.latch = latch;
	}
	@Override
	public void run() {
		try {
			if (null != personnel){
				//2.Lay thong tin thang luong, bac luong cua nhan su
				Long saltypeidlink = personnel.getSaltypeid_link(); //thang luong
				Long sallevelid_link = personnel.getSallevelid_link();//bac luong
				if (null != saltypeidlink && null != sallevelid_link){
//					System.out.println(personnel.getFullname());
					OrgSal_Type theSal_Type = saltypeService.findOne(saltypeidlink);
					if (null!=theSal_Type){
						//Neu la luong thoi gian
						if (theSal_Type.getType() == 0){
							//Tinh gia tri luong gio theo thang luong, bac luong va so ngay lam viec
							OrgSal_Type_Level theSal_Type_Level = saltype_levelService.get_bysaltype_and_level(saltypeidlink, sallevelid_link);
							if (null != theSal_Type_Level.getSalamount() && null != theSalBasic.getWorkingdays() && 0 != theSalBasic.getWorkingdays()){
								//Tinh luong co ban theo gio cua nhan su
								int cost_per_hour = theSal_Type_Level.getSalamount()/theSalBasic.getWorkingdays()/8;
								//Lay so cong lam trong thang cua nhan su
								int value_tg_sl = 22*8;
								//Query bang timesheet_sum với sumcolid_link=32 de lay ra sumvalue
								
								//Tinh luong thoi gian
//								cal_luong_tg_pivot(value_tg_sl,cost_per_hour);
								cal_luong_tg_grid(value_tg_sl,cost_per_hour);
								
							}
						}
						//Neu la luong nang suat
						if (theSal_Type.getType() == 1){
							cal_luong_sp_grid();
						}
					}
				}
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
	private void cal_luong_sp_grid(){
		System.out.println("Tinh luong san pham");
		//1.Lay danh sach lenh tinh luong trong thang cua don vi quan ly personnel (salary_sum_porders)
		List<Salary_Sum_POrders> POrder_ls = salarysum_pordersService.getall_byorg(orgid_link, year, month);
		System.out.println(orgid_link);
		System.out.println(POrder_ls);
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
			
			//Lay luong nang suat co ban theo s cua don vi
			OrgSal_Basic thesalBasic = salbasicService.getone_byorg(orgid_link);
			Integer costpersecond = thesalBasic.getCostpersecond();
			Integer total_wtime = 0;
			Integer total_sal_porder = 0;
			if (null!=costpersecond)
			for(Salary_Sum_POrders thePOrder:POrder_ls){
				//2.Lay danh sach cong doan, so luong, so tien cua cac lenh ma nhan su tham gia trong khoang tgian tinh luong
				//+ porder_processing_ns
				Integer wtime = porderprocessing_nsService.getTotalWTime_ByPorder(
						thePOrder.getPordergrantid_link(), 
						personnel.getId(), 
						dateStart, 
						dateEnd);
				if (null!=wtime){
					Integer sal_porder = costpersecond * wtime;
					total_wtime += wtime;
					total_sal_porder += sal_porder;
				}
			}
			
			//Ghi nhan luong cho nhan su
			Salary_Sum salary_sum = new Salary_Sum();
			salary_sum.setPersonnelid_link(personnel.getId());
			salary_sum.setYear(year);
			salary_sum.setMonth(month);
			
			salary_sum.setLuongsp_sl(total_wtime);
			salary_sum.setLuongsp_tien(total_sal_porder);
			
//			//Tinh nghi huong 100% luong
//			int value_nghi_sl = 0;
//			//Query bang timesheet_sum với sumcolid_link=34 de lay ra sumvalue
//			
//			//Tinh so tien tra nghi huong luong
//			int value_nghi_sotien = cost_per_hour * value_nghi_sl;
//			salary_sum.setNghi_sl(value_nghi_sl);
//			salary_sum.setNghi_tien(value_nghi_sotien);
			
			//Tinh phu cap chuc vu
			//1.Lay danh sach cac phu cap cua chuc vu - nhan su
			int value_phucap_chucvu = 0;
			if (null!=personnel.getPositionid_link()){
				List<OrgSal_Com> ls_com_chucvu = salcomService.getall_byposition(orgid_link, 0, personnel.getPositionid_link());
				for(OrgSal_Com com_chucvu:ls_com_chucvu) value_phucap_chucvu += null!=com_chucvu.getComamount()?com_chucvu.getComamount():0;
			}
			salary_sum.setPhucap_chucvu(value_phucap_chucvu);
			
			//Tinh phu cap khac
			//1.Lay danh sach cac phu cap khac - nhan su
			int value_phucap_khac = 0;
			if(null!=personnel.getLevelid_link()){
				List<OrgSal_Com> ls_com_khac = salcomService.getall_bylaborlevel(orgid_link, 0, personnel.getLevelid_link());
				for(OrgSal_Com com_khac:ls_com_khac) value_phucap_khac += null!=com_khac.getComamount()?com_khac.getComamount():0;
			}
			salary_sum.setPhucap_khac(value_phucap_khac);
			
			//Tong so
			int value_tongluong = total_sal_porder + value_phucap_chucvu + value_phucap_khac;
			salary_sum.setTongluong(value_tongluong);
			salarysumService.saveWithCheck(salary_sum);			
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	private void cal_luong_tg_grid(int value_tg_sl, int cost_per_hour){
		int value_tg_sotien = cost_per_hour * value_tg_sl;
		Salary_Sum salary_sum = new Salary_Sum();
		salary_sum.setPersonnelid_link(personnel.getId());
		salary_sum.setYear(year);
		salary_sum.setMonth(month);
		
		salary_sum.setLuongtg_sl(value_tg_sl);
		salary_sum.setLuongtg_tien(value_tg_sotien);
		
		//Tinh nghi huong 100% luong
		int value_nghi_sl = 0;
		//Query bang timesheet_sum với sumcolid_link=34 de lay ra sumvalue
		
		//Tinh so tien tra nghi huong luong
		int value_nghi_sotien = cost_per_hour * value_nghi_sl;
		salary_sum.setNghi_sl(value_nghi_sl);
		salary_sum.setNghi_tien(value_nghi_sotien);
		
		//Tinh phu cap chuc vu
		//1.Lay danh sach cac phu cap cua chuc vu - nhan su
		int value_phucap_chucvu = 0;
		if (null!=personnel.getPositionid_link()){
			List<OrgSal_Com> ls_com_chucvu = salcomService.getall_byposition(orgid_link, 0, personnel.getPositionid_link());
			for(OrgSal_Com com_chucvu:ls_com_chucvu) value_phucap_chucvu += null!=com_chucvu.getComamount()?com_chucvu.getComamount():0;
		}
		salary_sum.setPhucap_chucvu(value_phucap_chucvu);
		
		//Tinh phu cap khac
		//1.Lay danh sach cac phu cap khac - nhan su
		int value_phucap_khac = 0;
		if(null!=personnel.getLevelid_link()){
			List<OrgSal_Com> ls_com_khac = salcomService.getall_bylaborlevel(orgid_link, 0, personnel.getLevelid_link());
			for(OrgSal_Com com_khac:ls_com_khac) value_phucap_khac += null!=com_khac.getComamount()?com_khac.getComamount():0;
		}
		salary_sum.setPhucap_khac(value_phucap_khac);
		
		//Tong so
		int value_tongluong = value_tg_sotien + value_nghi_sotien + value_phucap_chucvu + value_phucap_khac;
		salary_sum.setTongluong(value_tongluong);
		salarysumService.saveWithCheck(salary_sum);
	}
//	private void cal_luong_tg_pivot(int value_tg_sl, int cost_per_hour){
//		int value_tg_sotien = cost_per_hour * value_tg_sl;
//		
//		Salary_Sum col_tg_sl = new Salary_Sum();
//		col_tg_sl.setPersonnelid_link(personnel.getId());
//		col_tg_sl.setYear(year);
//		col_tg_sl.setMonth(month);
//		col_tg_sl.setSumcoltypeid_link(CONST_SALARY_SUM_COL.COL_TYPE_LUONGTG);
//		col_tg_sl.setSumcolid_link(CONST_SALARY_SUM_COL.COL_TG_SL);
//		col_tg_sl.setSumvalue((float)value_tg_sl);
//		salarysumService.saveWithCheck(col_tg_sl);
//		
//		Salary_Sum col_tg_sotien = new Salary_Sum();
//		col_tg_sotien.setPersonnelid_link(personnel.getId());
//		col_tg_sotien.setYear(year);
//		col_tg_sotien.setMonth(month);
//		col_tg_sotien.setSumcoltypeid_link(CONST_SALARY_SUM_COL.COL_TYPE_LUONGTG);
//		col_tg_sotien.setSumcolid_link(CONST_SALARY_SUM_COL.COL_TG_SOTIEN);
//		col_tg_sotien.setSumvalue((float)value_tg_sotien);
//		salarysumService.saveWithCheck(col_tg_sotien);
//		
//		//Tinh nghi huong 100% luong
//		int value_nghi_sl = 0;
//		//Query bang timesheet_sum với sumcolid_link=34 de lay ra sumvalue
//		
//		//Tinh so tien tra nghi huong luong
//		int value_nghi_sotien = cost_per_hour * value_nghi_sl;
//		
//		Salary_Sum col_nghi_sl = new Salary_Sum();
//		col_nghi_sl.setPersonnelid_link(personnel.getId());
//		col_nghi_sl.setYear(year);
//		col_nghi_sl.setMonth(month);
//		col_nghi_sl.setSumcoltypeid_link(CONST_SALARY_SUM_COL.COL_TYPE_NGHI);
//		col_nghi_sl.setSumcolid_link(CONST_SALARY_SUM_COL.COL_NGHI_SL);
//		col_nghi_sl.setSumvalue((float)value_nghi_sl);
//		salarysumService.saveWithCheck(col_nghi_sl);
//		
//		Salary_Sum col_nghi_sotien = new Salary_Sum();
//		col_nghi_sotien.setPersonnelid_link(personnel.getId());
//		col_nghi_sotien.setYear(year);
//		col_nghi_sotien.setMonth(month);
//		col_nghi_sotien.setSumcoltypeid_link(CONST_SALARY_SUM_COL.COL_TYPE_NGHI);
//		col_nghi_sotien.setSumcolid_link(CONST_SALARY_SUM_COL.COL_NGHI_SOTIEN);
//		col_nghi_sotien.setSumvalue((float)value_nghi_sotien);
//		salarysumService.saveWithCheck(col_nghi_sotien);
//		
//		//Tinh phu cap chuc vu
//		//1.Lay danh sach cac phu cap cua chuc vu - nhan su
//		int value_phucap_chucvu = 0;
//		if (null!=personnel.getPositionid_link()){
//			List<OrgSal_Com> ls_com_chucvu = salcomService.getall_byposition(orgid_link, 0, personnel.getPositionid_link());
//			for(OrgSal_Com com_chucvu:ls_com_chucvu) value_phucap_chucvu += null!=com_chucvu.getComamount()?com_chucvu.getComamount():0;
//		}
//		
//		Salary_Sum col_phucap_chucvu = new Salary_Sum();
//		col_phucap_chucvu.setPersonnelid_link(personnel.getId());
//		col_phucap_chucvu.setYear(year);
//		col_phucap_chucvu.setMonth(month);
//		col_phucap_chucvu.setSumcolid_link(CONST_SALARY_SUM_COL.COL_PHUCAP_CHUCVU);
//		col_phucap_chucvu.setSumvalue((float)value_phucap_chucvu);
//		salarysumService.saveWithCheck(col_phucap_chucvu);
//		
//		//Tinh phu cap khac
//		//1.Lay danh sach cac phu cap khac - nhan su
//		int value_phucap_khac = 0;
//		if(null!=personnel.getLevelid_link()){
//			List<OrgSal_Com> ls_com_khac = salcomService.getall_bylaborlevel(orgid_link, 0, personnel.getLevelid_link());
//			for(OrgSal_Com com_khac:ls_com_khac) value_phucap_khac += null!=com_khac.getComamount()?com_khac.getComamount():0;
//		}
//		
//		Salary_Sum col_phucap_khac = new Salary_Sum();
//		col_phucap_khac.setPersonnelid_link(personnel.getId());
//		col_phucap_khac.setYear(year);
//		col_phucap_khac.setMonth(month);
//		col_phucap_khac.setSumcolid_link(CONST_SALARY_SUM_COL.COL_PHUCAP_KHAC);
//		col_phucap_khac.setSumvalue((float)value_phucap_khac);
//		salarysumService.saveWithCheck(col_phucap_khac);
//		
//		//Tong so
//		int value_tongluong = value_tg_sotien + value_nghi_sotien + value_phucap_chucvu + value_phucap_khac;
//		Salary_Sum col_tongluong = new Salary_Sum();
//		col_tongluong.setPersonnelid_link(personnel.getId());
//		col_tongluong.setYear(year);
//		col_tongluong.setMonth(month);
//		col_tongluong.setSumcolid_link(CONST_SALARY_SUM_COL.COL_TONGLUONG);
//		col_tongluong.setSumvalue((float)value_tongluong);
//		salarysumService.saveWithCheck(col_tongluong);
//		
//	}
}
