package vn.gpay.gsmart.core.personnel_position;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPersonnel_Position_Service extends Operations<Personnel_Position>{
	public List<Personnel_Position> getPersonnel_Position();
	public Personnel_Position getByName_Code(String name,String code);
}
