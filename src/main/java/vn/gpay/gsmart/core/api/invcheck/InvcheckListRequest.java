package vn.gpay.gsmart.core.api.invcheck;

import java.util.Date;

import vn.gpay.gsmart.core.base.RequestBase;

public class InvcheckListRequest extends RequestBase{

	public String stockcode;
	public String orgfrom_code;
	public Date invdateto_from;
	public Date invdateto_to;
	public Integer status;
}
