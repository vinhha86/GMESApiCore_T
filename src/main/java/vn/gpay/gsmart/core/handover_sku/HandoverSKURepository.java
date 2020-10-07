package vn.gpay.gsmart.core.handover_sku;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface HandoverSKURepository extends JpaRepository<HandoverSKU, Long>,JpaSpecificationExecutor<HandoverSKU>{

}
