package vn.gpay.gsmart.core.cutplan;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class CutPlanService extends AbstractService<CutPlan_Size> implements ICutPlan_Service {
	@Autowired CutPlan_Repository repo;
	
	@Override
	protected JpaRepository<CutPlan_Size, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<CutPlan_Size> getby_sku_and_porder(Long skuid_link, Long porderid_link, Long orgrootid_link) {
		// TODO Auto-generated method stub
		return repo.getby_sku_and_porder(skuid_link, porderid_link, orgrootid_link);
	}

}
