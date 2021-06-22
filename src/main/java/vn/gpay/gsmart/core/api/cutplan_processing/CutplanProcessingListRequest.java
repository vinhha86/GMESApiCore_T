package vn.gpay.gsmart.core.api.cutplan_processing;

import java.util.Date;

import vn.gpay.gsmart.core.base.RequestBase;

public class CutplanProcessingListRequest extends RequestBase{
	public Date processingdate_from;
	public Date processingdate_to;
	public Integer limit;
	public Integer page;
	public Long porderid_link;
}
