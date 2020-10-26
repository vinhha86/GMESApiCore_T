package vn.gpay.gsmart.core.handover_sku;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IHandoverSKUService extends Operations<HandoverSKU>{
	public List<HandoverSKU> getByHandoverId(Long handoverid_link, Long productid_link);
	public List<HandoverSKU> getByHandoverId(Long handoverid_link);
	public List<HandoverSKU> getByHandoverIdAndProductId(Long handoverid_link, Long handoverproductid_link);
}
