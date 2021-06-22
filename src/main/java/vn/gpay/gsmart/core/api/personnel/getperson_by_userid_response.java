package vn.gpay.gsmart.core.api.personnel;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.personel.Personnel_inout_request;

public class getperson_by_userid_response extends ResponseBase{
	public List<Personnel_inout_request> data;
}
