package vn.gpay.gsmart.core.pcontract_bom2_npl_poline_sku;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PContract_bom2_npl_poline_sku_Service extends AbstractService<PContract_bom2_npl_poline_sku> implements IPContract_bom2_npl_poline_sku_Service {
	@Autowired PContract_bom2_npl_poline_sku_repo repo;
	@Override
	protected JpaRepository<PContract_bom2_npl_poline_sku, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<PContract_bom2_npl_poline_sku> getby_po_and_sku(Long orgrootid_link, Long pcontract_poid_link,
			Long product_skuid_link, Long material_skuid_link) {
		// TODO Auto-generated method stub
		return repo.getby_po_and_sku(pcontract_poid_link, product_skuid_link, orgrootid_link, material_skuid_link);
	}
	@Override
	public List<PContract_bom2_npl_poline_sku> getby_po(Long orgrootid_link, Long pcontract_poid_link, Long material_skuid_link, Long productid_link) {
		// TODO Auto-generated method stub
		return repo.getby_po(pcontract_poid_link, orgrootid_link, material_skuid_link, productid_link);
	}
	@Override
	public List<PContract_bom2_npl_poline_sku> getone_rec(Long orgrootid_link, Long pcontractid_link,
			Long productid_link, Long pcontract_poid_link, Long product_skuid_link, Long material_skuid_link) {
		// TODO Auto-generated method stub
		return repo.getone(pcontract_poid_link, orgrootid_link, material_skuid_link, productid_link, product_skuid_link);
	}

}
