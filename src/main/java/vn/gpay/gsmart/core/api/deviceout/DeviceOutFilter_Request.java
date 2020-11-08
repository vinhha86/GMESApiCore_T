package vn.gpay.gsmart.core.api.deviceout;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.RequestBase;

public class DeviceOutFilter_Request extends RequestBase{
	public String deviceout_code;
	public Date deviceout_date_from;
	public Date deviceout_date_to;
	public Integer deviceouttypeid_link;
	public String invoice_code;
	public Long orgid_from_link;
	public Long orgid_to_link;
	public Integer usercreateid_link;
	public List<Integer> status;
}
