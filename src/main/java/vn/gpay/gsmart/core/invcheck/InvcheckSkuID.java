package vn.gpay.gsmart.core.invcheck;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class InvcheckSkuID implements Serializable {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 3395630602705643728L;

	@Column(name = "orgid_link")
    private Long orgid_link;
    
    @Column(name = "invcheckid_link")
    private Long invcheckid_link;
    
    @Column(name = "skuid_link")
    private Long skuid_link;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((invcheckid_link == null) ? 0 : invcheckid_link.hashCode());
		result = prime * result + ((orgid_link == null) ? 0 : orgid_link.hashCode());
		result = prime * result + ((skuid_link == null) ? 0 : skuid_link.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InvcheckSkuID other = (InvcheckSkuID) obj;
		if (invcheckid_link == null) {
			if (other.invcheckid_link != null)
				return false;
		} else if (!invcheckid_link.equals(other.invcheckid_link))
			return false;
		if (orgid_link == null) {
			if (other.orgid_link != null)
				return false;
		} else if (!orgid_link.equals(other.orgid_link))
			return false;
		if (skuid_link == null) {
			if (other.skuid_link != null)
				return false;
		} else if (!skuid_link.equals(other.skuid_link))
			return false;
		return true;
	}

	public Long getOrgid_link() {
		return orgid_link;
	}

	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}

	public Long getInvcheckid_link() {
		return invcheckid_link;
	}

	public void setInvcheckid_link(Long invcheckid_link) {
		this.invcheckid_link = invcheckid_link;
	}

	public Long getSkuid_link() {
		return skuid_link;
	}

	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}

    
    
}
