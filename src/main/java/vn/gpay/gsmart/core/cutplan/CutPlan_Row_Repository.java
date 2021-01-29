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
			+ "inner join CutPlan a on a.cutplanrowid_link = c.id "
			+ "inner join SKU_Attribute_Value b on c.product_skuid_link = b.skuid_link "
			+ " where a.skuid_link = :skuid_link " 
			+ "and a.porderid_link = :porderid_link "
			+ "and a.orgrootid_link = :orgrootid_link "
			+ "and b.attributeid_link = :attributeid_link "
			+ "and b.attributevalueid_link = :colorid_link")
	public List<CutPlan_Row> getby_sku_and_porder(
			@Param("skuid_link") final Long skuid_link,
			@Param("porderid_link") final Long porderid_link,
			@Param("orgrootid_link") final Long orgrootid_link,
			@Param("colorid_link") final Long colorid_link,
			@Param("attributeid_link") final Long attributeid_link);
}
