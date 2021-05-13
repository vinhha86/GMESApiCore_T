package vn.gpay.gsmart.core.porder_bom_sku;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.sku.ISKU_AttValue_Repository;

@Service
public class POrderBOMSKU_Service extends AbstractService<POrderBOMSKU> implements IPOrderBOMSKU_Service {
	@Autowired IPOrderBOMSKU_Repository repo;
	@Autowired ISKU_AttValue_Repository sku_att_repo;
	@Override
	protected JpaRepository<POrderBOMSKU, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	
	@Override
	public List<POrderBOMSKU> getByPOrderID_and_type(Long porderid_link, int type){
		return repo.getByPOrderID(porderid_link, type);
	}
	
	@Override
	public List<POrderBOMSKU> getSKUByMaterial(Long porderid_link, Long materialid_link){
		return repo.getSKUByMaterial(porderid_link, materialid_link);
	}
	
	@Override
	public List<POrderBOMSKU_By_Product> getByPOrderID_GroupByProduct(Long porderid_link){
		List<POrderBOMSKU_By_Product> lsBOMSKU = new ArrayList<POrderBOMSKU_By_Product>();
		List<Object[]> lsResult = repo.getByPOrderID_GroupByProduct(porderid_link);
		for (Object[] row : lsResult){
			POrderBOMSKU_By_Product theBOMSKU = new POrderBOMSKU_By_Product();
			theBOMSKU.setProductid_link(Long.parseLong(row[0].toString()));
			theBOMSKU.setMaterialid_link(Long.parseLong(row[1].toString()));
			theBOMSKU.setAmount(Float.parseFloat(row[2].toString()));
			lsBOMSKU.add(theBOMSKU);
		}
		return lsBOMSKU;
	}
	
	@Override
	public List<POrderBOMSKU_By_Color> getByPOrderID_GroupByColor(Long porderid_link){
		List<POrderBOMSKU_By_Color> lsBOMSKU = new ArrayList<POrderBOMSKU_By_Color>();
		List<Object[]> lsResult = repo.getByPOrderID_GroupByColor(porderid_link);
		for (Object[] row : lsResult){
			POrderBOMSKU_By_Color theBOMSKU = new POrderBOMSKU_By_Color();
			theBOMSKU.setProductcolor_name(row[0].toString());
			theBOMSKU.setMaterialid_link(Long.parseLong(row[1].toString()));
			theBOMSKU.setAmount(Float.parseFloat(row[2].toString()));
			lsBOMSKU.add(theBOMSKU);
		}
		return lsBOMSKU;
	}

	@Override
	public List<POrderBOMSKU> getby_porder_and_color(Long porderid_link, Long colorid_link) {
		// TODO Auto-generated method stub
		return repo.getByPOrder_and_color(porderid_link, colorid_link);
	}

	@Override
	public List<POrderBOMSKU> getby_porder_and_material(Long porderid_link, Long materialid_link) {
		// TODO Auto-generated method stub
		return repo.getByPOrder_and_material(porderid_link, materialid_link);
	}

	@Override
	public List<POrderBOMSKU> getby_porder_and_material_and_color(Long porderid_link, Long materialid_link,
			long colorid_link) {
		// TODO Auto-generated method stub
		return repo.getByPOrder_and_material_and_color(porderid_link, materialid_link, colorid_link);
	}

	@Override
	public List<POrderBOMSKU> getby_porder_and_material_and_color_and_size_and_type(Long porderid_link, Long productid_link, 
			Long materialid_link, long colorid_link,long sizeid_link, int type) {
		// TODO Auto-generated method stub
		List<Long> list_sku = sku_att_repo.getskuid_by_valueMau_and_valueCo(colorid_link, sizeid_link, productid_link);
		long skuid_link = 0;
		if(list_sku.size() > 0) {
			skuid_link = list_sku.get(0);
		}
		return repo.getByPOrder_and_material_and_sku_and_type(porderid_link, materialid_link, skuid_link, type);
	}

	@Override
	public List<POrderBOMSKU> getby_porder_and_material_and_sku_and_type(Long porderid_link,
			Long materialid_link, long skuid_link, int type) {
		// TODO Auto-generated method stub
		return repo.getByPOrder_and_material_and_sku_and_type(porderid_link, materialid_link, skuid_link, type);
	}
}
