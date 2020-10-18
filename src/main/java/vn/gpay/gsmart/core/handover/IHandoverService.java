package vn.gpay.gsmart.core.handover;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IHandoverService extends Operations<Handover>{
	public List<Handover> getByType(Long handovertypeid_link);
	public List<Handover> getByType(Long handovertypeid_link, Integer status);
	public List<Handover> getByHandoverCode(String handover_code);
	public List<Handover> getHandOverBySearch(
			Long handovertypeid_link,
			Date handover_datefrom,
			Date handover_dateto,
			Long orgid_from_link,
			Long orgid_to_link,
			Integer status
			);
}
