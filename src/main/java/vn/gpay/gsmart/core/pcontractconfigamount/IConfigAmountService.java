package vn.gpay.gsmart.core.pcontractconfigamount;

import vn.gpay.gsmart.core.base.Operations;

public interface IConfigAmountService extends Operations<ConfigAmount>{
	public ConfigAmount getby_amount(int amount);
}
