package vn.gpay.gsmart.core.tagencode;

import java.util.Date;

import org.springframework.data.domain.Page;

import vn.gpay.gsmart.core.base.Operations;

public interface IWareHouse_Encode_Service extends Operations<WareHouse_Encode> {
	public Page<WareHouse_Encode> getlist_bypage(long orgrootid_link, long orgencodeid_link, long usercreateid_link,
			Date timecreatefrom, Date timecreateto, int limit, int page);
}
