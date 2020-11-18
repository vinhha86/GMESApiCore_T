package vn.gpay.gsmart.core.salary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IOrgSal_ComRepository extends JpaRepository<OrgSal_Com, Long>, JpaSpecificationExecutor<OrgSal_Com> {
	@Query(value = "select c from OrgSal_Com c where c.orgid_link = :orgid_link and c.type = :typeid_link")
	public List<OrgSal_Com> getall_byorg(@Param ("orgid_link")final  Long orgid_link, @Param ("typeid_link")final  Integer typeid_link);

	@Query(value = "select c from OrgSal_Com c"
			+ " inner join OrgSal_Com_Position d on d.salcomid_link = c.id"
			+ " where c.orgid_link = :orgid_link and c.type = :typeid_link"
			+ " and d.positionid_link = :positionid_link")
	public List<OrgSal_Com> getall_byposition(@Param ("orgid_link")final  Long orgid_link, 
			@Param ("typeid_link")final  Integer typeid_link,
			@Param ("positionid_link")final  Long positionid_link);
	
	@Query(value = "select c from OrgSal_Com c"
			+ " inner join OrgSal_Com_LaborLevel d on d.salcomid_link = c.id"
			+ " where c.orgid_link = :orgid_link and c.type = :typeid_link"
			+ " and d.laborlevelid_link = :laborlevelid_link")
	public List<OrgSal_Com> getall_bylaborlevel(@Param ("orgid_link")final  Long orgid_link, 
			@Param ("typeid_link")final  Integer typeid_link,
			@Param ("laborlevelid_link")final  Long laborlevelid_link);
}
