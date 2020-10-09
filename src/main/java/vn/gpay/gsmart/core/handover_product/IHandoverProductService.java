package vn.gpay.gsmart.core.handover_product;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IHandoverProductService  extends Operations<HandoverProduct>{
	public List<HandoverProduct> getByHandoverId(Long handoverid_link);
}
