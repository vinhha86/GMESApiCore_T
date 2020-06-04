package vn.gpay.gsmart.core.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class ProductBomService extends AbstractService<ProductBOM> implements IProductBomService{
	@Autowired IProductBOMRepository repo;
	@Override
	protected JpaRepository<ProductBOM, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<ProductBOM> getproductBOMbyid(long productid_link) {
		// TODO Auto-generated method stub
		return repo.getall_material_in_productBOM(productid_link);
	}

}
