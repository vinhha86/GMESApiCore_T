package vn.gpay.gsmart.core.api.invoice;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.packinglist.PackingList;

public class invoice_getpkl_bylotnumber_response extends ResponseBase{
	public List<PackingList> data;
}
