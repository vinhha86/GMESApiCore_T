package vn.gpay.gsmart.core.pcontract_po;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;


import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PContract_POService extends AbstractService<PContract_PO> implements IPContract_POService {
	@Autowired IPContract_PORepository repo;
	@Override
	protected JpaRepository<PContract_PO, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<PContract_PO> getPriceByContract(Long orgrootid_link,
			Long pcontractid_link,Long productid_link){
		return repo.getPriceByContract(orgrootid_link, pcontractid_link, productid_link);
	}

}
