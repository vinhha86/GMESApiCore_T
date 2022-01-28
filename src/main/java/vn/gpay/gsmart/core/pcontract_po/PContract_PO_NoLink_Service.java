package vn.gpay.gsmart.core.pcontract_po;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PContract_PO_NoLink_Service extends AbstractService<PContract_PO_NoLink> implements IPContract_PO_NoLink_Service {
	@Autowired
	IPContract_PO_NoLink_Repository repo;

	@Override
	protected JpaRepository<PContract_PO_NoLink, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<PContract_PO_NoLink> getPO_HavetoShip(Long orgrootid_link, Date shipdate_from, Date shipdate_to, Long orgbuyerid_link) {
		// TODO Auto-generated method stub
//		System.out.println(shipdate_from);
//		System.out.println(shipdate_to);
		return repo.getPO_HavetoShip(orgrootid_link, shipdate_from, shipdate_to, orgbuyerid_link);
	}
}
