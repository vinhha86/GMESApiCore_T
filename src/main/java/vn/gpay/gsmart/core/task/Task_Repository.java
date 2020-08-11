package vn.gpay.gsmart.core.task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface Task_Repository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
	@Query(value = "select c from Task c where (userinchargeid_link = :userid_link "
			+ "or usercreatedid_link = :userid_link)")
	public List<Task> getby_user(
			@Param ("userid_link")final  long userid_link);
}
