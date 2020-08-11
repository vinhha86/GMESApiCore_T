package vn.gpay.gsmart.core.task_grant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ITask_Grant_Repository extends JpaRepository<Task_Grant, Long>, JpaSpecificationExecutor<Task_Grant> {
	@Query(value = "select c from Task_Grant c where tasktypeid_link = :tasktypeid_link "
			+ "and orgid_link = :orgid_link")
	public List<Task_Grant> getby_tasktype_and_org(
			@Param ("tasktypeid_link")final  long tasktypeid_link,
			@Param ("orgid_link")final  long orgid_link);
}
