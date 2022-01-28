package vn.gpay.gsmart.core.warehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.StringAbstractService;
import vn.gpay.gsmart.core.invcheck.InvcheckEpc;
import vn.gpay.gsmart.core.invcheck.InvcheckSku;
import vn.gpay.gsmart.core.stock.IStockspaceService;
import vn.gpay.gsmart.core.stock.Stockspace;

@Service
public class WarehouseRepositoryImpl extends StringAbstractService<Warehouse> implements IWarehouseService{

	@Autowired WarehouseRepository repositoty;
	@Autowired IStockspaceService stockSpaceService;

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
	public boolean epcExistedInStock(String epc, long stockid_link) {
		// TODO Auto-generated method stub
		List<Warehouse> lstEpc = repositoty.findMaterialByEPC(epc, stockid_link);
		if (null != lstEpc && lstEpc.size() > 0)
			return true;
		else
			return false;
	}
	
	@Override
	public List<Warehouse> findCheckedEPC(String token) {
		// TODO Auto-generated method stub
		return repositoty.findCheckedEPC(UUID.fromString(token));
	}

	@Override
	public List<InvcheckSku> invcheck_sku(long invcheckid_link,long orgrootid_link,Long bossid,Long orgfrom_code,Long productcode) {
		// TODO Auto-generated method stub
		List<InvcheckSku> listdata = new ArrayList<InvcheckSku>();
		try {
			List<Object[]> objectList = repositoty.invcheck_sku(orgfrom_code);
			for (Object[] row : objectList) {
				InvcheckSku entity = new InvcheckSku();
				//InvcheckSkuID id = new InvcheckSkuID();
				entity.setInvcheckid_link(invcheckid_link);
				entity.setOrgrootid_link(orgrootid_link);
				entity.setSkuid_link((Long) row[0]);
				entity.setYdsorigin((Float) row[1]);
		        entity.setUnitprice((Float)row[2]);
		        entity.setTotalamount((Float)row[3]);
		        entity.setUnitid_link((Integer)row[4]);
		       // entity.setInvchecksku_pk(id);;
				listdata.add(entity);
	        }
		}catch(Exception ex) {}
		return listdata;
	}

	@Override
	public List<InvcheckEpc> invcheck_epc(long invcheckid_link, long orgrootid_link, Long bossid, Long orgfrom_code,
			Long productcode) {
		// TODO Auto-generated method stub
		List<InvcheckEpc> listdata = new ArrayList<InvcheckEpc>();
		try {
			List<Object[]> objectList = repositoty.invcheck_epc(orgfrom_code);
			for (Object[] row : objectList) {
				//InvcheckEpcID id = new InvcheckEpcID();
				InvcheckEpc entity = new InvcheckEpc();
				entity.setInvcheckid_link(invcheckid_link);
				entity.setOrgrootid_link(orgrootid_link);
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
		return repositoty.inv_getby_stockinid_link(stockid_link);
	}

	@Override
	public String getspaces_bysku(Long stockid_link, Long skuid_link) {
		String sSpaces = "";
		try {
			List<Object[]> lsSpaces = repositoty.getspaces_bysku(skuid_link);
			int i=0;
			for(Object[] theSpace: lsSpaces){
				//Lay thong tin space
				String space_epc = (String)theSpace[0];
				Long item_count = (Long)theSpace[1];
				Double item_total = (Double)theSpace[2];
				List<Stockspace> lsSpaceEpc = stockSpaceService.findStockspaceByEpc(stockid_link, space_epc);
				if (lsSpaceEpc.size()>0){
					Stockspace theSpaceEpc = lsSpaceEpc.get(0);
					sSpaces += "D-" + theSpaceEpc.getStockrow_code() + "|"
							+ "H-" + theSpaceEpc.getSpacename() + "|"
							+ "T-" + theSpaceEpc.getFloorid().toString() + "|" 
							+ "(" + item_count.toString() + ")" 
							+ "(" + item_total.toString() + ")";
				} else {
					sSpaces += "KXD "
							+ "(" + item_count.toString() + ")" 
							+ "(" + item_total.toString() + ")";
				}
				i++;
				if (i<lsSpaces.size())sSpaces += "; ";
			}
			return sSpaces == ""?"Không có thông tin":sSpaces;
		} catch(Exception ex){
			ex.printStackTrace();
			return "Lỗi tính toán vị trí khoang";
		}
	}

	@Override
	public List<Warehouse> findMaterialByEPC(String epc) {
		return repositoty.findMaterialByEPC(epc);
	}
	@Override
	public Long getSumBy_Sku_Stock(Long skuid_link, Long stockid_link) {
		return repositoty.getSumBy_Sku_Stock(skuid_link, stockid_link);
	}
	@Override
	public Integer getSumBy_Sku(Long skuid_link) {
		return repositoty.getSumBy_Sku(skuid_link);
	}
}
