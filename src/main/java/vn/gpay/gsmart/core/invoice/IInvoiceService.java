package vn.gpay.gsmart.core.invoice;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import vn.gpay.gsmart.core.base.Operations;

public interface IInvoiceService extends Operations<Invoice>{

	public List<Invoice> findByInvoicenumber(String invoicenumber);
	
	
	public List<Invoice> InvoiceListComming(Long orgid_link,String stockcode,String orgfrom_code,String invoicenumber,Date shipdateto_from, Date shipdateto_to,int status);
	
	public List<InvoiceD> findInvoiceDBySkuCode(String invoicenumber,String skucode);
	
	public void updateStatusByInvoicenumber(String invoicenumber);
	
	public Page<Invoice> getlist_bypage(long orgrootid_link, String invoicenumber, String custom_declaration, Date invociedate_from,
			Date invoicedate_to, long org_prodviderid_link, int status, int page, int limit);
	
}
