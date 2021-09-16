package vn.gpay.gsmart.core.personnel_type;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPersonnelType_Service extends Operations<PersonnelType> {
	public List<PersonnelType>getByName(String name);
}
