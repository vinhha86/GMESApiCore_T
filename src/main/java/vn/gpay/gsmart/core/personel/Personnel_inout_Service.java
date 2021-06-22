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
	public List<Personnel_inout> getlist_not_checkout(String personnel_code) {
		// TODO Auto-generated method stub
		
		return repo.getlist_not_checkout(personnel_code);
	}
	@Override
	public List<Personnel_inout> getby_bikenumber_and_timein(String bike_number, Date timein) {
		// TODO Auto-generated method stub
		return repo.getby_bikenumber_and_timein(bike_number, timein);
	}

}
