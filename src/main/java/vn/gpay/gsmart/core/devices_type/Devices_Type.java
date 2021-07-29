package vn.gpay.gsmart.core.devices_type;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="devices_type")
@Entity
public class Devices_Type implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "devices_type_generator")
	@SequenceGenerator(name="devices_type_generator", sequenceName = "devices_type_id_seq", allocationSize=1)
	private Long id;
	private String code;
	private String name;
	private Boolean is_rfid;
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
	public Boolean getIs_rfid() {
		return is_rfid;
	}
	public void setIs_rfid(Boolean is_rfid) {
		this.is_rfid = is_rfid;
	}

}
