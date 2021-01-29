package vn.gpay.gsmart.core.org;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import vn.gpay.gsmart.core.org.Org;

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
			+ "order by c.code asc")
	public List<Org> findAllOrgbyType(@Param ("orgrootid")final long orgrootid,
			@Param ("orgtypeid_link")final Integer orgtypeid_link);
	
	@Query(value = "select c from Org c where c.orgtypeid_link in(1,8,9,13,14,17,21) order by c.id asc")
//	@Query(value = "select c from Org c where c.orgtypeid_link in(1,8,9,13,14,21) order by c.orgtypeid_link, c.is_manufacturer, c.code asc")
	public List<Org> findOrgByTypeForMenuOrg();
	
	@Query(value = "select c from Org c where c.parentid_link =:orgid_link")
	public List<Org> findOrgAllByParent(@Param ("orgid_link")final long orgid_link);
	
	@Query(value = "select c from Org c where c.orgtypeid_link in(:orgtypestring) order by c.name asc")
	public List<Org> findOrgByOrgTypeString(@Param ("orgtypestring")final String orgtypestring);
	
	@Query(value = "select c from Org c "
			+ "inner join POrderGrant a on c.id = a.granttoorgid_link "
			+ "inner join POrder b on a.porderid_link = b.id "
			+ "where a.porderid_link = :porderid_link order by c.id")
	public List<Org> getOrgByPorderIdLink(@Param ("porderid_link")final Long porderid_link);
	
	@Query(value = "select c from Org c "
			+ "where c.orgrootid_link = :orgrootid_link and c.code = :code")
	public List<Org> getbycode(
			@Param ("orgrootid_link")final Long orgrootid_link,
			@Param ("code")final String code);
	
	@Query(value = "select c from Org c "
			+ "where c.orgtypeid_link = :orgtypeid_link "
			+ "and trim(lower(replace(c.code,' ',''))) = trim(lower(replace(:code, ' ',''))) "
//			+ "and c.name = :name "
			)
	public List<Org> getByCodeAndType(
			@Param ("code")final String code,
			@Param ("orgtypeid_link")final Integer orgtypeid_link
			);
	
	@Query(value = "select c from Org c "
			+ "where c.orgtypeid_link = :orgtypeid_link " 
			+ "and trim(lower(replace(c.name,' ',''))) = trim(lower(replace(:name, ' ',''))) "
//			+ "and c.name = :name "
			)
	public List<Org> getByNameAndType(
			@Param ("name")final String name,
			@Param ("orgtypeid_link")final Integer orgtypeid_link
			);
	
	@Query(value = "select c from Org c where c.orgtypeid_link in(1,13,14,17) order by c.orgtypeid_link, c.name asc")
	public List<Org> findOrgByTypeForInvCheckDeviceMenuOrg();
	
	@Query(value = "select c from Org c where c.orgtypeid_link =12 and c.id not in :buyerIds and c.status = 1 order by c.code asc")
	public List<Org> getOrgForContractBuyerBuyerList(@Param ("buyerIds")final List<Long> buyerIds);
}
