package vn.gpay.gsmart.core.api.Schedule;

import java.util.List;

import vn.gpay.gsmart.core.porder_grant.POrderGrant_SKU;

public class break_porder_request {
	public long porderid_link;
	public long pordergrant_id_link;
	public int quantity;
	public int producttivity;
	public int resourceid;
	public long parentid_origin;
	public int duration;
	public List<POrderGrant_SKU> data;
}
