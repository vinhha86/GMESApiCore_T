package vn.gpay.gsmart.core.api.contractbuyer;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.contractbuyer.ContractBuyer;

public class ContractBuyer_getbypaging_response extends ResponseBase {
	public List<ContractBuyer> data;
	public long totalCount;
}
