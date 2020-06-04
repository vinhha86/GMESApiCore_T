package vn.gpay.gsmart.core.pcontract;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Sorts;
import com.github.wenhao.jpa.Specifications;

import vn.gpay.gsmart.core.api.pcontract.PContract_getbypaging_request;
import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PContractService extends AbstractService<PContract> implements IPContractService {
	@Autowired IPContractRepository repo;
	@Override
	protected JpaRepository<PContract, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public Page<PContract> getall_by_orgrootid_paging(Long orgrootid_link, PContract_getbypaging_request request) {
		// TODO Auto-generated method stub
		Specification<PContract> specification = Specifications.<PContract>and()
	            .eq(request.orgcustomerid_link > 0, "orgcustomerid_link", request.orgcustomerid_link)
	            .eq(request.branchid_link> 0 ,"branchid_link", request.branchid_link)
	            .eq(request.seasonid_link > 0, "seasonid_link", request.seasonid_link)
	            .eq("status", 1)
	            .eq("orgrootid_link", orgrootid_link)
	            .like("cust_contractcode","%"+request.cust_contractcode+"%")
        		.like("contractcode","%"+request.contractcode+"%")
	            .build();
		
		Sort sort = Sorts.builder()
		        .desc("datecreated")
		        .build();
		
		Page<PContract> lst = repo.findAll(specification, PageRequest.of(request.page - 1, request.limit, sort));
		return lst;
	}
	@Override
	public List<PContract> getby_code(long orgrootid_link, String contractcode, long pcontractid_link) {
		// TODO Auto-generated method stub
		return repo.get_byorgrootid_link_and_contractcode(orgrootid_link, pcontractid_link, contractcode);
	}

}
