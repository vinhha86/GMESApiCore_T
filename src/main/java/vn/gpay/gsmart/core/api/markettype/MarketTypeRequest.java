package vn.gpay.gsmart.core.api.markettype;

import vn.gpay.gsmart.core.base.RequestBase;
import vn.gpay.gsmart.core.markettype.MarketType;

public class MarketTypeRequest extends RequestBase{
	public MarketType data; // create, save
	public Long id; // delete
}
