package vn.gpay.gsmart.core.porder_bom_product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface POrderBomProduct_Repository extends JpaRepository<POrderBomProduct, Long>, JpaSpecificationExecutor<POrderBomProduct> {
	@Query("SELECT c FROM POrderBomProduct c where c.porderid_link = :porderid_link")
	public List<POrderBomProduct> getby_porder(
			@Param ("porderid_link")final Long porderid_link);
	
	@Query("SELECT c FROM POrderBomProduct c "
			+ "where c.porderid_link = :porderid_link "
			+ "and materialid_link = :material_skuid_link")
	public List<POrderBomProduct> getby_porder_and_material(
			@Param ("porderid_link")final Long porderid_link,
			@Param ("material_skuid_link")final Long material_skuid_link);
}
