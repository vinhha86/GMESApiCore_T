package vn.gpay.gsmart.core.salary;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;


@Service
public class OrgSal_Com_PositionService extends AbstractService< OrgSal_Com_Position> implements IOrgSal_Com_PositionService {
	@Autowired IOrgSal_Com_PositionRepository repo;
	
	@Override
	protected JpaRepository<OrgSal_Com_Position, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<OrgSal_Com_Position> getall_bysalcom(Long salcomid_link) {
		return repo.getall_bysalcom(salcomid_link);
	}
	
	@Override
	public List<OrgSal_Com_Position> getall_bysalcom_position(Long salcomid_link, Long positionid_link) {
		return repo.getall_bysalcom_position(salcomid_link, positionid_link);
	}
}
