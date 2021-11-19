package vn.gpay.gsmart.core.porder_grant_sku_plan;

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
public interface IPOrderGrant_SKU_Plan_Repository extends JpaRepository<POrderGrant_SKU_Plan, Long>, JpaSpecificationExecutor<POrderGrant_SKU_Plan>{
	@Query(value = "select distinct a from POrderGrant_SKU_Plan a " 
			+ "inner join POrderGrant_SKU b on a.porder_grant_skuid_link = b.id "
			+ "inner join POrderGrant c on b.pordergrantid_link = c.id "
			+ "where c.id = :porder_grantid_link "
			+ "and (date(a.date) >= date(:dateFrom) or date(:dateFrom) is null) "
			+ "and (date(a.date) <= date(:dateTo) or date(:dateTo) is null) "
			+ "order by a.porder_grant_skuid_link asc, a.date asc "
			)
	public List<POrderGrant_SKU_Plan> getByPOrderGrant_Date(
			@Param("porder_grantid_link") final Long porder_grantid_link,
			@Param("dateFrom") final Date dateFrom,
			@Param("dateTo") final Date dateTo);
	
	@Query(value = "select a from POrderGrant_SKU_Plan a " 
			+ "inner join POrderGrant_SKU b on a.porder_grant_skuid_link = b.id "
			+ "inner join POrderGrant c on b.pordergrantid_link = c.id "
			+ "where a.porder_grant_skuid_link = :porder_grant_skuid_link "
			+ "and (date(a.date) >= date(:dateFrom) or date(:dateFrom) is null) "
			+ "and (date(a.date) <= date(:dateTo) or date(:dateTo) is null) "
			+ "order by a.porder_grant_skuid_link asc, a.date asc "
			)
	public List<POrderGrant_SKU_Plan> getByPOrderGrant_SKU_Date(
			@Param("porder_grant_skuid_link") final Long porder_grant_skuid_link,
			@Param("dateFrom") final Date dateFrom,
			@Param("dateTo") final Date dateTo);
	
	@Query(value = "select a from POrderGrant_SKU_Plan a " 
			+ "inner join POrderGrant_SKU b on a.porder_grant_skuid_link = b.id "
			+ "inner join POrderGrant c on b.pordergrantid_link = c.id "
			+ "where a.porder_grant_skuid_link = :porder_grant_skuid_link "
			+ "and (date(a.date) = date(:date) or date(:date) is null) "
			+ "order by a.porder_grant_skuid_link asc, a.date asc "
			)
	public List<POrderGrant_SKU_Plan> getByPOrderGrant_SKU_Date(
			@Param("porder_grant_skuid_link") final Long porder_grant_skuid_link,
			@Param("date") final Date date
			);
	
	@Query(value = "select a from POrderGrant_SKU_Plan a " 
			+ "inner join POrderGrant_SKU b on a.porder_grant_skuid_link = b.id "
			+ "where a.porder_grant_skuid_link = :porder_grant_skuid_link "
			+ "and a.id != :id or :id is null "
			+ "order by a.porder_grant_skuid_link asc, a.date asc "
			)
	public List<POrderGrant_SKU_Plan> getByPOrderGrant_SKU_NotId(
			@Param("porder_grant_skuid_link") final Long porder_grant_skuid_link,
			@Param("id") final Long id
			);
	
	@Query(value = "select distinct a.date from POrderGrant_SKU_Plan a " 
			+ "inner join POrderGrant_SKU b on a.porder_grant_skuid_link = b.id "
			+ "inner join POrderGrant c on b.pordergrantid_link = c.id "
			+ "where c.id = :porder_grantid_link "
			+ "and (date(a.date) >= date(:dateFrom) or date(:dateFrom) is null) "
			+ "and (date(a.date) <= date(:dateTo) or date(:dateTo) is null) "
			+ "and (a.is_ordered is null or a.is_ordered is false ) "
			+ "and a.amount > 0 "
			+ "order by a.date asc "
			)
	public List<Date> getDate_ChuaYeuCau(
			@Param("porder_grantid_link") final Long porder_grantid_link,
			@Param("dateFrom") final Date dateFrom,
			@Param("dateTo") final Date dateTo
			);
	
	@Query(value = "select a from POrderGrant_SKU_Plan a " 
			+ "inner join POrderGrant_SKU b on a.porder_grant_skuid_link = b.id "
			+ "inner join POrderGrant c on b.pordergrantid_link = c.id "
			+ "where a.porder_grant_skuid_link = :porder_grant_skuid_link "
//			+ "and (date(a.date) in :date_list or :date_list is null) "
			+ "and (date(a.date) = date(:date) or date(:date) is null) "
			+ "order by a.porder_grant_skuid_link asc, a.date asc "
			)
	public List<POrderGrant_SKU_Plan> getByPOrderGrant_SKU_inDate(
			@Param("porder_grant_skuid_link") final Long porder_grant_skuid_link,
//			@Param("date_list") final List<Date> date_list
			@Param("date") final Date date
			);
}
