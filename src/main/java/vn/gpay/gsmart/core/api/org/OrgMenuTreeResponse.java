package vn.gpay.gsmart.core.api.org;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.org.OrgTree;

public class OrgMenuTreeResponse extends ResponseBase{
	public List<OrgTree> children;
}
