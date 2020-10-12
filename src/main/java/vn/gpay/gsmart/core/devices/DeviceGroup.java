package vn.gpay.gsmart.core.devices;

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



@Table(name="device_group")
@Entity
public class DeviceGroup implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "device_group_generator")
	@SequenceGenerator(name="device_group_generator", sequenceName = "device_group_id_seq", allocationSize=1)
//	protected Integer id;
	protected Long id;
	
	@Column(name ="name",length=200)
    private String name;
	
	@Column(name ="parentid_link")
    private Long parentid_link;
	
	@Column(name ="code",length=50)
    private String code;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="parentid_link",insertable=false,updatable =false)
    private DeviceGroup parent;
	
	@Transient
	public String getParentcode() {
		if(parent!=null) {
			return parent.getCode();
		}
		return "";
	}
	@Transient
	public String getParentname() {
		if(parent!=null) {
			return parent.getName();
		}
		return "";
	}
	
	@Transient
	public boolean checked;

	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

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

	public Long getParentid_link() {
		return parentid_link;
	}

	public void setParentid_link(Long parentid_link) {
		this.parentid_link = parentid_link;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
