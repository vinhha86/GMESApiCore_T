package vn.gpay.gsmart.core.api.stockin;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.packinglist.PackingList;

public class GetPackingListByStockinDIDOutput  extends ResponseBase{

	public List<PackingList> data;
}
