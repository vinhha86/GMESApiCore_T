package vn.gpay.gsmart.core.pcontract_bom2_npl_poline;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PContract_bom2_npl_poline_Service extends AbstractService<PContract_bom2_npl_poline> implements IPContract_bom2_npl_poline_Service {
	@Autowired IPContract_bom2_npl_poline_repo repo;
	@Override
	protected JpaRepository<PContract_bom2_npl_poline, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
//	@Override
//	List<PContractPO_NPL> getby_po_and_npl(Long pcontractpoid_link,Long material_skuid_link) {
//		// TODO Auto-generated method stub
//		return repo.getby_po(pcontractpoid_link);
//	}
	@Override
	public List<PContract_bom2_npl_poline> getby_po_and_npl(Long pcontractpoid_link, Long material_skuid_link) {
		// TODO Auto-generated method stub
		return repo.getby_po_and_npl(pcontractpoid_link, material_skuid_link);
	}
	@Override
	public List<PContract_bom2_npl_poline> getby_pcontract_and_npl(Long pcontractid_link, Long material_skuid_link) {
		// TODO Auto-generated method stub
		return repo.getby_pcontract_and_npl(pcontractid_link, material_skuid_link);
	}
	@Override
	public List<PContract_bom2_npl_poline> getby_product_and_npl(Long productid_link, Long pcontractid_link,
			Long material_skuid_link) {
		// TODO Auto-generated method stub
		return repo.getby_product_and_npl(pcontractid_link, material_skuid_link, productid_link);
	}

}
