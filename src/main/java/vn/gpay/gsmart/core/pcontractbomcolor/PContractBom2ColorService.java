package vn.gpay.gsmart.core.pcontractbomcolor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PContractBom2ColorService extends AbstractService<PContractBom2Color> implements IPContractBom2ColorService {
	@Autowired PContractBom2ColorRepository repo;
	@Override
	protected JpaRepository<PContractBom2Color, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<PContractBom2Color> getall_material_in_productBOMColor(long pcontractid_link, long productid_link,
			long colorid_link, long materialid_link) {
		// TODO Auto-generated method stub
		return repo.getall_material_in_productBOMColor(productid_link, pcontractid_link, colorid_link, materialid_link);
	}
	@Override
	public List<PContractBom2Color> getcolor_bymaterial_in_productBOMColor(long pcontractid_link, long productid_link,
			long materialid_link) {
		// TODO Auto-generated method stub
		return repo.getcolor_bymaterial_in_productBOMColor(productid_link, pcontractid_link, materialid_link);
	}
	@Override
	public List<PContractBom2Color> getall_byproduct(long pcontractid_link, long productid_link) {
		// TODO Auto-generated method stub
		return repo.getall_in_productBOMColor(productid_link, pcontractid_link);
	}
}
