package vn.gpay.gsmart.core.api.balance;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.pcontractbomsku.PContractBOM2SKU;

public class Balance_MaterialContract_Response extends ResponseBase {
	public List<PContractBOM2SKU> data;
}
