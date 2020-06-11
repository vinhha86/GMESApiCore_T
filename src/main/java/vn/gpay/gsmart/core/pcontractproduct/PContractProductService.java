package vn.gpay.gsmart.core.pcontractproduct;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.pcontractproductpairing.IPContractProductPairingRepository;


@Service
public class PContractProductService extends AbstractService<PContractProduct> implements IPContractProductService {
	@Autowired IPContractProductRepository repo;
	@Autowired IPContractProductPairingRepository pproductpair_repo;
	@Override
	protected JpaRepository<PContractProduct, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<PContractProduct> get_by_product_and_pcontract(long orgrootid_link, long productid_link,
			long pcontractid_link) {
		// TODO Auto-generated method stub
		return repo.get_by_product_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
	}
	@Override
	public List<Long> get_by_orgcustomer(Long orgrootid_link,Long orgbuyerid_link){
		return repo.get_by_orgcustomer(orgrootid_link, orgbuyerid_link);
	}
}
