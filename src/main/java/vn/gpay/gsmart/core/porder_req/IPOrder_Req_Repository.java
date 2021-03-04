package vn.gpay.gsmart.core.porder_req;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IPOrder_Req_Repository extends JpaRepository<POrder_Req, Long>, JpaSpecificationExecutor<POrder_Req> {
	@Query("SELECT c FROM POrder_Req c where c.pcontractid_link = :pcontractid_link")
	public List<POrder_Req> getByContract(@Param ("pcontractid_link")final Long pcontractid_link);
	
	@Query("SELECT c FROM POrder_Req c where c.pcontractid_link = :pcontractid_link and c.productid_link = :productid_link")
	public List<POrder_Req> getByContractAndProduct(@Param ("pcontractid_link")final Long pcontractid_link, @Param ("productid_link")final Long productid_link);

	@Query("SELECT c FROM POrder_Req c where c.pcontract_poid_link = :pcontract_poid_link and c.productid_link = :productid_link")
	public List<POrder_Req> getByPOAndProduct(@Param ("pcontract_poid_link")final Long pcontract_poid_link, @Param ("productid_link")final Long productid_link);
	
	@Query("SELECT c FROM POrder_Req c "
			+ "inner join PContract_PO a on c.pcontract_poid_link = a.id "
			+ "where a.parentpoid_link = :pcontract_poid_link "
			+ "and c.productid_link = :productid_link "
			+ "and c.status = 0 "
			+ "and c.granttoorgid_link = :orgid_link "
			+ "and a.po_typeid_link = 10"
			+ "order by a.shipdate asc")
	public List<POrder_Req> getByOfferAndProduct(
			@Param ("pcontract_poid_link")final Long pcontract_poid_link, 
			@Param ("productid_link")final Long productid_link,
			@Param ("orgid_link")final Long orgid_link);

	@Query("SELECT c FROM POrder_Req c where c.pcontractid_link = :pcontractid_link and c.pcontract_poid_link = :pcontract_poid_link")
	public List<POrder_Req> getByContractAndPO(@Param ("pcontractid_link")final Long pcontractid_link, @Param ("pcontract_poid_link")final Long pcontract_poid_link);
	
	@Query("SELECT c FROM POrder_Req c where c.pcontractid_link = :pcontractid_link and c.pcontract_poid_link = :pcontract_poid_link "
			+ "and c.granttoorgid_link = :granttoorgid_link "
			+ "and c.productid_link = :productid_link")
	public List<POrder_Req> getByContractAndPO_and_org(
			@Param ("pcontractid_link")final Long pcontractid_link, 
			@Param ("pcontract_poid_link")final Long pcontract_poid_link,
			@Param ("granttoorgid_link")final Long granttoorgid_link,
			@Param ("productid_link")final Long productid_link);

	@Query("SELECT c FROM POrder_Req c where c.pcontract_poid_link = :pcontract_poid_link")
	public List<POrder_Req> getByPO( @Param ("pcontract_poid_link")final Long pcontract_poid_link);
	
	@Query("SELECT c FROM POrder_Req c "
			+ "inner join PContract_PO a on c.pcontract_poid_link = a.id "
			+ "where a.parentpoid_link = :pcontract_poid_link")
	public List<POrder_Req> getByPO_Offer( @Param ("pcontract_poid_link")final Long pcontract_poid_link);
		
	@Query("SELECT c FROM POrder_Req c where c.pcontract_poid_link = :pcontract_poid_link "
			+ "and (c.granttoorgid_link = :orgid_link or 1 = :orgid_link) ")
	public List<POrder_Req> getByPO_and_org( @Param ("pcontract_poid_link")final Long pcontract_poid_link,
			 @Param ("orgid_link")final Long orgid_link);
	
	@Query("SELECT c FROM POrder_Req c where c.pcontract_poid_link = :pcontract_poid_link and c.productid_link = :productid_link and c.granttoorgid_link = :granttoorgid_link")
	public List<POrder_Req> getByOrg_PO_Product( @Param ("pcontract_poid_link")final Long pcontract_poid_link,
			@Param ("productid_link")final Long productid_link,
			@Param ("granttoorgid_link")final Long granttoorgid_link);
	
	@Query("SELECT c FROM POrder_Req c where c.pcontract_poid_link = :pcontract_poid_link and is_calculate = 'False'")
	public List<POrder_Req> getByPO_calculate( @Param ("pcontract_poid_link")final Long pcontract_poid_link);
	
	@Query("SELECT c FROM POrder_Req c where c.pcontract_poid_link = :pcontract_poid_link and c.granttoorgid_link = :orgid_link")
	public List<POrder_Req> getByPO_AndOrg( 
			@Param ("pcontract_poid_link")final Long pcontract_poid_link,
			@Param ("orgid_link")final Long orgid_link);

	@Query("SELECT c FROM POrder_Req c where c.status = :status")
	public List<POrder_Req> getByStatus(@Param ("status")final Integer status);
	
	@Query("SELECT c FROM POrder_Req c "
			+ "inner join PContract_PO a on c.pcontract_poid_link = a.id "
			+ "inner join PContract e on e.id = a.pcontractid_link "
			+ "where c.granttoorgid_link = :orgid_link "
			+ "and (e.orgvendorid_link in :vendors or :vendors is null) "
			+ "and (e.orgbuyerid_link in :buyers or :buyers is null) "
			+ "and a.status <= -1 "
			+ "and a.shipdate = "
			+ "(select min(b.shipdate) from POrder_Req d "
			+ "inner join PContract_PO b on d.pcontract_poid_link = b.id "
			+ "inner join PContract f on f.id = b.pcontractid_link "
			+ "where b.parentpoid_link = a.parentpoid_link "
			+ "and (f.orgvendorid_link in :vendors or :vendors is null) "
			+ "and (f.orgbuyerid_link in :buyers or :buyers is null) "
			+ "and d.granttoorgid_link = :orgid_link) "
			+ "order by a.shipdate asc")
	public List<POrder_Req> getByOrg(
			@Param ("orgid_link")final Long orgid_link,
			@Param ("vendors")final List<Long> vendors,
			@Param ("buyers")final List<Long> buyers);
	
}
