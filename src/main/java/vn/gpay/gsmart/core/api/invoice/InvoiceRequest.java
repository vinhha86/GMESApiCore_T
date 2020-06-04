package vn.gpay.gsmart.core.api.invoice;

import java.util.List;

import vn.gpay.gsmart.core.invoice.Invoice;
import vn.gpay.gsmart.core.invoice.InvoiceD;

public class InvoiceRequest {

	public Invoice invoice;
	public List<InvoiceD> detail;
}
