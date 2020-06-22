package vn.gpay.gsmart.core.markettype;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class MarketTypeService extends AbstractService<MarketType> implements IMarketTypeService{
	@Autowired IMarketType_Repository repo;
	
	@Override
	protected JpaRepository<MarketType, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<MarketType> getMarket_by_Status(int status) {
		// TODO Auto-generated method stub
		return repo.getby_status(status);
	}
}
