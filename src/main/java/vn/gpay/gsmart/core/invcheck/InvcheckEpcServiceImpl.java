package vn.gpay.gsmart.core.invcheck;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import vn.gpay.gsmart.core.base.AbstractService;
@Service
public class InvcheckEpcServiceImpl extends AbstractService<InvcheckEpc> implements IInvcheckEpcService{

	@Autowired
	InvcheckEpcRepository repository; 
	@Override
	protected JpaRepository<InvcheckEpc, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}
	@Override
	public List<InvcheckEpc> findEpcBySkuId(long invcheckid_link, long skuid_link) {
		// TODO Auto-generated method stub
		return repository.findEpcBySkuId(invcheckid_link, skuid_link);
	}

}
