package vn.gpay.gsmart.core.api.invoice;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.invoice.InvoiceList;

public class InvoiceListCommingResponse extends ResponseBase {
	public List<InvoiceList> data;
}
