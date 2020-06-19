package vn.gpay.gsmart.core.pcontracttype;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="pcontract_type")
@Entity
public class PContractType implements Serializable {

	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pcontract_type_generator")
	@SequenceGenerator(name="pcontract_type_generator", sequenceName = "pcontract_type_id_seq", allocationSize=1)
	protected Long id;
	@Column(name ="orgrootid_link")
	private Long orgrootid_link;
	@Column(name ="name",length =100)
	private String name;
	@Column(name ="issystemfix")
	private boolean issystemfix;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isIssystemfix() {
		return issystemfix;
	}
	public void setIssystemfix(boolean issystemfix) {
		this.issystemfix = issystemfix;
	}
	
	
}
