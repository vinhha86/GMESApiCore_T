package vn.gpay.gsmart.core.cutplan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CutPlan_Row_Repository extends JpaRepository<CutPlan_Row, Long>, JpaSpecificationExecutor<CutPlan_Row> {

}
