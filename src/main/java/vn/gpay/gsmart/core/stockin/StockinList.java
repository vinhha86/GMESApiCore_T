package vn.gpay.gsmart.core.stockin;

import java.util.Date;

public class StockinList {
	public String stockincode;
	public Date stockindate;
	public Long orgfrom_code;
	public String orgfrom_name;
	public Long stockcode;
	public String shipperson;
	public Float totalpackage;
	public String productcode;
	public StockinList(Object[] columns) {
	 	this.stockincode = (String) columns[0];
        this.stockindate = (Date) columns[1];
        this.orgfrom_code = (Long) columns[2];
        this.orgfrom_name = (String) columns[3];
        this.stockcode = (Long) columns[4];
        this.shipperson = (String) columns[5];
        this.totalpackage = (Float) columns[6];
        this.productcode = (String) columns[7];
	}
}
