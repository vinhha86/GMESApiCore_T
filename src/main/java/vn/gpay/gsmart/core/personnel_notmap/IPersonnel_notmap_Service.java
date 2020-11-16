package vn.gpay.gsmart.core.personnel_notmap;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPersonnel_notmap_Service extends Operations<Personnel_notmap>{
	List<Personnel_notmap> getby_registercode(String code);
}
