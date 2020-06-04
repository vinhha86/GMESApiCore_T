package vn.gpay.gsmart.core.invoice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class InvoiceDRepositoryImpl extends AbstractService<InvoiceD> implements IInvoiceDService{

	@Autowired
	InvoiceDRepository repositoty;

	@Override
	protected JpaRepository<InvoiceD, Long> getRepository() {
		// TODO Auto-generated method stub
		return repositoty;
	}

	@Override
	public List<InvoiceD> get_invoiced_bysku(long invoiceid_link, long skuid_link) {
		// TODO Auto-generated method stub
		return repositoty.getbysku(invoiceid_link, skuid_link);
	}

}
