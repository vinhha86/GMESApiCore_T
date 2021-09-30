package vn.gpay.gsmart.core.sizeset;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class SizeSetService extends AbstractService<SizeSet> implements ISizeSetService {
	@Autowired
	ISizeSetRepository repo;

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

	@Override
	public Integer getMaxSortValue() {
		// TODO Auto-generated method stub
		return repo.getMaxSortValue() + 1;
	}

	@Override
	public Long getbyname(String name) {
		// TODO Auto-generated method stub
		List<SizeSet> list = repo.getbyname(name);
		return list.size() > 0 ? list.get(0).getId() : 0;
	}

	@Override
	public Long getbyname_and_po(String name, Long pcontractid_link, Long productid_link) {
		// TODO Auto-generated method stub
		List<SizeSet> list = repo.getbyname_and_po(name, pcontractid_link, productid_link);
		return list.size() == 0 ? 0 : list.get(0).getId();
	}

	@Override
	public List<SizeSet> getSizeSetByName(String name) {
		// TODO Auto-generated method stub
		List<SizeSet> list = repo.getbyname(name);
		return list;
	}

}
