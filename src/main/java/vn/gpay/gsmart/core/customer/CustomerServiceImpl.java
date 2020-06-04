package vn.gpay.gsmart.core.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class CustomerServiceImpl extends AbstractService<Customer> implements ICustomerService{

	@Autowired
	CustomerRepository repositoty;

	@Override
	protected JpaRepository<Customer, Long> getRepository() {
		// TODO Auto-generated method stub
		return repositoty;
	}

	@Override
	public List<Customer> findbycode(String customercode) {
		// TODO Auto-generated method stub
		return repositoty.findbycode(customercode);
	}

}
