package vn.gpay.gsmart.core.api.porderprocessing;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.porderprocessing.POrderProcessingBinding;

public class POrderProcess_getForChart_response extends ResponseBase {
	public List<POrderProcessingBinding> data;
}
