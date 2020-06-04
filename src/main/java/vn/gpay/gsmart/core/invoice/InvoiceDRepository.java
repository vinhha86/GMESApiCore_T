package vn.gpay.gsmart.core.invoice;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface InvoiceDRepository extends JpaRepository<InvoiceD, Long>{
	@Query(value = "select c from InvoiceD c where skuid_link =:skuid_link and invoiceid_link = :invoiceid_link")
	public List<InvoiceD> getbysku(
			@Param ("invoiceid_link")final Long invoiceid_link,
			@Param ("skuid_link")final Long skuid_link);
}
