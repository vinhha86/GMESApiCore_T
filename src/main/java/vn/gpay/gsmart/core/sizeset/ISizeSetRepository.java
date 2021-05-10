package vn.gpay.gsmart.core.sizeset;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ISizeSetRepository extends JpaRepository<SizeSet, Long>, JpaSpecificationExecutor<SizeSet> {
	@Query(value = "select c from SizeSet c where c.orgrootid_link = :orgrootid_link order by c.sortvalue asc")
	public List<SizeSet> getall_byorgrootid(@Param ("orgrootid_link")final  Long orgrootid_link);
	
	@Query(value = "select c from SizeSet c where lower(c.name) = lower(:name)")
	public List<SizeSet> getbyname(@Param ("name")final  String name);
	
	@Query(value = "select max(sortvalue) from SizeSet c")	
	public int getMaxSortValue();
	
	@Query(value = "select c from SizeSet c "
			+ "inner join SizeSetAttributeValue a on c.id = a.sizesetid_link "
			+ "inner join SKU_Attribute_Value b on b.attributevalueid_link = a.attributevalueid_link "
			+ "inner join PContractProductSKU d on d.skuid_link = b.skuid_link "
			+ "where lower(c.name) = lower(:name) "
			+ "and d.productid_link = :productid_link "
			+ "and d.pcontractid_link = :pcontractid_link "
			+ "group by c")
	public List<SizeSet> getbyname_and_po(
			@Param ("name")final  String name,
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("productid_link")final  Long productid_link);
}
