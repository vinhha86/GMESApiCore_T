package vn.gpay.gsmart.core.pcontractproductcolor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PContractProductColorService extends AbstractService<PContractProductColor> implements IPContractProductColorService {
	@Autowired IPContractProductColorRepository repo;
	@Override
	public List<PContractProductColor> getcolor_by_pcontract_and_product(long orgrootid_link, long pcontractid_link,
			long productid_link) {
		// TODO Auto-generated method stub
		return repo.getcolor_by_product_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
	}

	@Override
	protected JpaRepository<PContractProductColor, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<Long> getcolorid_by_pcontract_and_product(long orgrootid_link, long pcontractid_link,
			long productid_link) {
		// TODO Auto-generated method stub
		return repo.getcolorid_by_product_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
	}

}
