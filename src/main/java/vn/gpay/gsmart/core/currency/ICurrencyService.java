package vn.gpay.gsmart.core.currency;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface ICurrencyService extends Operations<Currency>{
	public List<Currency> getAllCurrency();
}
