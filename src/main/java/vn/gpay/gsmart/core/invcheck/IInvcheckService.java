package vn.gpay.gsmart.core.invcheck;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IInvcheckService extends Operations<Invcheck>{

	public List<Invcheck> invcheck_list(Long orgid_link,String stockcode,String orgfrom_code,Date invdateto_from,Date invdateto_to,Integer status);
	
	public List<Invcheck> invcheck_getactive(Long orgcheckid_link);
	
	public List<Invcheck> invcheck_getbycode(String invcheckcode);
	
	public void invcheck_deactive(Long invcheckid);
}
