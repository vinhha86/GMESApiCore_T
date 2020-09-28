package vn.gpay.gsmart.core.category;

import java.io.Serializable;
import javax.persistence.Column;
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


@Table(name="Port")
@Entity
public class Port implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "port_generator")
	@SequenceGenerator(name="port_generator", sequenceName = "port_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name ="code",length=50)
    private String code;
	
	@Column(name ="name",length =100)
    private String name;
	
	@Column(name ="name_en",length=100)
    private String name_en;
	
	@Column(name ="orgid_link")
    private Long orgid_link;
	
	@Column(name ="shipmodeid_link")
    private Long shipmodeid_link;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="shipmodeid_link",insertable=false,updatable =false)
    private ShipMode shipmode;
	
	@Transient
	public String getShipModeName() {
		if(shipmode != null) {
			return shipmode.getName();
		}
		return "";
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getOrgid_link() {
		return orgid_link;
	}

	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}

	public Long getShipmodeid_link() {
		return shipmodeid_link;
	}

	public void setShipmodeid_link(Long shipmodeid_link) {
		this.shipmodeid_link = shipmodeid_link;
	}
	
	
}
