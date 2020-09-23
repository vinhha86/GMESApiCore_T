package vn.gpay.gsmart.core.category;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IShipModeService extends Operations<ShipMode>{
	public List<ShipMode> getbyname(String name);
}
