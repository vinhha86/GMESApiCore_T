package vn.gpay.gsmart.core.warehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.StringAbstractService;
import vn.gpay.gsmart.core.invcheck.InvcheckEpc;
import vn.gpay.gsmart.core.invcheck.InvcheckEpcID;
import vn.gpay.gsmart.core.invcheck.InvcheckSku;
import vn.gpay.gsmart.core.invcheck.InvcheckSkuID;
import vn.gpay.gsmart.core.stockin.Results;

@Service
public class WarehouseRepositoryImpl extends StringAbstractService<Warehouse> implements IWarehouseService{

	@Autowired
	WarehouseRepository repositoty;

	@Override
	protected JpaRepository<Warehouse, String> getRepository() {
		// TODO Auto-generated method stub
		return repositoty;
	}

	@Override
	public List<Warehouse> findBySpaceepc(String spaceepc) {
		// TODO Auto-generated method stub
		return repositoty.findBySpaceepc(spaceepc);
	}
	
	@Override
	public List<Warehouse> findByLotNumber(String lotnumber) {
		// TODO Auto-generated method stub
		return repositoty.findByLotNumber(lotnumber);
	}	

	@Override
	public List<Warehouse> findMaterialByEPC(String epc, long stockid_link) {
		// TODO Auto-generated method stub
		return repositoty.findMaterialByEPC(epc, stockid_link);
	}

	@Override
	public List<Warehouse> findCheckedEPC(String token) {
		// TODO Auto-generated method stub
		return repositoty.findCheckedEPC(UUID.fromString(token));
	}

	@Override
	public List<InvcheckSku> invcheck_sku(long invcheckid_link,long orgid_link,Long bossid,Long orgfrom_code,Long productcode) {
		// TODO Auto-generated method stub
		List<InvcheckSku> listdata = new ArrayList<InvcheckSku>();
		try {
			List<Object[]> objectList = repositoty.invcheck_sku(orgfrom_code);
			for (Object[] row : objectList) {
				InvcheckSku entity = new InvcheckSku();
				//InvcheckSkuID id = new InvcheckSkuID();
				entity.setInvcheckid_link(invcheckid_link);
				entity.setOrgid_link(orgid_link);
				entity.setSkuid_link((Long) row[0]);
				entity.setYdsorigin((Float) row[1]);
		        entity.setUnitprice((Float)row[2]);
		        entity.setTotalamount((Float)row[3]);
		        entity.setUnitid_link((Long)row[4]);
		       // entity.setInvchecksku_pk(id);;
				listdata.add(entity);
	        }
		}catch(Exception ex) {}
		return listdata;
	}

	@Override
	public List<InvcheckEpc> invcheck_epc(long invcheckid_link, long orgid_link, Long bossid, Long orgfrom_code,
			Long productcode) {
		// TODO Auto-generated method stub
		List<InvcheckEpc> listdata = new ArrayList<InvcheckEpc>();
		try {
			List<Object[]> objectList = repositoty.invcheck_epc(orgfrom_code);
			for (Object[] row : objectList) {
				//InvcheckEpcID id = new InvcheckEpcID();
				InvcheckEpc entity = new InvcheckEpc();
				entity.setInvcheckid_link(invcheckid_link);
				entity.setOrgid_link(orgid_link);
				entity.setEpc((String) row[0]);
		        //entity.setInvcheckepc_pk(id);
		        entity.setSkuid_link((Long)row[1]);
		        entity.setYdsorigin((Float)row[2]);
		        entity.setUnitprice((Float) row[3]);
		        entity.setRssi(0);
				listdata.add(entity);
	        }
		}catch(Exception ex) {}
		return listdata;
	}

	@Override
	public void deleteByEpc(String epc,long orgid_link) {
		repositoty.deleteByEpc(epc, orgid_link);
	}

	@Override
	public List<Warehouse> inv_getbyid(long stockid_link) {
		// TODO Auto-generated method stub
		return repositoty.inv_getbyid(stockid_link);
	}

}
