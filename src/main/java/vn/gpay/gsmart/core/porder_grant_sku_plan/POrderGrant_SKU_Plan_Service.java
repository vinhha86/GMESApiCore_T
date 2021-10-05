package vn.gpay.gsmart.core.porder_grant_sku_plan;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class POrderGrant_SKU_Plan_Service extends AbstractService<POrderGrant_SKU_Plan> implements IPOrderGrant_SKU_Plan_Service {

	@Autowired IPOrderGrant_SKU_Plan_Repository repo;
	
	@Override
	protected JpaRepository<POrderGrant_SKU_Plan, Long> getRepository() {
		return repo;
	}

	@Override
	public List<POrderGrant_SKU_Plan> getByPorderGrantSku_Date(Long porder_grant_skuid_link, Date dateFrom,
			Date dateTo) {
		return repo.getByPorderGrantSku_Date(porder_grant_skuid_link, dateFrom, dateTo);
	}

}
