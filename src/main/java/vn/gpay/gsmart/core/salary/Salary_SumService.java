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

	@Override
	public void saveWithCheck(Salary_Sum entity){
		List<Salary_Sum> a = repo.getby_key(entity.getPersonnelid_link(), entity.getYear(), entity.getMonth(), entity.getSumcolid_link());
		if (a.size() > 0){
			Salary_Sum theSalSum = a.get(0);
			theSalSum.setSumvalue(entity.getSumvalue());
			repo.save(theSalSum);
		} else {
			repo.save(entity);
		}
	}
}
