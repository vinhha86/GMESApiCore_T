package vn.gpay.gsmart.core.cutplan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CutPlan_Repository extends JpaRepository<CutPlan_Size, Long>, JpaSpecificationExecutor<CutPlan_Size> {
	@Query(value = "select c from CutPlan_Size c " + "inner join CutPlan_Row a on a.id = c.cutplanrowid_link "
			+ "where a.material_skuid_link = :material_skuid_link " + "and a.porderid_link = :porderid_link "
			+ "and c.orgrootid_link = :orgrootid_link")
	public List<CutPlan_Size> getby_sku_and_porder(@Param("material_skuid_link") final Long material_skuid_link,
			@Param("porderid_link") final Long porderid_link, @Param("orgrootid_link") final Long orgrootid_link);

	@Query(value = "select c from CutPlan_Size c " + "inner join CutPlan_Row a on a.id = c.cutplanrowid_link "
			+ "inner join SKU_Attribute_Value b on c.product_skuid_link = b.skuid_link "
			+ "where a.material_skuid_link = :material_skuid_link " + "and a.porderid_link = :porderid_link "
			+ "and c.orgrootid_link = :orgrootid_link "
			+ "and b.attributevalueid_link = :colorid_link and b.attributeid_link = 4 ")
	public List<CutPlan_Size> getby_sku_and_porder_color(@Param("material_skuid_link") final Long material_skuid_link,
			@Param("porderid_link") final Long porderid_link, @Param("orgrootid_link") final Long orgrootid_link,
			@Param("colorid_link") final Long colorid_link);

	@Query(value = "select c from CutPlan_Size c " + "inner join CutPlan_Row a on a.id = c.cutplanrowid_link "
			+ "inner join SKU_Attribute_Value b on c.product_skuid_link = b.skuid_link "
			+ "where a.material_skuid_link = :material_skuid_link "
			+ "and a.pcontractid_link = :pcontractid_link and a.productid_link = :productid_link "
			+ "and c.orgrootid_link = :orgrootid_link "
			+ "and b.attributevalueid_link = :colorid_link and b.attributeid_link = 4 ")
	public List<CutPlan_Size> getby_sku_and_pcontract_product_color(
			@Param("material_skuid_link") final Long material_skuid_link,
			@Param("pcontractid_link") final Long pcontractid_link, @Param("productid_link") final Long productid_link,
			@Param("orgrootid_link") final Long orgrootid_link, @Param("colorid_link") final Long colorid_link);

	@Query(value = "select c from CutPlan_Size c " + "where c.cutplanrowid_link = :cutplanrowid_link "
			+ "and c.orgrootid_link = :orgrootid_link")
	public List<CutPlan_Size> getby_row(@Param("cutplanrowid_link") final Long cutplanrowid_link,
			@Param("orgrootid_link") final Long orgrootid_link);

	@Query(value = "select c from CutPlan_Size c " + "where c.cutplanrowid_link = :cutplanrowid_link "
			+ "and c.product_skuid_link = :product_skuid_link " + "and c.orgrootid_link = :orgrootid_link")
	public List<CutPlan_Size> getby_row_and_productsku(@Param("cutplanrowid_link") final Long cutplanrowid_link,
			@Param("product_skuid_link") final Long product_skuid_link,
			@Param("orgrootid_link") final Long orgrootid_link);

	@Query(value = "select a from CutPlan_Row c " + "inner join CutPlan_Size a on a.cutplanrowid_link = c.id "
			+ " where c.material_skuid_link = :material_skuid_link " + "and c.porderid_link = :porderid_link "
			+ "and (a.product_skuid_link = :product_skuid_link or :product_skuid_link is null) " + "and c.type = :type "
			+ "and (c.name = :name or '' = :name) " + "group by a")
	public List<CutPlan_Size> getby_matsku_and_porder_and_productsku(
			@Param("material_skuid_link") final Long material_skuid_link,
			@Param("porderid_link") final Long porderid_link,
			@Param("product_skuid_link") final Long product_skuid_link, @Param("type") final Integer type,
			@Param("name") final String name);

	@Query(value = "select SUM(a.amount) from CutPlan_Size a where a.cutplanrowid_link = :cutplanrowid_link")
	public Integer getTotalAmount_By_CutPlanRow(@Param("cutplanrowid_link") final Long cutplanrowid_link);
}
