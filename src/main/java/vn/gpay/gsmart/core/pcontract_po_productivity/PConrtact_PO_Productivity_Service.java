package vn.gpay.gsmart.core.pcontract_po_productivity;

import java.util.List;

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
	@Override
	public Integer getProductivityByPOAndProduct(Long pcontract_poid_link, Long productid_link){
		List<PContract_PO_Productivity> a = repo.getByPOAndProduct(pcontract_poid_link, productid_link);
		if (a.size() > 0)
			return a.get(0).getPlan_productivity();
		else
			return null;
	}
	@Override
	public List<PContract_PO_Productivity> getbypo(Long pcontract_poid_link) {
		// TODO Auto-generated method stub
		return repo.getByPO(pcontract_poid_link);
	}
}
