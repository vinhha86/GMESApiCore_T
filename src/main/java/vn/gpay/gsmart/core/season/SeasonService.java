package vn.gpay.gsmart.core.season;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;


@Service
public class SeasonService extends AbstractService<Season> implements ISeasonService {
	@Autowired ISeasonRepository repo;
	
	@Override
	public List<Season> getall_byorgrootid(long orgrootid_link) {
		// TODO Auto-generated method stub
		return repo.getall_byorgrootid(orgrootid_link);
	}

	@Override
	protected JpaRepository<Season, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
