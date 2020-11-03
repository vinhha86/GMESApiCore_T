package vn.gpay.gsmart.core.packingtype;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IPackingTypeService extends Operations<PackingType> {

	List<PackingType> getall_byorgrootid(long orgrootid_link);
	List<PackingType> getbyname(String name, long orgrootid_link);
}
