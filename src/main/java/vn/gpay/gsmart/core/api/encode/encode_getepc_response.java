package vn.gpay.gsmart.core.api.encode;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.tagencode.WareHouse_Encode_EPC;

public class encode_getepc_response extends ResponseBase {
	public List<WareHouse_Encode_EPC> data;
}
