package vn.gpay.gsmart.core.salary;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Table(name="org_sal_type_laborlevel")
@Entity
public class OrgSal_Type_LaborLevel implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "org_sal_type_laborlevel_generator")
	@SequenceGenerator(name="org_sal_type_laborlevel_generator", sequenceName = "org_sal_type_laborlevel_id_seq", allocationSize=1)
	private Long id;
	
	private Long saltypeid_link;
	private Long laborlevelid_link;
	private Long salgroupid_link;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSaltypeid_link() {
		return saltypeid_link;
	}
	public void setSaltypeid_link(Long saltypeid_link) {
		this.saltypeid_link = saltypeid_link;
	}
	public Long getLaborlevelid_link() {
		return laborlevelid_link;
	}
	public void setLaborlevelid_link(Long laborlevelid_link) {
		this.laborlevelid_link = laborlevelid_link;
	}
	public Long getSalgroupid_link() {
		return salgroupid_link;
	}
	public void setSalgroupid_link(Long salgroupid_link) {
		this.salgroupid_link = salgroupid_link;
	}

}
