package vn.gpay.gsmart.core.stockout;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface StockOutRepository extends JpaRepository<StockOut, Long>,JpaSpecificationExecutor<StockOut>{


	@Query(value = "select c from StockOut c where orgid_link =:orgid_link and stockoutcode =:stockoutcode")
	public List<StockOut> findByStockinCode(@Param ("orgid_link")final Long orgid_link ,@Param ("stockoutcode")final String stockoutcode);
	
	@Query(value = "select c from StockOut c where orgid_link =:orgid_link and stockoutcode =:stockoutcode and orgid_to_link =:stockcode")
	public List<StockOut> stockout_getone(@Param ("orgid_link")final Long orgid_link ,@Param ("stockoutcode")final String stockoutcode, @Param ("stockcode")final String stockcode);
	
	
	@Query(value = "select c from StockOut c where orgid_link =:orgid_link "
			+ " and ( coalesce(:orgid_to_link, null) is null  or (( coalesce(:orgid_to_link, null) is not null ) and c.orgid_to_link=:orgid_to_link ) )"
			+ " and ( coalesce(:stockouttypeid_link, null) is null  or (( coalesce(:stockouttypeid_link, null) is not null ) and c.stockouttypeid_link=:stockouttypeid_link ) )"
			+ " and (  c.stockoutcode like '%'||:stockoutcode ||'%')"
			+ "     and   ( (coalesce(:stockoutdate_from, null) is null and coalesce(:stockoutdate_to, null) is null) "
	        + "             or ((coalesce(c.stockoutdate, null) is not null) "
	        + "                 and ( "
	        + "                        ((coalesce(:stockoutdate_from, null) is null and coalesce(:stockoutdate_to, null) is not null) and c.stockoutdate <= :stockoutdate_to) "
	        + "                     or ((coalesce(:stockoutdate_to, null) is null and coalesce(:stockoutdate_from, null) is not null) and c.stockoutdate >= :stockoutdate_from)"
	        + "                     or (c.stockoutdate between :stockoutdate_from and :stockoutdate_to)"
	        + "                 )"
	        + "             )"
	        + "       ) "
			)
	public List<StockOut> stockout_list(@Param ("orgid_link")final Long orgid_link,@Param ("stockouttypeid_link")final Integer stockouttypeid_link ,@Param ("stockoutcode")final String stockoutcode, @Param ("orgid_to_link")final Long orgid_to_link,
			@Param ("stockoutdate_from")final Date stockoutdate_from,@Param ("stockoutdate_to")final Date stockoutdate_to);
	
	@Query(value = "select c from StockOut c where status = 0"
			+ " and (:orgid_to_link is null or c.orgid_to_link =:orgid_to_link)"
			+ " and (:stockouttypeid_link is null or c.stockouttypeid_link =:stockouttypeid_link)")
	public List<StockOut> stockout_listByOrgTo(@Param ("stockouttypeid_link")final Integer stockouttypeid_link, @Param ("orgid_to_link")final long orgid_to_link);
	
	@Modifying
	@Query(value = "update StockOut set status = 1 where id=:id")
	public void updateStatusById(@Param ("id")final long id);
	
	@Query(value = "select c from StockOut c " 
			+ "where c.pcontract_poid_link = :pcontract_poid_link "
			+ "and c.stockouttypeid_link = :stockouttypeid_link "
			+ "and c.status = :status "
			)
	public List<StockOut> findByPO_Type_Status(
			@Param ("pcontract_poid_link")final Long pcontract_poid_link,
			@Param ("stockouttypeid_link")final Integer stockouttypeid_link, 
			@Param ("status")final Integer status
			);
	
}
