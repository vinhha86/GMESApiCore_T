package vn.gpay.gsmart.core.pcontract;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPContractRepository extends JpaRepository<PContract, Long>, JpaSpecificationExecutor<PContract> {
	@Query(value = "select c from PContract c " + "where c.orgrootid_link = :orgrootid_link " + "and c.status = 1 "
			+ "and contractcode = :contractcode " + "and id != :pcontractid_link")
	public List<PContract> get_byorgrootid_link_and_contractcode(@Param("orgrootid_link") final Long orgrootid_link,
			@Param("pcontractid_link") final Long pcontractid_link, @Param("contractcode") final String contractcode);

	@Query(value = "select c from PContract c " + "inner join PContract_PO b on b.pcontractid_link = c.id "
			+ "inner join Product a on b.productid_link = a.id " + "where b.po_buyer like :po_buyer "
			+ "and a.buyercode like :buyercode ")
	public List<PContract> getBySearchIgnoreCase(@Param("po_buyer") final String po_buyer,
			@Param("buyercode") final String buyercode);

	@Query(value = "select c from PContract c "
			+ "where lower(c.contractcode) like lower(concat('%',:contractcode,'%')) ")
	public List<PContract> findByContractcode(@Param("contractcode") final String contractcode);

	@Query(value = "select c from PContract c " + "where lower(c.contractcode) = lower(:contractcode) ")
	public List<PContract> findByExactContractcode(@Param("contractcode") final String contractcode);

	@Query(value = "select distinct a.productid_link from PContractProduct a " + "where a.productid_link not in ("
			+ "select b.productid_link from PContractProductBom2 b where b.pcontractid_link = :pcontractid_link)"
			+ " and a.pcontractid_link = :pcontractid_link ")
	public List<Long> GetProductNotBom(@Param("pcontractid_link") final Long pcontractid_link);

	@Query(value = "select c from PContract c " + "inner join ContractBuyer a on a.id = c.contractbuyerid_link "
			+ "where contract_year = :year ")
	public List<PContract> getPContractByYear(@Param("year") final Integer year);
	
	@Query(value = "select a from PContract a " 
			+ "inner join PContractProduct b on b.pcontractid_link = a.id "
			+ "where b.productid_link = :productid_link "
			)
	public List<PContract> getByProduct(
			@Param("productid_link") final Long productid_link
			);

	@Query(value = "select distinct a from PContract a " 
			+ "inner join PContractProductBom2 b on b.pcontractid_link = a.id "
			+ "where b.materialid_link in :skuid_list "
			)
	public List<PContract> getByBom_Sku(
			@Param("skuid_list") final List<Long> skuid_list
			);
}
