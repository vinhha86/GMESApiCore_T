package vn.gpay.gsmart.core.pcontractpo_npl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PContractPO_npl_Service extends AbstractService<PContractPO_NPL> implements IPContractPO_npl_Service {
	@Autowired IPContractpo_npl_repo repo;
	@Override
	protected JpaRepository<PContractPO_NPL, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
//	@Override
//	List<PContractPO_NPL> getby_po_and_npl(Long pcontractpoid_link,Long material_skuid_link) {
//		// TODO Auto-generated method stub
//		return repo.getby_po(pcontractpoid_link);
//	}
	@Override
	public List<PContractPO_NPL> getby_po_and_npl(Long pcontractpoid_link, Long material_skuid_link) {
		// TODO Auto-generated method stub
		return repo.getby_po_and_npl(pcontractpoid_link, material_skuid_link);
	}
	@Override
	public List<PContractPO_NPL> getby_pcontract_and_npl(Long pcontractid_link, Long material_skuid_link) {
		// TODO Auto-generated method stub
		return repo.getby_pcontract_and_npl(pcontractid_link, material_skuid_link);
	}

}
