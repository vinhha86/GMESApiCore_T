package vn.gpay.gsmart.core.api.balance;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
public class PContract_PO_Data implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Long id;
	public Long orgrootid_link;
	public Long pcontractid_link;
	public String code;
	public String po_buyer;
	public String po_vendor;
	public Long productid_link;
	public Integer po_quantity;
	public Long unitid_link;
	public Date shipdate;
	public Date matdate;
	public Float actual_quantity;
	public Date actual_shipdate;
	public Float price_cmp;
	public Float price_fob;
	public Float price_sweingtarget;
	public Float price_sweingfact;
	public Float price_add;
	public Float price_commission;
	public Float salaryfund;
	public Long currencyid_link;
	public Float exchangerate;
	public Date productiondate;
	public String packingnotice;
	public Long qcorgid_link;
	public String qcorgname;
	public Integer etm_from;
	public Integer etm_to;
	public Integer etm_avr;
	public Long usercreatedid_link;
	public Date datecreated;
	public Integer status;
	public Integer productiondays;
	public Integer productiondays_ns;
	public Long orgmerchandiseid_link;
	public Long merchandiserid_link;
	public Long parentpoid_link;
	public Boolean is_tbd;
	public Float sewtarget_percent;
	public Long portfromid_link;
	public Long porttoid_link;
	public Boolean isauto_calculate;
	public Long shipmodeid_link;
	public Date date_importdata;
	public Integer plan_productivity;
	public Float plan_linerequired;
	public Integer po_typeid_link;	
	List<PContractProductSKU_SUM> pcontractProductSKUs;
}
