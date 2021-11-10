package vn.gpay.gsmart.core.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class ProductTypeService extends AbstractService<ProductType> implements IProductTypeService {
	@Autowired IProductTypeRepository repo;
	@Override
	protected JpaRepository<ProductType, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
