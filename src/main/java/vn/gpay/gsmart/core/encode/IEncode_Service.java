package vn.gpay.gsmart.core.encode;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import vn.gpay.gsmart.core.base.Operations;

public interface IEncode_Service extends Operations<Encode> {
	public List<Encode> get_encode_by_porder_and_sku(long orgrootid_link, long porderid_link, long skuid_link);
	public List<Encode> get_encode_using_device(long orgrootid_link, long deviceid_link);
	public Page<Encode> getlist_bypage(long orgrootid_link, String pordercode, long usercreateid_link, Date encodedatefrom,
	Date encodedateto, int limit,int page);
}
