package vn.gpay.gsmart.core.attributevalue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Sorts;
import com.github.wenhao.jpa.Specifications;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.pcontractattributevalue.IPContractProductAtrributeValueRepository;

@Service
public class AttributeValueService extends AbstractService<Attributevalue> implements IAttributeValueService {

	@Autowired IAttibuteValueRepository repo;
	@Autowired IPContractProductAtrributeValueRepository pcavrepo;
	@Override
	public List<Attributevalue> getlist_byidAttribute(Long id) {
		// TODO Auto-generated method stub
		return repo.getlist_ByidAttribute(id);
	}
	
	@Override
	public Integer getMaxSortValue(Long id) {
		// TODO Auto-generated method stub
		Integer max = repo.getMaxSortValue(id);
		return max == null ? 1: max+1;
	}

	@Override
	protected JpaRepository<Attributevalue, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<Attributevalue> getlistid_notin_pcontract_attribute(long orgrootid_link, long pcontractid_link,
			long productid_link, long attributeid_link) {
		// TODO Auto-generated method stub
		List<Long> listAtt = pcavrepo.getlistvalueid_by_product_and_pcontract_and_attribute
				(orgrootid_link, productid_link, pcontractid_link, attributeid_link);
		
		Specification<Attributevalue> specification = Specifications.<Attributevalue>and()
				.notIn(listAtt.size() > 0,"id", listAtt.toArray())
				.eq("attributeid_link", attributeid_link)
	            .build();
		Sort sort = Sorts.builder()
				.asc("isdefault")
		        .asc("value")
		        .build();
		
		List<Attributevalue> lst = repo.findAll(specification, sort);
		return lst;
	}

	@Override
	public List<Attributevalue> getByValue(String value, Long attributeid_link) {
		// TODO Auto-generated method stub
		return repo.getByValue(value, attributeid_link);
	}

	@Override
	public List<Attributevalue> getColorForStockin(Long stockinid_link) {
		// TODO Auto-generated method stub
		return repo.getColorForStockin(stockinid_link);
	}


}
