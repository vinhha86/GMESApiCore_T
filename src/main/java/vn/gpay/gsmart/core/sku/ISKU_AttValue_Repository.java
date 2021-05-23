package vn.gpay.gsmart.core.sku;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ISKU_AttValue_Repository extends JpaRepository<SKU_Attribute_Value, Long> {
	@Query(value = "select c from SKU_Attribute_Value c "
			+ "inner join SKU d on c.skuid_link = d.id"
			+ " where  d.productid_link = :productid_link " 
			+ "and attributevalueid_link = :attributevalueid_link")
	public List<SKU_Attribute_Value> getone_byproduct_and_value(@Param("productid_link") final Long productid_link,
			@Param("attributevalueid_link") final Long attributevalueid_link);

	@Query(value = "select c from SKU_Attribute_Value c left join SKU d on c.skuid_link = d.id"
			+ " where  d.productid_link = :productid_link and attributeid_link = :attributeid_link")
	public List<SKU_Attribute_Value> get_byproduct_andAttribute(@Param("productid_link") final Long productid_link,
			@Param("attributeid_link") final Long attributeid_link);

	@Query(value = "select c from SKU_Attribute_Value c inner join SKU d on c.skuid_link = d.id"
			+ " where  d.productid_link = :productid_link")
	public List<SKU_Attribute_Value> get_byproduct(@Param("productid_link") final Long productid_link);
	
	@Query(value = "select c from SKU_Attribute_Value c "
			+ "inner join SKU d on c.skuid_link = d.id "
			+ "where ((c.attributeid_link = 4 and c.attributevalueid_link = :value_mau) "
			+ "or (c.attributeid_link = 30 and c.attributevalueid_link = :value_co)) "
			+ "and d.productid_link = :productid_link")
	public List<SKU_Attribute_Value> getList_by_valueMau_and_valueCo(
			@Param ("value_mau")final  Long value_mau,
			@Param ("value_co")final  Long value_co,
			@Param ("productid_link")final  Long productid_link);
	
	@Query(value = "select c.skuid_link from SKU_Attribute_Value c "
			+ "inner join SKU d on c.skuid_link = d.id "
			+ "where ((c.attributeid_link = 4 and c.attributevalueid_link = :value_mau) "
			+ "or (c.attributeid_link = 30 and c.attributevalueid_link = :value_co)) "
			+ "and d.productid_link = :productid_link "
			+ "group by c.skuid_link "
			+ "having count(c.skuid_link) = 2")
	public List<Long> getskuid_by_valueMau_and_valueCo(
			@Param ("value_mau")final  Long value_mau,
			@Param ("value_co")final  Long value_co,
			@Param ("productid_link")final  Long productid_link);
	
	@Query(value = "select c.skuid_link from SKU_Attribute_Value c "
			+ "inner join SKU d on c.skuid_link = d.id "
			+ "where ((c.attributeid_link = 4 and c.attributevalueid_link = :value_mau) "
			+ "or (c.attributeid_link = 36 and c.attributevalueid_link = :value_co)) "
			+ "and d.productid_link = :productid_link "
			+ "group by c.skuid_link "
			+ "having count(c.skuid_link) = 2")
	public List<Long> get_npl_skuid_by_valueMau_and_valueCo(
			@Param ("value_mau")final  Long value_mau,
			@Param ("value_co")final  Long value_co,
			@Param ("productid_link")final  Long productid_link);
	
	@Query(value = "select c from SKU_Attribute_Value c "
			+ "where skuid_link = :skuid_link")
	public List<SKU_Attribute_Value> getby_skuid_link(
			@Param ("skuid_link")final  Long skuid_link);
	
	@Query(value = "select a from SKU_Attribute_Value a "
			+ "inner join PContractProductSKU c on a.skuid_link = c.skuid_link "
			+ "where c.productid_link = :productid_link "
			+ "and c.pcontractid_link = :pcontractid_link and attributevalueid_link= :colorid_link "
			+ "group by a.skuid_link")
	public List<SKU_Attribute_Value> get_bycolorid_link(
			@Param ("productid_link")final  long productid_link, 
			@Param ("pcontractid_link")final  long pcontractid_link,
			@Param ("colorid_link")final long colorid_link);
}
