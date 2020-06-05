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
	public List<PContract_Price> getby_pcontractpo_id_link(long pcontract_poid_link) {
		// TODO Auto-generated method stub
		return repo.getby_pcontractpo_id_link(pcontract_poid_link);
	}

}
