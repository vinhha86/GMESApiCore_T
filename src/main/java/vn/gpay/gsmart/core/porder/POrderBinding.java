package vn.gpay.gsmart.core.porder;

import java.util.List;

public class POrderBinding {
	private Long orgId;
	private String orgName;
	private String orgCode;
	private String statusName;
	private Integer status;
	private Long sumChuaPhanChuyen; // porder status 0
	private Long sumChuaSanXuat; // porder status 1
	private Long sum;
	
	private List<POrderBinding> porderBinding_list;
	
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
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getSum() {
		return sum;
	}
	public void setSum(Long sum) {
		this.sum = sum;
	}
	public List<POrderBinding> getPorderBinding_list() {
		return porderBinding_list;
	}
	public void setPorderBinding_list(List<POrderBinding> porderBinding_list) {
		this.porderBinding_list = porderBinding_list;
	}
	
}
