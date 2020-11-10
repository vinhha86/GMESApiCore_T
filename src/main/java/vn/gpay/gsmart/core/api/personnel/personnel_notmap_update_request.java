package vn.gpay.gsmart.core.api.personnel;

import vn.gpay.gsmart.core.base.RequestBase;
import vn.gpay.gsmart.core.personnel_notmap.Personnel_notmap;

public class personnel_notmap_update_request extends RequestBase{
	public Personnel_notmap data;
	public Long personnelid_link;
}
