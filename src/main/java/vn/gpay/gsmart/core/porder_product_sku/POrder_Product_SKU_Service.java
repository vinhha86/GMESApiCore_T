package vn.gpay.gsmart.core.porder_product_sku;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class POrder_Product_SKU_Service extends AbstractService<POrder_Product_SKU> implements IPOrder_Product_SKU_Service{
	@Autowired IPOrder_Product_SKU_Repository repo;
	@Override
	protected JpaRepository<POrder_Product_SKU, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<POrder_Product_SKU> getby_productid_link(Long productid_link) {
		// TODO Auto-generated method stub
		return repo.getby_productidlink(productid_link);
	}
	@Override
	public List<POrder_Product_SKU> getby_porderandsku(Long porderid_link, Long skuid_link) {
		// TODO Auto-generated method stub
		return repo.getby_porderandsku(porderid_link, skuid_link);
	}
	
	@Override
	public List<POrder_Product_SKU> getby_porder(Long porderid_link) {
		// TODO Auto-generated method stub
		return repo.getby_porder(porderid_link);
	}
	@Override
	public POrder_Product_SKU get_sku_in_encode(Long porderid_link, Long skuid_link) {
		// TODO Auto-generated method stub
		return repo.get_sku_in_encode(skuid_link, porderid_link).get(0);
	}
	@Override
	public List<POrder_Product_SKU> getlist_sku_in_porder(Long orgrootid_link, Long porderid_link) {
		// TODO Auto-generated method stub
		return repo.get_sku_inporder(orgrootid_link, porderid_link);
	}
}
