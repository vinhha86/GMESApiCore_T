package vn.gpay.gsmart.core.stockin;

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
public interface StockInRepository extends JpaRepository<StockIn, Long>,JpaSpecificationExecutor<StockIn>{


	@Query(value = "select c from StockIn c where orgid_link=:orgid_link and stockincode =:stockincode")
	public List<StockIn> findByStockinCode(@Param ("orgid_link")final Long orgid_link ,@Param ("stockincode")final String stockincode);
	
	@Query(value = "select c from StockIn c where orgid_link=:orgid_link and stockincode =:stockincode and orgid_to_link =:stockcode")
	public List<StockIn> stockin_getone(@Param ("orgid_link")final Long orgid_link ,@Param ("stockincode")final String stockincode, @Param ("stockcode")final String stockcode);

	//@Query(value = "select c from StockIn c where orgid_link=:orgid_link")
	@Query(value = "select c from StockIn c where orgid_link =:orgid_link "
			+ " and ( coalesce(:orgid_to_link, null) is null  or (( coalesce(:orgid_to_link, null) is not null ) and c.orgid_to_link=:orgid_to_link ) )"
			+ " and ( coalesce(:orgid_from_link, null) is null  or (( coalesce(:orgid_from_link, null) is not null ) and c.orgid_from_link=:orgid_from_link ) )"
			+ " and ( coalesce(:stockintypeid_link, null) is null  or (( coalesce(:stockintypeid_link, null) is not null ) and c.stockintypeid_link=:stockintypeid_link ) )"
			+ " and (  c.stockincode like '%'||:stockincode ||'%')"
			+ "     and   ( (coalesce(:stockindate_from, null) is null and coalesce(:stockindate_to, null) is null) "
	        + "             or ((coalesce(c.stockindate, null) is not null) "
	        + "                 and ( "
	        + "                        ((coalesce(:stockindate_from, null) is null and coalesce(:stockoutdate_to, null) is not null) and c.stockindate <= :stockindate_to) "
	        + "                     or ((coalesce(:stockindate_to, null) is null and coalesce(:stockoutdate_from, null) is not null) and c.stockindate >= :stockindate_from)"
	        + "                     or (c.stockindate between :stockindate_from and :stockindate_to)"
	        + "                 )"
	        + "             )"
	        + "       ) "
			)
	public List<StockIn> stockin_list(@Param ("orgid_link")final Long orgid_link ,@Param ("stockintypeid_link")final long stockintypeid_link,@Param ("stockincode")final String stockincode,@Param ("orgid_from_link")final Long orgid_from_link, 
			@Param ("orgid_to_link")final Long orgid_to_link,@Param ("stockindate_from")final Date stockindate_from,@Param ("stockindate_to")final Date stockindate_to);

	@Query(value = "select c from StockIn c " 
			+ "where c.pcontract_poid_link = :pcontract_poid_link "
			+ "and c.stockintypeid_link = :stockintypeid_link "
			+ "and c.status = :status "
			)
	public List<StockIn> findByPO_Type_Status(
			@Param ("pcontract_poid_link")final Long pcontract_poid_link,
			@Param ("stockintypeid_link")final Integer stockintypeid_link, 
			@Param ("status")final Integer status
			);
}
