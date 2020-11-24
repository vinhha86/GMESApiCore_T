package vn.gpay.gsmart.core.porderprocessingns;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPorderProcessingNsService extends Operations<PorderProcessingNs> {

	Integer getTotalWTime_ByPorder(Long pordergrantid_link, Long personnelid_link, Date date_from, Date date_to);

	List<PorderProcessingNs> getByPersonnelDateAndShift(
			Long porderid_link,
			Long pordergrantid_link,
			Long personnelid_link,
			Date processingdate,
			Integer shifttypeid_link
			);
	
	List<PorderProcessingNs> getByPersonnelDateAndShiftAndPOrderSewingCost(
			Long porderid_link,
			Long pordergrantid_link,
			Long personnelid_link,
			Date processingdate,
			Integer shifttypeid_link,
			Long pordersewingcostid_link
			);
}
