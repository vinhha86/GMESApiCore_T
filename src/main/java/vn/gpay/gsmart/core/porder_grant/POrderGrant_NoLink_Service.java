package vn.gpay.gsmart.core.porder_grant;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Service;


import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class POrderGrant_NoLink_Service extends AbstractService<POrderGrant_NoLink> implements IPOrderGrant_NoLink_Service {
	@Autowired
	IPOrderGrant__NoLink_Repository repo;

	@Override
	protected JpaRepository<POrderGrant_NoLink, Long> getRepository() {
		return repo;
	}

	@Override
	public List<POrderGrant_NoLink> getPOrderGrantBySearch(
			String stylebuyer,
			String pobuyer,
			Long buyerid, 
			Long vendorid,
			String contractcode			
			) {
		if (buyerid != null || vendorid != null)
			return repo.getPOrderGrant_PContract(stylebuyer,
				pobuyer,
				buyerid,
				vendorid
				);
		else 
			return repo.getPOrderGrant(stylebuyer,
					pobuyer
					);
	}
	
}
