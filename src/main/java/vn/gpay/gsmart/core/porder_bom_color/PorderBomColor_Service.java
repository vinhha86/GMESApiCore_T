package vn.gpay.gsmart.core.porder_bom_color;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PorderBomColor_Service extends AbstractService<PorderBomColor> implements IPOrderBomColor_Service{
	@Autowired PorderBomColor_Repository repo;
	@Override
	protected JpaRepository<PorderBomColor, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<PorderBomColor> getby_porder(long porderid_link) {
		// TODO Auto-generated method stub
		return repo.getby_porder(porderid_link);
	}
	@Override
	public List<PorderBomColor> getby_porder_and_color(long porderid_link, long colorid_link) {
		// TODO Auto-generated method stub
		return repo.getby_porder_and_color(porderid_link, colorid_link);
	}
	@Override
	public List<PorderBomColor> getby_porder_and_material(long porderid_link, long materialid_link) {
		// TODO Auto-generated method stub
		return repo.getby_porder_and_material(porderid_link, materialid_link);
	}
	@Override
	public List<PorderBomColor> getby_porder_and_material_and_color(long porderid_link, long materialid_link,
			long colorid_link) {
		// TODO Auto-generated method stub
		return repo.getby_porder_and_color_and_material(porderid_link, colorid_link, materialid_link);
	}
	@Override
	public List<PorderBomColor> getby_pcontract_product_and_material_and_color(long pcontractid_link,
			Long productid_link, long materialid_link, long colorid_link) {
		// TODO Auto-generated method stub
		return repo.getby_pcontract_product_and_color_and_material(pcontractid_link, productid_link, colorid_link, materialid_link);
	}

}
