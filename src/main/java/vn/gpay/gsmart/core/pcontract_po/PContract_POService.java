package vn.gpay.gsmart.core.pcontract_po;

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


}
