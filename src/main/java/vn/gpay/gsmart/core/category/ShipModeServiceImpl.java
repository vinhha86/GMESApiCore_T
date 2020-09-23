package vn.gpay.gsmart.core.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class ShipModeServiceImpl  extends AbstractService<ShipMode> implements IShipModeService{

	@Autowired
	ShipModeRepository repositoty;
	
	@Override
	protected JpaRepository<ShipMode, Long> getRepository() {
		// TODO Auto-generated method stub
		return repositoty;
	}

	@Override
	public List<ShipMode> getbyname(String name) {
		// TODO Auto-generated method stub
		return repositoty.getby_name(name);
	}

}
