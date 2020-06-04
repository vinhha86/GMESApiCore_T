package vn.gpay.gsmart.core.warehouse;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class WarehouseId implements Serializable {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 3395630602705643728L;
	
	@Column(name ="epc",length=50)
    private String epc;
	
	@Column(name ="orgid_link")
    private Long orgid_link;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WarehouseId other = (WarehouseId) obj;
		if (epc == null) {
			if (other.epc != null)
				return false;
		} else if (!epc.equals(other.epc))
			return false;
		if (orgid_link == null) {
			if (other.orgid_link != null)
				return false;
		} else if (!orgid_link.equals(other.orgid_link))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((epc == null) ? 0 : epc.hashCode());
		result = prime * result + ((orgid_link == null) ? 0 : orgid_link.hashCode());
		return result;
	}

	public String getEpc() {
		return epc;
	}

	public void setEpc(String epc) {
		this.epc = epc;
	}

	public Long getOrgid_link() {
		return orgid_link;
	}

	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}
	
	
	
	

}
