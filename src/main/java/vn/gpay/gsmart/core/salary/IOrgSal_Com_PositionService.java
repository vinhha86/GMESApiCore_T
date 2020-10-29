package vn.gpay.gsmart.core.salary;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IOrgSal_Com_PositionService extends Operations<OrgSal_Com_Position> {

	List<OrgSal_Com_Position> getall_bysalcom_position(Long salcomid_link, Long positionid_link);

	List<OrgSal_Com_Position> getall_bysalcom(Long salcomid_link);

}
