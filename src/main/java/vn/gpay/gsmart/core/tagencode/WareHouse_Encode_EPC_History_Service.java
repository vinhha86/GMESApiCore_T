package vn.gpay.gsmart.core.tagencode;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class WareHouse_Encode_EPC_History_Service extends AbstractService<WareHouse_Encode_EPC_History>
		implements IWareHouse_Encode_EPC_History_Service {
	@Autowired
	WareHouse_Encode_EPC_History_Repository repo;

	@Override
	protected JpaRepository<WareHouse_Encode_EPC_History, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<WareHouse_Encode_EPC_History> get_epc_his(String epc, long orgencodeid_link) {
		// TODO Auto-generated method stub
		return repo.getEpc(epc, orgencodeid_link);
	}

}
