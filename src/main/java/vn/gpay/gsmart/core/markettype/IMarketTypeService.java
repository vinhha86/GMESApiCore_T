package vn.gpay.gsmart.core.markettype;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IMarketTypeService extends Operations<MarketType> {
	public List<MarketType> getMarket_by_Status(int status);
}
