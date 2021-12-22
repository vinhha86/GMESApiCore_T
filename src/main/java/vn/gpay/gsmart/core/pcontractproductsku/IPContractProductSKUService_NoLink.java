package vn.gpay.gsmart.core.pcontractproductsku;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPContractProductSKUService_NoLink extends Operations<PContractProductSKU_NoLink> {
	List<PContractProductSKU_NoLink> getsumsku_bypcontract(long pcontractid_link);
	List<PContractProductSKU_NoLink> getlistsku_bypcontract_nolink(long orgrootid_link, long pcontractid_link);
}
