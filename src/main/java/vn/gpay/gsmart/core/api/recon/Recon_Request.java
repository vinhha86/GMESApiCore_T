package vn.gpay.gsmart.core.api.recon;

public class Recon_Request {
	public Long pcontractid_link;
	public Long pcontract_poid_link;
	public Long porderid_link;
	public String list_productid;
	public String list_materialtypeid;
	public Integer balance_limit = 0; //0-Tinh het; 1-Chi tinh nguyen lieu; 2-Chi tinh phu lieu	
}
