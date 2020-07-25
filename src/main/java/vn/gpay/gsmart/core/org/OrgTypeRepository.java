package vn.gpay.gsmart.core.org;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface OrgTypeRepository extends JpaRepository<OrgType, Long>{
	@Query(value = "select c from OrgType c where c.id in (1,2,3,4,8,9,13,14) order by c.id asc")
	public List<OrgType> findOrgTypeForMenuOrg();
}
