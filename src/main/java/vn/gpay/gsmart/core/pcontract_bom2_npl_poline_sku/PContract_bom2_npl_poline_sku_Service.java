package vn.gpay.gsmart.core.pcontract_bom2_npl_poline_sku;

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

}
