package vn.gpay.gsmart.core.attribute;

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
import vn.gpay.gsmart.core.pcontractattributevalue.IPContractProductAtrributeValueRepository;
import vn.gpay.gsmart.core.utils.ProductType;


@Service
public class AttributeService extends AbstractService<Attribute> implements IAttributeService {

	@Autowired
	IAttributeRepository repository;
	@Autowired
	IPContractProductAtrributeValueRepository repoPContractAV;

	@Override
	protected JpaRepository<Attribute, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}

	@Override
	public List<Attribute> getList_byorgid_link(Long orgid_link) {
		// TODO Auto-generated method stub
		return repository.getall_byOrgId(orgid_link);
	}

	@Override
	public List<Attribute> getList_attribute_forproduct(Integer product_type, Long orgrootid_link) {
		// TODO Auto-generated method stub
		List<Attribute> list = new ArrayList<Attribute>();
		if (product_type >= ProductType.SKU_TYPE_PRODUCT_MIN && product_type < ProductType.SKU_TYPE_PRODUCT_MAX)
			list = repository.getattribute_for_product(orgrootid_link);
		else
			if (product_type >= ProductType.SKU_TYPE_MATERIAL_MIN && product_type <= ProductType.SKU_TYPE_MATERIAL_MAX)
				list = repository.getattribute_for_Material(orgrootid_link);
			else
				if (product_type >= ProductType.SKU_TYPE_SWEINGTRIM_MIN && product_type <= ProductType.SKU_TYPE_SWEINGTRIM_MAX)
					list = repository.getattribute_for_SewingTrim(orgrootid_link);
				else
					if (product_type >= ProductType.SKU_TYPE_PACKINGTRIM_MIN && product_type <= ProductType.SKU_TYPE_PACKINGTRIM_MAX)
						list = repository.getattribute_for_PackingTrim(orgrootid_link);
					else
						if (product_type >= ProductType.SKU_TYPE_SWEINGTHREAD_MIN && product_type <= ProductType.SKU_TYPE_SWEINGTHREAD_MAX)
							list = repository.getattribute_for_SewingThread(orgrootid_link);
		return list;
	}

	@Override
	public List<Attribute> getlist_notin_pcontractproduct(long pcontractid_link, long productid_link, long orgrootid_link) {
		// TODO Auto-generated method stub
		List<Long> listAtt = repoPContractAV.getlistattribute_by_product_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
		
		Specification<Attribute> specification = Specifications.<Attribute>and()
				.notIn(listAtt.size() > 0,"id", listAtt.toArray())
				.eq("orgrootid_link", orgrootid_link)
	            .build();
		Sort sort = Sorts.builder()
		        .asc("name")
		        .build();
		
		List<Attribute> lst = repository.findAll(specification, sort);
		return lst;
	}

}
