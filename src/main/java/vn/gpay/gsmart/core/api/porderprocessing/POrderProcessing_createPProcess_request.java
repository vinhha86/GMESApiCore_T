package vn.gpay.gsmart.core.api.porderprocessing;

import java.util.Date;

import vn.gpay.gsmart.core.base.RequestBase;

public class POrderProcessing_createPProcess_request extends RequestBase{
	public Long porderid_link;
	public Long pordergrantid_link;
	public Date processingdate;
}
