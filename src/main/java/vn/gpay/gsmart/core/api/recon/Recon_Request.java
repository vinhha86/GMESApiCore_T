package vn.gpay.gsmart.core.api.recon;

public class Recon_Request {
	public Long pcontractid_link;
	public Long pcontract_poid_link;
	public Long porderid_link;
	public String list_productid;
	public Long materialid_link = null;
	public String list_materialtypeid;
	public Integer balance_limit = 0; //0-Tinh het; 1-Chi tinh nguyen lieu; 2-Chi tinh phu lieu	
	public Integer recon_type = 0;//0-Quyet toan can doi; 1-Quyet toan Hai quan; 2-Quyet toan san xuat (noi bo)	
}
