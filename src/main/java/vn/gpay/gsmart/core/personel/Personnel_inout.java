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
	private Long personnelid_link;
	private Date time_in;
	private Date time_out;
	private String bike_number_out;
	private Long usercheck_checkout;
	
	 
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPersonnelid_link() {
		return personnelid_link;
	}
	public void setPersonnelid_link(Long personnelid_link) {
		this.personnelid_link = personnelid_link;
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
	public String getBike_number_out() {
		return bike_number_out;
	}
	public void setBike_number_out(String bike_number_out) {
		this.bike_number_out = bike_number_out;
	}
	public Long getUsercheck_checkout() {
		return usercheck_checkout;
	}
	public void setUsercheck_checkout(Long usercheck_checkout) {
		this.usercheck_checkout = usercheck_checkout;
	}
	
	
}
