package vn.gpay.gsmart.core.tagencode;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class WareHouse_Encode_D_Service extends AbstractService<WareHouse_Encode_D> implements IWareHouse_Encode_D_Service {
	@Autowired WareHouse_Encode_D_Repository repo;
	@Override
	protected JpaRepository<WareHouse_Encode_D, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public WareHouse_Encode_D getwarehouse_encode_d_By_skucode(String skucode, long warehouse_encodeid_link) {
		// TODO Auto-generated method stub
		List<WareHouse_Encode_D> list = repo.get_byskucode(warehouse_encodeid_link, skucode);
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	@Override
	public WareHouse_Encode_D getwarehouse_encode_d_By_skuid(Long skuid, long warehouse_encodeid_link) {
		// TODO Auto-generated method stub
		List<WareHouse_Encode_D> list = repo.get_byskuid(warehouse_encodeid_link, skuid);
		if (list.size() > 0)
			return list.get(0);
		return null;
	}
	
	@Override
	public List<WareHouse_Encode_D> encode_getbyencodeid(Long warehouse_encodeid_link) {
		// TODO Auto-generated method stub
		return repo.encode_getbyencodeid(warehouse_encodeid_link);
	}		
}
