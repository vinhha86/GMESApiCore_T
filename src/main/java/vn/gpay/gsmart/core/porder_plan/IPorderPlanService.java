package vn.gpay.gsmart.core.porder_plan;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPorderPlanService extends Operations<Porder_Plan> {
	public List<Porder_Plan> get_by_plantype(long porderid_link, int plantype);
}
