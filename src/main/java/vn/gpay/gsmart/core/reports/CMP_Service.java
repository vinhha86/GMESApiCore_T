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
import vn.gpay.gsmart.core.pcontract_price.IPContract_Price_Service;
import vn.gpay.gsmart.core.pcontract_price.PContract_Price;
import vn.gpay.gsmart.core.porder.IPOrder_Repository;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.utils.POrderStatus;


@Service
public class CMP_Service implements ICMP_Service {
	@Autowired IOrgService orgService;
	@Autowired IPOrder_Repository repoPOrder;
	@Autowired IPContract_Price_Service priceService;
	
	@Override
	public List<CMP_Data> getData_3Month(Long userrootorgid_link, Long userorgid_link, int month, int year, int reportmonths){
		List<CMP_Data> data =  new ArrayList<CMP_Data>();
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
			for(int i=1;i<month_prev;i++){
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
					Float cmp_prev = getCMP_Month(theOrg.getId(), theRMonth.getMonth(),theRMonth.getYear());
					
					CMP_Data data_prev = new CMP_Data();
					data_prev.setId(id);
					data_prev.setMonth(theRMonth.getMonth());
					data_prev.setYear(theRMonth.getYear());
					data_prev.setOrgid_link(theOrg.getId());
					data_prev.setOrgname(theOrg.getCode());
	//				data_prev.setParentorgid_link(theOrg.getParentid_link());
	//				data_prev.setParentorgname(theOrg.getParentcode());
					
					data_prev.setCmpamount(cmp_prev);
					data.add(data_prev);
					id++;
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return data;
	}
	private Float getCMP_Month(long orgid_link, Integer month, Integer year){
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
			Float totalCMP = (float) 0;
			for(POrder thePOrder: lsPOrder){
				//Lay gia CMP cua san pham trong pcontract_price
				PContract_Price thePriceCMP = priceService.getPrice_CMP(thePOrder.getPcontract_poid_link(), thePOrder.getProductid_link());
				if (null != thePriceCMP) {
					totalCMP = totalCMP + (null==thePriceCMP.getPrice_cmp()?0:thePriceCMP.getPrice_cmp())*(null==thePOrder.getTotalorder()?0:thePOrder.getTotalorder());
				}
			}
			return totalCMP;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return (float) 0;
		}

		
	}
}
