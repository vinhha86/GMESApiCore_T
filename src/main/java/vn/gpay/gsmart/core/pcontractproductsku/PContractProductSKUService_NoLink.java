package vn.gpay.gsmart.core.pcontractproductsku;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.sku.ISKU_Repository;

@Service
public class PContractProductSKUService_NoLink extends AbstractService<PContractProductSKU_NoLink>
		implements IPContractProductSKUService_NoLink {
	@Autowired	IPContractProductSKURepository_NoLink repo;
	@Autowired	ISKU_Repository sku_repo;

	@Override
	protected JpaRepository<PContractProductSKU_NoLink, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	
	@Override
	public List<PContractProductSKU_NoLink> getlistsku_bypcontract_nolink(long orgrootid_link, long pcontractid_link) {
		// TODO Auto-generated method stub
		return repo.getlistsku_bypcontract_nolink(orgrootid_link, pcontractid_link);
	}

	@Override
	public List<PContractProductSKU_NoLink> getsumsku_bypcontract(long pcontractid_link) {
		List<PContractProductSKU_NoLink> result = new ArrayList<PContractProductSKU_NoLink>();
		List<Object[]> rs = repo.getsumsku_bypcontract(pcontractid_link);
		for (Object[] record : rs) {
			PContractProductSKU_NoLink sku = new PContractProductSKU_NoLink();
			sku.setProductid_link((Long) record[0]);
			sku.setSkuid_link((Long) record[1]);
			if (null != record[2]) sku.setPquantity_porder(((Long) record[2]).intValue());
			if (null != record[3]) sku.setPquantity_sample(((Long) record[3]).intValue());
			if (null != record[4]) sku.setPquantity_granted(((Long) record[4]).intValue());
			if (null != record[5]) sku.setPquantity_total(((Long) record[5]).intValue());
			
//			SKU theOriginSKU = sku_repo.getOne(sku.getSkuid_link());
//			sku.setProduct_code(theOriginSKU.getProduct_code());
//			sku.setProduct_name(theOriginSKU.getProduct_name());
//			sku.setSku_code(theOriginSKU.getCode());
//			sku.setMausanpham(theOriginSKU.getMauSanPham());
//			sku.setCosanpham(theOriginSKU.getCoSanPham());
//			sku.setUnit_name(theOriginSKU.getUnit_name());
			
			result.add(sku);
			
		}
		return result;
	}
}
