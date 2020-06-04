package vn.gpay.gsmart.core.branch;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IBranchService extends Operations<Branch> {
	public List<Branch> getall_byorgrootid(long orgrootid_link);
}
