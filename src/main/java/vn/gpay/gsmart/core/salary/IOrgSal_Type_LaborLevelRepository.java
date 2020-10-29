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
public interface IOrgSal_Type_LaborLevelRepository extends JpaRepository<OrgSal_Type_LaborLevel, Long>, JpaSpecificationExecutor<OrgSal_Type_LaborLevel> {
	@Query(value = "select c from OrgSal_Type_LaborLevel c where c.saltypeid_link = :saltypeid_link ")
	public List<OrgSal_Type_LaborLevel> getall_bysaltype(@Param ("saltypeid_link")final  Long saltypeid_link);
	
	@Query(value = "select c from OrgSal_Type_LaborLevel c where c.saltypeid_link = :saltypeid_link and c.laborlevelid_link = :laborlevelid_link")
	public List<OrgSal_Type_LaborLevel> getall_bysaltype_laborlevel(@Param ("saltypeid_link")final  Long saltypeid_link, @Param ("laborlevelid_link")final  Long laborlevelid_link);
}
