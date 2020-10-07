package vn.gpay.gsmart.core.vat_type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class VatTypeService extends AbstractService<VatType> implements IVatTypeService {
	@Autowired IVatType_Repository repo;
	
	@Override
	protected JpaRepository<VatType, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
