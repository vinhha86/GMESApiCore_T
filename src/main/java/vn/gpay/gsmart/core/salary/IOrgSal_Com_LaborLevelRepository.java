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
public interface IOrgSal_Com_LaborLevelRepository extends JpaRepository<OrgSal_Com_LaborLevel, Long>, JpaSpecificationExecutor<OrgSal_Com_LaborLevel> {
	@Query(value = "select c from OrgSal_Com_LaborLevel c where c.salcomid_link = :salcomid_link ")
	public List<OrgSal_Com_LaborLevel> getall_bysalcom(@Param ("salcomid_link")final  Long salcomid_link);
	
	@Query(value = "select c from OrgSal_Com_LaborLevel c where c.salcomid_link = :salcomid_link and c.laborlevelid_link = :laborlevelid_link")
	public List<OrgSal_Com_LaborLevel> getall_bysalcom_laborlevel(@Param ("salcomid_link")final  Long salcomid_link, @Param ("laborlevelid_link")final  Long laborlevelid_link);

}
