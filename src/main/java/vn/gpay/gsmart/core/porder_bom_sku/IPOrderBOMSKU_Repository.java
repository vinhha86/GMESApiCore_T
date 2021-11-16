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
public interface IPOrderBOMSKU_Repository
		extends JpaRepository<POrderBOMSKU, Long>, JpaSpecificationExecutor<POrderBOMSKU> {
	@Query("SELECT c FROM POrderBOMSKU c " + "where c.porderid_link = :porderid_link " + "and type = :type")
	public List<POrderBOMSKU> getByPOrderID(@Param("porderid_link") final Long porderid_link,
			@Param("type") final int type);
	
	@Query("SELECT c FROM POrderBOMSKU c " + "where c.productid_link = :productid_link and pcontractid_link = :pcontractid_link " + "and type = :type")
	public List<POrderBOMSKU> getByPContract_Product(@Param("pcontractid_link") final Long pcontractid_link, @Param("productid_link") final Long productid_link,
			@Param("type") final int type);

	@Query("SELECT c FROM POrderBOMSKU c " + "inner join SKU_Attribute_Value d on c.skuid_link = d.skuid_link "
			+ "where c.porderid_link = :porderid_link" + " and d.attributevalueid_link = :colorid_link")
	public List<POrderBOMSKU> getByPOrder_and_color(@Param("porderid_link") final Long porderid_link,
			@Param("colorid_link") final Long colorid_link);

	@Query("SELECT c FROM POrderBOMSKU c " + "where c.porderid_link = :porderid_link"
			+ " and c.materialid_link = :materialid_link")
	public List<POrderBOMSKU> getByPOrder_and_material(@Param("porderid_link") final Long porderid_link,
			@Param("materialid_link") final Long materialid_link);

	@Query("SELECT c FROM POrderBOMSKU c " + "where c.porderid_link = :porderid_link"
			+ " and c.materialid_link = :materialid_link" + " and c.skuid_link = :skuid_link " + "and c.type = :type")
	public List<POrderBOMSKU> getByPOrder_and_material_and_sku_and_type(
			@Param("porderid_link") final Long porderid_link, @Param("materialid_link") final Long materialid_link,
			@Param("skuid_link") final Long skuid_link, @Param("type") final int type);
	
	@Query("SELECT c FROM POrderBOMSKU c " + "where c.productid_link = :productid_link and c.pcontractid_link = :pcontractid_link "
			+ " and c.materialid_link = :materialid_link" + " and c.skuid_link = :skuid_link " + "and c.type = :type")
	public List<POrderBOMSKU> getByPcontract_productr_and_material_and_sku_and_type(
			@Param("pcontractid_link") final Long pcontractid_link,@Param("productid_link") final Long productid_link, @Param("materialid_link") final Long materialid_link,
			@Param("skuid_link") final Long skuid_link, @Param("type") final int type);

	@Query("SELECT c FROM POrderBOMSKU c " + "inner join SKU_Attribute_Value d on c.skuid_link = d.skuid_link "
			+ "where c.porderid_link = :porderid_link " + "and c.materialid_link = :materialid_link "
			+ " and d.attributevalueid_link = :colorid_link")
	public List<POrderBOMSKU> getByPOrder_and_material_and_color(@Param("porderid_link") final Long porderid_link,
			@Param("materialid_link") final Long materialid_link, @Param("colorid_link") final Long colorid_link);

	@Query("SELECT c FROM POrderBOMSKU c where c.porderid_link = :porderid_link and materialid_link = :materialid_link")
	public List<POrderBOMSKU> getSKUByMaterial(@Param("porderid_link") final Long porderid_link,
			@Param("materialid_link") final Long materialid_link);

	@Query("SELECT c.productid_link, c.materialid_link, sum(c.amount) as amount FROM POrderBOMSKU c where c.porderid_link = :porderid_link group by c.productid_link, c.materialid_link")
	public List<Object[]> getByPOrderID_GroupByProduct(@Param("porderid_link") final Long porderid_link);

	@Query("SELECT c.productcolor_name, c.materialid_link, sum(c.amount) as amount FROM POrderBOMSKU c where c.porderid_link = :porderid_link group by c.productcolor_name, c.materialid_link")
	public List<Object[]> getByPOrderID_GroupByColor(@Param("porderid_link") final Long porderid_link);

	@Query("SELECT distinct c.materialid_link FROM POrderBOMSKU c " + "inner join SKU b on c.materialid_link = b.id "
			+ "where c.porderid_link = :porderid_link " + "and c.skuid_link = :skuid_link "
			+ "and b.skutypeid_link = :skutypeid_link ")
	public List<Long> getMaterialList_By_Porder_Sku(@Param("porderid_link") final Long porderid_link,
			@Param("skuid_link") final Long skuid_link, @Param("skutypeid_link") final Integer skutypeid_link);
}
