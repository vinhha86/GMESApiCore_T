package vn.gpay.gsmart.core.porder;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPOrder_Repository extends JpaRepository<POrder, Long>, JpaSpecificationExecutor<POrder> {
	@Query("SELECT c FROM POrder c where c.pcontractid_link = :pcontractid_link")
	public List<POrder> getByContract(@Param("pcontractid_link") final Long pcontractid_link);

	@Query("SELECT c FROM POrder c where c.id = :id")
	public List<POrder> getById(@Param("id") final Long id);

	@Query("SELECT c FROM POrder c where c.pcontractid_link = :pcontractid_link and c.productid_link = :productid_link")
	public List<POrder> getByContractAndProduct(@Param("pcontractid_link") final Long pcontractid_link,
			@Param("productid_link") final Long productid_link);

	@Query("SELECT c FROM POrder c where c.pcontract_poid_link = :pcontract_poid_link and c.productid_link = :productid_link")
	public List<POrder> getByPOAndProduct(@Param("pcontract_poid_link") final Long pcontract_poid_link,
			@Param("productid_link") final Long productid_link);

	@Query("SELECT c FROM POrder c " + "where c.pcontractid_link = :pcontractid_link "
			+ "and c.pcontract_poid_link = :pcontract_poid_link")
	public List<POrder> getByContractAndPO(@Param("pcontractid_link") final Long pcontractid_link,
			@Param("pcontract_poid_link") final Long pcontract_poid_link);

	@Query("SELECT c FROM POrder c " + "where c.pcontractid_link = :pcontractid_link "
			+ "and c.pcontract_poid_link = :pcontract_poid_link " + "and c.status >= 0")
	public List<POrder> getByContractAndPO_Granted(@Param("pcontractid_link") final Long pcontractid_link,
			@Param("pcontract_poid_link") final Long pcontract_poid_link);

	@Query("SELECT c FROM POrder c where c.porderreqid_link = :porderreqid_link and c.pcontract_poid_link = :pcontract_poid_link")
	public List<POrder> getByPOrder_Req(@Param("pcontract_poid_link") final Long pcontract_poid_link,
			@Param("porderreqid_link") final Long porderreqid_link);

	@Query("SELECT c FROM POrder c where c.status = :status")
	public List<POrder> getByStatus(@Param("status") final Integer status);

	@Query("SELECT c FROM POrder c where c.salarymonth is null")
	public List<POrder> getAll_SalaryUngranted(@Nullable Specification<POrder> spec, Sort sort);

	@Query(value = "select MAX(a.priority) from POrder a where a.status = 2")
	public Integer getMaxPriority();

	@Query(value = "select c from POrder c " + "inner join PContract_PO a on c.pcontract_poid_link = a.id "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and lower(a.po_buyer) like lower(concat('%',:ordercode,'%')) " + "and c.status <> -1")
	public List<POrder> get_by_code(@Param("orgrootid_link") final Long orgrootid_link,
			@Param("ordercode") final String ordercode);

	@Query(value = "select c from POrder c " + "inner join POrderGrant b on b.porderid_link = c.id "
			+ "where lower(c.ordercode) like lower(concat('%',:ordercode,'%')) "
			+ "and (:granttoorgid_link is null or b.granttoorgid_link = :granttoorgid_link) " + "and c.status > 0"
			+ "and c.status < 5")
	public List<POrder> getPOrderByOrdercode(@Param("ordercode") final String ordercode,
			@Param("granttoorgid_link") final Long granttoorgid_link);

	@Query(value = "select c from POrder c " + "where lower(c.ordercode) = lower(:ordercode) " + "and status > 0"
			+ "and status < 5")
	public List<POrder> getPOrderByExactOrdercode(@Param("ordercode") final String ordercode);

	@Query(value = "select  a from POrder a " + "inner join PContract b on a.pcontractid_link = b.id "
			+ "inner join PContract_PO c on a.pcontract_poid_link = c.id "
			+ "inner join Product d on a.productid_link = d.id "
			+ "where  (b.orgbuyerid_link = :buyerid or :buyerid is null) "
			+ "and (b.orgvendorid_link = :vendorid or :vendorid is null) "
			+ "and (a.granttoorgid_link = :factoryid or :factoryid is null) "
			+ "and (a.granttoorgid_link = :granttoorgid_link or :granttoorgid_link is null) "
			+ "and lower(c.po_buyer) like lower(concat('%',:pobuyer,'%')) "
			+ "and (d.buyercode is null or lower(d.buyercode) like lower(concat('%',:stylebuyer,'%'))) "
			+ "and lower(b.contractcode) like lower(concat('%',:contractcode,'%')) "
//			+ "and (:statuses is null or a.status in :statuses) "
			+ "and (a.status in :statuses or :statuses is null) "
			+ "and (CAST(:golivedatefrom AS date) IS NULL or a.golivedate >= :golivedatefrom) "
			+ "and (CAST(:golivedateto AS date) IS NULL or a.golivedate <= :golivedateto) "
			+ "order by a.granttoorgid_link, d.buyercode, a.golivedate "
//			+ "limit 0,1000 "
	)
	public List<POrder> getPOrderBySearch(@Param("buyerid") final Long buyerid, @Param("vendorid") final Long vendorid,
			@Param("factoryid") final Long factoryid, @Param("pobuyer") final String pobuyer,
			@Param("stylebuyer") final String stylebuyer, @Param("contractcode") final String contractcode,
			@Param("statuses") final List<Integer> statuses, @Param("granttoorgid_link") final Long granttoorgid_link,
			@Param("golivedatefrom") final Date golivedatefrom, @Param("golivedateto") final Date golivedateto);

	@Query(value = "select a from POrder a " + "inner join PContract b on a.pcontractid_link = b.id "
			+ "inner join PContract_PO c on a.pcontract_poid_link = c.id "
			+ "inner join Product d on a.productid_link = d.id "
			+ "where  (b.orgbuyerid_link = :buyerid or :buyerid is null) "
			+ "and (b.orgvendorid_link = :vendorid or :vendorid is null) "
			+ "and (a.granttoorgid_link = :factoryid or :factoryid is null) "
			+ "and (a.granttoorgid_link = :granttoorgid_link or :granttoorgid_link is null) "
			+ "and lower(c.po_buyer) like lower(concat('%',:pobuyer,'%')) "
			+ "and lower(d.buyercode) like lower(concat('%',:stylebuyer,'%')) "
			+ "and lower(b.contractcode) like lower(concat('%',:contractcode,'%')) "
			+ "and (CAST(:golivedatefrom AS date) IS NULL or a.golivedate >= :golivedatefrom) "
			+ "and (CAST(:golivedateto AS date) IS NULL or a.golivedate <= :golivedateto) "
			+ "order by a.granttoorgid_link, d.buyercode, a.golivedate "
//			+ "limit 0,1000 "
	)
	public List<POrder> getPOrderBySearch(@Param("buyerid") final Long buyerid, @Param("vendorid") final Long vendorid,
			@Param("factoryid") final Long factoryid, @Param("pobuyer") final String pobuyer,
			@Param("stylebuyer") final String stylebuyer, @Param("contractcode") final String contractcode,
			@Param("granttoorgid_link") final Long granttoorgid_link,
			@Param("golivedatefrom") final Date golivedatefrom, @Param("golivedateto") final Date golivedateto);

	@Query(value = "select sum(a.totalorder), b.name, a.status, b.id, b.code " + "from POrder a "
			+ "inner join Org b on a.granttoorgid_link = b.id " + "where a.status = 0 or a.status = 1 "
			+ "group by b.id, b.name, b.code, a.status " + "order by b.id ")
	public List<Object[]> getForNotInProductionChart();

	@Query(value = "select a from POrder a " + "inner join PContract_PO b on a.pcontract_poid_link = b.id "
			+ "inner join PContract c on c.id = b.pcontractid_link " + "where a.granttoorgid_link = :granttoorgid_link "
			+ "and a.status = 0 " + "and (c.orgvendorid_link in :vendors or :vendors is null) "
			+ "and (c.orgbuyerid_link in :buyers or :buyers is null) " + "and b.shipdate = "
			+ "(select min(d.shipdate) from POrder e " + "inner join PContract_PO d on d.id = e.pcontract_poid_link "
			+ "inner join PContract f on f.id = d.pcontractid_link " + "where d.parentpoid_link = b.parentpoid_link "
			+ "and (f.orgvendorid_link in :vendors or :vendors is null) "
			+ "and (f.orgbuyerid_link in :buyers or :buyers is null) " + "and e.granttoorgid_link = :granttoorgid_link "
			+ "and e.status = 0) " + " group by a")
	public List<POrder> getfree_groupby_product(@Param("granttoorgid_link") final Long granttoorgid_link,
			@Param("vendors") final List<Long> vendors, @Param("buyers") final List<Long> buyers);

	@Query(value = "select a from POrder a " + "inner join PContract_PO b on a.pcontract_poid_link = b.id "
			+ "where a.granttoorgid_link = :orgid_link " + "and a.productid_link = :productid_link "
			+ "and b.parentpoid_link = :pcontract_poid_link " + "and a.status = 0 " + "order by b.shipdate asc")
	public List<POrder> getby_offer(@Param("orgid_link") final Long orgid_link,
			@Param("pcontract_poid_link") final Long pcontract_poid_link,
			@Param("productid_link") final Long productid_link);

	@Query(value = "select a from POrder a " + "inner join PContract_PO b on a.pcontract_poid_link = b.id "
			+ "where a.granttoorgid_link in :orgs " + "and b.parentpoid_link = :parentid_link "
//			+ "and (a.ismap is null or a.ismap = 'False') "
			+ "order by b.shipdate asc")
	public List<POrder> getall_by_offer_and_orgs(@Param("parentid_link") final Long parentid_link,
			@Param("orgs") final List<Long> orgs);

	@Query(value = "select a from POrder a " + "inner join Product b on a.productid_link = b.id "
			+ "where (TRIM(LOWER(a.ordercode)) like TRIM(LOWER(concat('%',:ordercode,'%'))) ) "
			+ "and (TRIM(LOWER(b.buyercode)) like TRIM(LOWER(concat('%',:buyercode,'%'))) ) "
			+ "and :granttoorgid_link = a.granttoorgid_link " + "order by a.ordercode asc ")
	public List<POrder> getPOrderByOrderCodeAndProductBuyerCode(
			@Param("granttoorgid_link") final Long granttoorgid_link, @Param("ordercode") final String ordercode,
			@Param("buyercode") final String buyercode);

	@Query(value = "select c from POrder c " + "where lower(c.ordercode) like lower(concat('%',:ordercode,'%')) "
			+ "and c.granttoorgid_link = :granttoorgid_link " + "and c.status > 0" + "and c.status < 5")
	public List<POrder> getPorderByOrdercodeAndOrg(@Param("ordercode") final String ordercode,
			@Param("granttoorgid_link") final Long granttoorgid_link);

	@Query("SELECT c FROM POrder c where c.pcontract_poid_link = :pcontract_poid_link")
	public List<POrder> getByPcontractPO(@Param("pcontract_poid_link") final Long pcontract_poid_link);

	@Query(value = "select a from POrder a "
			+ "where (CAST(:golivedatefrom AS date) IS NULL or a.golivedate >= :golivedatefrom) "
			+ "and (CAST(:golivedateto AS date) IS NULL or a.golivedate <= :golivedateto) " + "and a.status >= 0 ")
	public List<POrder> findByGolivedate(@Param("golivedatefrom") final Date golivedatefrom,
			@Param("golivedateto") final Date golivedateto);

	@Query(value = "select count(a.id) from POrder a "
			+ "where (CAST(:golivedatefrom AS date) IS NULL or a.golivedate >= :golivedatefrom) "
			+ "and (CAST(:golivedateto AS date) IS NULL or a.golivedate <= :golivedateto) "
			+ "and a.granttoorgid_link = :granttoorgid_link " + "and a.status in :statuses ")
	public Long findTotalByGolivedate(@Param("golivedatefrom") final Date golivedatefrom,
			@Param("golivedateto") final Date golivedateto, @Param("granttoorgid_link") final Long granttoorgid_link,
			@Param("statuses") final List<Integer> statuses);

	@Query(value = "select count(a.id) from POrder a " + "inner join POrder_POLine b on b.porderid_link = a.id "
			+ "inner join PContract_PO c on b.pcontract_poid_link = c.id "
			+ "where (CAST(:golivedatefrom AS date) IS NULL or a.golivedate >= :golivedatefrom) "
			+ "and (CAST(:golivedateto AS date) IS NULL or a.golivedate <= :golivedateto) "
			+ "and a.granttoorgid_link = :granttoorgid_link " + "and a.status in :statuses "
//			+ "and (c.shipdate IS NOT NULL and a.finishdate_fact IS NOT NULL) "
			+ "and c.shipdate - a.finishdate_fact < 10 " + "and c.shipdate - a.finishdate_fact >= 5 ")
	public Long findTotalByGolivedate_SlowSmall(@Param("golivedatefrom") final Date golivedatefrom,
			@Param("golivedateto") final Date golivedateto, @Param("granttoorgid_link") final Long granttoorgid_link,
			@Param("statuses") final List<Integer> statuses);

	@Query(value = "select count(a.id) from POrder a " + "inner join POrder_POLine b on b.porderid_link = a.id "
			+ "inner join PContract_PO c on b.pcontract_poid_link = c.id "
			+ "where (CAST(:golivedatefrom AS date) IS NULL or a.golivedate >= :golivedatefrom) "
			+ "and (CAST(:golivedateto AS date) IS NULL or a.golivedate <= :golivedateto) "
			+ "and a.granttoorgid_link = :granttoorgid_link " + "and a.status in :statuses "
//			+ "and (c.shipdate IS NOT NULL and a.finishdate_fact IS NOT NULL) "
			+ "and c.shipdate - a.finishdate_fact < 5 " + "and c.shipdate - a.finishdate_fact >= 0 ")
	public Long findTotalByGolivedate_SlowMedium(@Param("golivedatefrom") final Date golivedatefrom,
			@Param("golivedateto") final Date golivedateto, @Param("granttoorgid_link") final Long granttoorgid_link,
			@Param("statuses") final List<Integer> statuses);

	@Query(value = "select count(a.id) from POrder a " + "inner join POrder_POLine b on b.porderid_link = a.id "
			+ "inner join PContract_PO c on b.pcontract_poid_link = c.id "
			+ "where (CAST(:golivedatefrom AS date) IS NULL or a.golivedate >= :golivedatefrom) "
			+ "and (CAST(:golivedateto AS date) IS NULL or a.golivedate <= :golivedateto) "
			+ "and a.granttoorgid_link = :granttoorgid_link " + "and a.status in :statuses "
//			+ "and (c.shipdate IS NOT NULL and a.finishdate_fact IS NOT NULL) "
			+ "and c.shipdate - a.finishdate_fact < 0 ")
	public Long findTotalByGolivedate_SlowBig(@Param("golivedatefrom") final Date golivedatefrom,
			@Param("golivedateto") final Date golivedateto, @Param("granttoorgid_link") final Long granttoorgid_link,
			@Param("statuses") final List<Integer> statuses);

	@Query(value = "select sum(pquantity_total) from POrder_Product_SKU " + "where porderid_link = :porderid_link ")
	public Integer getTotalOrder(@Param("porderid_link") final Long porderid_link);
}
