package vn.gpay.gsmart.core.api.holiday;

import vn.gpay.gsmart.core.base.RequestBase;

public class Holiday_create_request extends RequestBase {
	public Long startTime;
	public Long endTime;
	public String comment;
}
