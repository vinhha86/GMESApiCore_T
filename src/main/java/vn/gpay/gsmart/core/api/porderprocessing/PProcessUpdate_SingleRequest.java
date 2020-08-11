package vn.gpay.gsmart.core.api.porderprocessing;

import java.util.Date;
import vn.gpay.gsmart.core.base.RequestBase;

public class PProcessUpdate_SingleRequest extends RequestBase{
	public Date processingdate;
	public Long id;
	public Long porderid_link;
	public Long pordergrantid_link;
	public String dataIndex;
	public Integer newValue;
	public Integer newSumValue;
	public String commentValue;
}
