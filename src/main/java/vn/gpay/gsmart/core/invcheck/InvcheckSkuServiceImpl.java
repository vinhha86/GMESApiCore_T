package vn.gpay.gsmart.core.invcheck;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import vn.gpay.gsmart.core.base.AbstractService;
@Service
public class InvcheckSkuServiceImpl extends AbstractService<InvcheckSku> implements IInvcheckSkuService{

	@Autowired
	InvcheckSkuRepository repository; 
	@Override
	protected JpaRepository<InvcheckSku, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}
	@Override
	public List<InvcheckSku> invcheckSkuGetByInvcheckid_link(Long invcheckid_link) {
		// TODO Auto-generated method stub
		return repository.invcheckSkuGetByInvcheckid_link(invcheckid_link);
	}
	@Override
	public void updateTotalCheck(Long invcheckid_link, Long skuid_link) {
		// TODO Auto-generated method stub
		repository.updateTotalCheck(invcheckid_link, skuid_link);
	}
}
