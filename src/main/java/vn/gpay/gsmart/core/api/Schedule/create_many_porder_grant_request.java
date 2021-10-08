package vn.gpay.gsmart.core.api.Schedule;

import java.util.List;

import vn.gpay.gsmart.core.pcontract_po.PContractPO_Shipping;

public class create_many_porder_grant_request {
	public Integer productivity;
	public Long orgid_link;
	public Long orggrantid_link;
	public List<PContractPO_Shipping> list_pcontract_po;
	public Long colorid_link;
	public Long sizesetid_link;
}
