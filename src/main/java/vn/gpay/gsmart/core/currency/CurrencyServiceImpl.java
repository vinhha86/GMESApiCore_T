package vn.gpay.gsmart.core.currency;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class CurrencyServiceImpl extends AbstractService<Currency> implements ICurrencyService{

	@Autowired CurrencyRepository repo;
	
	@Override
	protected JpaRepository<Currency, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<Currency> getAllCurrency() {
		return repo.getAllCurrency();
	}

}
