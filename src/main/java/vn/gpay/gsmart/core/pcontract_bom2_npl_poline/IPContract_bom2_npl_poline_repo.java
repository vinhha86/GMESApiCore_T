package vn.gpay.gsmart.core.pcontract_bom2_npl_poline;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPContract_bom2_npl_poline_repo
		extends JpaRepository<PContract_bom2_npl_poline, Long>, JpaSpecificationExecutor<PContract_bom2_npl_poline> {
	@Query(value = "select c from PContract_bom2_npl_poline c " + "where pcontract_poid_link = :pcontractpoid_link "
			+ "and npl_skuid_link = :material_skuid_link")
	public List<PContract_bom2_npl_poline> getby_po_and_npl(@Param("pcontractpoid_link") final Long pcontractpoid_link,
			@Param("material_skuid_link") final Long material_skuid_link);

	@Query(value = "select c from PContract_bom2_npl_poline c " + "where pcontractid_link = :pcontractid_link "
			+ "and npl_skuid_link = :material_skuid_link")
	public List<PContract_bom2_npl_poline> getby_pcontract_and_npl(
			@Param("pcontractid_link") final Long pcontractid_link,
			@Param("material_skuid_link") final Long material_skuid_link);

	@Query(value = "select c from PContract_bom2_npl_poline c "
			+ "inner join PContract_PO a on c.pcontract_poid_link = a.id "
			+ "where c.pcontractid_link = :pcontractid_link "
			+ "and (npl_skuid_link = :material_skuid_link or :material_skuid_link is null) "
			+ "and productid_link = :productid_link")
	public List<PContract_bom2_npl_poline> getby_product_and_npl(@Param("pcontractid_link") final Long pcontractid_link,
			@Param("material_skuid_link") final Long material_skuid_link,
			@Param("productid_link") final Long productid_link);

	@Query(value = "select c from PContract_bom2_npl_poline c "
			+ "inner join PContract_PO a on c.pcontract_poid_link = a.id "
			+ "where c.pcontractid_link = :pcontractid_link " + "and productid_link = :productid_link")
	public List<PContract_bom2_npl_poline> getby_product(@Param("pcontractid_link") final Long pcontractid_link,
			@Param("productid_link") final Long productid_link);
}
