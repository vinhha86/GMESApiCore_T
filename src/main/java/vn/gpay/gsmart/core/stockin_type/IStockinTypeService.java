package vn.gpay.gsmart.core.stockin_type;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IStockinTypeService extends Operations<StockinType> {
	public List<StockinType> findType(Long typeFrom, Long typeTo);
}
