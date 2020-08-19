package vn.gpay.gsmart.core.task_object;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ITask_Object_Repository extends JpaRepository<Task_Object, Long>, JpaSpecificationExecutor<Task_Object> {
	@Query(value = "select c from Task_Object c where taskobjecttypeid_link = :objecttypeid_link "
			+ "and objectid_link = :objectid_link")
	public List<Task_Object> getby_tasktype_and_objectid_link(
			@Param ("objecttypeid_link")final  long objecttypeid_link,
			@Param ("objectid_link")final  long objectid_link);
	
	@Query(value = "select c.taskid_link from Task_Object c where taskobjecttypeid_link = :objecttypeid_link "
			+ "and objectid_link = :objectid_link group by c.taskid_link")
	public List<Long> getlistid_by_tasktype_and_objectid_link(
			@Param ("objecttypeid_link")final  long objecttypeid_link,
			@Param ("objectid_link")final  long objectid_link);
	
	@Query(value = "select c from Task_Object c where taskid_link = :taskid_link")
	public List<Task_Object> getbytask(
			@Param ("taskid_link")final  long taskid_link);
}
