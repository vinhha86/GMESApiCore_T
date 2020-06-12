package vn.gpay.gsmart.core.sizeset;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;


import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class SizeSetService extends AbstractService<SizeSet> implements ISizeSetService {
	@Autowired ISizeSetRepository repo;
	@Override
	protected JpaRepository<SizeSet, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	
	@Override
	public List<SizeSet> getall_byorgrootid(long orgrootid_link) {
		// TODO Auto-generated method stub
		return repo.getall_byorgrootid(orgrootid_link);
	}
}
