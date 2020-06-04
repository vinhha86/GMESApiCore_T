package vn.gpay.gsmart.core.api.pcontract;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.pcontract.PContract;


public class PContract_getbypaging_response extends ResponseBase{
	public List<PContract> data;
	public long totalCount;
}
