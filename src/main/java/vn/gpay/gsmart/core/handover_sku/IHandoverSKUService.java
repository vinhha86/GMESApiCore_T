package vn.gpay.gsmart.core.handover_sku;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IHandoverSKUService extends Operations<HandoverSKU>{
	public List<HandoverSKU> getByHandoverId(Long handoverid_link);
}
