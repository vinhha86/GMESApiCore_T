package vn.gpay.gsmart.core.dictionary_type;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;


@Service
public class DictionaryType_Service extends AbstractService<DictionaryType> implements IDictionaryType_Service{
	@Autowired IDictionaryType_Repository repo;

	@Override
	protected JpaRepository<DictionaryType, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<DictionaryType> loadDictionaryType() {
		// TODO Auto-generated method stub
		return repo.loadDictionaryType();
	}
	
}
