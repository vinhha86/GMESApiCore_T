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
	@Query(value = "select a from POrderGrant_SKU_Plan a " 
			+ "where a.porder_grant_skuid_link = :porder_grant_skuid_link "
			+ "and (a.date >= :dateFrom or :dateFrom is null) "
			+ "and (a.date <= :dateTo or :dateTo is null) "
			)
	public List<POrderGrant_SKU_Plan> getByPorderGrantSku_Date(
			@Param("porder_grant_skuid_link") final Long porder_grant_skuid_link,
			@Param("dateFrom") final Date dateFrom,
			@Param("dateTo") final Date dateTo);
}
