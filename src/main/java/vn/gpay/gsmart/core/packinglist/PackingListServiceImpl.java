package vn.gpay.gsmart.core.packinglist;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PackingListServiceImpl extends AbstractService<PackingList> implements IPackingListService{

	@Autowired
	PackingListRepository repositoty;
	
	@Override
	protected JpaRepository<PackingList, Long> getRepository() {
		// TODO Auto-generated method stub
		return repositoty;
	}

	@Override
	public List<PackingList> findByStockinDID(long stockindid) {
		// TODO Auto-generated method stub
		return repositoty.findByStockinDID(stockindid);
	}

	@Override
	public List<PackingList> inv_getbyid(long invoiceid_link) {
		// TODO Auto-generated method stub
		return repositoty.inv_getbyid(invoiceid_link);
	}

	@Override
	public List<PackingList> getPKL_by_invoiced(long invoicedid_link) {
		// TODO Auto-generated method stub
		return repositoty.getpkl_byinvoiced(invoicedid_link);
	}

	@Override
	public List<LotNumber> getLotNumber_byinvoiced(long invoicedid_link) {
		// TODO Auto-generated method stub
		List<PackingList> listpkl = repositoty.getpkl_byinvoiced(invoicedid_link);
		List<LotNumber> listlot = new ArrayList<LotNumber>();
		for(PackingList pkl : listpkl) {
			LotNumber lot = new LotNumber();
			lot.setLotnumber(pkl.getLotnumber());
			lot.setSizenumber(pkl.getSizenumber());
			
			boolean checkExist = false;
			
			for(LotNumber _lot : listlot) {
				if(_lot.getLotnumber().equals(lot.getLotnumber()) && _lot.getSizenumber().equals(lot.getSizenumber())) {
					checkExist = true;
					break;
				}
			}
			
			if(!checkExist) {
				listlot.add(lot);
			}
		}
		
		return listlot;
	}

	@Override
	public List<PackingList> getbylotnumber(String lotnumber, long invoicedid_link) {
		// TODO Auto-generated method stub
		return repositoty.getpackageid_bylotnumber(invoicedid_link, lotnumber);
	}

}
