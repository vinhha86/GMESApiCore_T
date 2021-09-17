package vn.gpay.gsmart.core.personnel_position;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class Personnel_Position_Service extends AbstractService<Personnel_Position>
		implements IPersonnel_Position_Service {
	@Autowired
	IPersonnel_Position_Repository repo;

	@Override
	public List<Personnel_Position> getPersonnel_Position() {
		// TODO Auto-generated method stub
		return repo.getPersonnel_Position();
	}

	@Override
	protected JpaRepository<Personnel_Position, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<Personnel_Position> getByName_Code(String name, String code) {
		// TODO Auto-generated method stub
		return repo.getByName_Code(name, code);
	}

	@Override
	public List<Personnel_Position> getByOrg(long orgid_link) {
		// TODO Auto-generated method stub
		return repo.getByOrg(orgid_link);
	}

}
