package vn.gpay.gsmart.core.salary;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Table(name="org_sal_com")
@Entity
public class OrgSal_Com implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "org_sal_com_generator")
	@SequenceGenerator(name="org_sal_com_generator", sequenceName = "org_sal_com_id_seq", allocationSize=1)
	private Long id;
	
	private Long orgrootid_link;
	private Long orgid_link;
	private String code;
	private String name;
	private Float comratio;
	private Integer comamount;
	private Boolean isforindividual;
	private Boolean isinsurance;
	private Integer type;
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
	public Float getComratio() {
		return comratio;
	}
	public void setComratio(Float comratio) {
		this.comratio = comratio;
	}
	public Integer getComamount() {
		return comamount;
	}
	public void setComamount(Integer comamount) {
		this.comamount = comamount;
	}
	public Boolean getIsforindividual() {
		return isforindividual;
	}
	public void setIsforindividual(Boolean isforindividual) {
		this.isforindividual = isforindividual;
	}
	public Boolean getIsinsurance() {
		return isinsurance;
	}
	public void setIsinsurance(Boolean isinsurance) {
		this.isinsurance = isinsurance;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
}
