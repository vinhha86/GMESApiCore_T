package vn.gpay.gsmart.core.salary;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;


@Service
public class Salary_SumService extends AbstractService<Salary_Sum> implements ISalary_SumService {
	@Autowired ISalary_SumRepository repo;
	
	@Override
	public List<Salary_Sum> getall_byorg(long orgid_link) {
		// TODO Auto-generated method stub
		return repo.getall_byorg(orgid_link);
	}

	@Override
	protected JpaRepository<Salary_Sum, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
