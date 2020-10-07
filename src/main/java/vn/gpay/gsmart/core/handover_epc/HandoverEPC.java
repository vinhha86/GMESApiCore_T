package vn.gpay.gsmart.core.handover_epc;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="handover_epc")
@Entity
public class HandoverEPC implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "handover_epc_generator")
	@SequenceGenerator(name="handover_epc_generator", sequenceName = "handover_epc_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name ="orgrootid_link")
	private Long orgrootid_link;
	
	@Column(name ="handoverid_link")
	private Long handoverid_link;
	
	@Column(name ="handoverproductid_link")
	private Long handoverproductid_link;
	
	@Column(name ="handoverskuid_link")
	private Long handoverskuid_link;
	
	@Column(name ="epc", length =50)
	private String epc;
	
	@Column(name ="status")
	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrgrootid_link() {
		return orgrootid_link;
	}

	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}

	public Long getHandoverid_link() {
		return handoverid_link;
	}

	public void setHandoverid_link(Long handoverid_link) {
		this.handoverid_link = handoverid_link;
	}

	public Long getHandoverproductid_link() {
		return handoverproductid_link;
	}

	public void setHandoverproductid_link(Long handoverproductid_link) {
		this.handoverproductid_link = handoverproductid_link;
	}

	public Long getHandoverskuid_link() {
		return handoverskuid_link;
	}

	public void setHandoverskuid_link(Long handoverskuid_link) {
		this.handoverskuid_link = handoverskuid_link;
	}

	public String getEpc() {
		return epc;
	}

	public void setEpc(String epc) {
		this.epc = epc;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
