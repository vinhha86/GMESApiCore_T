package vn.gpay.gsmart.core.timesheet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class TimeSheet_Service extends AbstractService<TimeSheet> implements ITimeSheet_Service{
	@Autowired TimeSheet_repository repo;
	@Override
	protected JpaRepository<TimeSheet, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
