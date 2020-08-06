package vn.gpay.gsmart.core.pcontract_price;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PContract_Price_Service extends AbstractService<PContract_Price> implements IPContract_Price_Service {
	@Autowired IPContract_Price_Repository repo;
	@Override
	protected JpaRepository<PContract_Price, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<PContract_Price> getPrice_ByPO(long pcontract_poid_link) {
		// TODO Auto-generated method stub
		return repo.getPrice_ByPO(pcontract_poid_link);
	}
	@Override
	public PContract_Price getPrice_CMP(long pcontract_poid_link, long productid_link) {
		// TODO Auto-generated method stub
		List<PContract_Price> a= repo.getPrice_CMP(pcontract_poid_link, productid_link);
		if (a.size()>0)
			return a.get(0);
		else
			return null;
	}
	@Override
	public List<PContract_Price> getPrice_by_product(long pcontract_poid_link, long productid_link) {
		// TODO Auto-generated method stub
		return repo.getPrice_by_product(pcontract_poid_link, productid_link);
	}
}
