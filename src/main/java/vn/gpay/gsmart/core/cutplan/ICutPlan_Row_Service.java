package vn.gpay.gsmart.core.cutplan;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface ICutPlan_Row_Service extends Operations<CutPlan_Row>{
	List<CutPlan_Row> getby_color(Long porderid_link, Long material_skuid_link, Long colorid_link, Long orgrootid_link);
	List<CutPlan_Row> getby_porder_matsku_productsku(Long porderid_link, Long material_skuid_link, Long product_skuid_link, Integer type, String name);
}
