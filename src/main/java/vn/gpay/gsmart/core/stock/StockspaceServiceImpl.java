package vn.gpay.gsmart.core.stock;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class StockspaceServiceImpl extends AbstractService<Stockspace> implements IStockspaceService{

	@Autowired
	StockspaceRepository repositoty;
	
	@Override
	protected JpaRepository<Stockspace, Long> getRepository() {
		// TODO Auto-generated method stub
		return repositoty;
	}

	@Override
	public List<Stockspace> findStockspaceByEpc(long orgid, String spaceepc) {
		// TODO Auto-generated method stub
		return repositoty.findStockspaceByEpc(orgid, spaceepc);
	}	
}
