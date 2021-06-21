package vn.gpay.gsmart.core.personel;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="personnel_inout")
@Entity
public class Personnel_inout implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personnel_inout_generator")
	@SequenceGenerator(name="personnel_inout_generator", sequenceName = "personnel_inout_id_seq", allocationSize=1)
	protected Long id;
	private Long personnelid_link_out; //Id nhân viên
	private Date time_in; //giờ đi xe vào
	private Date time_out; //giờ lấy xe về
	private String bike_number; //biển số xe trong trường hợp không khớp với biển số trong hồ sơ
	private Long usercheck_checkout; //id tài khoản của bảo về đăng nhập
	private String personnel_code;	
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getTime_in() {
		return time_in;
	}
	public void setTime_in(Date time_in) {
		this.time_in = time_in;
	}
	public Date getTime_out() {
		return time_out;
	}
	public void setTime_out(Date time_out) {
		this.time_out = time_out;
	}
	public Long getUsercheck_checkout() {
		return usercheck_checkout;
	}
	public void setUsercheck_checkout(Long usercheck_checkout) {
		this.usercheck_checkout = usercheck_checkout;
	}
	public Long getPersonnelid_link_out() {
		return personnelid_link_out;
	}
	public void setPersonnelid_link_out(Long personnelid_link_out) {
		this.personnelid_link_out = personnelid_link_out;
	}
	public String getBike_number() {
		return bike_number;
	}
	public void setBike_number(String bike_number) {
		this.bike_number = bike_number;
	}
	public String getPersonnel_code() {
		return personnel_code;
	}
	public void setPersonnel_code(String personnel_code) {
		this.personnel_code = personnel_code;
	}
	
	
}
