package vn.gpay.gsmart.core.personel;

public class Personnel_inout_request{

	/**
	 * 
	 */
	private Long id;
	private String personnel_code;	//Ma nhan vien gui xe
	private String time_in; //giờ đi xe vào
	private String time_out; //giờ lấy xe về
	private String bike_number; //biển số xe trong trường hợp không khớp với biển số trong hồ sơ
	private Long usercheck_checkout; //id tài khoản của bảo về đăng nhập
	private String personnel_code_out; //Ma nhân viên lay xe ra
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTime_in() {
		return time_in;
	}
	public void setTime_in(String time_in) {
		this.time_in = time_in;
	}
	public String getTime_out() {
		return time_out;
	}
	public void setTime_out(String time_out) {
		this.time_out = time_out;
	}
	public Long getUsercheck_checkout() {
		return usercheck_checkout;
	}
	public void setUsercheck_checkout(Long usercheck_checkout) {
		this.usercheck_checkout = usercheck_checkout;
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
	public String getPersonnel_code_out() {
		return personnel_code_out;
	}
	public void setPersonnel_code_out(String personnel_code_out) {
		this.personnel_code_out = personnel_code_out;
	}
	
	
}
