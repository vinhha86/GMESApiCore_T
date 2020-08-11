package vn.gpay.gsmart.core.invoice;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Sorts;
import com.github.wenhao.jpa.Specifications;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.utils.GPAYDateFormat;

@Service
public class InvoiceServiceImpl extends AbstractService<Invoice> implements IInvoiceService{

	@Autowired
	InvoiceRepository repositoty;
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	protected JpaRepository<Invoice, Long> getRepository() {
		// TODO Auto-generated method stub
		return repositoty;
	}

	@Override
	public List<Invoice> findByInvoicenumber(String invoicenumber) {
		// TODO Auto-generated method stub
		return repositoty.findByInvoicenumber(invoicenumber);
	}

	@Override
	public List<InvoiceD> findInvoiceDBySkuCode(String invoicenumber, String skucode) {
		// TODO Auto-generated method stub
		return repositoty.findInvoiceDBySkuCode(invoicenumber, skucode);
	}
	
	@Override
	public List<Invoice> InvoiceListComming(Long orgid_link,String stockcode,String orgfrom_code,String invoicenumber,Date shipdateto_from, Date shipdateto_to,int status){
		
		Specification<Invoice> specification = Specifications.<Invoice>and()
	            .eq( "orgid_link", orgid_link)
	            .like(Objects.nonNull(invoicenumber), "invoicenumber", "%"+invoicenumber+"%")
	            .eq(Objects.nonNull(stockcode), "orgid_to_link", stockcode)
	            .ge((shipdateto_from!=null && shipdateto_to==null),"invoicedate",GPAYDateFormat.atStartOfDay(shipdateto_from))
                .le((shipdateto_from==null && shipdateto_to!=null),"invoicedate",GPAYDateFormat.atEndOfDay(shipdateto_to))
                .between((shipdateto_from!=null && shipdateto_to!=null),"invoicedate", GPAYDateFormat.atStartOfDay(shipdateto_from), GPAYDateFormat.atEndOfDay(shipdateto_to))
                .eq(status!=-1, "status", status)
	            .build();

	    return repositoty.findAll(specification);
	}
	/*
	public List<InvoiceList> InvoiceListComming(String stockcode,String orgfrom_code,String invoicenumber,Date shipdateto_from, Date shipdateto_to){
		
		List<InvoiceList> listdata =new ArrayList<>();
		try {
		Query query = entityManager.createNativeQuery("SELECT c.invoicenumber,c.invoicedate, '' as orgfrom_code,'' as orgfrom_name"
				+ " from gsmart_inv.invoice c  ");
        //query.setParameter("stockincode", "%"+stockcode+"%"); 
        List<Object[]> objectList = query.getResultList();
        for (Object[] row : objectList) {
        	listdata.add( new InvoiceList(row));
        }
		}catch(Exception ex) {
			ex.getStackTrace();
		}
        return listdata;
	}*/

	@Override
	public void updateStatusByInvoicenumber(String invoicenumber) {
		// TODO Auto-generated method stub
		repositoty.updateStatusByInvoicenumber(invoicenumber);
	}

	@Override
	public Page<Invoice> getlist_bypage(long orgrootid_link, String invoicenumber, String custom_declaration,
			Date invociedate_from, Date invoicedate_to, long org_prodviderid_link, int status, int page, int limit) {
		// TODO Auto-generated method stub
		Specification<Invoice> specification = Specifications.<Invoice>and()
	            .eq( "orgrootid_link", orgrootid_link)
	            .ne("status", -1)
	            .like(invoicenumber != "" && invoicenumber != null, "invoicenumber", "%"+invoicenumber+"%")
	            .eq(custom_declaration != "" && custom_declaration != null, "custom_declaration", "%"+invoicenumber+"%")
	            .eq(org_prodviderid_link != 0, "org_prodviderid_link" , org_prodviderid_link)
	            .ge((invociedate_from!=null && invoicedate_to==null),"invoicedate",GPAYDateFormat.atStartOfDay(invociedate_from))
                .le((invociedate_from==null && invoicedate_to!=null),"invoicedate",GPAYDateFormat.atEndOfDay(invoicedate_to))
                .between((invociedate_from!=null && invoicedate_to!=null),"invoicedate",
                		GPAYDateFormat.atStartOfDay(invociedate_from), GPAYDateFormat.atEndOfDay(invoicedate_to))
                .eq(status!=0, "status", status)
	            .build();
		
		Sort sort = Sorts.builder()
		        .asc("invoicenumber")
		        .build();
		
		Page<Invoice> lst = repositoty.findAll(specification, PageRequest.of(page - 1, limit, sort));
		
		return lst;
	}

	

}
