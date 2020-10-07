package vn.gpay.gsmart.core.api.encode;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.encode.Encode;

public class Encode_Porder_getlist_response extends ResponseBase {
	public List<Encode> data;
	public long totalCount;
}
