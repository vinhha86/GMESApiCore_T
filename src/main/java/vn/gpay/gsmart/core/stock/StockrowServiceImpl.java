package vn.gpay.gsmart.core.stock;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class StockrowServiceImpl extends AbstractService<Stockrow> implements IStockrowService{

	@Autowired
	StockrowRepository repositoty;
	
	@Override
	protected JpaRepository<Stockrow, Long> getRepository() {
		// TODO Auto-generated method stub
		return repositoty;
	}

	@Override
	public List<Stockrow> findStockrowByOrgID(long id) {
		// TODO Auto-generated method stub
		return repositoty.findStockrowByOrgID(id);
	}

}
