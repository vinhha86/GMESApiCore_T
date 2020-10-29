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

import vn.gpay.gsmart.core.position.Position;


@Table(name="org_sal_com_position")
@Entity
public class OrgSal_Com_Position implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "org_sal_com_position_generator")
	@SequenceGenerator(name="org_sal_com_position_generator", sequenceName = "org_sal_com_position_id_seq", allocationSize=1)
	private Long id;

	private Long salcomid_link;
	private Long positionid_link;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="positionid_link",insertable=false,updatable =false)
    private Position position;
	
	@Transient
	public String getPosition_name() {
		if(position!=null) {
			return position.getName();
		}
		return "";
	}
	
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
	public Long getPositionid_link() {
		return positionid_link;
	}
	public void setPositionid_link(Long positionid_link) {
		this.positionid_link = positionid_link;
	}

	
}
