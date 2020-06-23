package vn.gpay.gsmart.core.org;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class OrgTypeServiceImpl extends AbstractService<OrgType> implements IOrgTypeService{

	@Autowired
	OrgTypeRepository repositoty;

	@Override
	protected JpaRepository<OrgType, Long> getRepository() {
		// TODO Auto-generated method stub
		return repositoty;
	}

	@Override
	public List<OrgType> findOrgTypeForMenuOrg() {
		// TODO Auto-generated method stub
		return repositoty.findOrgTypeForMenuOrg();
	}

}
