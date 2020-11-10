package vn.gpay.gsmart.core.personel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Sorts;
import com.github.wenhao.jpa.Specifications;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class Personnel_Service extends AbstractService<Personel> implements IPersonnel_Service {
	@Autowired Personnel_repository repo;
	@Override
	protected JpaRepository<Personel, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<Personel> getby_orgmanager(Long orgmanagerid_link, long orgrootid_link) {
		// TODO Auto-generated method stub
		Specification<Personel> specification = Specifications.<Personel>and()
				.eq("orgmanagerid_link", orgmanagerid_link)
				.eq("orgrootid_link", orgrootid_link)
				.build();

		Sort sort = Sorts.builder().asc("code").build();
		List<Personel> lst = repo.findAll(specification, sort);
		return lst;
	}
	@Override
	public List<Personel> getby_org(Long orgid_link, long orgrootid_link) {
		// TODO Auto-generated method stub
		Specification<Personel> specification = Specifications.<Personel>and()
				.eq("orgid_link", orgid_link)
				.eq("orgrootid_link", orgrootid_link)
				.build();

		Sort sort = Sorts.builder().asc("code").build();
		List<Personel> lst = repo.findAll(specification, sort);
		return lst;
	}
	@Override
	public List<Personel> getByNotRegister() {
		// TODO Auto-generated method stub
		return repo.getByNotRegister();
	}

}
