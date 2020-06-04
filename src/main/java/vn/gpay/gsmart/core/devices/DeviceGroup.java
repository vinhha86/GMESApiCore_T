package vn.gpay.gsmart.core.devices;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Table(name="device_group")
@Entity
public class DeviceGroup {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "device_group_generator")
	@SequenceGenerator(name="devices_generator", sequenceName = "device_group_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name ="name",length=200)
    private String name;

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
}
