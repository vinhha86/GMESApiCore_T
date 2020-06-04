package vn.gpay.gsmart.core.api.porder;

import java.util.Date;

public class PProcessBalanceStatusRequest{
	public Long porderid_link;
	public Long pprocesingid;
	public String ordercode;
	public Integer balance_status;
	public Date balance_date;
	public Float balance_rate;
}
