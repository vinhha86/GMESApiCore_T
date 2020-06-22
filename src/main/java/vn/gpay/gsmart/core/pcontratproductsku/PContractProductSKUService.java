package vn.gpay.gsmart.core.pcontratproductsku;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;


@Service
public class PContractProductSKUService extends AbstractService<PContractProductSKU> implements IPContractProductSKUService {
	@Autowired IPContractProductSKURepository repo;
	@Override
	protected JpaRepository<PContractProductSKU, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<PContractProductSKU> getlistsku_byproduct_and_pcontract(long orgrootid_link, long productid_link,
			long pcontractid_link) {
		// TODO Auto-generated method stub
		return repo.getlistsku_byproduct_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
	}
	@Override
	public List<PContractProductSKU> getlistsku_bypo_and_pcontract(long orgrootid_link, long pcontract_poid_link,
			long pcontractid_link) {
		// TODO Auto-generated method stub
		return repo.getlistsku_bypo_and_pcontract(orgrootid_link, pcontract_poid_link, pcontractid_link);
	}
	@Override
	public List<PContractProductSKU> getlistsku_bysku_and_pcontract(long skuid_link, long pcontractid_link) {
		// TODO Auto-generated method stub
		return repo.getlistsku_bysku_and_pcontract(skuid_link, pcontractid_link);
	}
	@Override
	public List<Long> getlistvalue_by_product(long pcontractid_link, long productid_link, long attributeid_link) {
		// TODO Auto-generated method stub
		List<Long> list = repo.getvaluesize_in_product(productid_link, pcontractid_link, attributeid_link);
		return list;
	}
	@Override
	public List<Long> getsku_bycolor(long pcontractid_link, long productid_link, long colorid_link) {
		// TODO Auto-generated method stub
		return repo.getskuid_bycolorid_link(productid_link, pcontractid_link, colorid_link);
	}
	@Override
	public List<PContractProductSKU> getbypo_and_product(long pcontract_poid_link, long productid_link) {
		// TODO Auto-generated method stub
		return repo.getlistsku_bypo_and_product(pcontract_poid_link, productid_link);
	}
}
