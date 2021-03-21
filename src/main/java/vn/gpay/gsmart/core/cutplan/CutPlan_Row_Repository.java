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
public interface CutPlan_Row_Repository extends JpaRepository<CutPlan_Row, Long>, JpaSpecificationExecutor<CutPlan_Row> {
	@Query(value = "select c from CutPlan_Row c "
			+ "inner join CutPlan_Size a on a.cutplanrowid_link = c.id "
			+ "inner join SKU_Attribute_Value b on a.product_skuid_link = b.skuid_link "
			+ " where c.material_skuid_link = :material_skuid_link " 
			+ "and c.porderid_link = :porderid_link "
			+ "and a.orgrootid_link = :orgrootid_link "
			+ "and b.attributeid_link = :attributeid_link "
			+ "and b.attributevalueid_link = :colorid_link "
			+ "group by c")
	public List<CutPlan_Row> getby_sku_and_porder(
			@Param("porderid_link") final Long porderid_link,
			@Param("material_skuid_link") final Long material_skuid_link,			
			@Param("orgrootid_link") final Long orgrootid_link,
			@Param("colorid_link") final Long colorid_link,
			@Param("attributeid_link") final Long attributeid_link);
	
	@Query(value = "select c from CutPlan_Row c "
			+ "inner join CutPlan_Size a on a.cutplanrowid_link = c.id "
			+ "where c.material_skuid_link = :material_skuid_link " 
			+ "and c.porderid_link = :porderid_link "
			+ "and a.product_skuid_link = :product_skuid_link "
			+ "and c.type = :type "
			+ "and (c.name = :name or '' = :name) ")
	public List<CutPlan_Row> getby_matsku_and_porder_and_productsku(
			@Param("material_skuid_link") final Long material_skuid_link,
			@Param("porderid_link") final Long porderid_link,
			@Param("product_skuid_link") final Long product_skuid_link,
			@Param("type") final Integer type,
			@Param("name") final String name);
	
	@Query(value = "select c from CutPlan_Row c "
			+ " where c.porderid_link = :porderid_link "
			+ "order by c.id")
	public List<CutPlan_Row> findByPOrder(
			@Param("porderid_link") final Long porderid_link);
}
