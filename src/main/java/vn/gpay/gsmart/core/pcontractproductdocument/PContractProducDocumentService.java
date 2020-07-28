package vn.gpay.gsmart.core.pcontractproductdocument;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;


@Service
public class PContractProducDocumentService extends AbstractService<PContractProductDocument> implements IPContractProducDocumentService {
	@Autowired IPContractProductDocumentRepository repo;
	@Override
	protected JpaRepository<PContractProductDocument, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<PContractProductDocument> getlist_byproduct(long orgrootid_link, long pcontractid_link,
			long productid_link) {
		// TODO Auto-generated method stub
		return repo.getall_byproduct(orgrootid_link, pcontractid_link, productid_link);
	}
	@Override
	public List<PContractProductDocument> getlist_bycontract(long orgrootid_link, long pcontractid_link) {
		// TODO Auto-generated method stub
		return repo.getall_bycontract(orgrootid_link, pcontractid_link);
	}
}
