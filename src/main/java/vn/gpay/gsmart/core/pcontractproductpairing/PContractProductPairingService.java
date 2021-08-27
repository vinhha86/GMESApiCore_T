package vn.gpay.gsmart.core.pcontractproductpairing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.pcontractproduct.IPContractProductRepository;

@Service
public class PContractProductPairingService extends AbstractService<PContractProductPairing>
		implements IPContractProductPairingService {
	@Autowired
	IPContractProductPairingRepository repo;
	@Autowired
	IPContractProductRepository pp_repo;

	@Override
	protected JpaRepository<PContractProductPairing, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<PContractProductPairing> getall_bypcontract(long orgrootid_link, long pcontractid_link) {
		// TODO Auto-generated method stub
		return repo.getall_product_pair_bypcontract(pcontractid_link, orgrootid_link);
	}

	@Override
	public List<PContractProductPairing> getdetail_bypcontract_and_productpair(long orgrootid_link,
			long pcontractid_link, long productpairid_link) {
		// TODO Auto-generated method stub
		return repo.getall_product_pairdetail_bypcontract(pcontractid_link, productpairid_link, orgrootid_link);
	}

	@Override
	public Integer getAmountinSet(long pcontractid_link, long productid_link, long product_setid_link) {
		// TODO Auto-generated method stub
		Integer a = repo.getAmountinSet(pcontractid_link, productid_link, product_setid_link);
		a = a == null ? 1 : a;
		return a;
	}

}
