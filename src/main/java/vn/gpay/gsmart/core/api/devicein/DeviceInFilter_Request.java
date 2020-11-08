package vn.gpay.gsmart.core.api.devicein;

import java.util.Date;

import vn.gpay.gsmart.core.base.RequestBase;

public class DeviceInFilter_Request extends RequestBase{
	public String devicein_code;
	public Date devicein_date_from;
	public Date devicein_date_to;
	public Integer deviceintypeid_link;
	public String invoice_code;
	public Long deviceoutid_link;
	public Long orgid_from_link;
	public Long orgid_to_link;
	public Integer usercreateid_link;
}
