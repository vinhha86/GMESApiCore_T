package vn.gpay.gsmart.core.api.balance;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;

public class Balance_Response extends ResponseBase {
	public List<Balance_Product_Data> product_data;
	public List<SKUBalance_Data> data;
}
