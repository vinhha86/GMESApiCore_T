package vn.gpay.gsmart.core.pcontratproductsku;

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
	@Query(value = "select c from PContractProductSKU c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and productid_link = :productid_link "
			+ "and pcontractid_link = :pcontractid_link")
	public List<PContractProductSKU> getlistsku_byproduct_and_pcontract(
			@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("productid_link")final  long productid_link, 
			@Param ("pcontractid_link")final  long pcontractid_link);
	
	@Query(value = "select c from PContractProductSKU c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and pcontract_poid_link = :pcontract_poid_link "
			+ "and pcontractid_link = :pcontractid_link")
	public List<PContractProductSKU> getlistsku_bypo_and_pcontract(
			@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("pcontract_poid_link")final  long pcontract_poid_link, 
			@Param ("pcontractid_link")final  long pcontractid_link);
	
	@Query(value = "select c from PContractProductSKU c where c.skuid_link = :skuid_link and c.pcontractid_link = :pcontractid_link")
	public List<PContractProductSKU> getlistsku_bysku_and_pcontract(@Param ("skuid_link")final  long skuid_link,@Param ("pcontractid_link")final  long pcontractid_link);
	
	@Query(value = "select a.attributevalueid_link from SKU_Attribute_Value a "
			+ "inner join PContractProductSKU c on a.skuid_link = c.skuid_link "
			+ "where c.productid_link = :productid_link "
			+ "and c.pcontractid_link = :pcontractid_link and attributeid_link= :attributeid_link ")
	public List<Long> getvaluesize_in_product(
			@Param ("productid_link")final  long productid_link, 
			@Param ("pcontractid_link")final  long pcontractid_link,
			@Param ("attributeid_link")final long attributeid_link);
	
	@Query(value = "select a.skuid_link from SKU_Attribute_Value a "
			+ "inner join PContractProductSKU c on a.skuid_link = c.skuid_link "
			+ "where c.productid_link = :productid_link "
			+ "and c.pcontractid_link = :pcontractid_link and attributevalueid_link= :colorid_link "
			+ "group by a.skuid_link")
	public List<Long> getskuid_bycolorid_link(
			@Param ("productid_link")final  long productid_link, 
			@Param ("pcontractid_link")final  long pcontractid_link,
			@Param ("colorid_link")final long colorid_link);
}
