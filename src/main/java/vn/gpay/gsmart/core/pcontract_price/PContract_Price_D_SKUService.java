package vn.gpay.gsmart.core.pcontract_price;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PContract_Price_D_SKUService extends AbstractService<PContract_Price_D_SKU> implements IPContract_Price_D_SKUService{
	@Autowired IPContract_Price_D_SKURepository repo;
	@Override
	protected JpaRepository<PContract_Price_D_SKU, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<PContract_Price_D_SKU> getPrice_D_SKU_ByPO(Long pcontract_poid_link) {
		// TODO Auto-generated method stub
		return repo.getPrice_D_SKU_ByPO(pcontract_poid_link);
	}
}
