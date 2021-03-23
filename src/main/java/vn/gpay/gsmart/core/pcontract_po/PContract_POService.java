package vn.gpay.gsmart.core.pcontract_po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.pcontract_price.IPContract_Price_Repository;

@Service
public class PContract_POService extends AbstractService<PContract_PO> implements IPContract_POService {
	@Autowired
	IPContract_PORepository repo;
	@Autowired
	IPContract_Price_Repository price_repo;
	@Autowired
	EntityManager em;

	@Override
	protected JpaRepository<PContract_PO, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<PContract_PO> getPOByContractProduct(Long orgrootid_link, Long pcontractid_link, Long productid_link,
			Long userid_link, Long orgid_link, Integer potype) {
		if (orgid_link == 1)
			userid_link = null;
		if (potype == 0)
			return repo.getPO_Chaogia(orgrootid_link, pcontractid_link, productid_link, userid_link);
		else
			return repo.getPO_Duyet(orgrootid_link, pcontractid_link, productid_link, userid_link);
	}

	@Override
	public List<PContract_PO> getPOByContractAndProduct(Long pcontractid_link, Long productid_link) {
		return repo.getPOByContractAndProduct(pcontractid_link, productid_link);
	}

//	@Override
//	//Chi lay cac PO o muc la
//	public List<PContract_PO> getPO_LeafOnly(Long orgrootid_link,
//			Long pcontractid_link,Long productid_link, Long userid_link, Long orgid_link){
//		try{
//			if(orgid_link == 1) userid_link = null;
//			List<PContract_PO> a = repo.getPOByContractProduct(orgrootid_link, pcontractid_link, productid_link, userid_link);
//			return a;
//		} catch(Exception ex){
//			ex.printStackTrace();
//			return null;
//		}
//	}

	@Override
	public List<PContract_PO> getPOLeafOnlyByContract(Long pcontractid_link, Long productid_link) {
		try {
			productid_link = productid_link == 0 ? null : productid_link;
			return repo.getPOLeafOnlyByContract(pcontractid_link, productid_link);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	@Override
	public List<PContract_PO> getPOByContract(Long orgrootid_link, Long pcontractid_link) {
		return repo.getPOByContract(orgrootid_link, pcontractid_link);
	}

	@Override
	public List<PContract_PO> getPO_LaterShipdate(Long orgrootid_link, Long pcontractid_link, Long productid_link,
			Date shipdate) {
		return repo.getPO_LaterShipdate(orgrootid_link, pcontractid_link, productid_link, shipdate);
	}

	@Override
	public List<PContract_PO> getPO_Offer_Accept_ByPContract(Long pcontractid_link, Long productid_link) {
		// TODO Auto-generated method stub
		try {
			return repo.getPO_Offer_Accept_ByPContract(pcontractid_link, productid_link == 0 ? null : productid_link);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public List<PContract_PO> getPcontractPoByPContractAndPOBuyer(Long pcontractid_link, String po_buyer,
			String buyercode) {
		return repo.getPcontractPoByPContractAndPOBuyer(pcontractid_link, po_buyer, buyercode);
	}

	@Override
	public List<PContract_PO> getone_by_template_set(String PO_No, Date ShipDate, long productid_link,
			long shipmodeid_link, long pcontractid_link) {
		Long shipmode = shipmodeid_link == 0 ? null : shipmodeid_link;
		return repo.getone_by_template_set(PO_No, shipmode, productid_link, ShipDate, pcontractid_link);
	}

	@Override
	public List<PContract_PO> check_exist_po(Date ShipDate, long productid_link, long shipmodeid_link,
			long pcontractid_link, String po_buyer) {
		po_buyer = (po_buyer == "" || po_buyer.toUpperCase() == "TBD") ? null : po_buyer;
		List<PContract_PO> list_po = repo.getone_by_template(shipmodeid_link, productid_link, ShipDate,
				pcontractid_link, po_buyer);

		return list_po;
	}

	@Override
	public List<PContract_PO> get_by_parentid(Long pcontractpo_parentid_link) {
		// TODO Auto-generated method stub
		return repo.getby_parentid_link(pcontractpo_parentid_link);
	}

	@Override
	public List<PContract_PO> check_exist_po_children(String PO_No, Date Shipdate, Long shipmodeid_link,
			Long pcontractid_link, Long parentid_link) {
		// TODO Auto-generated method stub
		return repo.getone_po_upload(PO_No, shipmodeid_link, Shipdate, pcontractid_link, parentid_link);
	}

	@Override
	public List<PContract_PO> check_exist_PONo(String PO_No, Long pcontractid_link) {
		return repo.getone_po_byPO_no(PO_No, pcontractid_link);
	}

	@Override
	public List<PContract_PO> getBySearch(String po_code, List<Long> orgs) {
		List<PContract_PO> lst = new ArrayList<PContract_PO>();
//		if (products.size() > 0)
//			if (orgs.size() > 0)
//				lst = repo.getBySearch(po_code,orgs);
//			else
//				lst = repo.getBySearch_ProductOnly(po_code);
//		else 
		po_code = po_code == null ? "" : po_code;
		if (orgs.size() > 0)
			lst = repo.getBySearch_OrgOnly(po_code, orgs);
		else
			lst = repo.getBySearch_CodeOnly(po_code);

		return lst;
	}

	@Override
	public List<PContract_POBinding> getForMarketTypeChart() {

		List<PContract_POBinding> data = new ArrayList<PContract_POBinding>();
		List<Object[]> objects = repo.getForMarketTypeChart();

		for (Object[] row : objects) {
			Long sum = (Long) row[0];
			String name = (String) row[1] == null ? "Khác" : (String) row[1];
			PContract_POBinding temp = new PContract_POBinding();
			temp.setSum(sum);
			temp.setMarketName(name);
			data.add(temp);
		}

		return data;
	}

	@Override
	public List<PContract_PO> getPO_Offer_Accept_ByPContract_AndOrg(Long pcontractid_link, Long productid_link,
			List<Long> list_orgid_link) {
		// TODO Auto-generated method stub
		productid_link = productid_link == 0 ? null : productid_link;
		if (list_orgid_link.size() == 0)
			return repo.getPO_Offer_Accept_ByPContract(pcontractid_link, productid_link);
		else
			return repo.getPO_Offer_Accept_ByPContract_AndOrg(pcontractid_link, productid_link, list_orgid_link);
	}

	@Override
	public List<PContract_PO> getby_porder(Long porderid_link) {
		// TODO Auto-generated method stub
		return repo.getby_porder(porderid_link);
	}

	@Override
	public List<PContract_PO> check_exist_line(Date ShipDate, long productid_link, long pcontractid_link,
			long parentid_link) {
		// TODO Auto-generated method stub
		return repo.getone_line_giaohang(productid_link, ShipDate, pcontractid_link, parentid_link);
	}

	@Override
	public List<PContract_PO> get_by_parent_and_type(Long pcontractpo_parentid_link, int po_typeid_link) {
		// TODO Auto-generated method stub
		return repo.getby_parentid_link_and_type(pcontractpo_parentid_link, po_typeid_link);
	}

	@Override
	public List<PContract_PO> getall_offers_by_org(List<Long> orgid_link) {
		// TODO Auto-generated method stub
		return repo.getOffers_byOrg(orgid_link);
	}

	@Override
	public List<PContract_PO> getby_parent_and_type(Long parentid_link, Integer po_typeid_link) {
		// TODO Auto-generated method stub
		return repo.getby_parent_and_type(parentid_link, po_typeid_link);
	}

	@Override
	public List<Long> getpcontract_BySearch(String po_code, List<Long> orgs) {
		// TODO Auto-generated method stub
		po_code = po_code == null ? "" : po_code;
		orgs = orgs.size() == 0 ? null : orgs;

		return repo.getPContractBySearch_OrgOnly(po_code, orgs);
	}

	@Override
	public List<PContract_PO> getBySearch_andType(String po_code, List<Long> orgs, int po_type) {
		// TODO Auto-generated method stub
		orgs = orgs.size() == 0 ? null : orgs;
		return repo.getBySearch_OrgAndType(po_code, orgs, po_type);
	}
}
