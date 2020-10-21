package vn.gpay.gsmart.core.personnel_history;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class Personnel_His_Service extends AbstractService<Personnel_His> implements IPersonnel_His_Service {
	@Autowired IPersonnel_His_repository repo;
	@Override
	protected JpaRepository<Personnel_His, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<Personnel_His> gethis_by_person(Long personnelid_link) {
		// TODO Auto-generated method stub
		return repo.getHis_person(personnelid_link);
	}
	@Override
	public Long getmaxid_bytype_andperson(Long personnelid_link, int type) {
		// TODO Auto-generated method stub
		List<Personnel_His> list = repo.getmaxid_bytype_andperson(personnelid_link, type);
		if(list.size() == 0)
			return (long)0;
		else
			return list.get(0).getId();
	}
	@Override
	public Long getpreid_bytype_andperson(Long personnelid_link, int type) {
		// TODO Auto-generated method stub
		List<Personnel_His> list = repo.getmaxid_bytype_andperson(personnelid_link, type);
		if(list.size() > 1)
			return (long)0;
		else
			return list.get(1).getId();
	}
	@Override
	public Personnel_His getprehis_bytype_andperson(Long personnelid_link, int type) {
		// TODO Auto-generated method stub
		List<Personnel_His> list = repo.getmaxid_bytype_andperson(personnelid_link, type);
		if(list.size() < 2)
			return null;
		else
			return list.get(1);
	}

}
