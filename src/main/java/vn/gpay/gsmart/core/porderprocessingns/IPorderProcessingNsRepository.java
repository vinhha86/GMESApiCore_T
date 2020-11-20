package vn.gpay.gsmart.core.porderprocessingns;

import java.util.Date;

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
}
