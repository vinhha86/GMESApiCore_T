package vn.gpay.gsmart.core.handover;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="handover_type")
@Entity
public class HandoverType implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "handover_type_generator")
//	@SequenceGenerator(name="handover_type_generator", sequenceName = "handover_type_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name ="name",length =100)
	private String name;
	
	@Column(name ="in_out")
	private Integer in_out;
	
	@Column(name ="orgtype",length =50)
	private String orgtype;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIn_out() {
		return in_out;
	}

	public void setIn_out(Integer in_out) {
		this.in_out = in_out;
	}

	public String getOrgtype() {
		return orgtype;
	}

	public void setOrgtype(String orgtype) {
		this.orgtype = orgtype;
	}
	
	
}
