package vn.gpay.gsmart.core.api.invoice;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.packinglist.LotNumber;

public class invoice_getlotnumber_response extends ResponseBase {
	public List<LotNumber> data;
}
