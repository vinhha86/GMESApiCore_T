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
	@Query(value = "select c from OrgSal_Com c where c.orgid_link = :orgid_link ")
	public List<OrgSal_Com> getall_byorg(@Param ("orgid_link")final  Long orgid_link);
}
