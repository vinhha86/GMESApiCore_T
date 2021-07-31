package vn.gpay.gsmart.core.personel;

import java.util.Date;
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
				.ge("status", 0)
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
	@Override
	public List<Personel> getPerson_by_register_code(Long orgrootid_link, String register_code) {
		// TODO Auto-generated method stub
		return repo.getby_registercode(register_code, orgrootid_link);
	}
	@Override
	public List<Personel> getForPProcessingProductivity(Long orgid_link, Integer shifttypeid_link, Date workingdate) {
		return repo.getForPProcessingProductivity(orgid_link, shifttypeid_link, workingdate);
	}
	@Override
	public List<Personel> getby_orgs(List<Long> orgid_link, long orgrootid_link, boolean ishas_bikenumber) {
		// TODO Auto-generated method stub
//		Specification<Personel> specification = Specifications.<Personel>and()
//				.in("orgid_link", orgid_link.toArray())
//				.eq("orgrootid_link", orgrootid_link)
//				.ne(ishas_bikenumber == true, "bike_number", "")
//				.ne(ishas_bikenumber == true, "bike_number", nu)
//				.build();
//
//		Sort sort = Sorts.builder().asc("code").build();
//		List<Personel> lst = repo.findAll(specification, sort);
		return repo.getperson_and_bikenumber(orgid_link, ishas_bikenumber, orgrootid_link);
	}
	@Override
	public List<Personel> getby_bikenumber(String bike_number) {
		// TODO Auto-generated method stub
		return repo.getby_bikenumber(bike_number);
	}
	@Override
	public Personel getPersonelBycode(String personnel_code) {
		// TODO Auto-generated method stub
		return repo.getPersonelBycode(personnel_code);
	}

}
