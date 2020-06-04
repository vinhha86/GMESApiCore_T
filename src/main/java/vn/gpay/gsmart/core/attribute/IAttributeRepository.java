package vn.gpay.gsmart.core.attribute;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IAttributeRepository extends JpaRepository<Attribute, Long>, JpaSpecificationExecutor<Attribute> {
	@Query(value = "select c from Attribute c where c.orgrootid_link = :orgrootid_link ")
	public List<Attribute> getall_byOrgId(@Param ("orgrootid_link")final  Long orgrootid_link);
	
	@Query(value = "select c from Attribute c where c.isproduct= True and c.orgrootid_link = :orgrootid_link")
	public List<Attribute> getattribute_for_product(@Param ("orgrootid_link")final  Long orgrootid_link);
	
	@Query(value = "select c from Attribute c where c.ismaterial= True and c.orgrootid_link = :orgrootid_link ")
	public List<Attribute> getattribute_for_Material(@Param ("orgrootid_link")final  Long orgrootid_link);
	
	@Query(value = "select c from Attribute c where c.issewingtrims= True and c.orgrootid_link = :orgrootid_link ")
	public List<Attribute> getattribute_for_SewingTrim(@Param ("orgrootid_link")final  Long orgrootid_link);
	
	@Query(value = "select c from Attribute c where c.ispackingtrims= True and c.orgrootid_link = :orgrootid_link ")
	public List<Attribute> getattribute_for_PackingTrim(@Param ("orgrootid_link")final  Long orgrootid_link);
	
	}
