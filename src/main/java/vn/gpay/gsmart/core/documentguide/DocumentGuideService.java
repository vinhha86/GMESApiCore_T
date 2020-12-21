package vn.gpay.gsmart.core.documentguide;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class DocumentGuideService extends AbstractService<DocumentGuide> implements IDocumentGuide_Service{
	@Autowired DocumentGuide_Repository repo;
	@Override
	protected JpaRepository<DocumentGuide, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
