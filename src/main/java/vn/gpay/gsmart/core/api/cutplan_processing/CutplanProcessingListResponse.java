package vn.gpay.gsmart.core.api.cutplan_processing;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.cutplan_processing.CutplanProcessing;

public class CutplanProcessingListResponse extends ResponseBase{
	public List<CutplanProcessing> data;
	public Long totalCount;
}
