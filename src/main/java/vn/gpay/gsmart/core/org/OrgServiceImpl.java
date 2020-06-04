package vn.gpay.gsmart.core.org;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Sorts;
import com.github.wenhao.jpa.Specifications;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class OrgServiceImpl extends AbstractService<Org> implements IOrgService{

	@Autowired
	OrgRepository repositoty;

	@Override
	protected JpaRepository<Org, Long> getRepository() {
		// TODO Auto-generated method stub
		return repositoty;
	}

	@Override
	public List<Org> findOrgByType(long root_orgid,long orgid,long type) {
		// TODO Auto-generated method stub
		//return repositoty.findOrgByType(root_orgid,orgid,type);
		Specification<Org> specification = Specifications.<Org>and()
	            .eq( "orgrootid_link", root_orgid)
	            .ne("id", orgid)
	            .eq(type!=1, "orgtypeid_link", type)
	            .build();
		Sort sort = Sorts.builder()
		        .desc("id")
		        .build();
	    return repositoty.findAll(specification,sort);
		
	}
	@Override
	public List<Org> findStoreByType(long root_orgid, Long orgid, long type) {
		// TODO Auto-generated method stub
		Specification<Org> specification = Specifications.<Org>and()
	            .eq( "orgrootid_link", root_orgid)
	            .eq(Objects.nonNull(orgid),"id", orgid)
	            .eq(type!=1, "orgtypeid_link", type)
	            .build();
		Sort sort = Sorts.builder()
		        .desc("id")
		        .build();
	    return repositoty.findAll(specification,sort);
	}

	@Override
	public List<Org> findOrgInvCheckByType(long orgid) {
		// TODO Auto-generated method stub
		return repositoty.findOrgInvCheckByType(orgid);
	}

	@Override
	public List<Org> findRootOrgInvCheckByType(long orgid) {
		// TODO Auto-generated method stub
		return repositoty.findRootOrgInvCheckByType(orgid);
	}

	@Override
	public List<Org> findOrgAllByRoot(long orgid) {
		// TODO Auto-generated method stub
		return repositoty.findOrgAllByRoot(orgid);
	}

	@Override
	public List<Org> findAllorgByTypeId(int orgtypeid_link, long orgrootid) {
		// TODO Auto-generated method stub
		return repositoty.findAllOrgbyType(orgrootid, orgtypeid_link);
	}

	@Override
	public List<Org> findOrgAllByRoot(long orgrootid, long orgid, List<String> list_typeid, boolean isincludeorg) {
		// TODO Auto-generated method stub
		List<Org> list  = new ArrayList<Org>();
		Specification<Org> specification = Specifications.<Org>and()
	            .eq("orgrootid_link", orgrootid)
	            .ne(!isincludeorg, "id", orgid)
	            .in(list_typeid.size() > 0 && orgid != orgrootid,"orgtypeid_link", list_typeid.toArray())
	            .build();
		Sort sort = Sorts.builder()
		        .desc("id")
		        .build();
		list =  repositoty.findAll(specification,sort);
		if(isincludeorg)
		list = getOrgChildrenbyId(orgid, list);
		return list;
	}
	
	private List<Org> getOrgChildrenbyId(long orgid, List<Org> listAll){
		List<Org> list = new ArrayList<>();
		
		if(list.size() == 0) {
			for(Org org : listAll) {
				if(org.getId() == orgid) {
					list.add(org);
					listAll.remove(org);
					break;
				}
			}
		}
		else { 
			for(Org org : listAll) {
				check(list, org);
			}
		}
		
		return list;
	}

	private void check(List<Org> list, Org obj) {
		for(Org org : list) {
			if(org.getId() == obj.getParentid_link()) {
				list.add(org);
			}
		}
	}
}
