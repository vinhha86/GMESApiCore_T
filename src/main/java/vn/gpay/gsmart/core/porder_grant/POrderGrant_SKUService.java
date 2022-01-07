package vn.gpay.gsmart.core.porder_grant;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.sku.SKU;

@Service
public class POrderGrant_SKUService extends AbstractService<POrderGrant_SKU> implements IPOrderGrant_SKUService {
	@Autowired IPOrderGrant_SKURepository repo;
	@Autowired IOrgService orgService;
	@Autowired
	ISKU_Service sku_repo;
	
	@Override
	protected JpaRepository<POrderGrant_SKU, Long> getRepository() {
		return repo;
	}
	
	@Override
	public List<POrderGrant_SKU>getPOrderGrant_SKU(Long pordergrantid_link){
		return repo.getPOrderGrant_SKU(pordergrantid_link);
	}

	@Override
	public POrderGrant_SKU getPOrderGrant_SKUbySKUid_linkAndGrantId(Long skuid_link, Long pordergrantid_link) {
		return repo.getPOrderGrant_SKUbySKUid_linkAndGrantId(skuid_link, pordergrantid_link);
	}

	@Override
	public List<POrderGrant_SKU> getByPContractPOAndSKU(Long pcontract_poid_link, Long skuid_link) {
		return repo.getByPContractPOAndSKU(pcontract_poid_link, skuid_link);
	}

	@Override
	public POrderGrant_SKU getPOrderGrant_SKUbySKUAndGrantAndPcontractPo(Long skuid_link, Long pordergrantid_link,
			Long pcontract_poid_link) {
		return repo.getPOrderGrant_SKUbySKUAndGrantAndPcontractPo(skuid_link, pordergrantid_link, pcontract_poid_link);
	}

	@Override
	public POrderGrant_SKU getPOrderGrant_SKUbySKUid_linkAndGrantId_andPO(Long skuid_link, Long pordergrantid_link,
			Long pcontract_poid_link) {
		return repo.getPOrderGrant_SKUbySKUid_linkAndGrantId_AndPO(skuid_link, pordergrantid_link, pcontract_poid_link);
	}

	@Override
	public List<String> getlistmau_by_grant(Long pordergrantid_link) {
		return repo.getlistmau(pordergrantid_link);
	}

	@Override
	public List<POrderGrant_SKU> getlistco_by_grant_andmau(Long pordergrantid_link, long colorid_link) {
		return repo.getlistco_by_mau(pordergrantid_link, colorid_link);
	}

	@Override
	public List<String> getlistco(Long porderid_link) {
//		List<Object> list = repo.getlistco(porderid_link);
//		List<String> list_ret = new ArrayList<String>();
//		for(Object obj : list) {
////			list_ret.add(o)
//		}
		return repo.getlistco(porderid_link);
	}

	@Override
	public String getProductionLines(Long pcontract_poid_link){
		String orgLines = "";
		List<Long> orglines = repo.getProductionLines(pcontract_poid_link);
		for(Long orgid:orglines){
			//Lay thong tin Org
			Org theOrg = orgService.findOne(orgid);
			if(theOrg!=null) {
				String name  = theOrg.getName() == null ? "" : theOrg.getName();
				orgLines += name + ";";
			}
			
		}
		return orgLines;
	}
//	@Override
//	public POrderGrant_SKU getPOrderGrant_SKUbySKUid_link(Long skuid_link) {
//		return repo.getPOrderGrant_SKUbySKUid_link(skuid_link);
//	}

	@Override
	public Integer porder_get_qty_grant(Long porderid_link, Long skuid_link,Long pcontract_poid_link) {
		Integer qty = repo.porder_get_qty_grant(porderid_link, skuid_link, pcontract_poid_link);
		return qty == null ? 0 : qty;
	}

	@Override
	public List<POrderGrant_SKU> getGrantSKUByGrantAndPO(Long pordergrantid_link, Long pcontract_poid_link) {
		return repo.getgrantsku_by_grant_and_po(pordergrantid_link, pcontract_poid_link);
	}

	@Override
	public List<POrderGrant_SKU> get_POrderGrant_SKU_byPorderGrant(Long pordergrantid_link) {
		List<POrderGrant_SKU> result = new ArrayList<POrderGrant_SKU>();
		
		List<Object[]> rs = repo.get_POrderGrant_SKU_byPorderGrant(pordergrantid_link);
		for (Object[] record : rs) {
			POrderGrant_SKU newPOrderGrant_SKU = new POrderGrant_SKU();
			
			Long rs_pordergrantid_link = (Long) record[0];
			Long rs_skuid_link = (Long) record[1];
			Long rs_grantamount = (Long) record[2];
			Integer rs_grantamount_int = rs_grantamount.intValue();
			
			newPOrderGrant_SKU.setPordergrantid_link(rs_pordergrantid_link);
			newPOrderGrant_SKU.setSkuid_link(rs_skuid_link);
			newPOrderGrant_SKU.setGrantamount(rs_grantamount_int);
			
			SKU theOriginSKU = sku_repo.findOne(rs_skuid_link);
			newPOrderGrant_SKU.setMa_SanPham(theOriginSKU.getProduct_code());
			newPOrderGrant_SKU.setTen_SanPham(theOriginSKU.getProduct_name());
			newPOrderGrant_SKU.setMa_SKU(theOriginSKU.getCode());
			newPOrderGrant_SKU.setMau_SanPham(theOriginSKU.getMauSanPham());
			newPOrderGrant_SKU.setCo_SanPham(theOriginSKU.getCoSanPham());
			
			result.add(newPOrderGrant_SKU);
		}
		
		return result;
	}
}
