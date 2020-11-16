package vn.gpay.gsmart.core.porder;

public class POrderBinding {
	private Long orgId;
	private String orgName;
	private String orgCode;
	private Long sumChuaPhanChuyen; // porder status 0
	private Long sumChuaSanXuat; // porder status 1
	
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public Long getSumChuaPhanChuyen() {
		return sumChuaPhanChuyen;
	}
	public void setSumChuaPhanChuyen(Long sumChuaPhanChuyen) {
		this.sumChuaPhanChuyen = sumChuaPhanChuyen;
	}
	public Long getSumChuaSanXuat() {
		return sumChuaSanXuat;
	}
	public void setSumChuaSanXuat(Long sumChuaSanXuat) {
		this.sumChuaSanXuat = sumChuaSanXuat;
	}
	
	
}
