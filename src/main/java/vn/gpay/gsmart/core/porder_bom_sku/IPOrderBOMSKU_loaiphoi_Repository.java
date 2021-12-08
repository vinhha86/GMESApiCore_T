package vn.gpay.gsmart.core.porder_bom_sku;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPOrderBOMSKU_loaiphoi_Repository
		extends JpaRepository<porder_bom_sku_loaiphoi, Long>, JpaSpecificationExecutor<porder_bom_sku_loaiphoi> {
	@Query("SELECT c FROM porder_bom_sku_loaiphoi c " + "where c.pcontractid_link = :pcontractid_link "
			+ "and productid_link = :productid_link and material_skuid_link = :material_skuid_link and skuid_link = :skuid_link and loaiphoi =:loaiphoi")
	public List<porder_bom_sku_loaiphoi> getby_pcontract_product_material_sku_loaiphoi(
			@Param("pcontractid_link") final Long pcontractid_link, @Param("productid_link") final Long productid_link,
			@Param("material_skuid_link") final Long material_skuid_link, @Param("skuid_link") final Long skuid_link,
			@Param("loaiphoi") final String loaiphoi);

	@Query("SELECT c FROM porder_bom_sku_loaiphoi c " + "where c.pcontractid_link = :pcontractid_link "
			+ "and productid_link = :productid_link and material_skuid_link = :material_skuid_link and skuid_link = :skuid_link")
	public List<porder_bom_sku_loaiphoi> getby_pcontract_product_material_sku(
			@Param("pcontractid_link") final Long pcontractid_link, @Param("productid_link") final Long productid_link,
			@Param("material_skuid_link") final Long material_skuid_link, @Param("skuid_link") final Long skuid_link);

}
