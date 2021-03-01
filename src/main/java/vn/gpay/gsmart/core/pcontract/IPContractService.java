package vn.gpay.gsmart.core.pcontract;

import java.util.List;

import org.springframework.data.domain.Page;

import vn.gpay.gsmart.core.api.pcontract.PContract_getbypaging_request;
import vn.gpay.gsmart.core.api.pcontract.PContract_getbysearch_request;
import vn.gpay.gsmart.core.base.Operations;

public interface IPContractService extends Operations<PContract> {
	public Page<PContract> getall_by_orgrootid_paging(Long orgrootid_link, PContract_getbypaging_request request);

	public List<PContract> getalllist_by_orgrootid_paging(Long orgrootid_link, PContract_getbypaging_request request);

	public List<PContract> getby_code(long orgrootid_link, String contractcode, long pcontractid_link);

	public long getby_buyer_merchandiser(long orgrootid_link, long orgbuyerid_link, long merchandiserid_link);

	List<PContract> getBySearch(PContract_getbysearch_request entity);

	List<PContract> getBySearch_PosList(PContract_getbysearch_request entity, List<Long> pos, List<Long> product);

	public List<PContract> findByContractcode(String contractcode);
	
	public List<PContract> findByExactContractcode(String contractcode);
}
