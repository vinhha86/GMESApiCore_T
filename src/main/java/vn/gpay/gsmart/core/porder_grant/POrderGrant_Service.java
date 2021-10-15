package vn.gpay.gsmart.core.porder_grant;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.porder_grant_balance.IPOrderGrantBalanceService;
import vn.gpay.gsmart.core.porder_grant_balance.POrderGrantBalance;
import vn.gpay.gsmart.core.porder_grant_timesheet.IPOrderGrantTimesheetService;
import vn.gpay.gsmart.core.porder_grant_timesheet.POrderGrantTimesheet;

@Service
public class POrderGrant_Service extends AbstractService<POrderGrant> implements IPOrderGrant_Service {
	@Autowired
	IPOrderGrant_Repository repo;
	@Autowired
	IPOrderGrantBalanceService pordergrantbalanceService;
	@Autowired
	IPOrderGrantTimesheetService pordergranttimesheetService;
	@Autowired
	IPOrderGrant_SKUService pordergrantskuService;

	@Override
	protected JpaRepository<POrderGrant, Long> getRepository() {
		return repo;
	}

	@Override
	public List<POrderGrant> getByOrderCodeAndOrg(Long granttoorgid_link, String ordercode) {
		return repo.getByOrderCodeAndOrg(granttoorgid_link, ordercode);
	}

	@Override
	public POrderGrant getByOrderIDAndOrg(Long granttoorgid_link, Long porderid_link) {
		List<POrderGrant> a = repo.getByOrderIDAndOrg(granttoorgid_link, porderid_link);
		if (a.size() > 0)
			return a.get(0);
		else
			return null;
	}

	@Override
	public void deleteByOrderId(Long porderid_link) {
		for (POrderGrant pordergrant : repo.getByOrderId(porderid_link)) {
			repo.delete(pordergrant);
		}
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public void deleteAll(POrderGrant pordergrant) {
		try {
			// Xoa porder_grant_balance
			for (POrderGrantBalance pordergrant_balance : pordergrantbalanceService
					.getByPorderGrant(pordergrant.getId())) {
				pordergrantbalanceService.delete(pordergrant_balance);
			}
			// Xoa porder_grant_timesheet
			for (POrderGrantTimesheet pordergrant_timesheet : pordergranttimesheetService
					.getByPorderGrant(pordergrant.getId())) {
				pordergranttimesheetService.delete(pordergrant_timesheet);
			}
			// Xoa porder_grant_po
			// Xoa porder_grant_sku
			for (POrderGrant_SKU pordergrant_sku : pordergrantskuService.getPOrderGrant_SKU(pordergrant.getId())) {
				pordergrantskuService.delete(pordergrant_sku);
			}

			repo.delete(pordergrant);
		} catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
		}
	}

	@Override
	// Danh sach cac lenh duoc phan cho Phan xuong va da phan chuyen
	public List<POrderGrant> get_granted_bygolivedate(Date golivedate_from, Date golivedate_to, Long granttoorgid_link,
			String POBuyer, String contractcode, Long orgbuyerid_link, Long orgvendorid_link) {

		int status = 1;
//		POBuyer = "%"+POBuyer+"%";
		orgvendorid_link = orgvendorid_link == 0 ? null : orgvendorid_link;
		orgbuyerid_link = orgbuyerid_link == 0 ? null : orgbuyerid_link;

		List<POrderGrant> a = repo.get_granted_bygolivedate(status, granttoorgid_link, golivedate_from, golivedate_to,
				POBuyer, contractcode, orgbuyerid_link, orgvendorid_link);
		return a;
	}

	@Override
	public List<POrderGrant> get_porder_test(Date golivedate_from, Date golivedate_to, Long granttoorgid_link,
			String POBuyer, String contractcode, Long orgbuyerid_link, Long orgvendorid_link) {
		// TODO Auto-generated method stub
//		POBuyer = "%"+POBuyer+"%";
		orgvendorid_link = orgvendorid_link == 0 ? null : orgvendorid_link;
		orgbuyerid_link = orgbuyerid_link == 0 ? null : orgbuyerid_link;
		List<POrderGrant> a = repo.get_grantedTest_bygolivedate(granttoorgid_link, golivedate_from, golivedate_to,
				POBuyer, contractcode, orgbuyerid_link, orgvendorid_link);
		return a;
	}

	@Override
	public List<POrderGrant> getByOrderId(Long porderid_link) {
		return repo.getByOrderId(porderid_link);
	}

	@Override
	public List<POrderGrant> getByOrgId(Long granttoorgid_link) {
		return repo.getByOrgId(granttoorgid_link);
	}

	@Override
	public List<POrderGrant> getProcessingByOrgId(Long granttoorgid_link) {
		return repo.getProcessingByOrgId(granttoorgid_link);
	}

	@Override
	public List<POrderGrant> getbyporder_andpo(Long porderid_link, Long pcontract_poid_link) {
		return repo.getbyporder_andpo(porderid_link, pcontract_poid_link);
	}

	@Override
	public List<POrderGrant> getgrant_contain_Day(Date holiday) {
		return repo.getByDay(holiday);
	}

	@Override
	public List<POrderGrant> get_grant_change(Long orgid_link) {
		return repo.get_grant_change(orgid_link);
	}

	@Override
	public Integer getProductivity_PO(Long pordergrantid_link) {
		return repo.getProductivity_po(pordergrantid_link);
	}

	@Override
	public List<POrderGrant> getByOfferAndOrg(Long parentpoid_link, List<Long> orgs) {
		orgs = orgs.size() == 0 ? null : orgs;
		return repo.getByOfferAndOrg(parentpoid_link, orgs);
	}

	@Override
	public List<Long> getToSXIdByPcontractPO(Long pcontract_poid_link) {
		return repo.getToSXIdByPcontractPO(pcontract_poid_link);
	}

	@Override
	public List<Long> getGrantPlanByProduct(Long productid_link) {
		// TODO Auto-generated method stub
		return repo.getPlanByProduct(productid_link);
	}
}
