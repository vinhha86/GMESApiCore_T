package vn.gpay.gsmart.core.pcontractattributevalue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PContractProductAtrributeValueService extends AbstractService<PContractAttributeValue> implements IPContractProductAtrributeValueService {
	@Autowired IPContractProductAtrributeValueRepository repo;
	@Override
	protected JpaRepository<PContractAttributeValue, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<PContractAttributeValue> getattribute_by_product_and_pcontract(long orgrootid_link, long pcontractid_link,
			long productid_link) {
		// TODO Auto-generated method stub
		return repo.getattribute_by_product_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
	}
	@Override
	public List<PContractAttributeValue> getvalue_by_product_and_pcontract_and_attribute(long orgrootid_link,
			long pcontractid_link, long productid_link, long attributeid_link) {
		// TODO Auto-generated method stub
		return repo.getlistvalue_by_product_and_pcontract_and_attribute(orgrootid_link, productid_link, pcontractid_link, attributeid_link);
	}
	@Override
	public List<Long> getvalueid_by_product_and_pcontract_and_attribute(long orgrootid_link, long pcontractid_link,
			long productid_link, long attributeid_link) {
		// TODO Auto-generated method stub
		return repo.getlistvalueid_by_product_and_pcontract_and_attribute(orgrootid_link, productid_link, pcontractid_link, attributeid_link);
	}
	@Override
	public List<PContractAttributeValue> getbyvalue(long pcontractid_link, long productid_link, long attributeid_link,
			long attributevalueid_link) {
		// TODO Auto-generated method stub
		return repo.getbyvalue(productid_link, pcontractid_link, attributeid_link, attributevalueid_link);
	}

}
