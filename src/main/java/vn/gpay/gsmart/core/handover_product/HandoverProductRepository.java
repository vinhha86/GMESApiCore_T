package vn.gpay.gsmart.core.handover_product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface HandoverProductRepository extends JpaRepository<HandoverProduct, Long>,JpaSpecificationExecutor<HandoverProduct> {

}
