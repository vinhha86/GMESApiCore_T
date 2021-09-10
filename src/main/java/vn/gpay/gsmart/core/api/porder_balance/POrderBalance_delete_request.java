package vn.gpay.gsmart.core.api.porder_balance;

import java.util.List;

import vn.gpay.gsmart.core.base.RequestBase;

public class POrderBalance_delete_request extends RequestBase{
	public Long id;
	public List<Long> idList;
}
