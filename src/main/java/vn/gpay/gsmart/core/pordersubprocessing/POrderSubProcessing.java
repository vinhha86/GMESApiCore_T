package vn.gpay.gsmart.core.pordersubprocessing;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
import javax.persistence.Table;
//import javax.persistence.Transient;

@Table(name="porder_subprocessing")
@Entity
public class POrderSubProcessing implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@Column(name ="orgrootid_link")
    private Long orgrootid_link;
	
	@Column(name ="porderid_link")
    private Long porderid_link;

	@Column(name ="ordercode", length =50)
    private String ordercode;

	@Column(name ="workingprocessid_link")
    private Long workingprocessid_link;
	
	@Column(name ="workingprocessname", length =100)
    private String workingprocessname;	

	@Column(name ="status")
    private Integer status;	
	
	@Column(name ="usercreatedid_link")
    private Long usercreatedid_link;	
	
	@Column(name ="timecreated")
    private Date timecreated;

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

	public Long getPorderid_link() {
		return porderid_link;
	}

	public void setPorderid_link(Long porderid_link) {
		this.porderid_link = porderid_link;
	}

	public String getOrdercode() {
		return ordercode;
	}

	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}


	public Long getWorkingprocessid_link() {
		return workingprocessid_link;
	}

	public void setWorkingprocessid_link(Long workingprocessid_link) {
		this.workingprocessid_link = workingprocessid_link;
	}

	public String getWorkingprocessname() {
		return workingprocessname;
	}

	public void setWorkingprocessname(String workingprocessname) {
		this.workingprocessname = workingprocessname;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getUsercreatedid_link() {
		return usercreatedid_link;
	}

	public void setUsercreatedid_link(Long usercreatedid_link) {
		this.usercreatedid_link = usercreatedid_link;
	}

	public Date getTimecreated() {
		return timecreated;
	}

	public void setTimecreated(Date timecreated) {
		this.timecreated = timecreated;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
