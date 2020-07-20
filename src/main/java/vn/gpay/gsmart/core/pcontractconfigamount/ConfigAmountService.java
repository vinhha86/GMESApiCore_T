package vn.gpay.gsmart.core.pcontractconfigamount;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Specifications;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class ConfigAmountService extends AbstractService<ConfigAmount> implements IConfigAmountService {
	@Autowired IConfigAmountRepository repo;
	@Override
	protected JpaRepository<ConfigAmount, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public ConfigAmount getby_amount(int amount) {
		// TODO Auto-generated method stub
		Specification<ConfigAmount> specification = Specifications.<ConfigAmount>and()
	            .le(amount > 0, "amount_from",amount)
	            .ge(amount > 0,"amount_to" , amount)
	            .build();
		
		List<ConfigAmount> lst = repo.findAll(specification);
		return lst.size() > 0 ? lst.get(0) : null;
	}

}
