package vn.gpay.gsmart.core.pcontract_po_shipping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Specifications;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PContract_PO_Shipping_DService extends AbstractService<PContract_PO_Shipping_D> implements IPContract_PO_Shipping_DService {
	@Autowired IPContract_PO_Shipping_DRepository repo;
	@Override
	protected JpaRepository<PContract_PO_Shipping_D, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<PContract_PO_Shipping_D> getByShippingID(Long pcontract_po_shippingid_link) {
		// TODO Auto-generated method stub
		Specification<PContract_PO_Shipping_D> specification = Specifications.<PContract_PO_Shipping_D>and()
	            .eq("pcontract_po_shippingid_link", pcontract_po_shippingid_link)
	            .build();
	
		return repo.findAll(specification);
	}
}
