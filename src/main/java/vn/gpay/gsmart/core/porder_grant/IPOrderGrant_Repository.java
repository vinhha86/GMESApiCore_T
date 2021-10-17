package vn.gpay.gsmart.core.porder_grant;
//import java.util.List;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPOrderGrant_Repository
		extends JpaRepository<POrderGrant, Long>, JpaSpecificationExecutor<POrderGrant> {
	@Query(value = "select a from POrderGrant a where a.granttoorgid_link = :granttoorgid_link and a.ordercode = :ordercode")
	public List<POrderGrant> getByOrderCodeAndOrg(@Param("granttoorgid_link") final Long granttoorgid_link,
			@Param("ordercode") final String ordercode);

	@Query(value = "select a from POrderGrant a where a.granttoorgid_link = :granttoorgid_link and a.porderid_link = :porderid_link")
	public List<POrderGrant> getByOrderIDAndOrg(@Param("granttoorgid_link") final Long granttoorgid_link,
			@Param("porderid_link") final Long porderid_link);

	@Query(value = "select a from POrderGrant a where a.porderid_link = :porderid_link")
	public List<POrderGrant> getByOrderId(@Param("porderid_link") final Long porderid_link);

	@Query(value = "select a from POrderGrant a " + "inner join POrder b on a.porderid_link = b.id "
					+ "where a.granttoorgid_link = :granttoorgid_link "
			+ "and b.status >= :status " + "and a.finish_date_plan >= :golivedate_from "
			+ "and a.finish_date_plan <= :golivedate_to "
			+ "and (a.status = 2 or (a.status = 1 and (a.ismap = 'False' or a.ismap is null))) ")
	public List<POrderGrant> get_granted_bygolivedate(@Param("status") final int status,
			@Param("granttoorgid_link") final long granttoorgid_link,
			@Param("golivedate_from") final Date golivedate_from, @Param("golivedate_to") final Date golivedate_to);

	@Query(value = "select a from POrderGrant a " + "inner join POrder b on a.porderid_link = b.id "
			+ "inner join PContract_PO c on b.pcontract_poid_link = c.id "
			+ "inner join PContract d on b.pcontractid_link = d.id " + "where a.granttoorgid_link = :granttoorgid_link "
			+ "and b.status = -1 " + "and b.finishdate_plan >= :golivedate_from "
			+ "and b.finishdate_plan <= :golivedate_to "
//			+ "and c.po_buyer like :POBuyer "
			+ "and lower(c.po_buyer) like lower(concat('%',:POBuyer,'%')) "
			+ "and lower(d.contractcode) like lower(concat('%',:contractcode,'%')) "
			+ "and (:orgbuyerid_link is null or d.orgbuyerid_link = :orgbuyerid_link) "
			+ "and (:orgvendorid_link is null or d.orgvendorid_link = :orgvendorid_link)")
	public List<POrderGrant> get_grantedTest_bygolivedate(@Param("granttoorgid_link") final long granttoorgid_link,
			@Param("golivedate_from") final Date golivedate_from, @Param("golivedate_to") final Date golivedate_to,
			@Param("POBuyer") final String POBuyer, @Param("contractcode") final String contractcode,
			@Param("orgbuyerid_link") final Long orgbuyerid_link,
			@Param("orgvendorid_link") final Long orgvendorid_link);

	@Query(value = "select a from POrderGrant a where a.granttoorgid_link = :granttoorgid_link")
	public List<POrderGrant> getByOrgId(@Param("granttoorgid_link") final Long granttoorgid_link);

	@Query(value = "select a from POrderGrant a where (a.granttoorgid_link = :orgid_link or :orgid_link is null) "
			+ "and reason_change is not null")
	public List<POrderGrant> get_grant_change(@Param("orgid_link") final Long orgid_link);

	@Query(value = "select a from POrderGrant a " + "where a.start_date_plan <= :holiday "
			+ "and a.finish_date_plan >= :holiday")
	public List<POrderGrant> getByDay(@Param("holiday") final Date holiday);

	@Query(value = "select a from POrderGrant a where" + " a.granttoorgid_link = :granttoorgid_link"
			+ " and a.status >= 4")
	public List<POrderGrant> getProcessingByOrgId(@Param("granttoorgid_link") final Long granttoorgid_link);

	@Query(value = "select a from POrderGrant a " + "inner join POrderGrant_SKU b on a.id = b.pordergrantid_link "
			+ "where (a.porderid_link = :porderid_link or :porderid_link is null) "
			+ " and b.pcontract_poid_link =  :pcontract_poid_link " + "group by a")
	public List<POrderGrant> getbyporder_andpo(@Param("porderid_link") final Long porderid_link,
			@Param("pcontract_poid_link") final Long pcontract_poid_link);

	@Query(value = "select c.plan_productivity from POrderGrant a " + "inner join POrder b on a.porderid_link = b.id "
			+ "inner join PContract_PO c on b.pcontract_poid_link = c.id " + "where a.id = :pordergrantid_link")
	public Integer getProductivity_po(@Param("pordergrantid_link") final Long pordergrantid_link);

	@Query(value = "select a from POrderGrant a " + "inner join POrder b on a.porderid_link = b.id "
			+ "inner join PContract_PO c on b.pcontract_poid_link = c.id "
			+ "where c.parentpoid_link = :parentpoid_link and (b.granttoorgid_link in :orgs or :orgs is null) and (a.ismap = false or a.ismap is null)")
	public List<POrderGrant> getByOfferAndOrg(@Param("parentpoid_link") final Long parentpoid_link,
			@Param("orgs") final List<Long> orgs);
	
	@Query(value = "select distinct a.granttoorgid_link from POrderGrant a " 
			+ "inner join POrder b on a.porderid_link = b.id "
			+ "inner join PContract_PO c on b.pcontract_poid_link = c.id "
			+ "where c.id = :pcontract_poid_link ")
	public List<Long> getToSXIdByPcontractPO(
			@Param("pcontract_poid_link") final Long pcontract_poid_link);
	
	@Query(value = "select distinct a.id from POrderGrant a " 
			+ "inner join POrder b on a.porderid_link = b.id "
			+ "inner join PContract_PO c on b.pcontract_poid_link = c.id "
			+ "where b.productid_link = :productid_link and c.po_typeid_link = 10")
	public List<Long> getPlanByProduct(
			@Param("productid_link") final Long productid_link);

}
