package vn.gpay.gsmart.core.contractbuyer;

import java.util.List;

import vn.gpay.gsmart.core.api.contractbuyer.ContractBuyer_getbypaging_request;
import vn.gpay.gsmart.core.base.Operations;

public interface IContractBuyerService extends Operations<ContractBuyer>{
	List<ContractBuyer> getByContractCode(String contract_code);
	List<ContractBuyer> getOtherContractBuyerByContractCode(String contract_code, long id);
	List<Integer> getAllYears();
	List<ContractBuyer> getContractBuyerBySearch(ContractBuyer_getbypaging_request entity);
	List<ContractBuyer> getByBuyer(long buyerid_link);
}
