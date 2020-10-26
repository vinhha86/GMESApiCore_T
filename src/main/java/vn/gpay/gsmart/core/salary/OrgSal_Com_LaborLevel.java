package vn.gpay.gsmart.core.salary;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Table(name="org_sal_com_laborlevel")
@Entity
public class OrgSal_Com_LaborLevel implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "org_sal_com_laborlevel_generator")
	@SequenceGenerator(name="org_sal_com_laborlevel_generator", sequenceName = "org_sal_com_laborlevel_id_seq", allocationSize=1)
	private Long id;

	private Long salcomid_link;
	private Long laborlevelid_link;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSalcomid_link() {
		return salcomid_link;
	}
	public void setSalcomid_link(Long salcomid_link) {
		this.salcomid_link = salcomid_link;
	}
	public Long getLaborlevelid_link() {
		return laborlevelid_link;
	}
	public void setLaborlevelid_link(Long laborlevelid_link) {
		this.laborlevelid_link = laborlevelid_link;
	}
	
}
