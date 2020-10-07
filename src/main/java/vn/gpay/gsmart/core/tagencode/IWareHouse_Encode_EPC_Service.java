package vn.gpay.gsmart.core.tagencode;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;



public interface IWareHouse_Encode_EPC_Service extends Operations<WareHouse_Encode_EPC>{
	
	public List<WareHouse_Encode_EPC> encode_getbydevice(Long orgrootid_link,Long deviceid);
	
	
	public void deleteByEpc(String Epc,long orgid_link);
	
	public  List<WareHouse_Encode_EPC> getTagEncode_byOldEPC(String OldEPC, long orgrootid_link, long warehouse_encodeid_link);


	public List<WareHouse_Encode_EPC> encode_getbyepc(Long orgrootid_link, String epc);


	public List<WareHouse_Encode_EPC> encode_getbyencodeid(Long warehouse_encodeid_link);
}
