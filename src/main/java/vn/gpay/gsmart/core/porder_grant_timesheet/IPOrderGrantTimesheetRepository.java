package vn.gpay.gsmart.core.porder_grant_timesheet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IPOrderGrantTimesheetRepository extends JpaRepository<POrderGrantTimesheet, Long>, JpaSpecificationExecutor<POrderGrantTimesheet> {
	@Query("SELECT c FROM POrderGrantTimesheet c " 
			+ "where c.pordergrantid_link = :pordergrantid_link "
			+ "and c.porderbalanceid_link = :porderbalanceid_link "
			+ "and c.personnelid_link = :personnelid_link ")
	public List<POrderGrantTimesheet> getByPorderGrantAndPorderBalanceAndPersonnel(
			@Param ("pordergrantid_link")final Long pordergrantid_link,
			@Param ("porderbalanceid_link")final Long porderbalanceid_link,
			@Param ("personnelid_link")final Long personnelid_link
			);
	@Query("SELECT c FROM POrderGrantTimesheet c " 
			+ "where c.pordergrantid_link = :pordergrantid_link "
			+ "and c.porderbalanceid_link = :porderbalanceid_link "
			+ "order by c.time_in desc "
			)
	public List<POrderGrantTimesheet> getByPorderGrantAndPorderBalance(
			@Param ("pordergrantid_link")final Long pordergrantid_link,
			@Param ("porderbalanceid_link")final Long porderbalanceid_link
			);
	
	@Query("SELECT c FROM POrderGrantTimesheet c " 
			+ "where c.pordergrantid_link = :pordergrantid_link"
			)
	public List<POrderGrantTimesheet> getByPorderGrant(
			@Param ("pordergrantid_link")final Long pordergrantid_link
			);
}
