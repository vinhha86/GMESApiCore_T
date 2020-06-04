package vn.gpay.gsmart.core.api.invoice;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.invoice.Invoice;
import vn.gpay.gsmart.core.packinglist.PackingList;

public class InvoiceByCodeResponse extends ResponseBase{

	public List<Invoice> data;
	public List<PackingList> epcs;
}
