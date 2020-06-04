package vn.gpay.gsmart.core.api.plan;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.porder_plan.PorderPlanBinding;

public class Plan_getall_response extends ResponseBase{
	public List<PorderPlanBinding> children;
}
