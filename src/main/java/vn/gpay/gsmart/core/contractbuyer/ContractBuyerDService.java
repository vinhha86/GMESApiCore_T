package vn.gpay.gsmart.core.contractbuyer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class ContractBuyerDService extends AbstractService<ContractBuyerD> implements IContractBuyerDService{
	
	@Autowired IContractBuyerDRepository repo;

	@Override
	protected JpaRepository<ContractBuyerD, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<ContractBuyerD> getByContractBuyerIdAndBuyerId(Long contractbuyerid_link, Long buyerid_link) {
		// TODO Auto-generated method stub
		return repo.getByContractBuyerIdAndBuyerId(contractbuyerid_link, buyerid_link);
	}

	@Override
	public List<ContractBuyerD> getByContractBuyerId(Long contractbuyerid_link) {
		// TODO Auto-generated method stub
		return repo.getByContractBuyerId(contractbuyerid_link);
	}
	
}
