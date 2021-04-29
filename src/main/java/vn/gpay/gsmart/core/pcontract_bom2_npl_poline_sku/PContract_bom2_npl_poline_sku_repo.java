package vn.gpay.gsmart.core.pcontract_bom2_npl_poline_sku;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PContract_bom2_npl_poline_sku_repo extends JpaRepository<PContract_bom2_npl_poline_sku, Long>, JpaSpecificationExecutor<PContract_bom2_npl_poline_sku> {

}
