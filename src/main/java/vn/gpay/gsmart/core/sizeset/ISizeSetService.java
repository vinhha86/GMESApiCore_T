package vn.gpay.gsmart.core.sizeset;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface ISizeSetService extends Operations<SizeSet> {

	List<SizeSet> getall_byorgrootid(long orgrootid_link);

	Long getbyname(String name);

	Long getbyname_and_po(String name, Long pcontractid_link, Long productid_link);

	Integer getMaxSortValue();

	List<SizeSet> getSizeSetByName(String name);

	List<SizeSet> getbyproduct(Long productid_link, Long attributeid_link);
}
