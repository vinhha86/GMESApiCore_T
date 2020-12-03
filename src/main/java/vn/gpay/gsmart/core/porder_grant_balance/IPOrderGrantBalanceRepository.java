package vn.gpay.gsmart.core.porder_grant_balance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IPOrderGrantBalanceRepository extends JpaRepository<POrderGrantBalance, Long>, JpaSpecificationExecutor<POrderGrantBalance>{
	@Query("SELECT c FROM POrderGrantBalance c " 
			+ "where c.pordergrantid_link = :pordergrantid_link")
	public List<POrderGrantBalance> getByPorderGrant(
			@Param ("pordergrantid_link")final Long pordergrantid_link
			);
	
	@Query("SELECT c FROM POrderGrantBalance c " 
			+ "where c.pordergrantid_link = :pordergrantid_link "
			+ "and c.porderbalanceid_link = :porderbalanceid_link ")
	public List<POrderGrantBalance> getByPorderGrantAndPorderBalance(
			@Param ("pordergrantid_link")final Long pordergrantid_link,
			@Param ("porderbalanceid_link")final Long porderbalanceid_link
			);
	
	@Query("SELECT c FROM POrderGrantBalance c " 
			+ "where c.pordergrantid_link = :pordergrantid_link "
			+ "and c.personnelid_link = :personnelid_link ")
	public List<POrderGrantBalance> getByPorderGrantAndPersonnel(
			@Param ("pordergrantid_link")final Long pordergrantid_link,
			@Param ("personnelid_link")final Long personnelid_link
			);
}
