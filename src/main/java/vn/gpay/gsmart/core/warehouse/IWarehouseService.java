package vn.gpay.gsmart.core.warehouse;

import java.util.List;

import vn.gpay.gsmart.core.base.StringOperations;
import vn.gpay.gsmart.core.invcheck.InvcheckEpc;
import vn.gpay.gsmart.core.invcheck.InvcheckSku;

public interface IWarehouseService extends StringOperations<Warehouse>{

	public List<Warehouse> findBySpaceepc(String spaceepc);
	public List<Warehouse> findByLotNumber(String lotnumber);
	public List<Warehouse> findMaterialByEPC(String epc, long stockid_link);
	public boolean epcExistedInStock(String epc, long stockid_link);
	public List<Warehouse> findCheckedEPC(String token);
	
	public void deleteByEpc(String Epc,long stockid_link);
	
	public List<InvcheckSku> invcheck_sku(long invcheckid_link,long orgid_link,Long bossid,Long orgfrom_code,Long productcode);
	
	public List<InvcheckEpc> invcheck_epc(long invcheckid_link,long orgid_link,Long bossid,Long orgfrom_code,Long productcode);
	
	
	public List<Warehouse> inv_getbyid(long stockid_link);
	String getspaces_bysku(Long stockid_link, Long skuid_link);
}
