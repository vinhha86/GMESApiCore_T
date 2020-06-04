package vn.gpay.gsmart.core.api.pordersubprocessing;

import java.util.List;

import vn.gpay.gsmart.core.base.RequestBase;
import vn.gpay.gsmart.core.workingprocess.WorkingProcess;

public class PSubProcessUpdateRequest extends RequestBase{
	public Long porderid_link;
	public Long pprocesingid;
	public String ordercode;
	public List<WorkingProcess> data;
}
