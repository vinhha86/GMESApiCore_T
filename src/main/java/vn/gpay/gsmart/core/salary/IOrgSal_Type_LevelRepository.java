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
	@Query(value = "select c from OrgSal_Type_Level c where c.orgrootid_link = :orgrootid_link ")
	public List<OrgSal_Type_Level> getall_byorgrootid(@Param ("orgrootid_link")final  Long orgrootid_link);
	
	@Query(value = "select c from OrgSal_Type_Level c "
			+ "inner join OrgSal_Type d on c.saltypeid_link = d.id"
			+ " where d.orgid_link = :orgid_link and d.type = :type")
	public List<OrgSal_Type_Level> getall_byorg_and_type(@Param ("orgid_link")final  Long orgid_link, @Param ("type")final  Integer type);
}
