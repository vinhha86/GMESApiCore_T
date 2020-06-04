package vn.gpay.gsmart.core.productattributevalue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class ProductAttributeService extends AbstractService<ProductAttributeValue> implements IProductAttributeService {
	
	@Autowired IProductAttributeRepository repo;
	
	@Override
	public List<ProductAttributeValue> getall_byProductId(Long productid_link) {
		// TODO Auto-generated method stub
		return repo.getall_byProductId(productid_link);
	}

	@Override
	protected JpaRepository<ProductAttributeValue, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<ProductAttributeValue> getList_byAttId(Long attributeid_link, Long productid_link) {
		// TODO Auto-generated method stub
		return repo.getlistvalue_byattribute(attributeid_link, productid_link);
	}

	@Override
	public List<ProductAttributeValue> getOne_byproduct_and_value(long productid_link, long attributeid_link,
			long attributevalueid_link) {
		// TODO Auto-generated method stub
		return repo.getOne_byproduct_and_value(attributeid_link, productid_link, attributevalueid_link);
	}

}
