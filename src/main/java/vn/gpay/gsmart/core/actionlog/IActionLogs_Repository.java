package vn.gpay.gsmart.core.actionlog;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IActionLogs_Repository extends JpaRepository<ActionLogs, Long>{
	@Query(value = "select a from ActionLogs a where a.userid_link =:userid_link order by a.action_time")
	public List<ActionLogs> findUserByUser(@Param ("userid_link")final String userid_link);	
}
