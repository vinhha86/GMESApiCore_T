package vn.gpay.gsmart.core.salary;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;


@Service
public class Salary_Sum_Porders_Service extends AbstractService<Salary_Sum_POrders> implements ISalary_Sum_POrdersService {
	@Autowired ISalary_Sum_POrdersRepository repo;
	
	@Override
	public List<Salary_Sum_POrders> getall_byorg(long orgid_link, int year, int month) {
		// TODO Auto-generated method stub
		return repo.getall_byorg(orgid_link, year, month);
	}

	@Override
	protected JpaRepository<Salary_Sum_POrders, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	
	@Override
	public void saveWithCheck(Salary_Sum_POrders entity){
		List<Salary_Sum_POrders> a = repo.getby_key(entity.getPordergrantid_link(), entity.getYear(), entity.getMonth());
		if (a.size() > 0){
			Salary_Sum_POrders theSalSum_Porder = a.get(0);
			entity.setId(theSalSum_Porder.getId());
			repo.save(entity);
		} else {
			repo.save(entity);
		}
	}
}
