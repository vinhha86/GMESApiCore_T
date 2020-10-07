package vn.gpay.gsmart.core.handover_epc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface HandoverEPCRepository extends JpaRepository<HandoverEPC, Long>,JpaSpecificationExecutor<HandoverEPC>{

}
