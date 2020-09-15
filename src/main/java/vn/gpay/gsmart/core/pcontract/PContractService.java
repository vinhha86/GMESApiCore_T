package vn.gpay.gsmart.core.pcontract;

import java.util.List;
import java.util.Objects;

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
import vn.gpay.gsmart.core.api.pcontract.PContract_getbysearch_request;
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
	            .eq(request.orgbuyerid_link > 0, "orgbuyerid_link", request.orgbuyerid_link)
	            .eq(request.orgvendorid_link > 0, "orgvendorid_link", request.orgvendorid_link)
	            .eq("status", 1)
	            .eq("orgrootid_link", orgrootid_link)
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
	@Override
	public long getby_buyer_merchandiser(long orgrootid_link, long orgbuyerid_link, long merchandiserid_link) {
		// TODO Auto-generated method stub
		Specification<PContract> specification = Specifications.<PContract>and()
	            .eq(orgbuyerid_link > 0, "orgbuyerid_link", orgbuyerid_link)
	            .eq(merchandiserid_link > 0, "merchandiserid_link", merchandiserid_link)
	            .eq("status", 1)
	            .eq("orgrootid_link", orgrootid_link)
	            .build();
		
		Sort sort = Sorts.builder()
		        .desc("datecreated")
		        .build();
		
		List<PContract> lst = repo.findAll(specification, sort);
		return lst.size() == 0 ? 0 : lst.get(0).getId() ;
	}
	
	@Override
	public List<PContract> getalllist_by_orgrootid_paging(Long orgrootid_link, PContract_getbypaging_request request) {
		Specification<PContract> specification = Specifications.<PContract>and()
	            .eq(request.orgbuyerid_link > 0, "orgbuyerid_link", request.orgbuyerid_link)
	            .eq(request.orgvendorid_link > 0, "orgvendorid_link", request.orgvendorid_link)
	            .eq("status", 1)
	            .eq("orgrootid_link", orgrootid_link)
//        		.like("contractcode","%"+request.contractcode+"%")
	            .build();
		
		Sort sort = Sorts.builder()
		        .desc("datecreated")
		        .build();
		
		List<PContract> lst = repo.findAll(specification, sort);
		return lst;
	}
	
	@Override
	public List<PContract> getBySearch(PContract_getbysearch_request entity) {
		Specification<PContract> specification = Specifications.<PContract>and()
	            .eq(entity.orgbuyerid_link > 0, "orgbuyerid_link", entity.orgbuyerid_link)
	            .eq(entity.orgvendorid_link > 0, "orgvendorid_link", entity.orgvendorid_link)
//	            .eq(Objects.nonNull(entity.contractbuyer_year), "contractbuyer.contract_year", entity.contractbuyer_year)
	            .ge(Objects.nonNull(entity.contractbuyer_yearfrom), "contractbuyer.contract_year", entity.contractbuyer_yearfrom)
	            .le(Objects.nonNull(entity.contractbuyer_yearto), "contractbuyer.contract_year", entity.contractbuyer_yearto)
	            .build();
		
		Sort sort = Sorts.builder()
		        .desc("contractdate")
		        .build();
		
		List<PContract> lst = repo.findAll(specification, sort);
		return lst;
	}

}
