package vn.gpay.gsmart.core.pcontract_product_bom_log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPContract_bom2_sku_log_Repo extends JpaRepository<PContract_bom2_sku_log, Long>, JpaSpecificationExecutor<PContract_bom2_sku_log> {

}
