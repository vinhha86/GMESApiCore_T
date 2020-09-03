package vn.gpay.gsmart.core.api.packingtype;

import vn.gpay.gsmart.core.base.RequestBase;
import vn.gpay.gsmart.core.packingtype.PackingType;

public class PackingTypeRequest extends RequestBase{
	public PackingType data; // create, save
	public Long id; // delete
}