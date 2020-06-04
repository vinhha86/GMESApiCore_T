package vn.gpay.gsmart.core.api.invcheck;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.invcheck.InvcheckEpc;

public class EpcBySkuResponse extends ResponseBase{
	public List<InvcheckEpc> data;
}
