package vn.gpay.gsmart.core.pordersubprocessing;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPOrderSubProcessing_Service extends Operations<POrderSubProcessing>{

	List<POrderSubProcessing> findByOrderCode(String ordercode);

	List<POrderSubProcessing> findByProcessID(String ordercode, Long workingprocessid_link);

	List<POrderSubProcessing> findByOrderID(Long porderid_link);

	List<POrderSubProcessing> findByProcessID(Long porderid_link, Long workingprocessid_link);

}
