package vn.gpay.gsmart.core.personnel_history;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPersonnel_His_repository extends JpaRepository<Personnel_His, Long>,JpaSpecificationExecutor<Personnel_His>{
	@Query("SELECT c FROM Personnel_His c where c.personnelid_link = :personnelid_link order by decision_date desc")
	public List<Personnel_His> getHis_person(@Param ("personnelid_link")final Long personnelid_link);
	
	@Query("SELECT c FROM Personnel_His c "
			+ "where c.personnelid_link = :personnelid_link "
			+ "and c.type = :type "
			+ "order by id desc")
	public List<Personnel_His> getmaxid_bytype_andperson(
			@Param ("personnelid_link")final Long personnelid_link,
			@Param ("type")final Integer type);
}
