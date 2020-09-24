package vn.gpay.gsmart.core.pcontract_po;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPContract_PORepository extends JpaRepository<PContract_PO, Long>, JpaSpecificationExecutor<PContract_PO> {
	@Query(value = "select c from PContract_PO c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and c.parentpoid_link = null "
			+ "and c.pcontractid_link = :pcontractid_link "
			+ "and (c.productid_link = :productid_link or 0 = :productid_link) "
			+ "and (:userid_link is null or c.merchandiserid_link = :userid_link) ")
	public List<PContract_PO> getPOByContractProduct(@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("productid_link")final  Long productid_link,
			@Param ("userid_link")final  Long userid_link);

	@Query(value = "select c from PContract_PO c "
			+ "where c.pcontractid_link = :pcontractid_link "
			+ "and c.productid_link = :productid_link")
	public List<PContract_PO> getPOByContractAndProduct(
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("productid_link")final  Long productid_link);
	
	@Query(value = "select c from PContract_PO c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and c.pcontractid_link = :pcontractid_link ")
	public List<PContract_PO> getPOByContract(@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("pcontractid_link")final  Long pcontractid_link);
	
	@Query(value = "select c from PContract_PO c "
			+ "where c.pcontractid_link = :pcontractid_link "
			+ "and (:productid_link is null or c.productid_link = :productid_link) "
			+ "and c.parentpoid_link != null")
	public List<PContract_PO> getPOLeafOnlyByContract(@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("productid_link")final  Long productid_link);	
	
	@Query(value = "select c from PContract_PO c "
			+ "where c.pcontractid_link = :pcontractid_link "
			+ "and (:productid_link is null or c.productid_link = :productid_link) "
			+ "and c.parentpoid_link = null "
			+ "and c.status = 0 ")
	public List<PContract_PO> getPO_Offer_Accept_ByPContract(
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("productid_link")final  Long productid_link);
	

	@Query(value = "select c from PContract_PO c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and c.pcontractid_link = :pcontractid_link "
			+ "and c.productid_link = :productid_link "
			+ "and c.shipdate > :shipdate "
			)
	public List<PContract_PO> getPO_LaterShipdate(@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("pcontractid_link")final  Long pcontractid_link, 
			@Param ("productid_link")final  Long productid_link, 
			@Param ("shipdate")final  Date shipdate
			);
	
	@Query(value = "select c from PContract_PO c "
			+ "where c.po_buyer = :po_buyer "
			+ "and c.shipmodeid_link = :shipmodeid_link "
			+ "and c.productid_link = :productid_link "
			+ "and c.shipdate = :shipdate "
			+ "and c.pcontractid_link = :pcontractid_link"
			)
	public List<PContract_PO> getone_by_template(
			@Param ("po_buyer")final  String po_buyer,
			@Param ("shipmodeid_link")final  Long shipmodeid_link, 
			@Param ("productid_link")final  Long productid_link, 
			@Param ("shipdate")final  Date shipdate,
			@Param ("pcontractid_link")final  Long pcontractid_link
			);
	
	@Query(value = "select c from PContract_PO c "
			+ "inner join Product b on b.id = c.productid_link "
			+ "where c.pcontractid_link = :pcontractid_link "
			+ "and lower(c.po_buyer) like lower(concat('%',:po_buyer,'%')) "
			+ "and lower(b.buyercode) like lower(concat('%',:buyercode,'%')) "
//			+ "and c.parentpoid_link != null "
			)
	public List<PContract_PO> getPcontractPoByPContractAndPOBuyer(
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("po_buyer")final String po_buyer,
			@Param ("buyercode")final String buyercode);
}
