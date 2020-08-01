package vn.gpay.gsmart.core.porder_sewingcost;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class POderSewingCost_Service extends AbstractService<POrderSewingCost> implements IPorderSewingCost_Service {
	@Autowired IPOrderSewignCost_Repository repo;
	@Override
	protected JpaRepository<POrderSewingCost, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<POrderSewingCost> getby_porder_and_workingprocess(Long porderid_link, Long workingprocessid_link) {
		// TODO Auto-generated method stub
		workingprocessid_link = workingprocessid_link == 0 ? null : workingprocessid_link;
		
		return repo.getby_porder_and_workingprocess(porderid_link, workingprocessid_link);
	}

}
