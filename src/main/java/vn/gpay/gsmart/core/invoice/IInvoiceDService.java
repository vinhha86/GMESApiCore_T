package vn.gpay.gsmart.core.invoice;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IInvoiceDService extends Operations<InvoiceD>{
	public List<InvoiceD> get_invoiced_bysku(long invoiceid_link, long skuid_link);
}
