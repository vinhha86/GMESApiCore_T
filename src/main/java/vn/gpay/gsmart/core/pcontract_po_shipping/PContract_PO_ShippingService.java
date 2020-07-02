package vn.gpay.gsmart.core.pcontract_po_shipping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Specifications;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PContract_PO_ShippingService extends AbstractService<PContract_PO_Shipping> implements IPContract_PO_ShippingService {
	@Autowired IPContract_PO_ShippingRepository repo;
	@Override
	protected JpaRepository<PContract_PO_Shipping, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<PContract_PO_Shipping> getByPOID(Long pcontract_poid_link) {
		// TODO Auto-generated method stub
		Specification<PContract_PO_Shipping> specification = Specifications.<PContract_PO_Shipping>and()
	            .eq("pcontract_poid_link", pcontract_poid_link)
	            .build();
	
		return repo.findAll(specification);
	}
}
