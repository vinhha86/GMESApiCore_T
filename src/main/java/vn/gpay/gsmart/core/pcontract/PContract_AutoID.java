package vn.gpay.gsmart.core.pcontract;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

@Table(name="pcontract_autoid")
@Entity
public class PContract_AutoID implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pcontract_autoid_generator")
	@SequenceGenerator(name="pcontract_autoid_generator", sequenceName = "pcontract_autoid_id_seq", allocationSize=1)
	private Long id;

	private String prefix;
    private Integer maxvalue;
    @Version
    private Integer version;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public Integer getMaxvalue() {
		return maxvalue;
	}
	public void setMaxvalue(Integer maxvalue) {
		this.maxvalue = maxvalue;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
