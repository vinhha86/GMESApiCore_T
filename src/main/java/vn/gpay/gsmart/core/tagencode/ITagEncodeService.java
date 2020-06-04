package vn.gpay.gsmart.core.tagencode;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;



public interface ITagEncodeService extends Operations<TagEncode>{
	
	public List<TagEncode> encode_getbydevice(Long orgid,Long deviceid);
	
	
	public void deleteByEpc(String Epc,long orgid_link);
}
