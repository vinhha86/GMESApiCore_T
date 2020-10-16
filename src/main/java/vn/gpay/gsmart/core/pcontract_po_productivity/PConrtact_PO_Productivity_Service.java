package vn.gpay.gsmart.core.pcontract_po_productivity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PConrtact_PO_Productivity_Service extends AbstractService<PContract_PO_Productivity> implements IPContract_PO_Productivity_Service {
	@Autowired PContract_PO_Productivity_Repository repo;
	@Override
	protected JpaRepository<PContract_PO_Productivity, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
