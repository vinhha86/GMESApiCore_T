package vn.gpay.gsmart.core.porder;

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
	public List<POrder> getByContract(@Param ("pcontractid_link")final Long pcontractid_link);
	
	@Query("SELECT c FROM POrder c where c.id = :id")
	public List<POrder> getById(@Param ("id")final Long id);

	@Query("SELECT c FROM POrder c where c.pcontractid_link = :pcontractid_link and c.productid_link = :productid_link")
	public List<POrder> getByContractAndProduct(@Param ("pcontractid_link")final Long pcontractid_link, @Param ("productid_link")final Long productid_link);

	@Query("SELECT c FROM POrder c where c.pcontract_poid_link = :pcontract_poid_link and c.productid_link = :productid_link")
	public List<POrder> getByPOAndProduct(@Param ("pcontract_poid_link")final Long pcontract_poid_link, @Param ("productid_link")final Long productid_link);

	@Query("SELECT c FROM POrder c where c.pcontractid_link = :pcontractid_link and c.pcontract_poid_link = :pcontract_poid_link")
	public List<POrder> getByContractAndPO(@Param ("pcontractid_link")final Long pcontractid_link, @Param ("pcontract_poid_link")final Long pcontract_poid_link);

	@Query("SELECT c FROM POrder c where c.pcontractid_link = :pcontractid_link and c.pcontract_poid_link = :pcontract_poid_link and c.status >= 0")
	public List<POrder> getByContractAndPO_Granted(@Param ("pcontractid_link")final Long pcontractid_link, @Param ("pcontract_poid_link")final Long pcontract_poid_link);

	@Query("SELECT c FROM POrder c where c.porderreqid_link = :porderreqid_link and c.pcontract_poid_link = :pcontract_poid_link")
	public List<POrder> getByPOrder_Req(@Param ("pcontract_poid_link")final Long pcontract_poid_link, @Param ("porderreqid_link")final Long porderreqid_link);

	@Query("SELECT c FROM POrder c where c.status = :status")
	public List<POrder> getByStatus(@Param ("status")final Integer status);
	
	@Query("SELECT c FROM POrder c where c.salarymonth is null")
	public List<POrder> getAll_SalaryUngranted(@Nullable Specification<POrder> spec, Sort sort);
	
	@Query(value = "select MAX(a.priority) from POrder a where a.status = 2")
	public Integer getMaxPriority();
	
	@Query(value = "select c from POrder c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and ordercode = :ordercode "
			+ "and status <> -1")
	public List<POrder> get_by_code(@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("ordercode")final  String ordercode);
	
	@Query(value = "select c from POrder c "
			+ "where lower(c.ordercode) like lower(concat('%',:ordercode,'%')) "
			+ "and status > 0"
			+ "and status < 5"
			)
	public List<POrder> getPOrderByOrdercode(
			@Param ("ordercode")final String ordercode);
	
	@Query(value = "select c from POrder c "
			+ "where lower(c.ordercode) = lower(:ordercode) "
			+ "and status > 0"
			+ "and status < 5"
			)
	public List<POrder> getPOrderByExactOrdercode(
			@Param ("ordercode")final String ordercode);
	
	@Query(value = "select a from POrder a "
			+ "inner join PContract b on a.pcontractid_link = b.id "
			+ "inner join PContract_PO c on a.pcontract_poid_link = c.id "
			+ "inner join Product d on a.productid_link = d.id "
			+ "where  (b.orgbuyerid_link = :buyerid or :buyerid is null) "
			+ "and (b.orgvendorid_link = :vendorid or :vendorid is null) "
			+ "and (a.granttoorgid_link = :factoryid or :factoryid is null) "
			+ "and (a.granttoorgid_link = :granttoorgid_link or :granttoorgid_link is null) "
			+ "and lower(c.po_buyer) like lower(concat('%',:pobuyer,'%')) "
			+ "and lower(d.buyercode) like lower(concat('%',:stylebuyer,'%')) "
//			+ "and (:statuses is null or a.status in :statuses) "
			+ "and a.status in :statuses "
			+ "order by a.granttoorgid_link, d.buyercode, a.golivedate "
			)
	public List<POrder> getPOrderBySearch(
			@Param ("buyerid")final Long buyerid,
			@Param ("vendorid")final Long vendorid,
			@Param ("factoryid")final Long factoryid,
			@Param ("pobuyer")final String pobuyer,
			@Param ("stylebuyer")final String stylebuyer,
			@Param ("statuses")final List<Integer> statuses,
			@Param ("granttoorgid_link")final Long granttoorgid_link
			);
	
	@Query(value = "select a from POrder a "
			+ "inner join PContract b on a.pcontractid_link = b.id "
			+ "inner join PContract_PO c on a.pcontract_poid_link = c.id "
			+ "inner join Product d on a.productid_link = d.id "
			+ "where  (b.orgbuyerid_link = :buyerid or :buyerid is null) "
			+ "and (b.orgvendorid_link = :vendorid or :vendorid is null) "
			+ "and (a.granttoorgid_link = :factoryid or :factoryid is null) "
			+ "and (a.granttoorgid_link = :granttoorgid_link or :granttoorgid_link is null) "
			+ "and lower(c.po_buyer) like lower(concat('%',:pobuyer,'%')) "
			+ "and lower(d.buyercode) like lower(concat('%',:stylebuyer,'%')) "
			+ "order by a.granttoorgid_link, d.buyercode, a.golivedate "
			)
	public List<POrder> getPOrderBySearch(
			@Param ("buyerid")final Long buyerid,
			@Param ("vendorid")final Long vendorid,
			@Param ("factoryid")final Long factoryid,
			@Param ("pobuyer")final String pobuyer,
			@Param ("stylebuyer")final String stylebuyer,
			@Param ("granttoorgid_link")final Long granttoorgid_link
			);
	
	
	
	@Query(value = "select sum(a.totalorder), b.name, a.status, b.id, b.code "
			+ "from POrder a "
			+ "inner join Org b on a.granttoorgid_link = b.id "
			+ "where a.status = 0 or a.status = 1 "
			+ "group by b.id, b.name, b.code, a.status "
			+ "order by b.id "
			)
	public List<Object[]> getForNotInProductionChart();
}
