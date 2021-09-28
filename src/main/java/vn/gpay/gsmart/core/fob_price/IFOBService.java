package vn.gpay.gsmart.core.fob_price;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IFOBService extends Operations<FOBPrice> {
	public List<FOBPrice> getPrice_by_orgroot(Long orgrootid_link);
	public List<FOBPrice> getAllDefault();
	List<FOBPrice> getByName(String name);
	List<FOBPrice> getByName_other(String name, Long id);
}
