package vn.gpay.gsmart.core.api.cutplan_processing;

import java.util.List;

import vn.gpay.gsmart.core.base.RequestBase;
import vn.gpay.gsmart.core.cutplan_processing.CutplanProcessing;

public class CutplanProcessingCreateRequest extends RequestBase{
	public List<CutplanProcessing> data;
	public Long porderid_link;
	public Long material_skuid_link;
	public Long colorid_link;
}
