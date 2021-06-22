package vn.gpay.gsmart.core.cutplan_processing;

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
public interface CutplanProcessingRepository extends JpaRepository<CutplanProcessing, Long>,JpaSpecificationExecutor<CutplanProcessing>{
	@Query(value = "select c from CutplanProcessing c where cutplanrowid_link =:cutplanrowid_link")
	public List<CutplanProcessing> getby_cutplanrow(
			@Param ("cutplanrowid_link")final Long cutplanrowid_link);
	
	@Query(value = "SELECT sum(a.amountcut), a.processingdate "
			+ "from CutplanProcessing a "
			+ "inner join CutPlan_Row b on a.cutplanrowid_link = b.id "
			+ "where b.porderid_link = :porderid_link "
			+ "group by a.processingdate "
			+ "order by a.processingdate "
			)
	public List<Object[]> getForChart_TienDoCat(
			@Param ("porderid_link")final Long porderid_link
			);
}
