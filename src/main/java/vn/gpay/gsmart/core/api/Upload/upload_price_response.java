package vn.gpay.gsmart.core.api.Upload;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.pcontract_price.PContract_Price_D;

public class upload_price_response extends ResponseBase {
	public List<PContract_Price_D> data;
}
