package vn.gpay.gsmart.core.reports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Specifications;

import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.pcontract_price.IPContract_Price_Service;
import vn.gpay.gsmart.core.pcontract_price.PContract_Price;
import vn.gpay.gsmart.core.porder.IPOrder_Repository;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Repository;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
import vn.gpay.gsmart.core.porder_req.IPOrder_Req_Simple_Repository;
import vn.gpay.gsmart.core.utils.POrderStatus;


@Service
public class CMP_Service implements ICMP_Service {
	@Autowired IOrgService orgService;
	@Autowired IPOrder_Repository repoPOrder;
	@Autowired IPOrder_Req_Simple_Repository repoPOrder_Req_Simple;
	@Autowired IPOrderGrant_Repository repoPOrderGrant;
	@Autowired IPContract_Price_Service priceService;
	@Autowired IPContract_POService poService;
	
	@Override
	public List<CMP_Data> getData_ByMonth(Long userrootorgid_link, Long userorgid_link, int month, int year, int reportmonths){
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
		for(int i=0;i<=reportmonths;i++){
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
		List<Org> ls_factory = orgService.findOrgByType(userrootorgid_link, userorgid_link,13);
		int id=0;
		try {
			CountDownLatch latch_cmp = new CountDownLatch(ls_factory.size()*month_data.size());
			CountDownLatch latch_cmp_req = new CountDownLatch(ls_factory.size()*month_data.size());
			
			for(Org theOrg: ls_factory){
//				System.out.println(theOrg.getCode());
				for(ReportMonth theRMonth: month_data){
					//Lenh san xuat
//					Float cmp_month = getCMP_Month(theOrg.getId(), theRMonth.getMonth(),theRMonth.getYear());
//					
					CMP_Data data_month = new CMP_Data();
					data_month.setId(id);
					data_month.setMonth(theRMonth.getMonth());
					data_month.setYear(theRMonth.getYear());
					data_month.setOrgid_link(theOrg.getId());
					data_month.setOrgname("Đã chốt");
					data_month.setParentorgid_link(theOrg.getId());
					data_month.setParentorgname(theOrg.getCode());
//					
//					data_month.setCmpamount(cmp_month);
					data.add(data_month);
					id++;
					CMP_Month_Cal theCMP_Month_Cal =  new CMP_Month_Cal(
							data_month,
							repoPOrder,
							priceService,
							poService,
							latch_cmp);
					theCMP_Month_Cal.start();
					
//					//Yeu cau xep ke hoach
//					Float cmp_month_req = getCMP_Month_Req(theOrg.getId(), theRMonth.getMonth(),theRMonth.getYear());
//					
					CMP_Data data_month_req = new CMP_Data();
					data_month_req.setId(id);
					data_month_req.setMonth(theRMonth.getMonth());
					data_month_req.setYear(theRMonth.getYear());
					data_month_req.setOrgid_link(theOrg.getId());
					data_month_req.setOrgname("Chưa chốt");
					data_month_req.setParentorgid_link(theOrg.getId());
					data_month_req.setParentorgname(theOrg.getCode());
//					
//					data_month_req.setCmpamount(cmp_month_req);
					data.add(data_month_req);
					id++;
					CMP_Month_Req_Cal theCMP_Month_Req_Cal =  new CMP_Month_Req_Cal(
							data_month_req,
							repoPOrder_Req_Simple,
							priceService,
							poService,
							latch_cmp_req);
					theCMP_Month_Req_Cal.start();
				}
			}
			latch_cmp.await();
			latch_cmp_req.await();
			return data;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public List<CMP_Data> getData_ByMonth_ToSX(Long userrootorgid_link, Long userorgid_link, int month, int year, int reportmonths){
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
		
		List<Org> ls_tosx;
		if (userrootorgid_link == userorgid_link)
			//Lay danh sach toan bo cac phan xuong trong he thong
			ls_tosx = orgService.findOrgByType(userrootorgid_link, userorgid_link,14);
		else
			//Lay danh sach toan bo cac to thuoc phan xuong
			ls_tosx = orgService.findChildByType(userrootorgid_link, userorgid_link,14);		
		int id=0;
		try {
			for(Org theOrg: ls_tosx){
//				System.out.println(theOrg.getCode());
				for(ReportMonth theRMonth: month_data){
					Float cmp_month = getCMP_Month_ToSX(theOrg.getId(), theRMonth.getMonth(),theRMonth.getYear());
					
					CMP_Data data_month = new CMP_Data();
					data_month.setId(id);
					data_month.setMonth(theRMonth.getMonth());
					data_month.setYear(theRMonth.getYear());
					data_month.setOrgid_link(theOrg.getId());
					data_month.setOrgname(theOrg.getName());
					data_month.setParentorgid_link(theOrg.getParentid_link());
					data_month.setParentorgname(theOrg.getParentcode());
					
					data_month.setCmpamount(cmp_month);
					data.add(data_month);
					id++;
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return data;
	}	
//	private Float getCMP_Month(long orgid_link, Integer month, Integer year){
//		//StartDate from 00:00:00 of the Date 7 of the month
//		//EndDate from 23:59:59 of the Date 6 of next month
//		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
//		String dateStartString = "07-" + month.toString() + "-" + year.toString() + " 00:00:00";
//		Integer month_next = month + 1;
//		Integer year_next = year;
//		if (month_next == 13){
//			month_next = 1;
//			year_next = year + 1;
//		}
//		String dateEndString = "06-" + month_next.toString() + "-" + year_next.toString() + " 23:59:59";
//		try {
//			Date dateStart = sdf.parse(dateStartString);
//			Date dateEnd = sdf.parse(dateEndString);
//			//Lay danh sach cac Porder trong khoang thoi gian
//			Specification<POrder> specification = Specifications.<POrder>and()
//					.eq("granttoorgid_link", orgid_link)
//					.ge("status", POrderStatus.PORDER_STATUS_FREE)
//		            .ge("pcontract_po.shipdate",dateStart)
//	                .le("pcontract_po.shipdate",dateEnd)
//		            .build();
////			Sort sort = Sorts.builder()
////			        .desc("ordercode")
////			        .build();
//			List<POrder> lsPOrder = repoPOrder.findAll(specification);
////			System.out.println(lsPOrder);
//			Float totalCMP = (float) 0;
//			for(POrder thePOrder: lsPOrder){
//				//Lay gia CMP cua san pham trong pcontract_price
//				PContract_Price thePriceCMP = priceService.getPrice_CMP(thePOrder.getPcontract_poid_link(), thePOrder.getProductid_link());
//				if (null != thePriceCMP) {
//					totalCMP = totalCMP + (null==thePriceCMP.getPrice_cmp()?0:thePriceCMP.getPrice_cmp())*(null==thePOrder.getTotalorder()?0:thePOrder.getTotalorder());
//				} else {
//					//Kiem tra xem PO co phai PO con ko
//					PContract_PO thePO = poService.findOne(thePOrder.getPcontract_poid_link());
//					if (null != thePO){
//						if (null != thePO.getParentpoid_link()){
//							//Lay gia CMP cua san pham trong PO cha
//							PContract_Price thePriceCMP_Parent = priceService.getPrice_CMP(thePO.getParentpoid_link(), thePOrder.getProductid_link());
//							if (null != thePriceCMP_Parent){
//								totalCMP = totalCMP + (null==thePriceCMP_Parent.getPrice_cmp()?0:thePriceCMP_Parent.getPrice_cmp())*(null==thePOrder.getTotalorder()?0:thePOrder.getTotalorder());
//							}
//						}
//					}
//				}
//			}
//			return totalCMP;
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return (float) 0;
//		}
//	}
//	private Float getCMP_Month_Req(long orgid_link, Integer month, Integer year){
//		//StartDate from 00:00:00 of the Date 7 of the month
//		//EndDate from 23:59:59 of the Date 6 of next month
//		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
//		String dateStartString = "07-" + month.toString() + "-" + year.toString() + " 00:00:00";
//		Integer month_next = month + 1;
//		Integer year_next = year;
//		if (month_next == 13){
//			month_next = 1;
//			year_next = year + 1;
//		}
//		String dateEndString = "06-" + month_next.toString() + "-" + year_next.toString() + " 23:59:59";
//		try {
//			Date dateStart = sdf.parse(dateStartString);
//			Date dateEnd = sdf.parse(dateEndString);
//			//Lay danh sach cac Porder trong khoang thoi gian
//			Specification<POrder_Req_Simple> specification = Specifications.<POrder_Req_Simple>and()
//					.eq("granttoorgid_link", orgid_link)
//					.le("status", POrderReqStatus.STATUS_TESTED)
//		            .ge("pcontract_po.shipdate",dateStart)
//	                .le("pcontract_po.shipdate",dateEnd)
//		            .build();
////			Sort sort = Sorts.builder()
////			        .desc("ordercode")
////			        .build();
//			List<POrder_Req_Simple> lsPOrder = repoPOrder_Req_Simple.findAll(specification);
////			System.out.println(lsPOrder);
//			Float totalCMP = (float) 0;
//			for(POrder_Req_Simple thePOrder: lsPOrder){
//				//Lay gia CMP cua san pham trong pcontract_price
//				PContract_Price thePriceCMP = priceService.getPrice_CMP(thePOrder.getPcontract_poid_link(), thePOrder.getProductid_link());
//				if (null != thePriceCMP) {
//					totalCMP = totalCMP + (null==thePriceCMP.getPrice_cmp()?0:thePriceCMP.getPrice_cmp())*(null==thePOrder.getTotalorder()?0:thePOrder.getTotalorder());
//				} else {
//					//Kiem tra xem PO co phai PO con ko
//					PContract_PO thePO = poService.findOne(thePOrder.getPcontract_poid_link());
//					if (null != thePO){
//						if (null != thePO.getParentpoid_link()){
//							//Lay gia CMP cua san pham trong PO cha
//							PContract_Price thePriceCMP_Parent = priceService.getPrice_CMP(thePO.getParentpoid_link(), thePOrder.getProductid_link());
//							if (null != thePriceCMP_Parent){
//								totalCMP = totalCMP + (null==thePriceCMP_Parent.getPrice_cmp()?0:thePriceCMP_Parent.getPrice_cmp())*(null==thePOrder.getTotalorder()?0:thePOrder.getTotalorder());
//							}
//						}
//					}
//				}
//			}
//			return totalCMP;
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return (float) 0;
//		}
//	}
	private Float getCMP_Month_ToSX(long orgid_link, Integer month, Integer year){
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
			//Lay danh sach cac PorderGrant trong khoang thoi gian
			Specification<POrderGrant> specification = Specifications.<POrderGrant>and()
					.eq("granttoorgid_link", orgid_link)
					.ge("status", POrderStatus.PORDER_STATUS_FREE)
//		            .ge("porder.finishdate_plan",dateStart)
//	                .le("porder.finishdate_plan",dateEnd)
		            .ge("finish_date_plan",dateStart)
	                .le("finish_date_plan",dateEnd)
		            .build();
//			Sort sort = Sorts.builder()
//			        .desc("ordercode")
//			        .build();
			List<POrderGrant> lsPOrderGrant = repoPOrderGrant.findAll(specification);
//			if (orgid_link==312){
//				System.out.println(lsPOrderGrant);
//			}
			Float totalCMP = (float) 0;
			for(POrderGrant thePOrder: lsPOrderGrant){
				//Lay gia CMP cua san pham trong pcontract_price
				PContract_Price thePriceCMP = priceService.getPrice_CMP(thePOrder.getPcontract_poid_link(), thePOrder.getProductid_link());
				if (null != thePriceCMP) {
					totalCMP = totalCMP + (null==thePriceCMP.getPrice_cmp()?0:thePriceCMP.getPrice_cmp())*(null==thePOrder.getGrantamount()?0:thePOrder.getGrantamount());
				} else {
					//Kiem tra xem PO co phai PO con ko
					PContract_PO thePO = poService.findOne(thePOrder.getPcontract_poid_link());
					if (null != thePO){
						if (null != thePO.getParentpoid_link()){
							//Lay gia CMP cua san pham trong PO cha
							PContract_Price thePriceCMP_Parent = priceService.getPrice_CMP(thePO.getParentpoid_link(), thePOrder.getProductid_link());
							if (null != thePriceCMP_Parent){
								totalCMP = totalCMP + (null==thePriceCMP_Parent.getPrice_cmp()?0:thePriceCMP_Parent.getPrice_cmp())*(null==thePOrder.getGrantamount()?0:thePOrder.getGrantamount());
							}
						}
					}
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
