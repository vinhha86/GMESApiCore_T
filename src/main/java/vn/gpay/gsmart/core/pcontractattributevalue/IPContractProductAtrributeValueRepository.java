package vn.gpay.gsmart.core.pcontractattributevalue;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPContractProductAtrributeValueRepository extends JpaRepository<PContractAttributeValue, Long>, JpaSpecificationExecutor<PContractAttributeValue> {
	@Query(value = "select c from PContractAttributeValue c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and (productid_link = :productid_link or 0 = :productid_link) and pcontractid_link = :pcontractid_link")
	public List<PContractAttributeValue> getattribute_by_product_and_pcontract(@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("productid_link")final Long productid_link,@Param ("pcontractid_link")final Long pcontractid_link);
	
	@Query(value = "select c.attributeid_link from PContractAttributeValue c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and (productid_link = :productid_link or 0 = :productid_link) and pcontractid_link = :pcontractid_link")
	public List<Long> getlistattribute_by_product_and_pcontract(@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("productid_link")final Long productid_link,@Param ("pcontractid_link")final Long pcontractid_link);
	
	@Query(value = "select c.attributevalueid_link from PContractAttributeValue c "
			+ "where c.orgrootid_link = :orgrootid_link and attributeid_link= :attributeid_link "
			+ "and (productid_link = :productid_link or 0 = :productid_link) "
			+ "and pcontractid_link = :pcontractid_link "
			+ "and c.attributevalueid_link > 0")
	public List<Long> getlistvalueid_by_product_and_pcontract_and_attribute(@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("productid_link")final Long productid_link,
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("attributeid_link")final Long attributeid_link);
	
	@Query(value = "select c from PContractAttributeValue c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and attributeid_link= :attributeid_link "
			+ "and (productid_link = :productid_link or 0 = :productid_link) "
			+ "and pcontractid_link = :pcontractid_link "
			+ "and ((c.attributevalueid_link > 0 and (:attributeid_link = 30 or :attributeid_link = 4)) or "
			+ "(:attributeid_link != 30 and :attributeid_link != 4))")
	public List<PContractAttributeValue> getlistvalue_by_product_and_pcontract_and_attribute(
			@Param ("productid_link")final Long productid_link,
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("attributeid_link")final Long attributeid_link);
	
	@Query(value = "select c from PContractAttributeValue c "
			+ "where attributeid_link= :attributeid_link "
			+ "and (productid_link = :productid_link or 0 = :productid_link) "
			+ "and pcontractid_link = :pcontractid_link "
			+ "and c.attributevalueid_link = :attributevalueid_link ")
	public List<PContractAttributeValue> getbyvalue(
			@Param ("productid_link")final Long productid_link,
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("attributeid_link")final Long attributeid_link,
			@Param ("attributevalueid_link")final Long attributevalueid_link);
	
	
	
}
