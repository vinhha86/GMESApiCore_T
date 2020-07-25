package vn.gpay.gsmart.core.porder_grant;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class POrderGrant_SKUService extends AbstractService<POrderGrant_SKU> implements IPOrderGrant_SKUService {
	@Autowired IPOrderGrant_SKURepository repo;
	@Override
	protected JpaRepository<POrderGrant_SKU, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	
	@Override
	public List<POrderGrant_SKU>getPOrderGrant_SKU(Long pordergrantid_link){
		return repo.getPOrderGrant_SKU(pordergrantid_link);
	}

	@Override
	public POrderGrant_SKU getPOrderGrant_SKUbySKUid_link(Long skuid_link) {
		return repo.getPOrderGrant_SKUbySKUid_link(skuid_link);
	}
}
