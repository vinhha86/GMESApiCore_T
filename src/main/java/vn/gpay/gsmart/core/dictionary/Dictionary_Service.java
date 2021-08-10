package vn.gpay.gsmart.core.dictionary;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;


@Service
public class Dictionary_Service extends AbstractService<Dictionary> implements IDictionary_Service{
	@Autowired IDictionary_Repository repo;
	@Override
	public List<Dictionary> loadDictionary() {
		// TODO Auto-generated method stub
		return repo.loadDictionary();
	}

	@Override
	protected JpaRepository<Dictionary, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
