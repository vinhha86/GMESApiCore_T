package vn.gpay.gsmart.core.api.invoice;

import java.util.Date;

import vn.gpay.gsmart.core.base.RequestBase;

public class InvoiceListCommingRequest extends RequestBase{

	public String stockcode;
	public String orgfrom_code;
	public String invoicenumber;
	public Date shipdateto_from;
	public Date shipdateto_to;
	public int status;
}
