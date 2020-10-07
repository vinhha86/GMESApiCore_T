package vn.gpay.gsmart.core.encodeepc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class Encode_EPC_Service extends AbstractService<Encode_EPC> implements IEncode_EPC_Service{
	@Autowired
	Encode_EPC_Repository repo;
	@Override
	protected JpaRepository<Encode_EPC, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<Encode_EPC> get_epc_by_encodeid_link(long encodeid_link) {
		// TODO Auto-generated method stub
		return repo.get_epc_by_encodeid_link(encodeid_link);
	}

}
