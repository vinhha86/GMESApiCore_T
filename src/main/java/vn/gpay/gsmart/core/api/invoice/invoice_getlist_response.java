package vn.gpay.gsmart.core.api.invoice;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.invoice.Invoice;

public class invoice_getlist_response extends ResponseBase {
	public List<Invoice> data;
	public long totalCount;
}
