package vn.gpay.gsmart.core.timesheetinout;

public class TimeSheetInOut {
	private String device_serial;
    private String personel_code;
    private int verify_type;
    private String time;
    private String fullname;
    
    

    public String getDevice_serial() {
        return device_serial;
    }

    public void setDevice_serial(String device_serial) {
        this.device_serial = device_serial;
    }

    public String getPersonel_code() {
        return personel_code;
    }

    public void setPersonel_code(String personel_code) {
        this.personel_code = personel_code;
    }

    public int getVerify_type() {
        return verify_type;
    }

    public void setVerify_type(int verify_type) {
        this.verify_type = verify_type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
}
