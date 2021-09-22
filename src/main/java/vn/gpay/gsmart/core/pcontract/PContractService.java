package vn.gpay.gsmart.core.pcontract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
	@Autowired
	IPContractRepository repo;
	@Autowired
	EntityManager em;

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
				.eq(request.orgvendorid_link > 0, "orgvendorid_link", request.orgvendorid_link).eq("status", 1)
				.eq("orgrootid_link", orgrootid_link).like("contractcode", "%" + request.contractcode + "%").build();

		Sort sort = Sorts.builder().desc("datecreated").build();

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
				.eq(merchandiserid_link > 0, "merchandiserid_link", merchandiserid_link).eq("status", 1)
				.eq("orgrootid_link", orgrootid_link).build();

		Sort sort = Sorts.builder().desc("datecreated").build();

		List<PContract> lst = repo.findAll(specification, sort);
		return lst.size() == 0 ? 0 : lst.get(0).getId();
	}

	@Override
	public List<PContract> getalllist_by_orgrootid_paging(Long orgrootid_link, PContract_getbypaging_request request) {
		Specification<PContract> specification = Specifications.<PContract>and()
				.eq(request.orgbuyerid_link > 0, "orgbuyerid_link", request.orgbuyerid_link)
				.eq(request.orgvendorid_link > 0, "orgvendorid_link", request.orgvendorid_link).eq("status", 1)
				.eq("orgrootid_link", orgrootid_link)
//        		.like("contractcode","%"+request.contractcode+"%")
				.build();

		Sort sort = Sorts.builder().desc("datecreated").build();

		List<PContract> lst = repo.findAll(specification, sort);
		return lst;
	}

	@Override
	public List<PContract> getBySearch(PContract_getbysearch_request entity) {
		Specification<PContract> specification = Specifications.<PContract>and()
				.eq(entity.orgbuyerid_link > 0, "orgbuyerid_link", entity.orgbuyerid_link)
				.eq(entity.orgvendorid_link > 0, "orgvendorid_link", entity.orgvendorid_link)
//	            .eq(Objects.nonNull(entity.contractbuyer_year), "contractbuyer.contract_year", entity.contractbuyer_year)
				.ge(Objects.nonNull(entity.contractbuyer_yearfrom), "contractbuyer.contract_year",
						entity.contractbuyer_yearfrom)
				.le(Objects.nonNull(entity.contractbuyer_yearto), "contractbuyer.contract_year",
						entity.contractbuyer_yearto)
				.predicate(Objects.nonNull(entity.po_code) && entity.po_code.length() > 0,
						Specifications.or().like("pos.po_buyer", "%" + entity.po_code.toUpperCase() + "%")
								.like("pos.po_buyer", "%" + entity.po_code.toLowerCase() + "%").build())
				.build();

		Sort sort = Sorts.builder().desc("contractdate").build();

		List<PContract> lst = repo.findAll(specification, sort);
		return lst;
	}

	@Override
	public List<PContract> getBySearch_PosList(PContract_getbysearch_request entity, List<Long> pos, List<Long> product,
			List<Long> vendors, List<Long> buyers) {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<PContract> cq_po = cb.createQuery(PContract.class);
		Root<PContract> rootPcontract = cq_po.from(PContract.class);
		List<Predicate> thePredicates = new ArrayList<>();

		// pos
		if (pos.size() > 0) {
			In<Long> inContractClause = cb.in(rootPcontract.get("id"));
			for (Long thePContract : pos) {
				inContractClause.value(thePContract);
			}
			thePredicates.add(cb.and(inContractClause));
		} else {
			return null;
		}

		// product
		if (product.size() > 0) {
			In<Long> inContractProductClause = cb.in(rootPcontract.get("id"));
			for (Long p : product) {
				inContractProductClause.value(p);
			}
			thePredicates.add(cb.and(inContractProductClause));
		} else {
			return null;
		}

		// orgbuyerid_link
		if (entity.orgbuyerid_link > 0) {
			thePredicates.add(cb.equal(rootPcontract.get("orgbuyerid_link"), entity.orgbuyerid_link));
		}
		// orgvendorid_link
		if (entity.orgvendorid_link > 0) {
			thePredicates.add(cb.equal(rootPcontract.get("orgvendorid_link"), entity.orgvendorid_link));
		}

		// contractbuyer_code
		if (entity.contractbuyer_code.length() > 0) {
			thePredicates
					.add(cb.equal(rootPcontract.get("contractbuyer").get("contract_code"), entity.contractbuyer_code));
		}
		// contractbuyer_yearfrom
		if (Objects.nonNull(entity.contractbuyer_yearfrom)) {
			thePredicates
					.add(cb.ge(rootPcontract.get("contractbuyer").get("contract_year"), entity.contractbuyer_yearfrom));
		}
		// contractbuyer_yearto
		if (Objects.nonNull(entity.contractbuyer_yearto)) {
			thePredicates
					.add(cb.le(rootPcontract.get("contractbuyer").get("contract_year"), entity.contractbuyer_yearto));
		}

		// vendor
		if (vendors.size() > 0) {
			In<Long> inVendorClause = cb.in(rootPcontract.get("orgvendorid_link"));
			for (Long vendor : vendors) {
				inVendorClause.value(vendor);
			}
			thePredicates.add(cb.and(inVendorClause));
		}

		// buyer
		if (buyers.size() > 0) {
			In<Long> inBuyerClause = cb.in(rootPcontract.get("orgbuyerid_link"));
			for (Long buyer : buyers) {
				inBuyerClause.value(buyer);
			}
			thePredicates.add(cb.and(inBuyerClause));
		}

		Predicate p = cb.and(thePredicates.toArray(new Predicate[0]));
		cq_po.where(p);

//		List<Order> orderList = new ArrayList<Order>();
//
//		orderList.add(cb.desc(rootPcontract.get("datecreated")));
//		cq_po.orderBy(orderList);

		List<PContract> lst = em.createQuery(cq_po).getResultList();
		return lst;
	}

	@Override
	public List<PContract> findByContractcode(String contractcode) {
		// TODO Auto-generated method stub
		return repo.findByContractcode(contractcode);
	}

	@Override
	public List<PContract> findByExactContractcode(String contractcode) {
		// TODO Auto-generated method stub
		return repo.findByExactContractcode(contractcode);
	}

	@Override
	public int getProductNotBom(Long pcontractid_link) {
		// TODO Auto-generated method stub
		return repo.GetProductNotBom(pcontractid_link).size();
	}

	@Override
	public List<PContract> getPContractByYear(int year) {
		// TODO Auto-generated method stub
		return repo.getPContractByYear(year);
	}
}
