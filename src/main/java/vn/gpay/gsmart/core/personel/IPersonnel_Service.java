package vn.gpay.gsmart.core.personel;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPersonnel_Service extends Operations<Personel> {
	List<Personel> getby_orgmanager(Long orgmanagerid_link, long orgrootid_link);
	List<Personel> getby_org(Long orgid_link, long orgrootid_link);
	List<Personel> getByNotRegister();
}
