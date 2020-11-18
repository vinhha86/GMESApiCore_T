package vn.gpay.gsmart.core.contractbuyer;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IContractBuyerDService extends Operations<ContractBuyerD>{
	List<ContractBuyerD> getByContractBuyerIdAndBuyerId(Long contractbuyerid_link, Long buyerid_link);
	
	List<ContractBuyerD> getByContractBuyerId(Long contractbuyerid_link);
}
