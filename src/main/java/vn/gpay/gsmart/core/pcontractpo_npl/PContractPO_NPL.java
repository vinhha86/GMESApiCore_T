package vn.gpay.gsmart.core.pcontractpo_npl;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.gsmart.core.pcontract_po.PContract_PO;

@Table(name="pcontractpo_npl")
@Entity
public class PContractPO_NPL implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pcontractpo_npl_generator")
	@SequenceGenerator(name="pcontractpo_npl_generator", sequenceName = "pcontractpo_npl_id_seq", allocationSize=1)
	private Long id;
	private Long pcontractid_link;
	private Long pcontract_poid_link;
	private Long npl_skuid_link;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="pcontract_poid_link",insertable=false,updatable =false)
    private PContract_PO po;
	
	@Transient
	public String getPO_Buyer() {
		return po.getPo_buyer();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPcontractid_link() {
		return pcontractid_link;
	}
	public void setPcontractid_link(Long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}
	public Long getPcontract_poid_link() {
		return pcontract_poid_link;
	}
	public void setPcontract_poid_link(Long pcontract_poid_link) {
		this.pcontract_poid_link = pcontract_poid_link;
	}
	public Long getNpl_skuid_link() {
		return npl_skuid_link;
	}
	public void setNpl_skuid_link(Long npl_skuid_link) {
		this.npl_skuid_link = npl_skuid_link;
	}
	
	
}
