package vn.gpay.gsmart.core.cutplan_processing;

import java.util.Date;

import org.springframework.data.domain.Page;

import vn.gpay.gsmart.core.base.Operations;

public interface ICutplanProcessingService extends Operations<CutplanProcessing> {
	public Page<CutplanProcessing> cutplanProcessing_page(Date stockindate_from, Date stockindate_to, int limit, int page);
}
