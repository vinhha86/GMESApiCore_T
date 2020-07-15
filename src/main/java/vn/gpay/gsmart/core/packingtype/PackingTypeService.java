package vn.gpay.gsmart.core.packingtype;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;


import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PackingTypeService extends AbstractService<PackingType> implements IPackingTypeService {
	@Autowired IPackingTypeRepository repo;
	@Override
	protected JpaRepository<PackingType, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	
	@Override
	public List<PackingType> getall_byorgrootid(long orgrootid_link) {
		// TODO Auto-generated method stub
		return repo.getall_byorgrootid(orgrootid_link);
	}
}
