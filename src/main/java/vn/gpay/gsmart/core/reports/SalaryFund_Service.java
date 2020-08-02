package vn.gpay.gsmart.core.reports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Sorts;
import com.github.wenhao.jpa.Specifications;

import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.pcontract_price.IPContract_Price_Service;
import vn.gpay.gsmart.core.pcontract_price.PContract_Price;
import vn.gpay.gsmart.core.porder.IPOrder_Repository;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.utils.POrderStatus;


@Service
public class SalaryFund_Service implements ISalaryFund_Service {
	@Autowired IOrgService orgService;
	@Autowired IPOrder_Repository repoPOrder;
	@Autowired IPContract_Price_Service priceService;
	@Autowired IPContract_POService poService;
	
	@Override
	public List<SalaryFund_Data> getData_ByMonth(Long userrootorgid_link, Long userorgid_link, int month, int year, int reportmonths){
		List<SalaryFund_Data> data =  new ArrayList<SalaryFund_Data>();
		List<ReportMonth> month_data =  new ArrayList<ReportMonth>();
		//Tinh nam va thang cua thang truoc do
		int month_prev = month -1;
		int year_prev = year;
		if (month_prev == 0){
			month_prev = 12;
			year_prev = year -1;
		}
	
		//Neu month_prev <> 1 va 12 --> Tinh luon cac thang tu dau nam (de hien duoc so tong ca nam trong bang CMP)
		if (month_prev !=1 && month_prev !=12){
			for(int i=1;i<=month_prev;i++){
				month_data.add(new ReportMonth(i,year_prev));
			}
		}
		month_data.add(new ReportMonth(month_prev,year_prev));
		month_data.add(new ReportMonth(month,year));
		for(int i=0;i<reportmonths;i++){
			int month_next = month + 1;
			int year_next = year;
			if (month_next == 13){
				month_next = 1;
				year_next = year + 1;
			}
			
			month_data.add(new ReportMonth(month_next,year_next));
			month = month_next;
			year = year_next;
		}
		
		//Lay danh sach toan bo cac phan xuong trong he thong
		List<Org> ls_tosx = orgService.findOrgByType(userrootorgid_link, userorgid_link,13);
		int id=0;
		try {
			for(Org theOrg: ls_tosx){
//				System.out.println(theOrg.getCode());
				for(ReportMonth theRMonth: month_data){
					Integer cmp_month = getSalaryFund_Month(theOrg.getId(), theRMonth.getMonth(),theRMonth.getYear());
					
					SalaryFund_Data data_month = new SalaryFund_Data();
					data_month.setId(id);
					data_month.setMonth(theRMonth.getMonth());
					data_month.setYear(theRMonth.getYear());
					data_month.setOrgid_link(theOrg.getId());
					data_month.setOrgname(theOrg.getCode());
	//				data_prev.setParentorgid_link(theOrg.getParentid_link());
	//				data_prev.setParentorgname(theOrg.getParentcode());
					
					data_month.setSalaryfundamount(cmp_month);
					data.add(data_month);
					id++;
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return data;
	}
	private Integer getSalaryFund_Month(long orgid_link, Integer month, Integer year){
		//StartDate from 00:00:00 of the Date 7 of the month
		//EndDate from 23:59:59 of the Date 6 of next month
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
			//Lay danh sach cac Porder trong khoang thoi gian
			Specification<POrder> specification = Specifications.<POrder>and()
					.eq("granttoorgid_link", orgid_link)
					.ge("status", POrderStatus.PORDER_STATUS_FREE)
		            .ge("pcontract_po.shipdate",dateStart)
	                .le("pcontract_po.shipdate",dateEnd)
		            .build();
			Sort sort = Sorts.builder()
			        .desc("ordercode")
			        .build();
			List<POrder> lsPOrder = repoPOrder.findAll(specification,sort);
			Integer totalSalaryFund = 0;
			for(POrder thePOrder: lsPOrder){
				//Neu Price_Sewingcost =0 --> Lay gia Price_Sweingtarget. Else lay gia Price_Sewingcost
				PContract_Price thePrice = priceService.getPrice_CMP(thePOrder.getPcontract_poid_link(), thePOrder.getProductid_link());
				if (null != thePrice) {
					int price = Math.round(thePrice.getPrice_sewingcost()>0?thePrice.getPrice_sewingcost():thePrice.getPrice_sewingtarget());
					int totalorder = Math.round(null==thePOrder.getTotalorder()?0:thePOrder.getTotalorder());
					totalSalaryFund = totalSalaryFund + (price*totalorder);
				} else {
					//Kiem tra xem PO co phai PO con ko
					PContract_PO thePO = poService.findOne(thePOrder.getPcontract_poid_link());
					if (null != thePO){
						if (null != thePO.getParentpoid_link()){
							//Lay gia CMP cua san pham trong PO cha
							PContract_Price thePrice_Parent = priceService.getPrice_CMP(thePO.getParentpoid_link(), thePOrder.getProductid_link());
							if (null != thePrice_Parent){
								int price = Math.round(thePrice_Parent.getPrice_sewingcost()>0?thePrice_Parent.getPrice_sewingcost():thePrice_Parent.getPrice_sewingtarget());
								int totalorder = Math.round(null==thePOrder.getTotalorder()?0:thePOrder.getTotalorder());
								totalSalaryFund = totalSalaryFund + (price*totalorder);							}
						}
					}					
				}
			}
			return totalSalaryFund;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

		
	}
}
