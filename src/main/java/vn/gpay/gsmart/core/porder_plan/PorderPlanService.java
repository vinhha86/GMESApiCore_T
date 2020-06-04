package vn.gpay.gsmart.core.porder_plan;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PorderPlanService extends AbstractService<Porder_Plan> implements IPorderPlanService {
	@Autowired PorderPlanRepository repo;
	@Override
	protected JpaRepository<Porder_Plan, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<Porder_Plan> get_by_plantype(long porderid_link, int plan_type) {
		// TODO Auto-generated method stub
		return repo.getby_porder(porderid_link, plan_type);
	}

}
