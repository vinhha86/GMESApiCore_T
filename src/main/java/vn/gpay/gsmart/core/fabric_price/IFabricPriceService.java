package vn.gpay.gsmart.core.fabric_price;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IFabricPriceService extends Operations<FabricPrice>{

	public List<FabricPrice> getByMaterial(Long materialid_link);
}
