package vn.gpay.gsmart.core.api.balance;

import java.io.Serializable;
import java.math.BigDecimal;

public class SKU_Response implements Serializable{
	private static final long serialVersionUID = 1L;
	public Long id;
	public Long orgrootid_link;
	public String code;
	public String partnercode;
	public String barcode;
	public Integer skutypeid_link;
	public String name;
	public String name_en;
	public Integer categoryid_link;
	public Integer bossid_link;
	public Integer providerid_link;
	public Integer fabricid_link;
	public Integer packingtype;
	public Long unitid_link;
	public String imgurl1;
	public String imgurl2;
	public String imgurl3;
	public String hscode;
	public String hsname;
	public Float saleprice;
	public BigDecimal discountpercent;
	public BigDecimal vatpercent;
	public Long productid_link;
	public String color_name;
	public String size_name;
}
