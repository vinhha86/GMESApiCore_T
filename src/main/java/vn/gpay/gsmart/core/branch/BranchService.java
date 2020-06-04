package vn.gpay.gsmart.core.branch;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;


@Service
public class BranchService extends AbstractService<Branch> implements IBranchService {
	@Autowired IBranchRepository repo;
	
	@Override
	public List<Branch> getall_byorgrootid(long orgrootid_link) {
		// TODO Auto-generated method stub
		return repo.getall_byorgrootid(orgrootid_link);
	}

	@Override
	protected JpaRepository<Branch, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
