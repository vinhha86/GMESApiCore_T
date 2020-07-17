package vn.gpay.gsmart.core.api.org;

import java.util.ArrayList;
import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.org.Org;

public class get_orgreq_response extends ResponseBase {
	public List<Org> data = new ArrayList<Org>();
}
