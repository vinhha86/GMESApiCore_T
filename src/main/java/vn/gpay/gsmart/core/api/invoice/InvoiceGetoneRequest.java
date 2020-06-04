package vn.gpay.gsmart.core.api.invoice;

import vn.gpay.gsmart.core.base.RequestBase;

public class InvoiceGetoneRequest extends RequestBase{

	public String msgtype;
	public String token;
	public String stockcode;
	public String invoicenumber;
}
