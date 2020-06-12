package vn.gpay.gsmart.core.category;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Table(name="Unit")
@Entity
public class Unit implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long id;
	
	@Column(name ="orgid_link")
    private Long orgid_link;
	
	@Column(name ="orgrootid_link")
    private Long orgrootid_link;
	
	@Column(name ="code",length=50)
    private String code;
	
	@Column(name ="name",length =100)
    private String name;
	
	@Column(name ="name_en",length =100)
    private String name_en;
	
	@Column(name ="unittype")
    private Integer unittype;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrgid_link() {
		return orgid_link;
	}

	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName_en() {
		return name_en;
	}

	public void setName_en(String name_en) {
		this.name_en = name_en;
	}

	public Long getOrgrootid_link() {
		return orgrootid_link;
	}

	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}

	public Integer getUnittype() {
		return unittype;
	}

	public void setUnittype(Integer unittype) {
		this.unittype = unittype;
	}
	
	
	
}
