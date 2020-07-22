package vn.gpay.gsmart.core.porder_status;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPOrder_Status_Repository extends JpaRepository<POrder_Status, Long>, JpaSpecificationExecutor<POrder_Status>{

}
