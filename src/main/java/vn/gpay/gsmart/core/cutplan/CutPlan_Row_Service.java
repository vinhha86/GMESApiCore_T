package vn.gpay.gsmart.core.cutplan;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.utils.AtributeFixValues;

@Service
public class CutPlan_Row_Service extends AbstractService<CutPlan_Row> implements ICutPlan_Row_Service{
	@Autowired CutPlan_Row_Repository repo;
	@Override
	protected JpaRepository<CutPlan_Row, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<CutPlan_Row> getby_color(Long porderid_link, Long material_skuid_link, Long colorid_link, Long orgrootid_link) {
		// TODO Auto-generated method stub
		Long attributeid_link = AtributeFixValues.ATTR_COLOR;
		return repo.getby_sku_and_porder(material_skuid_link, porderid_link, orgrootid_link, colorid_link, attributeid_link);
	}
	@Override
	public List<CutPlan_Row> getby_porder_matsku_productsku(Long porderid_link, Long material_skuid_link,
			Long product_skuid_link, Integer type, String name) {
		// TODO Auto-generated method stub
		return repo.getby_matsku_and_porder_and_productsku(material_skuid_link, porderid_link, product_skuid_link, type, name);
	}

}
