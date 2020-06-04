package vn.gpay.gsmart.core.api.porderprocessing;

import java.util.Date;
import vn.gpay.gsmart.core.base.RequestBase;
import vn.gpay.gsmart.core.porderprocessing.POrderProcessing;

public class PProcessUpdateRequest extends RequestBase{
	public Date processingdate;
	public POrderProcessing data;
}
