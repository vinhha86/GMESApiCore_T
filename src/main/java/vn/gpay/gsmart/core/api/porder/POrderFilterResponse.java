package vn.gpay.gsmart.core.api.porder;

import java.util.ArrayList;
import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.porder.POrderFilter;

public class POrderFilterResponse extends ResponseBase{
	public List<POrderFilter> data =  new ArrayList<POrderFilter>();
}
