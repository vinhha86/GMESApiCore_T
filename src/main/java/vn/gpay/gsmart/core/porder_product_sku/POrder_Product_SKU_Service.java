package vn.gpay.gsmart.core.porder_product_sku;

import java.util.ArrayList;
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
	@Override
	public List<Long> getlist_colorid_byporder(Long porderid_link) {
		// TODO Auto-generated method stub
		return repo.get_colorid_byporder(porderid_link);
	}
	
	@Override
	public List<POrder_Product_SKU> getsumsku_byporder(long porderid_link) {
		List<POrder_Product_SKU> result = new ArrayList<POrder_Product_SKU>();
		List<Object[]> rs = repo.getsumsku_byporder(porderid_link);
		for(Object[] record:rs){
			POrder_Product_SKU sku = new POrder_Product_SKU();
			sku.setSkuid_link((Long)record[0]);
			sku.setPquantity_total(((Long)record[1]).intValue());
			result.add(sku);
		}
		return result;
	}
}
