package vn.gpay.gsmart.core.cutplan_processing;

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
}
