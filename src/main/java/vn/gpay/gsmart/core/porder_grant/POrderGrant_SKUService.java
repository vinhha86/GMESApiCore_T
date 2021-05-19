package vn.gpay.gsmart.core.porder_grant;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;

@Service
public class POrderGrant_SKUService extends AbstractService<POrderGrant_SKU> implements IPOrderGrant_SKUService {
	@Autowired IPOrderGrant_SKURepository repo;
	@Autowired IOrgService orgService;
	@Override
	protected JpaRepository<POrderGrant_SKU, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	
	@Override
	public List<POrderGrant_SKU>getPOrderGrant_SKU(Long pordergrantid_link){
		return repo.getPOrderGrant_SKU(pordergrantid_link);
	}

	@Override
	public POrderGrant_SKU getPOrderGrant_SKUbySKUid_linkAndGrantId(Long skuid_link, Long pordergrantid_link) {
		// TODO Auto-generated method stub
		return repo.getPOrderGrant_SKUbySKUid_linkAndGrantId(skuid_link, pordergrantid_link);
	}

	@Override
	public List<POrderGrant_SKU> getByPContractPOAndSKU(Long pcontract_poid_link, Long skuid_link) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return repo.getPOrderGrant_SKUbySKUid_linkAndGrantId_AndPO(skuid_link, pordergrantid_link, pcontract_poid_link);
	}

	@Override
	public List<String> getlistmau_by_grant(Long pordergrantid_link) {
		// TODO Auto-generated method stub
		return repo.getlistmau(pordergrantid_link);
	}

	@Override
	public List<POrderGrant_SKU> getlistco_by_grant_andmau(Long pordergrantid_link, long colorid_link) {
		// TODO Auto-generated method stub
		return repo.getlistco_by_mau(pordergrantid_link, colorid_link);
	}

	@Override
	public List<String> getlistco(Long porderid_link) {
		// TODO Auto-generated method stub
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
			orgLines += theOrg.getName() + ";";
		}
		return orgLines;
	}
//	@Override
//	public POrderGrant_SKU getPOrderGrant_SKUbySKUid_link(Long skuid_link) {
//		return repo.getPOrderGrant_SKUbySKUid_link(skuid_link);
//	}

	@Override
	public Integer porder_get_qty_grant(Long porderid_link, Long skuid_link,Long pcontract_poid_link) {
		// TODO Auto-generated method stub
		Integer qty = repo.porder_get_qty_grant(porderid_link, skuid_link, pcontract_poid_link);
		return qty == null ? 0 : qty;
	}

	@Override
	public List<POrderGrant_SKU> getGrantSKUByGrantAndPO(Long pordergrantid_link, Long pcontract_poid_link) {
		// TODO Auto-generated method stub
		return repo.getgrantsku_by_grant_and_po(pordergrantid_link, pcontract_poid_link);
	}
}
