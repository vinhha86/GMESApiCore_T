package vn.gpay.gsmart.core.api.handover;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.handover.Handover;

public class Handover_getall_response extends ResponseBase{
	public List<Handover> data;
	public Integer totalCount;
}
