package vn.gpay.gsmart.core.pcontract_po_productivity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PContract_PO_Productivity_Repository extends JpaRepository<PContract_PO_Productivity, Long>, JpaSpecificationExecutor<PContract_PO_Productivity>{

}
