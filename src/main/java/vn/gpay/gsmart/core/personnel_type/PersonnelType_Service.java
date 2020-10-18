package vn.gpay.gsmart.core.personnel_type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PersonnelType_Service extends AbstractService<PersonnelType> implements IPersonnelType_Service{
	@Autowired PersonnelType_Repository repo;
	@Override
	protected JpaRepository<PersonnelType, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
