package vn.gpay.gsmart.core.stockin_type;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class StockinTypeService extends AbstractService<StockinType> implements IStockinTypeService {
	@Autowired IStockinTypeRepository repo;
	@Override
	protected JpaRepository<StockinType, Long> getRepository() {
		return repo;
	}
	@Override
	public List<StockinType> findType(Long typeFrom, Long typeTo) {
		return repo.findType(typeFrom, typeTo);
	}

}
