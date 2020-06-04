package vn.gpay.gsmart.core.api.invoice;

import java.util.Date;

public class invoice_getlist_request {
	public String invoicenumber;
	public String custom_declaration;
	public Date invociedate_from;
	public Date invoicedate_to;
	public long org_prodviderid_link;
	public int status;
	public int page;
	public int limit;
}
