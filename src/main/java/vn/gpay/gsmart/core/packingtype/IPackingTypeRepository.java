package vn.gpay.gsmart.core.packingtype;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPackingTypeRepository extends JpaRepository<PackingType, Long>, JpaSpecificationExecutor<PackingType> {
	@Query(value = "select c from PackingType c where c.orgrootid_link = :orgrootid_link")
	public List<PackingType> getall_byorgrootid(@Param ("orgrootid_link")final  Long orgrootid_link);
	
	@Query(value = "select c from PackingType c where c.orgrootid_link = :orgrootid_link "
			+ "and trim(lower(replace(c.name,' ',''))) = trim(lower(replace(:name, ' ','')))")
	public List<PackingType> getbyname(
			@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("name")final  String name);
	
	@Query(value = "select c from PackingType c where c.orgrootid_link = :orgrootid_link "
			+ "and c.id in :listid")
	public List<PackingType> getbylistid(
			@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("listid")final  List<Long> listid);
}
