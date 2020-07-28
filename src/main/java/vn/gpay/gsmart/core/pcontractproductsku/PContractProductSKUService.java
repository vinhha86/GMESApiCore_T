package vn.gpay.gsmart.core.pcontractproductsku;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU;


@Service
public class PContractProductSKUService extends AbstractService<PContractProductSKU> implements IPContractProductSKUService {
	@Autowired IPContractProductSKURepository repo;
	@Autowired IPOrder_Service porder_Service;
	@Override
	protected JpaRepository<PContractProductSKU, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<PContractProductSKU> getlistsku_byproduct_and_pcontract(long orgrootid_link, long productid_link,
			long pcontractid_link) {
		// TODO Auto-generated method stub
		return repo.getlistsku_byproduct_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
	}	
	@Override
	public List<PContractProductSKU> getlistsku_bypcontract(long orgrootid_link, long pcontractid_link) {
		// TODO Auto-generated method stub
		return repo.getlistsku_bypcontract(orgrootid_link, pcontractid_link);
	}
	@Override
	public List<PContractProductSKU> getlistsku_bypo_and_pcontract(long orgrootid_link, long pcontract_poid_link,
			long pcontractid_link) {
		// TODO Auto-generated method stub
		return repo.getlistsku_bypo_and_pcontract(orgrootid_link, pcontract_poid_link, pcontractid_link);
	}
	@Override
	public List<PContractProductSKU> getlistsku_bypo_and_pcontract_free(long orgrootid_link, long pcontract_poid_link,
			long pcontractid_link) {
		// TODO Auto-generated method stub
		List<PContractProductSKU> a= repo.getlistsku_bypo_and_pcontract(orgrootid_link, pcontract_poid_link, pcontractid_link);
		
		List<POrder> lsPOrders = porder_Service.getByContractAndPO(pcontractid_link, pcontract_poid_link);
		for(POrder thePOrder:lsPOrders){
			for(POrder_Product_SKU thePorderSKU:thePOrder.getPorder_product_sku()){
				a.removeIf(sku -> sku.getSkuid_link().equals(thePorderSKU.getSkuid_link()));
			}
		}
		return a;
	}
	@Override
	public List<PContractProductSKU> getlistsku_bysku_and_pcontract(long skuid_link, long pcontractid_link) {
		// TODO Auto-generated method stub
		return repo.getlistsku_bysku_and_pcontract(skuid_link, pcontractid_link);
	}
	@Override
	public List<Long> getlistvalue_by_product(long pcontractid_link, long productid_link, long attributeid_link) {
		// TODO Auto-generated method stub
		List<Long> list = repo.getvaluesize_in_product(productid_link, pcontractid_link, attributeid_link);
		return list;
	}
	@Override
	public List<Long> getsku_bycolor(long pcontractid_link, long productid_link, long colorid_link) {
		// TODO Auto-generated method stub
		return repo.getskuid_bycolorid_link(productid_link, pcontractid_link, colorid_link);
	}
	@Override
	public List<PContractProductSKU> getbypo_and_product(long pcontract_poid_link, long productid_link) {
		// TODO Auto-generated method stub
		List<PContractProductSKU> a = repo.getlistsku_bypo_and_product(pcontract_poid_link, productid_link);
		return a;
	}
	
	//Chi lay cac SKU chua co trong Lenh SX
	@Override
	public List<PContractProductSKU> getbypo_and_product_free(long porderreqid_link, long pcontractid_link, long pcontract_poid_link, long productid_link) {
		// TODO Auto-generated method stub
		List<PContractProductSKU> a = repo.getlistsku_bypo_and_product(pcontract_poid_link, productid_link);
		
		List<POrder> lsPOrders = porder_Service.getByContractAndPO(pcontractid_link, pcontract_poid_link);
		for(POrder thePOrder:lsPOrders){
			for(POrder_Product_SKU thePorderSKU:thePOrder.getPorder_product_sku()){
				a.removeIf(sku -> sku.getSkuid_link().equals(thePorderSKU.getSkuid_link()));
			}
		}
		
		return a;
	}
	
	@Override
	public List<PContractProductSKU> getlistsku_bysku_and_product_PO(long skuid_link, long pcontract_poid_link,
			long productid_link) {
		// TODO Auto-generated method stub
		return repo.getlistsku_bysku_and_product_PO(skuid_link, productid_link, pcontract_poid_link);
	}
}
