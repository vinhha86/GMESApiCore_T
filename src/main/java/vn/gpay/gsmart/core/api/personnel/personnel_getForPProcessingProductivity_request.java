package vn.gpay.gsmart.core.api.personnel;

import vn.gpay.gsmart.core.base.RequestBase;
import java.util.Date;

public class personnel_getForPProcessingProductivity_request extends RequestBase{
	public Long orgid_link;
	public Integer shifttypeid_link;
	public Date workingdate;
}
