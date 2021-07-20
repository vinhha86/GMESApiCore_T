package vn.gpay.gsmart.core.security;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface GpayUserOrgRepository extends JpaRepository<GpayUserOrg, Long>,JpaSpecificationExecutor<GpayUserOrg> {
	@Query(value = "select c from GpayUserOrg c where c.userid_link = :userid_link ")
	public List<GpayUserOrg> getall_byuser(@Param ("userid_link")final  Long userid_link);
	
	@Query(value = "select c from GpayUserOrg c "
			+ " inner join Org d on c.orgid_link = d.id"
			+ " where c.userid_link = :userid_link"
			+ " and d.orgtypeid_link = :orgtypeid_link")
	public List<GpayUserOrg> getall_byuser_andtype(
			@Param ("userid_link")final  Long userid_link,
			@Param ("orgtypeid_link")final  Integer orgtypeid_link);
	
	@Query(value = "select c from GpayUserOrg c "
			+ " inner join Org d on c.orgid_link = d.id"
			+ " where c.userid_link = :userid_link"
			+ " and d.orgtypeid_link in :orgtypeid_link_list")
	public List<GpayUserOrg> getall_byuser_andtypelist(
			@Param ("userid_link")final  Long userid_link,
			@Param ("orgtypeid_link_list")final  List<Integer> orgtypeid_link_list);
	
	@Query(value = "select c from GpayUserOrg c where c.userid_link = :userid_link and c.orgid_link = :orgid_link")
	public List<GpayUserOrg> getby_user_org(@Param ("userid_link")final  Long userid_link, @Param ("orgid_link")final  Long orgid_link);
}