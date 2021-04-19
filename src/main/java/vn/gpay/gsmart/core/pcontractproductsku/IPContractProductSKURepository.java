package vn.gpay.gsmart.core.pcontractproductsku;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IPContractProductSKURepository extends JpaRepository<PContractProductSKU, Long>, JpaSpecificationExecutor<PContractProductSKU> {
	@Query(value = "select a from PContractProductSKU a "
			+ "inner join SKU_Attribute_Value c on a.skuid_link = c.skuid_link "
			+ "inner join Attributevalue b on b.id = c.attributevalueid_link "
			+ "where a.orgrootid_link = :orgrootid_link "
			+ "and productid_link = :productid_link "
			+ "and pcontractid_link = :pcontractid_link "
			+ "group by a")
	public List<PContractProductSKU> getlistsku_byproduct_and_pcontract(
			@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("productid_link")final  long productid_link, 
			@Param ("pcontractid_link")final  long pcontractid_link);
	
	
	@Query(value = "select c from PContractProductSKU c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and pcontractid_link = :pcontractid_link")
	public List<PContractProductSKU> getlistsku_bypcontract(
			@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("pcontractid_link")final  long pcontractid_link);	
	
	@Query(value = "select c.skuid_link, sum(c.pquantity_total) from PContractProductSKU c "
			+ "where pcontractid_link = :pcontractid_link "
			+ "group by c.skuid_link")
	public List<SumSKU> getsumsku_bypcontract(
			@Param ("pcontractid_link")final  long pcontractid_link);	
	
	@Query(value = "select c from PContractProductSKU c "
			+ "where c.pcontract_poid_link = :pcontract_poid_link "
			+ "and c.pcontractid_link = :pcontractid_link")
	public List<PContractProductSKU> getlistsku_bypo_and_pcontract(
			@Param ("pcontract_poid_link")final  long pcontract_poid_link, 
			@Param ("pcontractid_link")final  long pcontractid_link);
	
	@Query(value = "select c from PContractProductSKU c "
			+ "where c.pcontract_poid_link = :pcontract_poid_link")
	public List<PContractProductSKU> getlistsku_bypo(
			@Param ("pcontract_poid_link")final  long pcontract_poid_link);
	
	@Query(value = "select c.productid_link, c.skuid_link, "
			+ "sum(c.pquantity_total) as pquantity_total, "
			+ "sum(c.pquantity_production) as pquantity_production, "
			+ "sum(c.pquantity_sample) as pquantity_sample "
			+ "from PContractProductSKU c "
			+ "inner join PContract_PO a on a.id = c.pcontract_poid_link "
			+ "where a.parentpoid_link = :pcontract_poid_link "
			+ "group by c.productid_link, c.skuid_link")
	public List<Object[]> getsumsku_bypo_parent(
			@Param ("pcontract_poid_link")final  Long pcontract_poid_link);
	
	@Query(value = "select c.productid_link, c.skuid_link, "
			+ "sum(c.pquantity_total) as pquantity_total, "
			+ "(select sum(b.pquantity_total) "
				+ "from POrder_Product_SKU b "
				+ "inner join POrder d on d.id= b.porderid_link "
				+ "inner join PContract_PO e on e.id = d.pcontract_poid_link "
				+ "where b.productid_link = :productid_link "
				+ "and b.skuid_link = c.skuid_link "
				+ "and e.parentpoid_link = :pcontract_poid_link) as pquantity_granted, "
			+ "sum(c.pquantity_production) as pquantity_production, "
			+ "sum(c.pquantity_sample) as pquantity_sample "
			+ "from PContractProductSKU c "
			+ "inner join PContract_PO a on a.id = c.pcontract_poid_link "
			+ "where a.parentpoid_link = :pcontract_poid_link and c.productid_link = :productid_link "
			+ "group by c.productid_link, c.skuid_link")
	public List<Object[]> getsumsku_bypo_parent_and_product(
			@Param ("pcontract_poid_link")final  Long pcontract_poid_link,
			@Param ("productid_link")final  Long productid_link);
	
	
	@Query(value = "select c from PContractProductSKU c "
			+ "where pcontract_poid_link = :pcontract_poid_link "
			+ "and productid_link = :productid_link")
	public List<PContractProductSKU> getlistsku_bypo_and_product(
			@Param ("pcontract_poid_link")final  long pcontract_poid_link, 
			@Param ("productid_link")final  long productid_link);
	
	@Query(value = "select c from PContractProductSKU c "
			+ "where c.skuid_link = :skuid_link "
			+ "and c.pcontractid_link = :pcontractid_link")
	public List<PContractProductSKU> getlistsku_bysku_and_pcontract(
			@Param ("skuid_link")final  long skuid_link,
			@Param ("pcontractid_link")final  long pcontractid_link);
	
	@Query(value = "select c from PContractProductSKU c "
			+ "where c.skuid_link = :skuid_link "
			+ "and c.productid_link = :productid_link "
			+ "and c.pcontract_poid_link = :pcontract_poid_link")
	public List<PContractProductSKU> getlistsku_bysku_and_product_PO(
			@Param ("skuid_link")final  long skuid_link,
			@Param ("productid_link")final  long productid_link,
			@Param ("pcontract_poid_link")final  long pcontract_poid_link);
	
	@Query(value = "select b.id from SKU_Attribute_Value a "
			+ "inner join PContractProductSKU c on a.skuid_link = c.skuid_link "
			+ "inner join Attributevalue b on b.id = a.attributevalueid_link "
			+ "where c.productid_link = :productid_link "
			+ "and c.pcontractid_link = :pcontractid_link and a.attributeid_link= :attributeid_link "
			+ "group by b.id, b.sortvalue "
			+ "order by b.sortvalue asc")
	public List<Long> getvaluesize_in_product(
			@Param ("productid_link")final  long productid_link, 
			@Param ("pcontractid_link")final  long pcontractid_link,
			@Param ("attributeid_link")final long attributeid_link);
	
	@Query(value = "select b.value from SKU_Attribute_Value a "
			+ "inner join PContractProductSKU c on a.skuid_link = c.skuid_link "
			+ "inner join Attributevalue b on b.id = a.attributevalueid_link "
			+ "where c.productid_link = :productid_link "
			+ "and c.pcontractid_link = :pcontractid_link and a.attributeid_link= :attributeid_link "
			+ "group by b.value, b.sortvalue "
			+ "order by b.sortvalue asc")
	public List<String> getvaluename_in_product(
			@Param ("productid_link")final  long productid_link, 
			@Param ("pcontractid_link")final  long pcontractid_link,
			@Param ("attributeid_link")final long attributeid_link);
	
	@Query(value = "select a.skuid_link from SKU_Attribute_Value a "
			+ "inner join PContractProductSKU c on a.skuid_link = c.skuid_link "
			+ "where c.productid_link = :productid_link "
			+ "and c.pcontractid_link = :pcontractid_link "
			+ "and (attributevalueid_link= :colorid_link or :colorid_link = 0) "
			+ "group by a.skuid_link")
	public List<Long> getskuid_bycolorid_link(
			@Param ("productid_link")final  long productid_link, 
			@Param ("pcontractid_link")final  long pcontractid_link,
			@Param ("colorid_link")final long colorid_link);
	
	@Query(value = "select c from SKU_Attribute_Value a "
			+ "inner join PContractProductSKU c on a.skuid_link = c.skuid_link "
			+ "where c.productid_link = :productid_link "
			+ "and c.pcontractid_link = :pcontractid_link and attributevalueid_link= :colorid_link "
			+ "group by c")
	public List<PContractProductSKU> getPContractProductSKU_bycolorid_link(
			@Param ("productid_link")final  long productid_link, 
			@Param ("pcontractid_link")final  long pcontractid_link,
			@Param ("colorid_link")final long colorid_link);
	
	@Query(value = "select c from PContractProductSKU c "
			+ "where c.skuid_link = :skuid_link "
			+ "and c.pcontract_poid_link = :pcontract_poid_link ")
	public List<PContractProductSKU> getBySkuAndPcontractPo(
			@Param ("skuid_link")final  long skuid_link,
			@Param ("pcontract_poid_link")final  long pcontract_poid_link);
	
}
