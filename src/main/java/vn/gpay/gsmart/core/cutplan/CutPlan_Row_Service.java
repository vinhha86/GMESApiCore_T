package vn.gpay.gsmart.core.cutplan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import vn.gpay.gsmart.core.base.AbstractService;

public class CutPlan_Row_Service extends AbstractService<CutPlan_Row> implements ICutPlan_Row_Service{
	@Autowired CutPlan_Row_Repository repo;
	@Override
	protected JpaRepository<CutPlan_Row, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
