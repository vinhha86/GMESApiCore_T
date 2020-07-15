package vn.gpay.gsmart.core.api.Schedule;

import java.util.Date;

import vn.gpay.gsmart.core.Schedule.Schedule_porder;

public class move_porder_request {
	public long porderid_link;
	public long pordergrant_id_link;
	public long orggrant_toid_link;
	public Date startdate;
	public Date enddate;
	public Schedule_porder schedule;
}
