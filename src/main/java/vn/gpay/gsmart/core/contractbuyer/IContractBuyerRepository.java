package vn.gpay.gsmart.core.contractbuyer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IContractBuyerRepository extends JpaRepository<ContractBuyer, Long>, JpaSpecificationExecutor<ContractBuyer> {
	@Query(value = "select c from ContractBuyer c "
			+ "where c.contract_code = :contract_code ")
	public List<ContractBuyer> getByContractCode(@Param ("contract_code")final String contract_code);
	
	@Query(value = "select c from ContractBuyer c "
			+ "where c.contract_code = :contract_code and c.id != :id")
	public List<ContractBuyer> getOtherContractBuyerByContractCode(
			@Param ("contract_code")final String contract_code,
			@Param ("id")final long id);
	
	@Query(value = "select distinct c.contract_year from ContractBuyer c order by c.contract_year desc")
	public List<Integer> getAllYears();
	
	@Query(value = "select c from ContractBuyer c "
			+ "inner join ContractBuyerD b on b.contractbuyerid_link = c.id "
			+ "where b.buyerid_link = :buyerid_link order by c.contract_code")
	public List<ContractBuyer> getByBuyer(@Param ("buyerid_link")final long buyerid_link);
}
