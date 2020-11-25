package vn.gpay.gsmart.core.api.porderprocessingns;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.RequestBase;
import vn.gpay.gsmart.core.porder_sewingcost.POrderSewingCostBinding;

public class PorderProcessingNs_save_request extends RequestBase{
	public List<POrderSewingCostBinding> data;
	public Long personnelid_link;
    public Integer shifttypeid_link;
    public Date processingdate;
    public Long porderid_link;
    public Long pordergrantid_link;
}
