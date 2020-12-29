package vn.gpay.gsmart.core.fabric_price;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class FabricPriceService extends AbstractService<FabricPrice> implements IFabricPriceService {

	@Autowired IFabricPriceRepository repo;
	
	@Override
	protected JpaRepository<FabricPrice, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<FabricPrice> getByMaterial(Long materialid_link) {
		// TODO Auto-generated method stub
		return repo.getByMaterial(materialid_link);
	}
	
}
