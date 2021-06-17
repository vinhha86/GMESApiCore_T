package vn.gpay.gsmart.core.personel;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.utils.Common;

@Service
public class Personnel_inout_Service extends AbstractService<Personnel_inout> implements IPersonnel_inout_Service {
	@Autowired Personnel_inout_repository repo;
	@Autowired Common commonService;
	
	@Override
	protected JpaRepository<Personnel_inout, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<Personnel_inout> getby_person(Long personnelid_link, Date today) {
		// TODO Auto-generated method stub
		Date date_from = commonService.getBeginOfDate(today);
		Date date_to = commonService.getEndOfDate(today);
		
		return repo.getby_person(personnelid_link, date_from, date_to);
	}
	@Override
	public List<Personnel_inout> getPersonInout(Date today) {
		// TODO Auto-generated method stub
		Date date_from = commonService.getBeginOfDate(today);
		Date date_to = commonService.getEndOfDate(today);
		return repo.GetPersonInOut(date_from, date_to);
	}

}