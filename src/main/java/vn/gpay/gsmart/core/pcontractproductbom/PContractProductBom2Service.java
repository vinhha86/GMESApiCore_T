package vn.gpay.gsmart.core.pcontractproductbom;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PContractProductBom2Service extends AbstractService<PContractProductBom2> implements IPContractProductBom2Service {
	@Autowired IPContractProductBom2Repository repo;
	
	@Override
	public List<PContractProductBom2> get_pcontract_productBOMbyid(long productid_link, long pcontractid_link) {
		// TODO Auto-generated method stub
		return repo.getall_material_in_pcontract_productBOM(productid_link, pcontractid_link);
	}

	@Override
	protected JpaRepository<PContractProductBom2, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<PContractProductBom2> getby_pcontract_product_material(long productid_link, long pcontractid_link,
			long materialid_link) {
		// TODO Auto-generated method stub
		return repo.getby_material_pcontract_product(productid_link, pcontractid_link, materialid_link);
	}

}
