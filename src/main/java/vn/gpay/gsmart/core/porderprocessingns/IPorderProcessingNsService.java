package vn.gpay.gsmart.core.porderprocessingns;

import java.util.Date;

import vn.gpay.gsmart.core.base.Operations;

public interface IPorderProcessingNsService extends Operations<PorderProcessingNs> {

	Integer getTotalWTime_ByPorder(Long pordergrantid_link, Long personnelid_link, Date date_from, Date date_to);

}
