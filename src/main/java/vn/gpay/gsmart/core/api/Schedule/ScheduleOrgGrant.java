package vn.gpay.gsmart.core.api.Schedule;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.time.DateUtils;

import vn.gpay.gsmart.core.Schedule.Schedule_plan;
import vn.gpay.gsmart.core.Schedule.Schedule_porder;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
import vn.gpay.gsmart.core.porderprocessing.IPOrderProcessing_Service;
import vn.gpay.gsmart.core.utils.AtributeFixValues;
import vn.gpay.gsmart.core.utils.Common;

public class ScheduleOrgGrant implements Runnable {
	private Thread t;
	private Schedule_plan sch_org_grant;
	private Schedule_plan sch_process;
	private Schedule_plan sch_estimation;
	private Org org_grant;
	private Date startdate;
	private Date toDate;
	private String PO_code;
	private String contractcode;
	private long orgbuyerid_link;
	private long orgvendorid_link;
	private long orgid;
	private long orgrootid_link;
	private List<Schedule_porder> rows_grant;
	private IPOrderGrant_Service granttService;
	private Common commonService;
	private IPOrderProcessing_Service processService;

	CountDownLatch latch;

	ScheduleOrgGrant(Schedule_plan sch_org_grant, Schedule_plan sch_process, Schedule_plan sch_estimation,
			Org org_grant, Date startdate, Date toDate, String PO_code, String contractcode, long orgbuyerid_link,
			long orgvendorid_link, long orgid, long orgrootid_link, List<Schedule_porder> rows_grant,
			IPOrderGrant_Service granttService, Common commonService, IPOrderProcessing_Service processService,
			CountDownLatch latch) {
		this.sch_org_grant = sch_org_grant;
		this.sch_process = sch_process;
		this.sch_estimation = sch_estimation;
		this.org_grant = org_grant;
		this.startdate = startdate;
		this.toDate = toDate;
		this.PO_code = PO_code;
		this.contractcode = contractcode;
		this.orgbuyerid_link = orgbuyerid_link;
		this.orgvendorid_link = orgvendorid_link;
		this.orgid = orgid;
		this.orgrootid_link = orgrootid_link;
		this.rows_grant = rows_grant;
		this.granttService = granttService;
		this.commonService = commonService;
		this.processService = processService;
		this.latch = latch;
	}

	@Override
	public void run() {
		try {
			showOrgGrant();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			latch.countDown();
		}
	}

	public void start() {
		if (t == null) {
			int unboundedRandomValue = ThreadLocalRandom.current().nextInt();
			t = new Thread(this, String.valueOf(unboundedRandomValue));
			t.start();
		}
	}

	// Tinh SL da yeu cau xuat
	private void showOrgGrant() {
		// Lấy các lệnh của các tổ

		List<POrderGrant> list_porder = granttService.get_granted_bygolivedate(startdate, toDate, org_grant.getId(),
				PO_code, contractcode, orgbuyerid_link, orgvendorid_link);

		int day_grant = 0;
		Date date_end = null;
		Date date_start = null;
		int total_po = list_porder.size();
		int total_po_pate = 0;

		for (POrderGrant pordergrant : list_porder) {
			if (pordergrant.getType().equals(1))
				total_po_pate++;
			// neu don hang bi cham
//						total_day = commonService.getDuration(startdate, toDate, orgrootid_link, year);
			Date start = commonService.getBeginOfDate(pordergrant.getStart_date_plan());
			Date end = commonService.getEndOfDate(pordergrant.getFinish_date_plan());
			Date start_free = start, end_free = end;

			if (start_free.before(startdate))
				start_free = startdate;

			if (end_free.after(toDate))
				end_free = toDate;

			if (date_end == null)
				date_end = end_free;
			else if (end_free.after(date_end))
				date_end = end_free;

			if (date_start == null)
				date_start = start_free;
			else if (start_free.before(date_start))
				date_start = start_free;

			int duration = pordergrant.getDuration();
			int productivity = pordergrant.getProductivity();

			Schedule_porder sch_porder = new Schedule_porder();
			if (null != pordergrant.getImgProduct() && pordergrant.getImgProduct().length() > 0)
				sch_porder.setIcon(AtributeFixValues.folder_image + pordergrant.getImgProduct());
			else
				sch_porder.setIcon(null);
			sch_porder.setCls(pordergrant.getCls());
			sch_porder.setEndDate(end);
			sch_porder.setId_origin(pordergrant.getPorderid_link());
			sch_porder.setPorderid_link(pordergrant.getPorderid_link());
			sch_porder.setMahang(pordergrant.getMaHang());
			sch_porder.setName(pordergrant.getMaHang());
			sch_porder.setResourceId(sch_org_grant.getId());
			sch_porder.setStartDate(start);
			sch_porder.setDuration(duration);
			sch_porder.setTotalpackage(pordergrant.getGrantamount());
			sch_porder.setProductivity(productivity);
			sch_porder.setVendorname(pordergrant.getVendorname());
			sch_porder.setBuyername(pordergrant.getBuyername());
			sch_porder.setPordercode(pordergrant.getpordercode());
			sch_porder.setParentid_origin(orgid);
			sch_porder.setStatus(pordergrant.getStatus());
			sch_porder.setPorder_grantid_link(pordergrant.getId());
			sch_porder.setProductid_link(pordergrant.getProductid_link());
			sch_porder.setPcontract_poid_link(pordergrant.getPcontract_poid_link());
			sch_porder.setPcontractid_link(pordergrant.getPcontractid_link());
			sch_porder.setProductbuyercode(pordergrant.getProductcode());
			sch_porder.setIs_show_image(pordergrant.getIs_show_image());

			Integer po_productivity = granttService.getProductivity_PO(pordergrant.getId());
			sch_porder.setProductivity_po(po_productivity);
//						sch_porder.setProductivity_po(1000);

			sch_porder.setProductivity_porder(pordergrant.getProductivity_porder());
			sch_porder.setGrant_type(pordergrant.getType());

			int d = commonService.getDuration(start_free, end_free, orgrootid_link);
			day_grant += d;

			rows_grant.add(sch_porder);
		}

		// Xac dinh so ngay lam viec trong khoang thoi gian dang xem
		if (date_end != null && date_start != null) {
			int total_day = commonService.getDuration(date_start, date_end, orgrootid_link);

			String cls = (total_day - day_grant) <= 0 ? "" : "free";
			sch_org_grant.setCls(cls);
		}

		String name = sch_org_grant.getName();
		name += " - (" + total_po_pate + "/" + total_po + ")";
		sch_org_grant.setName(name);

		// Lay thong tin tien do thuc te cua lenh
//					ArrayList<Thread> arrThreads = new ArrayList<Thread>();
		for (POrderGrant pordergrant : list_porder) {
//						Thread thread = new Thread(){
//						public void run(){						
			// ngay dau va ngay cuoi cua lenh co trang thai > 3 (dang sx)
			POrderGrant theProcessing = processService.get_processing_bygolivedate(pordergrant.getPorderid_link(),
					pordergrant.getId());

			if (null != theProcessing) {
				Date start = commonService.getBeginOfDate(theProcessing.getStart_date_plan());
				Date end = commonService.getEndOfDate(theProcessing.getFinish_date_plan());
				int duration = commonService.getDuration(start, end, orgrootid_link);

				Schedule_porder sch_porder_process = new Schedule_porder();
				sch_porder_process.setCls(pordergrant.getCls());
				sch_porder_process.setEndDate(end);
				sch_porder_process.setId_origin(pordergrant.getPorderid_link());
				sch_porder_process.setPorderid_link(pordergrant.getPorderid_link());
				sch_porder_process.setMahang(pordergrant.getMaHang());
				sch_porder_process.setName(pordergrant.getMaHang());
				sch_porder_process.setResourceId(sch_process.getId());
				sch_porder_process.setStartDate(start);
				sch_porder_process.setDuration(duration);

				// Gia tri ra chyen luy ke tinh den ngay cuoi cung
				sch_porder_process.setTotalpackage(pordergrant.getGrantamount());
				// Gia tri ra chuyen cua ngay cuoi
				sch_porder_process.setProductivity(theProcessing.getAmountcutsum());

				sch_porder_process.setVendorname(pordergrant.getVendorname());
				sch_porder_process.setBuyername(pordergrant.getBuyername());
				sch_porder_process.setPordercode(pordergrant.getpordercode());
				sch_porder_process.setParentid_origin(orgid);
				sch_porder_process.setStatus(pordergrant.getStatus());
				sch_porder_process.setPorder_grantid_link(pordergrant.getId());
				sch_porder_process.setProductid_link(pordergrant.getProductid_link());
				sch_porder_process.setPcontract_poid_link(pordergrant.getPcontract_poid_link());
				sch_porder_process.setPcontractid_link(pordergrant.getPcontractid_link());

				rows_grant.add(sch_porder_process);

				// Thong tin du bao
				Schedule_porder sch_porder_estimation = new Schedule_porder();
				int daystoend = 0;
				if (sch_porder_process.getProductivity() > 0) {
					daystoend = (int) Math
							.ceil((double) pordergrant.getGrantamount() / sch_porder_process.getProductivity());
				} else {
					if (duration > 0) {
						int avarageProductivyty = (int) Math.ceil((double) theProcessing.getGrantamount() / duration);
						if (avarageProductivyty > 0)
							daystoend = (int) Math.ceil((double) pordergrant.getGrantamount() / avarageProductivyty);
						else
							daystoend = 0;
					} else
						daystoend = 0;
				}
				Date end_estimation = commonService.getEndOfDate(DateUtils.addDays(start, daystoend));
				int duration_estimation = commonService.getDuration(start, end_estimation, orgrootid_link);

				sch_porder_estimation.setCls(pordergrant.getCls());
				sch_porder_estimation.setStartDate(start);
				sch_porder_estimation.setEndDate(end_estimation);
				sch_porder_estimation.setDuration(duration_estimation);
				sch_porder_estimation.setTotalpackage(pordergrant.getGrantamount());
				sch_porder_estimation.setProductivity(sch_porder_process.getProductivity());

				sch_porder_estimation.setResourceId(sch_estimation.getId());
				sch_porder_estimation.setMahang(pordergrant.getMaHang());
				sch_porder_estimation.setPordercode(pordergrant.getpordercode());

				rows_grant.add(sch_porder_estimation);
			}
//					    }};
//						thread.start();
//						arrThreads.add(thread);						
		}

//		            for (int i = 0; i < arrThreads.size(); i++) 
//		            {
//		                arrThreads.get(i).join(); 
//		            }

		// Lay cac lenh dang thu
		List<POrderGrant> list_porder_test = granttService.get_porder_test(startdate, toDate, org_grant.getId(),
				PO_code, contractcode, orgbuyerid_link, orgvendorid_link);

		for (POrderGrant pordergrant : list_porder_test) {
			Date start = commonService.getBeginOfDate(pordergrant.getStart_date_plan());
			Date end = commonService.getEndOfDate(pordergrant.getFinish_date_plan());
			int duration = pordergrant.getDuration();
			int productivity = pordergrant.getProductivity();

			Schedule_porder sch_porder = new Schedule_porder();
			if (null != pordergrant.getImgProduct() && pordergrant.getImgProduct().length() > 0)
				sch_porder.setIcon(AtributeFixValues.folder_image + pordergrant.getImgProduct());
			else
				sch_porder.setIcon(null);

			sch_porder.setCls(pordergrant.getCls());
			sch_porder.setEndDate(end);
			sch_porder.setId_origin(pordergrant.getPorderid_link());
			sch_porder.setMahang(pordergrant.getMaHang());
			sch_porder.setName(pordergrant.getMaHang());
			sch_porder.setResourceId(sch_org_grant.getId());
			sch_porder.setStartDate(start);
			sch_porder.setDuration(duration);
			sch_porder.setTotalpackage(pordergrant.getGrantamount());
			sch_porder.setProductivity(productivity);
			sch_porder.setVendorname(pordergrant.getVendorname());
			sch_porder.setBuyername(pordergrant.getBuyername());
			sch_porder.setPordercode(pordergrant.getpordercode());
			sch_porder.setParentid_origin(orgid);
			sch_porder.setStatus(pordergrant.getStatusPorder());
			sch_porder.setPorder_grantid_link(pordergrant.getId());
			sch_porder.setProductid_link(pordergrant.getProductid_link());
			sch_porder.setPcontract_poid_link(pordergrant.getPcontract_poid_link());
			sch_porder.setPorderid_link(pordergrant.getPorderid_link());
			sch_porder.setProductivity_po(pordergrant.getProductivity_po());
			sch_porder.setProductivity_porder(pordergrant.getProductivity_porder());
			sch_porder.setPcontractid_link(pordergrant.getPcontractid_link());
			sch_porder.setProductbuyercode(pordergrant.getProductcode());
			sch_porder.setGrant_type(pordergrant.getType());

//						String FolderPath = commonService.getFolderPath(pordergrant.getProductType());
//						String filename = pordergrant.getImgProduct();
//						if(filename != "" && filename != null) {
//							String uploadRootPath = request.getServletContext().getRealPath("");
//							File uploadRootDir = new File(uploadRootPath);
//							String filePath = uploadRootDir.getParent()+"/"+FolderPath+"/"+ filename;
//							Path path = Paths.get(filePath);
//							byte[] data = Files.readAllBytes(path);
//							sch_porder.setImg(data);
//						}

			rows_grant.add(sch_porder);
		}
	}
}
