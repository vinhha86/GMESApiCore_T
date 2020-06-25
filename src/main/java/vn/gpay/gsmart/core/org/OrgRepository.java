package vn.gpay.gsmart.core.org;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository
@Transactional
public interface OrgRepository extends JpaRepository<Org, Long>,JpaSpecificationExecutor<Org>{

	@Query(value = "select c from Org c where c.orgrootid_link=:orgrootid_link"
			+ " and orgtypeid_link =:type "
			+ "and id <> :orgid_link ")
	public List<Org> findOrgByType(@Param ("orgrootid_link")final long orgrootid_link,
			@Param ("orgid_link")final long orgid_link
			,@Param ("type")final long type);
	
	@Query(value = "select c from Org c where c.id =:orgid_link and c.orgtypeid_link in(3,4,8)")
	public List<Org> findOrgInvCheckByType(@Param ("orgid_link")final long orgid_link);
	
	@Query(value = "select c from Org c where c.orgtypeid_link in(3,4,8) and c.orgrootid_link =:orgid_link")
	public List<Org> findRootOrgInvCheckByType(@Param ("orgid_link")final long orgid_link);
	
	@Query(value = "select c from Org c where c.orgrootid_link =:orgid_link")
	public List<Org> findOrgAllByRoot(@Param ("orgid_link")final long orgid_link);
	
	@Query(value = "select c from Org c where c.orgrootid_link =:orgrootid "
			+ "and orgtypeid_link = :orgtypeid_link and c.status = 1 "
			+ "order by c.id asc")
	public List<Org> findAllOrgbyType(@Param ("orgrootid")final long orgrootid,
			@Param ("orgtypeid_link")final Integer orgtypeid_link);
	
	@Query(value = "select c from Org c where c.orgtypeid_link in(1,2,3,4,8,9,13,14) order by c.id asc")
	public List<Org> findOrgByTypeForMenuOrg();
	
	
}
