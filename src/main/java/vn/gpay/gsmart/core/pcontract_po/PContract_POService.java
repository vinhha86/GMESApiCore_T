package vn.gpay.gsmart.core.pcontract_po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.pcontract_price.PContract_Price;

@Service
public class PContract_POService extends AbstractService<PContract_PO> implements IPContract_POService {
	@Autowired IPContract_PORepository repo;
	@Override
	protected JpaRepository<PContract_PO, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<PContract_PO> getPOByContractProduct(Long orgrootid_link,
			Long pcontractid_link,Long productid_link, Long userid_link, Long orgid_link){
		if(orgid_link == 1) userid_link = null;
		return repo.getPOByContractProduct(orgrootid_link, pcontractid_link, productid_link, userid_link);
	}

	@Override
	public List<PContract_PO> getPOByContractAndProduct(Long pcontractid_link,Long productid_link){
		return repo.getPOByContractAndProduct(pcontractid_link, productid_link);
	}
	
	@Override
	//Chi lay cac PO o muc la
	public List<PContract_PO> getPO_LeafOnly(Long orgrootid_link,
			Long pcontractid_link,Long productid_link, Long userid_link, Long orgid_link){
		try{
			if(orgid_link == 1) userid_link = null;
			List<PContract_PO> a = repo.getPOByContractProduct(orgrootid_link, pcontractid_link, productid_link, userid_link);
//			List<PContract_PO> parentPO = new ArrayList<PContract_PO>();
			List<PContract_PO> returnPO = new ArrayList<PContract_PO>();
			for(PContract_PO thePO: a){
				if (thePO.getSub_po().size() > 0){
					for(PContract_PO subPO: thePO.getSub_po()){
						returnPO.add(subPO);
					}
//					parentPO.add(thePO);
				} else
					returnPO.add(thePO);
			}
//			a.removeAll(parentPO);
			return returnPO;
		} catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<PContract_PO> getPOLeafOnlyByContract(Long pcontractid_link, Long productid_link){
		try{
			productid_link = productid_link == 0 ? null : productid_link;
			return repo.getPOLeafOnlyByContract(pcontractid_link, productid_link);
		} catch(Exception ex){
			ex.printStackTrace();
			return null;
		}

	}
	@Override
	public List<PContract_PO> getPOByContract(Long orgrootid_link,
			Long pcontractid_link){
		return repo.getPOByContract(orgrootid_link, pcontractid_link);
	}
	
	@Override
	public List<PContract_PO> getPO_LaterShipdate(Long orgrootid_link,
			Long pcontractid_link, Long productid_link, Date shipdate){
		return repo.getPO_LaterShipdate(orgrootid_link, pcontractid_link, productid_link, shipdate);
	}
	@Override	
	public List<PContract_PO> getPO_Offer_Accept_ByPContract(Long pcontractid_link, Long productid_link) {
		// TODO Auto-generated method stub
		try {
			System.out.println(productid_link);
			return repo.getPO_Offer_Accept_ByPContract(pcontractid_link, productid_link == 0 ? null : productid_link);
		} catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	@Override	
	public List<PContract_PO> getPcontractPoByPContractAndPOBuyer(Long pcontractid_link, String po_buyer, String buyercode) {
		return repo.getPcontractPoByPContractAndPOBuyer(pcontractid_link, po_buyer, buyercode);
	}
	@Override
	public List<PContract_PO> getone_by_template_set(String PO_No, Date ShipDate, long productid_link,
			long shipmodeid_link, long pcontractid_link) {
		Long shipmode = shipmodeid_link == 0 ? null : shipmodeid_link;
		return repo.getone_by_template_set(PO_No, shipmode, productid_link, ShipDate, pcontractid_link);
	}
	@Override
	public List<PContract_PO> check_exist_po(String PO_No, Date ShipDate, long productid_link, long shipmodeid_link,
			long pcontractid_link, float vendor_targer) {
		
		List<PContract_PO> list_po = repo.getone_by_template(PO_No, shipmodeid_link, productid_link, ShipDate, pcontractid_link);
		List<PContract_PO> list_remove = new ArrayList<PContract_PO>();
		
		for (PContract_PO pContract_PO : list_po) {
			List<PContract_Price> list_price = pContract_PO.getPcontract_price();
			boolean check = false;
			for (PContract_Price price : list_price) {
				if(price.getSizesetid_link() != 1) continue;
				if(price.getPrice_vendortarget().equals(vendor_targer)) {
					check = true;
					break;
				}
			}
			
			if(!check) {
				list_remove.add(pContract_PO);
			}
		}
		
		for (PContract_PO pContract_PO : list_remove) {
			list_po.remove(pContract_PO);
		}
		
		return list_po;
	}
	@Override
	public List<PContract_PO> get_by_parentid(Long pcontractpo_parentid_link) {
		// TODO Auto-generated method stub
		return repo.getby_parentid_link(pcontractpo_parentid_link);
	}
}
