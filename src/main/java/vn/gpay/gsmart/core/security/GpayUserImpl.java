package vn.gpay.gsmart.core.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Sorts;
import com.github.wenhao.jpa.Specifications;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.org.Org;

@Service
public class GpayUserImpl  extends AbstractService<GpayUser> implements IGpayUserService{

	@Autowired
	GpayUserRepository repository; 
	@Override
	protected JpaRepository<GpayUser, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}
	@Override
	public GpayUser findByUsername(String username) {
		// TODO Auto-generated method stub
		return repository.findByUsername(username);
	}
	@Override
	public GpayUser findByEmail(String email) {
		// TODO Auto-generated method stub
		return repository.findByEmail(email);
	}
	@Override
	public GpayUser findById(long id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}
	@Override
	public List<GpayUser> getUserList(long orgid_link, String textsearch, int status) {
		Specification<GpayUser> specification = Specifications.<GpayUser>and()
	            .eq( status!=-1,"status", status)
	            .eq(orgid_link > 1 ,"orgid_link", orgid_link)
	            .predicate(Specifications.or()
	            		.like("useremail","%"+textsearch+"%")
	            		.like("firstname","%"+textsearch+"%")
	            		.like("lastname","%"+textsearch+"%")
	            		.build())
	            .build();
		Sort sort = Sorts.builder()
		        .desc("id")
		        .build();
	    return repository.findAll(specification,sort);
	}
	@Override
	public List<GpayUser> getUserList_page(String firstname, String middlename, String lastname, String username,
			Long groupuserid_link) {
		// TODO Auto-generated method stub
		Specification<GpayUser> specification = Specifications.<GpayUser>and()
//	            .eq( status!=-1,"status", status)
	            .like("firstname","%"+firstname+"%")
        		.like("middlename","%"+middlename+"%")
        		.like("lastname","%"+lastname+"%")
        		.like("username","%"+username+"%")
	            .build();
		Sort sort = Sorts.builder()
		        .desc("id")
		        .build();
		List<GpayUser> listuser = repository.findAll(specification,sort);
		if (groupuserid_link != null && groupuserid_link != 0)
			listuser.removeIf(c-> !c.getUsergroup_name().contains(groupuserid_link.toString()));
	    return listuser;
	}
	@Override
	public List<GpayUser> getUserinOrgid(List<Org> listorg) {
		// TODO Auto-generated method stub
		List<Long> listid = new ArrayList<>();
		
		for(Org org : listorg) {
			listid.add(org.getId());
		}
		
		Specification<GpayUser> specification = Specifications.<GpayUser>and()
	            .ne("status", -1)
	            .in(listid.size() > 0 ,"orgid_link", listid.toArray())
	            .build();
		Sort sort = Sorts.builder()
		        .desc("id")
		        .build();
		List<GpayUser> list = repository.findAll(specification,sort);
	    return list;
	}
	@Override
	public List<GpayUser> getUserBycode_Personel(String personnel_code, Long id) {
		// TODO Auto-generated method stub
		return repository.getUserBycode_Personel(personnel_code, id);
	}
}
