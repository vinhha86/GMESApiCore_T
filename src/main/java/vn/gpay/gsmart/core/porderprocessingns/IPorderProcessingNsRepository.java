package vn.gpay.gsmart.core.porderprocessingns;

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
public interface IPorderProcessingNsRepository extends JpaRepository<PorderProcessingNs, Long>, JpaSpecificationExecutor<PorderProcessingNs>{
	@Query(value = "select SUM(a.amount_timespent) from PorderProcessingNs a where"
			+ " a.pordergrantid_link = :pordergrantid_link"
			+ " and a.personnelid_link = :personnelid_link"
			+ " and a.processingdate >= :date_from"
			+ " and a.processingdate <= :date_to")
	public Integer getTotalWTime_ByPorder(
			@Param ("pordergrantid_link")final Long pordergrantid_link,
			@Param ("personnelid_link")final Long personnelid_link,
			@Param ("date_from")final Date date_from,
			@Param ("date_to")final Date date_to);
	
	@Query(value = "select a from PorderProcessingNs a where"
			+ " a.pordergrantid_link = :pordergrantid_link"
			+ " and a.porderid_link = :porderid_link"
			+ " and a.personnelid_link = :personnelid_link"
			+ " and a.processingdate = :processingdate"
			+ " and a.shifttypeid_link = :shifttypeid_link"
			)
	public List<PorderProcessingNs> getByPersonnelDateAndShift(
			@Param ("porderid_link")final Long porderid_link,
			@Param ("pordergrantid_link")final Long pordergrantid_link,
			@Param ("personnelid_link")final Long personnelid_link,
			@Param ("processingdate")final Date processingdate,
			@Param ("shifttypeid_link")final Integer shifttypeid_link
			);
	
	@Query(value = "select a from PorderProcessingNs a where"
			+ " a.pordergrantid_link = :pordergrantid_link"
			+ " and a.porderid_link = :porderid_link"
			+ " and a.personnelid_link = :personnelid_link"
			+ " and a.processingdate = :processingdate"
			+ " and a.shifttypeid_link = :shifttypeid_link"
			+ " and a.pordersewingcostid_link = :pordersewingcostid_link"
			)
	public List<PorderProcessingNs> getByPersonnelDateAndShiftAndPOrderSewingCost(
			@Param ("porderid_link")final Long porderid_link,
			@Param ("pordergrantid_link")final Long pordergrantid_link,
			@Param ("personnelid_link")final Long personnelid_link,
			@Param ("processingdate")final Date processingdate,
			@Param ("shifttypeid_link")final Integer shifttypeid_link,
			@Param ("pordersewingcostid_link")final Long pordersewingcostid_link
			);
}
