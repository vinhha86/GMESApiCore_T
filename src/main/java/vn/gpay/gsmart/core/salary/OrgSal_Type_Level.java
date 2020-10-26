package vn.gpay.gsmart.core.salary;

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


@Table(name="org_sal_type_level")
@Entity
public class OrgSal_Type_Level implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "org_sal_type_level_generator")
	@SequenceGenerator(name="org_sal_type_level_generator", sequenceName = "org_sal_type_level_id_seq", allocationSize=1)
	private Long id;
	
	private Long orgrootid_link;
	private Long saltypeid_link;
	private Long sallevelid_link;
	private Float salratio;
	private Integer salamount;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="saltypeid_link",insertable=false,updatable =false)
    private OrgSal_Type saltype;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="sallevelid_link",insertable=false,updatable =false)
    private OrgSal_Level sallevel;
	
	@Transient
	public String getSaltype_code() {
		if(saltype!=null) {
			return saltype.getCode();
		}
		return "";
	}
	@Transient
	public String getSaltype_name() {
		if(saltype!=null) {
			return saltype.getName();
		}
		return "";
	}
	@Transient
	public String getSallevel_code() {
		if(sallevel!=null) {
			return sallevel.getCode();
		}
		return "";
	}
	@Transient
	public String getSallevel_name() {
		if(sallevel!=null) {
			return sallevel.getName();
		}
		return "";
	}
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
	public Long getSaltypeid_link() {
		return saltypeid_link;
	}
	public void setSaltypeid_link(Long saltypeid_link) {
		this.saltypeid_link = saltypeid_link;
	}
	public Long getSallevelid_link() {
		return sallevelid_link;
	}
	public void setSallevelid_link(Long sallevelid_link) {
		this.sallevelid_link = sallevelid_link;
	}
	public Float getSalratio() {
		return salratio;
	}
	public void setSalratio(Float salratio) {
		this.salratio = salratio;
	}
	public Integer getSalamount() {
		return salamount;
	}
	public void setSalamount(Integer salamount) {
		this.salamount = salamount;
	}

}
