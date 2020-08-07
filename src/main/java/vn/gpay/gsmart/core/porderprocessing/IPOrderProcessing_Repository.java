package vn.gpay.gsmart.core.porderprocessing;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
//import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPOrderProcessing_Repository extends JpaRepository<POrderProcessing, Long>, JpaSpecificationExecutor<POrderProcessing>{
	//get all production order until the time of querying
	@Query(value = "select a from POrderProcessing a where a.status <6 and a.processingdate = "
			+ "(select max(b.processingdate) from POrderProcessing b where b.porderid_link = a.porderid_link)")
	public List<POrderProcessing>getLatest_All();

	//get all production order until the given date
	@Query(value = "select a from POrderProcessing a where a.status <6 and a.processingdate = "
			+ "(select max(b.processingdate) from POrderProcessing b where b.processingdate <= :processingdate_to "
			+ "and b.porderid_link = a.porderid_link)")
	public List<POrderProcessing>getByDate(@Param ("processingdate_to")final Date processingdate_to);
	
	@Query(value = "select a from POrderProcessing a inner join POrder c on a.porderid_link = c.id "
			+ "where c.granttoorgid_link = :factoryid_link "
			+ "and a.status <6 and a.processingdate = "
			+ "(select max(b.processingdate) from POrderProcessing b where b.processingdate <= :processingdate_to "
			+ "and b.porderid_link = a.porderid_link and b.pordergrantid_link = a.pordergrantid_link)")
	public List<POrderProcessing>getByDateAndFactory(@Param ("processingdate_to")final Date processingdate_to, @Param ("factoryid_link")final Long factoryid_link);
	
	//get all by salaryymonth
	@Query(value = "select a from POrderProcessing a inner join POrder b on a.porderid_link = b.id where b.salaryyear=:salaryyear and b.salarymonth=:salarymonth")
	public List<POrderProcessing>getBySalaryMonth(@Param ("salaryyear")final Integer salaryyear, @Param ("salarymonth")final Integer salarymonth);

	//get all production order cutting until the given date
	@Query(value = "select a from POrderProcessing a where (a.status >=20 and a.status <=24) and a.processingdate = "
			+ "(select max(b.processingdate) from POrderProcessing b where CAST(b.processingdate as date) <= CAST(:processingdate_to as date) "
			+ "and b.porderid_link = a.porderid_link)")
	public List<POrderProcessing>getByDate_Cutting(@Param ("processingdate_to")final Date processingdate_to);	
	
	//get exact production order until the given date
	@Query(value = "select a from POrderProcessing a where (a.status >=20 and a.status <=24) and CAST(a.processingdate as date) = CAST(:processingdate_to as date) "
			+ "and a.porderid_link = :porderid_link")
	public List<POrderProcessing>getPProcess_Cutting(@Param ("processingdate_to")final Date processingdate_to, @Param ("porderid_link")final Long porderid_link);	

	//get a production order until the given date
	@Query(value = "select a from POrderProcessing a where a.ordercode = :ordercode and a.processingdate = "
			+ "(select max(b.processingdate) from POrderProcessing b where b.processingdate <= :processingdate_to "
			+ "and b.ordercode = :ordercode)")
	public List<POrderProcessing>getByDateAndOrderCode(@Param ("ordercode")final String ordercode, @Param ("processingdate_to")final Date processingdate_to);

	//get 1st line of processing
	@Query(value = "select a from POrderProcessing a where a.ordercode = :ordercode and a.processingdate = "
			+ "(select min(b.processingdate) from POrderProcessing b where b.ordercode = :ordercode)")
	public List<POrderProcessing> getProductionStartDate(@Param ("ordercode")final String ordercode);

	//get a production order before the given date
	@Query(value = "select a from POrderProcessing a where a.ordercode = :ordercode and a.processingdate = "
			+ "(select max(b.processingdate) from POrderProcessing b where b.processingdate < :processingdate_to "
			+ "and b.ordercode = :ordercode)")
	public List<POrderProcessing>getByBeforeDateAndOrderCode(@Param ("ordercode")final String ordercode, @Param ("processingdate_to")final Date processingdate_to);

	//get a production order before the given date
	@Query(value = "select a from POrderProcessing a where a.porderid_link = :porderid_link and a.processingdate = "
			+ "(select max(b.processingdate) from POrderProcessing b where b.processingdate <= :processingdate_to "
			+ "and b.porderid_link = :porderid_link)")
	public List<POrderProcessing>getByBeforeDateAndOrderID(@Param ("porderid_link")final Long porderid_link, @Param ("processingdate_to")final Date processingdate_to);

	//get all production order until the given date
	@Query(value = "select a from POrderProcessing a where a.status = :status and a.processingdate = "
			+ "(select max(b.processingdate) from POrderProcessing b where b.processingdate <= :processingdate_to "
			+ "and b.porderid_link = a.porderid_link)")
	public List<POrderProcessing>getByDateAndStatus(@Param ("processingdate_to")final Date processingdate_to, @Param ("status")final Integer status);

	//get a line of processing by date
	@Query(value = "select a from POrderProcessing a where a.processingdate = :processingdate and a.porderid_link = :porderid")
	public List<POrderProcessing>findByIdAndPDate(@Param ("porderid")final Long porderid, @Param ("processingdate")final Date processingdate);
	
	@Query(value = "select a from POrderProcessing a where a.processingdate = :processingdate and a.porderid_link = :porderid_link and a.pordergrantid_link = :pordergrantid_link")
	public List<POrderProcessing>findByIdAndPDate(@Param ("porderid_link")final Long porderid_link, @Param ("pordergrantid_link")final Long pordergrantid_link, @Param ("processingdate")final Date processingdate);

	//get a line of processing by date and code
	@Query(value = "select a from POrderProcessing a where a.processingdate = :processingdate and a.ordercode = :ordercode")
	public List<POrderProcessing>findByCodeAndPDate(@Param ("ordercode")final String ordercode, @Param ("processingdate")final Date processingdate);

	@Query(value = "select a from POrderProcessing a where a.porderid_link = :porderid_link order by processingdate asc")
	public List<POrderProcessing>getByOrderId(@Param ("porderid_link")final Long porderid_link);
	
	@Query(value = "select a from POrderProcessing a where a.porderid_link = :porderid_link and pordergrantid_link = :pordergrantid_link order by processingdate asc")
	public List<POrderProcessing>getByOrderId_And_GrantId(
			@Param ("porderid_link")final Long porderid_link,
			@Param ("pordergrantid_link")final Long pordergrantid_link);

	//get all processing of an order after the given date
	@Query(value = "select a from POrderProcessing a where a.porderid_link = :porderid_link and a.pordergrantid_link = :pordergrantid_link and a.processingdate > :processingdate_to order by a.processingdate ASC")
	public List<POrderProcessing>getAfterDate(@Param ("porderid_link")final Long porderid_link, @Param ("pordergrantid_link")final Long pordergrantid_link, @Param ("processingdate_to")final Date processingdate_to);

}
