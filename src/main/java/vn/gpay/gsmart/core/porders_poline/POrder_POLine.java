package vn.gpay.gsmart.core.porders_poline;

import java.io.Serializable;
import java.util.Date;

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
import vn.gpay.gsmart.core.porder.POrder;

@Table(name="porders_poline")
@Entity
public class POrder_POLine implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "porders_poline_generator")
	@SequenceGenerator(name="porders_poline_generator", sequenceName = "porders_poline_id_seq", allocationSize=1)
	protected Long id;
	private Long porderid_link;
	private Long pcontract_poid_link;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="porderid_link",insertable=false,updatable =false)
	private POrder porder;
	
	@Transient
	public String getOrderCode() {
		if(porder!=null)
			return porder.getOrdercode();
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="pcontract_poid_link",insertable=false,updatable =false)
	private PContract_PO pcontract_po;
	
	@Transient
	public Date getPcontractPOShipdate() {
		if(pcontract_po!=null) {
			if(pcontract_po.getShipdate() != null) {
				return pcontract_po.getShipdate();
			}
		}
		return null;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPorderid_link() {
		return porderid_link;
	}
	public void setPorderid_link(Long porderid_link) {
		this.porderid_link = porderid_link;
	}
	public Long getPcontract_poid_link() {
		return pcontract_poid_link;
	}
	public void setPcontract_poid_link(Long pcontract_poid_link) {
		this.pcontract_poid_link = pcontract_poid_link;
	}
	
	
	
}
