package vn.gpay.gsmart.core.porder_bom_sku;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class POrderBOMSKU_LoaiPhoi_Service extends AbstractService<porder_bom_sku_loaiphoi>
		implements IPOrderBOMSKU_LoaiPhoi_Service {
	@Autowired
	IPOrderBOMSKU_loaiphoi_Repository repo;

	@Override
	protected JpaRepository<porder_bom_sku_loaiphoi, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<porder_bom_sku_loaiphoi> getbypcontract_product_material_sku_loaiphoi(Long pcontractid_link,
			Long productid_link, Long material_skuid_link, Long skuid_link, String loaiphoi) {
		// TODO Auto-generated method stub
		return repo.getby_pcontract_product_material_sku_loaiphoi(pcontractid_link, productid_link, material_skuid_link,
				skuid_link, loaiphoi);
	}

	@Override
	public List<porder_bom_sku_loaiphoi> getbypcontract_product_material_sku(Long pcontractid_link, Long productid_link,
			Long material_skuid_link, Long skuid_link) {
		// TODO Auto-generated method stub
		return repo.getby_pcontract_product_material_sku(pcontractid_link, productid_link, material_skuid_link,
				skuid_link);
	}

}
