package vn.gpay.gsmart.core.pcontract_product_bom_log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import vn.gpay.gsmart.core.base.AbstractService;

public class PContract_bom2_sku_log_Service extends AbstractService<PContract_bom2_sku_log> implements IPContract_bom2_sku_log_Service{
	@Autowired IPContract_bom2_sku_log_Repo repo;
	
	@Override
	protected JpaRepository<PContract_bom2_sku_log, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
