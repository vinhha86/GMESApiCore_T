package vn.gpay.gsmart.core.contractbuyer;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Specifications;

import vn.gpay.gsmart.core.api.contractbuyer.ContractBuyer_getbypaging_request;
import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.utils.GPAYDateFormat;

@Service
public class ContractBuyerService extends AbstractService<ContractBuyer> implements IContractBuyerService  {
	
	@Autowired IContractBuyerRepository repo;

	@Override
	protected JpaRepository<ContractBuyer, Long> getRepository() {
		return repo;
	}

	@Override
	public List<ContractBuyer> getByContractCode(String contract_code) {
		return repo.getByContractCode(contract_code);
	}

	@Override
	public List<ContractBuyer> getOtherContractBuyerByContractCode(String contract_code, long id) {
		return repo.getOtherContractBuyerByContractCode(contract_code, id);
	}

	@Override
	public List<Integer> getAllYears() {
		return repo.getAllYears();
	}
	
	@Override
	public List<ContractBuyer> getContractBuyerBySearch(ContractBuyer_getbypaging_request entity) {

		Specification<ContractBuyer> specification = Specifications.<ContractBuyer>and()
				.eq(entity.buyerid_link > 0, "buyerid_link", entity.buyerid_link)
				.eq(entity.vendorid_link > 0, "vendorid_link", entity.vendorid_link)
				.ge(Objects.nonNull(entity.contract_datefrom),"contract_date",GPAYDateFormat.atStartOfDay(entity.contract_datefrom))
                .le(Objects.nonNull(entity.contract_dateto),"contract_date",GPAYDateFormat.atEndOfDay(entity.contract_dateto))
                .eq(Objects.nonNull(entity.contract_year), "contract_year", entity.contract_year)
				.build();
		
		List<ContractBuyer> list = repo.findAll(specification);
		Comparator<ContractBuyer> compareByContractCode = (ContractBuyer c1, ContractBuyer c2) -> c1.getContract_code().compareTo( c2.getContract_code());
		Collections.sort(list, compareByContractCode);
		
		return list;
	}

}
