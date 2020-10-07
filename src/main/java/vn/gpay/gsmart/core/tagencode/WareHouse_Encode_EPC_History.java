package vn.gpay.gsmart.core.tagencode;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="warehouse_encode_epc_history")
@Entity
public class WareHouse_Encode_EPC_History implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "warehouse_encode_epc_history_generator")
	@SequenceGenerator(name="warehouse_encode_epc_history_generator", sequenceName = "warehouse_encode_epc_history_id_seq", allocationSize=1)
	protected Long id;
		
	private Long orgrootid_link;
	
	private Long orgencodeid_link;
	
	private String epc;
	
	private String oldepc;
	
	private String tid;
	
	private Long skuid_link;
	
	private Long deviceid_link;
	
	private Integer status;
	
	private Long usercreateid_link;
	
	private Date timecreate;
	
	private Long lastuserupdateid_link;
	
	private Date lasttimeupdate;
	
	private Long warehouse_encodeid_link;
	
	private Long warehouse_encodedid_link;
	
	private String warehouse_epc;
	
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

	public Long getOrgencodeid_link() {
		return orgencodeid_link;
	}

	public void setOrgencodeid_link(Long orgencodeid_link) {
		this.orgencodeid_link = orgencodeid_link;
	}

	public String getEpc() {
		return epc;
	}

	public void setEpc(String epc) {
		this.epc = epc;
	}

	public String getOldepc() {
		return oldepc;
	}

	public void setOldepc(String oldepc) {
		this.oldepc = oldepc;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public Long getSkuid_link() {
		return skuid_link;
	}

	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}

	public Long getDeviceid_link() {
		return deviceid_link;
	}

	public void setDeviceid_link(Long deviceid_link) {
		this.deviceid_link = deviceid_link;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getUsercreateid_link() {
		return usercreateid_link;
	}

	public void setUsercreateid_link(Long usercreateid_link) {
		this.usercreateid_link = usercreateid_link;
	}

	public Date getTimecreate() {
		return timecreate;
	}

	public void setTimecreate(Date timecreate) {
		this.timecreate = timecreate;
	}

	public Long getLastuserupdateid_link() {
		return lastuserupdateid_link;
	}

	public void setLastuserupdateid_link(Long lastuserupdateid_link) {
		this.lastuserupdateid_link = lastuserupdateid_link;
	}

	public Date getLasttimeupdate() {
		return lasttimeupdate;
	}

	public void setLasttimeupdate(Date lasttimeupdate) {
		this.lasttimeupdate = lasttimeupdate;
	}

	public Long getWarehouse_encodeid_link() {
		return warehouse_encodeid_link;
	}

	public void setWarehouse_encodeid_link(Long warehouse_encodeid_link) {
		this.warehouse_encodeid_link = warehouse_encodeid_link;
	}

	public Long getWarehouse_encodedid_link() {
		return warehouse_encodedid_link;
	}

	public void setWarehouse_encodedid_link(Long warehouse_encodedid_link) {
		this.warehouse_encodedid_link = warehouse_encodedid_link;
	}

	public String getWarehouse_epc() {
		return warehouse_epc;
	}

	public void setWarehouse_epc(String warehouse_epc) {
		this.warehouse_epc = warehouse_epc;
	}

}
