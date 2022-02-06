package vn.gpay.gsmart.core.pcontract_price;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPContract_Price_DRepository
		extends JpaRepository<PContract_Price_D, Long>, JpaSpecificationExecutor<PContract_Price_D> {
	@Query(value = "select c from PContract_Price_D c " + "where pcontract_poid_link = :pcontract_poid_link")
	public List<PContract_Price_D> getPrice_D_ByPO(@Param("pcontract_poid_link") final Long pcontract_poid_link);

	@Query(value = "select c from PContract_Price_D c " + "where pcontract_poid_link = :pcontract_poid_link "
			+ "and c.productid_link =:productid_link")
	public List<PContract_Price_D> getPrice_D_ByPO_Product(@Param("pcontract_poid_link") final Long pcontract_poid_link,
			@Param("productid_link") final Long productid_link);

	@Query(value = "select c from PContract_Price_D c " + "where pcontractpriceid_link = :pcontractpriceid_link "
			+ "and fobpriceid_link = :fobpriceid_link ")
	public List<PContract_Price_D> getPrice_D_ByFobPriceAndPContractPrice(
			@Param("pcontractpriceid_link") final Long pcontractpriceid_link,
			@Param("fobpriceid_link") final Long fobpriceid_link);

	@Query(value = "select c from PContract_Price_D c " + "inner join FOBPrice a on a.id = c.fobpriceid_link "
			+ "where pcontractpriceid_link = :pcontractpriceid_link " + "and name = :fobprice_name ")
	public List<PContract_Price_D> getPrice_D_ByFobPriceNameAndPContractPrice(
			@Param("pcontractpriceid_link") final Long pcontractpriceid_link,
			@Param("fobprice_name") final String fobprice_name);

	@Query(value = "select c from PContract_Price_D c " + "where pcontractpriceid_link = :pcontractpriceid_link ")
	public List<PContract_Price_D> getPrice_D_ByPContractPrice(
			@Param("pcontractpriceid_link") final Long pcontractpriceid_link);
	
	@Query(value = "select distinct b.name,c.code from PContract_Price_D a "
			+ "inner join FOBPrice b on a.fobpriceid_link = b.id "
			+ "left join Org c on a.providerid_link = c.id "
			+ "where a.pcontractid_link = :pcontractid_link and a.pcontract_poid_link = :pcontract_poid_link and a.isfob=true")
	public List<Object[]> getPrice_FOB(
			@Param("pcontractid_link") final Long pcontractid_link,
			@Param("pcontract_poid_link") final Long pcontract_poid_link);
}
