package vn.gpay.gsmart.core.cutplan;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class CutPlan_Size_Service extends AbstractService<CutPlan_Size> implements ICutPlan_Size_Service {
	@Autowired
	CutPlan_Repository repo;

	@Override
	protected JpaRepository<CutPlan_Size, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<CutPlan_Size> getby_sku_and_porder(Long skuid_link, Long porderid_link, Long orgrootid_link) {
		// TODO Auto-generated method stub
		return repo.getby_sku_and_porder(skuid_link, porderid_link, orgrootid_link);
	}

	@Override
	public List<CutPlan_Size> getby_row(Long orgrootid_link, Long cutplan_rowid_link) {
		// TODO Auto-generated method stub
		return repo.getby_row(cutplan_rowid_link, orgrootid_link);
	}

	@Override
	public List<CutPlan_Size> getby_row_and_productsku(Long orgrootid_link, Long cutplanrowid_link,
			Long product_skuid_link) {
		// TODO Auto-generated method stub
		return repo.getby_row_and_productsku(cutplanrowid_link, product_skuid_link, orgrootid_link);
	}

	@Override
	public List<CutPlan_Size> getby_porder_matsku_productsku(Long porderid_link, Long material_skuid_link,
			Long product_skuid_link, Integer type, String name) {
		// TODO Auto-generated method stub
		product_skuid_link = product_skuid_link == 0 ? null : product_skuid_link;
		return repo.getby_matsku_and_porder_and_productsku(material_skuid_link, porderid_link, product_skuid_link, type,
				name);
	}

	@Override
	public List<CutPlan_Size> getby_sku_and_porder_and_color(Long material_skuid_link, Long porderid_link,
			Long orgrootid_link, Long colorid_link) {
		// TODO Auto-generated method stub
		return repo.getby_sku_and_porder_color(material_skuid_link, porderid_link, orgrootid_link, colorid_link);
	}

	@Override
	public Integer getTotalAmount_By_CutPlanRow(Long cutplanrowid_link) {
		return repo.getTotalAmount_By_CutPlanRow(cutplanrowid_link);
	}

	@Override
	public List<CutPlan_Size> getby_sku_and_pcontract_product_and_color(Long material_skuid_link, Long pcontractid_link,
			Long productid_link, Long orgrootid_link, Long colorid_link) {
		// TODO Auto-generated method stub
		return repo.getby_sku_and_pcontract_product_color(material_skuid_link, pcontractid_link, productid_link,
				orgrootid_link, colorid_link);
	}

	@Override
	public List<CutPlan_Size> getby_pcontract_product_matsku_productsku(Long pcontractid_link, Long productid_link, Long material_skuid_link,
			Long product_skuid_link, Integer type, String name) {
		// TODO Auto-generated method stub
		product_skuid_link = product_skuid_link == 0 ? null :product_skuid_link;
		return repo.getby_matsku_and_pcontract_product_and_productsku(material_skuid_link, pcontractid_link, productid_link, product_skuid_link, type, name);
	}

	@Override
	public List<CutPlan_Size> getby_sku_and_pcontract_product(Long material_skuid_link, Long pcontractid_link,
			Long productid_link, Long orgrootid_link) {
		// TODO Auto-generated method stub
		return repo.getby_sku_and_pcontract_product(material_skuid_link, pcontractid_link, productid_link, orgrootid_link);
	}
}
