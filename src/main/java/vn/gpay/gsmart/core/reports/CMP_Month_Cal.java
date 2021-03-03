package vn.gpay.gsmart.core.reports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.data.jpa.domain.Specification;

import com.github.wenhao.jpa.Specifications;

import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.pcontract_price.IPContract_Price_Service;
import vn.gpay.gsmart.core.pcontract_price.PContract_Price;
import vn.gpay.gsmart.core.porder.IPOrder_Repository;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.utils.POrderStatus;

public class CMP_Month_Cal implements Runnable{
	private Thread t;
	private IPOrder_Repository repoPOrder;
	private IPContract_Price_Service priceService;
	private IPContract_POService poService;
	CMP_Data data_month;
	
	String token;
	CountDownLatch latch;
	
	CMP_Month_Cal(
			CMP_Data data_month,
			IPOrder_Repository repoPOrder,
			IPContract_Price_Service priceService,
			IPContract_POService poService,
			CountDownLatch latch){
		this.data_month = data_month;
		this.repoPOrder = repoPOrder;
		this.priceService =priceService;
		this.poService = poService;
		
		this.latch = latch;
	}
	@Override
	public void run() {
		try {
			//Lenh san xuat
			Float cmp_month = getCMP_Month(data_month.getOrgid_link(),data_month.getMonth(),data_month.getYear());

			data_month.setCmpamount(cmp_month);
//			System.out.println(data_month.getOrgname() + "-" + cmp_month);
			
			latch.countDown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void start () {
		if (t == null) {
			int unboundedRandomValue = ThreadLocalRandom.current().nextInt();
			t = new Thread (this, String.valueOf(unboundedRandomValue));
			t.start ();
		}
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
//			Sort sort = Sorts.builder()
//			        .desc("ordercode")
//			        .build();
			List<POrder> lsPOrder = repoPOrder.findAll(specification);
//			System.out.println(lsPOrder);
			Float totalCMP = (float) 0;
			for(POrder thePOrder: lsPOrder){
				//Lay gia CMP cua san pham trong pcontract_price
				PContract_Price thePriceCMP = priceService.getPrice_CMP(thePOrder.getPcontract_poid_link(), thePOrder.getProductid_link());
				if (null != thePriceCMP) {
					totalCMP = totalCMP + (null==thePriceCMP.getPrice_cmp()?0:thePriceCMP.getPrice_cmp())*(null==thePOrder.getTotalorder()?0:thePOrder.getTotalorder());
				} else {
					//Kiem tra xem PO co phai PO con ko
					PContract_PO thePO = poService.findOne(thePOrder.getPcontract_poid_link());
					if (null != thePO){
						if (null != thePO.getParentpoid_link()){
							//Lay gia CMP cua san pham trong PO cha
							PContract_Price thePriceCMP_Parent = priceService.getPrice_CMP(thePO.getParentpoid_link(), thePOrder.getProductid_link());
							if (null != thePriceCMP_Parent){
								totalCMP = totalCMP + (null==thePriceCMP_Parent.getPrice_cmp()?0:thePriceCMP_Parent.getPrice_cmp())*(null==thePOrder.getTotalorder()?0:thePOrder.getTotalorder());
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
