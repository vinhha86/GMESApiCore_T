package vn.gpay.gsmart.core.sizesetattributevalue;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ISizeSetAttributeRepository extends JpaRepository<SizeSetAttributeValue, Long>, JpaSpecificationExecutor<SizeSetAttributeValue> {

	@Query(value = "select c from SizeSetAttributeValue c where c.sizesetid_link= :sizesetid_link order by c.attributeid_link asc")
	public List<SizeSetAttributeValue> getall_bySizeSetId(@Param ("sizesetid_link")final  Long sizesetid_link);
	
	@Query(value = "select c from SizeSetAttributeValue c "
			+ "where c.attributeid_link = :attributeid_link"
			+ " and c.sizesetid_link = :sizesetid_link")
	public List<SizeSetAttributeValue> getlistvalue_byattribute(
			@Param ("attributeid_link")final  Long attributeid_link,
			@Param ("sizesetid_link")final  Long sizesetid_link);
	
	@Query(value = "select c from SizeSetAttributeValue c "
			+ "where c.attributeid_link = :attributeid_link "
			+ "and sizesetid_link = :sizesetid_link "
			+ "and attributevalueid_link = :attributevalueid_link")
	public List<SizeSetAttributeValue> getOne_bysizeset_and_value(
			@Param ("sizesetid_link")final  Long sizesetid_link,
			@Param ("attributeid_link")final  Long attributeid_link,
			@Param ("attributevalueid_link")final  Long attributevalueid_link);
	
	@Query(value = "select distinct sizesetid_link from SizeSetAttributeValue")
	public List<Long> getId_Distinct(@Nullable Specification<SizeSetAttributeValue> spec);
	
}
