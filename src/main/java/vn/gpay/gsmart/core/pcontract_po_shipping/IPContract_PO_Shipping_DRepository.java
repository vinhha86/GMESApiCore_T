package vn.gpay.gsmart.core.pcontract_po_shipping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPContract_PO_Shipping_DRepository extends JpaRepository<PContract_PO_Shipping_D, Long>, JpaSpecificationExecutor<PContract_PO_Shipping_D> {

}
