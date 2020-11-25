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
public interface IOrgSal_Type_LevelRepository extends JpaRepository<OrgSal_Type_Level, Long>, JpaSpecificationExecutor<OrgSal_Type_Level> {
	@Query(value = "select c from OrgSal_Type_Level c where c.orgrootid_link = :orgrootid_link")
	public List<OrgSal_Type_Level> getall_byorgrootid(@Param ("orgrootid_link")final  Long orgrootid_link);
	
	@Query(value = "select c from OrgSal_Type_Level c where c.saltypeid_link = :saltypeid_link"
			+ " and c.sallevelid_link = :sallevelid_link")
	public List<OrgSal_Type_Level> get_bysaltype_and_level(@Param ("saltypeid_link")final  Long saltypeid_link, @Param ("sallevelid_link")final  Long sallevelid_link);
	
	@Query(value = "select c from OrgSal_Type_Level c where c.saltypeid_link = :saltypeid_link")
	public List<OrgSal_Type_Level> get_bysaltype(@Param ("saltypeid_link")final  Long saltypeid_link);

	@Query(value = "select c from OrgSal_Type_Level c "
			+ "inner join OrgSal_Type d on c.saltypeid_link = d.id"
			+ " where d.orgid_link = :orgid_link and d.type = :type")
	public List<OrgSal_Type_Level> getall_byorg_and_type(@Param ("orgid_link")final  Long orgid_link, @Param ("type")final  Integer type);

	@Query(value = "select c from OrgSal_Type_Level c "
			+ "inner join OrgSal_Type d on c.saltypeid_link = d.id"
			+ " where d.orgid_link = :orgid_link and d.type = :type"
			+ " and d.id in (select saltypeid_link from OrgSal_Type_LaborLevel e where e.laborlevelid_link = :laborlevelid_link)")
	public List<OrgSal_Type_Level> getall_bylaborlevel(@Param ("orgid_link")final  Long orgid_link, 
			@Param ("type")final  Integer type,
			@Param ("laborlevelid_link")final  Long laborlevelid_link);
}
