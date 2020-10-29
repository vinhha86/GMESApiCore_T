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
public interface IOrgSal_LevelRepository extends JpaRepository<OrgSal_Level, Long>, JpaSpecificationExecutor<OrgSal_Level> {
	@Query(value = "select c from OrgSal_Level c where c.orgrootid_link = :orgrootid_link ")
	public List<OrgSal_Level> getall_byorgrootid(@Param ("orgrootid_link")final  Long orgrootid_link);

}
