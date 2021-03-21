package vn.gpay.gsmart.core.cutplan_processing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CutplanProcessingRepository extends JpaRepository<CutplanProcessing, Long>,JpaSpecificationExecutor<CutplanProcessing>{

}
