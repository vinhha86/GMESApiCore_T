package vn.gpay.gsmart.core.pcontract_po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;


import vn.gpay.gsmart.core.base.AbstractService;

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
	public List<PContract_PO> getPOByContract(Long orgrootid_link,
			Long pcontractid_link){
		return repo.getPOByContract(orgrootid_link, pcontractid_link);
	}
	
	@Override
	public List<PContract_PO> getPO_LaterShipdate(Long orgrootid_link,
			Long pcontractid_link, Long productid_link, Date shipdate){
		return repo.getPO_LaterShipdate(orgrootid_link, pcontractid_link, productid_link, shipdate);
	}
}
