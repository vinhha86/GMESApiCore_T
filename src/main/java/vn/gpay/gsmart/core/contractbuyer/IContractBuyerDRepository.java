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
public interface IContractBuyerDRepository extends JpaRepository<ContractBuyerD, Long>, JpaSpecificationExecutor<ContractBuyerD>{
	@Query(value = "select c from ContractBuyerD c "
			+ "where c.contractbuyerid_link = :contractbuyerid_link and c.buyerid_link = :buyerid_link")
	public List<ContractBuyerD> getByContractBuyerIdAndBuyerId(
			@Param ("contractbuyerid_link")final Long contractbuyerid_link,
			@Param ("buyerid_link")final Long buyerid_link
			);
	
	@Query(value = "select c from ContractBuyerD c "
			+ "where c.contractbuyerid_link = :contractbuyerid_link ")
	public List<ContractBuyerD> getByContractBuyerId(
			@Param ("contractbuyerid_link")final Long contractbuyerid_link
			);
}
