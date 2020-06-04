package vn.gpay.gsmart.core.api.invoice;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.invoice.Invoice;

public class InvoiceResponse extends ResponseBase{
	public List<Invoice> data;
}
