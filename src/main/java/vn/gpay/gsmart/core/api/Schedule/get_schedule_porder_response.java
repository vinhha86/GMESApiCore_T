package vn.gpay.gsmart.core.api.Schedule;

import vn.gpay.gsmart.core.Schedule.Schedule_rowholiday;
import vn.gpay.gsmart.core.Schedule.Schedule_rowplan;
import vn.gpay.gsmart.core.Schedule.Schedule_rowporder;
import vn.gpay.gsmart.core.base.ResponseBase;

public class get_schedule_porder_response extends ResponseBase{
	public boolean success;
	public  Schedule_rowporder events;
	public Schedule_rowplan resources;
	public Schedule_rowholiday zones;
}
