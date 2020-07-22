package vn.gpay.gsmart.core.porder_bom_sku;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class POrderBOMSKU_Service extends AbstractService<POrderBOMSKU> implements IPOrderBOMSKU_Service {
	@Autowired IPOrderBOMSKU_Repository repo;
	@Override
	protected JpaRepository<POrderBOMSKU, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	
	@Override
	public List<POrderBOMSKU> getByPOrderID(Long porderid_link){
		return repo.getByPOrderID(porderid_link);
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
}
