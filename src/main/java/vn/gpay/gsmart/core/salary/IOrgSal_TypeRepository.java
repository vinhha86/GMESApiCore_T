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
public interface IOrgSal_TypeRepository extends JpaRepository<OrgSal_Type, Long>, JpaSpecificationExecutor<OrgSal_Type> {
	@Query(value = "select c from OrgSal_Type c where c.type = :type and c.orgid_link = :orgid_link")
	public List<OrgSal_Type> getall_byorg(@Param ("orgid_link")final  Long orgid_link, @Param ("type")final  Integer type);
}
