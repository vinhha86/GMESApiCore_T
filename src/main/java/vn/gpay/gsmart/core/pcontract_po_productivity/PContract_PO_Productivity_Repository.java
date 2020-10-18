package vn.gpay.gsmart.core.pcontract_po_productivity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PContract_PO_Productivity_Repository extends JpaRepository<PContract_PO_Productivity, Long>, JpaSpecificationExecutor<PContract_PO_Productivity>{
	@Query(value = "select c from PContract_PO_Productivity c "
			+ "where c.pcontract_poid_link = :pcontract_poid_link "
			+ "and c.productid_link = :productid_link")
	public List<PContract_PO_Productivity> getByPOAndProduct(
			@Param ("pcontract_poid_link")final  Long pcontract_poid_link,
			@Param ("productid_link")final  Long productid_link);
	
	@Query(value = "select c from PContract_PO_Productivity c "
			+ "where c.pcontract_poid_link = :pcontract_poid_link ")
	public List<PContract_PO_Productivity> getByPO(
			@Param ("pcontract_poid_link")final  Long pcontract_poid_link);
}
