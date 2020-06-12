package vn.gpay.gsmart.core.sizeset;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface ISizeSetService extends Operations<SizeSet> {

	List<SizeSet> getall_byorgrootid(long orgrootid_link);

}
