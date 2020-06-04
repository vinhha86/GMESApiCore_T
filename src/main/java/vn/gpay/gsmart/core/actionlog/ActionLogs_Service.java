package vn.gpay.gsmart.core.actionlog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class ActionLogs_Service extends AbstractService<ActionLogs> implements IActionLogs_Service {
	@Autowired IActionLogs_Repository repo;
	@Override
	protected JpaRepository<ActionLogs, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	
	@Override
	public List<ActionLogs> findUserByUser(String userid_link){
		return repo.findUserByUser(userid_link);
	}
}
