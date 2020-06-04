package vn.gpay.gsmart.core.invoice;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface InvoiceRepository extends JpaRepository<Invoice, Long>,JpaSpecificationExecutor<Invoice>{

	@Query(value = "select c from Invoice c where invoicenumber =:invoicenumber")
	public List<Invoice> findByInvoicenumber(@Param ("invoicenumber")final String invoicenumber);
	
	
	@Query(value = "select a from InvoiceD a "
			+ "inner join Invoice b on a.invoiceid_link = b.id "
			+ "inner join SKU c on a.skuid_link = c.id where b.invoicenumber =:invoicenumber and c.code =:skucode")
	public List<InvoiceD> findInvoiceDBySkuCode( @Param ("invoicenumber")final String invoicenumber,@Param ("skucode")final String skucode);
	
	
	@Modifying
	@Query(value = "update Invoice set status = 1 where invoicenumber=:invoicenumber")
	public void updateStatusByInvoicenumber(@Param ("invoicenumber")final String invoicenumber);

}
