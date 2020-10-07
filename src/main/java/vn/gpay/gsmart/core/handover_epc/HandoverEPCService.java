package vn.gpay.gsmart.core.handover_epc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class HandoverEPCService extends AbstractService<HandoverEPC> implements IHandoverEPCService {
	@Autowired HandoverEPCRepository repo;

	@Override
	protected JpaRepository<HandoverEPC, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
}
