package vn.gpay.gsmart.core.cutplan;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface ICutPlan_Service extends Operations<CutPlan_Size> {
	List<CutPlan_Size> getby_sku_and_porder(Long skuid_link, Long porderid_link, Long orgrootid_link);
}
