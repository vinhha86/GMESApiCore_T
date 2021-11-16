package vn.gpay.gsmart.core.porder_bom_color;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPOrderBomColor_Service extends Operations<PorderBomColor> {
	public List<PorderBomColor> getby_porder(long porderid_link);
	public List<PorderBomColor> getby_porder_and_color(long porderid_link, long colorid_link);
	public List<PorderBomColor> getby_porder_and_material(long porderid_link, long materialid_link);
	public List<PorderBomColor> getby_porder_and_material_and_color(long porderid_link, long materialid_link, long colorid_link);
	public List<PorderBomColor> getby_pcontract_product_and_material_and_color(long pcontractid_link, Long productid_link, long materialid_link, long colorid_link);
}
