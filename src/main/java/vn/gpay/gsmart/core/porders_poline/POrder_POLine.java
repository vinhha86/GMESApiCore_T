package vn.gpay.gsmart.core.porders_poline;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
