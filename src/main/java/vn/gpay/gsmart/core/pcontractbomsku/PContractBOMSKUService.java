package vn.gpay.gsmart.core.pcontractbomsku;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.sku.ISKU_AttValue_Repository;


@Service
public class PContractBOMSKUService extends AbstractService<PContractBOMSKU> implements IPContractBOMSKUService {
	@Autowired IPContractBOMSKURepository repo;
	@Autowired ISKU_AttValue_Repository sku_att_repo;
	@Override
	protected JpaRepository<PContractBOMSKU, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<PContractBOMSKU> getall_material_in_productBOMSKU(long pcontractid_link, long productid_link,
			long sizeid_link, long colorid_link, long materialid_link) {
		List<Long> list_sku = sku_att_repo.getskuid_by_valueMau_and_valueCo(colorid_link, sizeid_link, productid_link);
		long skuid_link = 0;
		if(list_sku.size() > 0) {
			skuid_link = list_sku.get(0);
		}
		
		return repo.getall_material_in_productBOMSKU(productid_link, pcontractid_link, skuid_link, materialid_link);
	}
	@Override
	public List<PContractBOMSKU> getcolor_bymaterial_in_productBOMSKU(long pcontractid_link, long productid_link, long materialid_link) {
		// TODO Auto-generated method stub
		return repo.getall_bymaterial_in_productBOMSKU(productid_link, pcontractid_link, materialid_link);
	}
	@Override
	public List<PContractBOMSKU> getmaterial_bycolorid_link(long pcontractid_link, long productid_link,
			long colorid_link, long materialid_link) {
		// TODO Auto-generated method stub
		return repo.get_material_by_colorid_link(productid_link, pcontractid_link, colorid_link, materialid_link);
	}
	@Override
	public long getskuid_link_by_color_and_size(long colorid_link, long sizeid_link, long productid_link) {
		// TODO Auto-generated method stub
		List<Long> list_sku = sku_att_repo.getskuid_by_valueMau_and_valueCo(colorid_link, sizeid_link, productid_link);
		
		long skuid_link = 0;
		if(list_sku.size() > 0) {
			skuid_link = list_sku.get(0);
		}
		return skuid_link;
	}
	@Override
	public List<Long> getsize_bycolor(long pcontractid_link, long productid_link, long colorid_link) {
		// TODO Auto-generated method stub
		return repo.getsize_by_colorid_link(productid_link, pcontractid_link, colorid_link);
	}
	@Override
	public List<PContractBOMSKU> getMaterials_BySKUId(Long skuid_link){
		return repo.getMaterials_BySKUId(skuid_link);
	}
}
