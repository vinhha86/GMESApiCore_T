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
			+ "and (:userid_link is null or c.merchandiserid_link = :userid_link) "
			+ "and po_typeid_link = 10")
	public List<PContract_PO> getPOByContractProduct(@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("productid_link")final  Long productid_link,
			@Param ("userid_link")final  Long userid_link);
	
	@Query(value = "select c from PContract_PO c "
			+ "where id = :pcontractpoid_link")
	public List<PContract_PO> getbyId(
			@Param ("pcontractpoid_link")final  Long pcontractpoid_link);
	
	@Query(value = "select c from PContract_PO c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and c.parentpoid_link = null "
			+ "and c.pcontractid_link = :pcontractid_link "
			+ "and (c.productid_link = :productid_link or 0 = :productid_link) "
			+ "and (:userid_link is null or c.merchandiserid_link = :userid_link) "
			+ "and c.po_typeid_link in (0,1,10)")
	public List<PContract_PO> getPO_Chaogia(@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("productid_link")final  Long productid_link,
			@Param ("userid_link")final  Long userid_link);
	
	@Query(value = "select c from PContract_PO c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and c.parentpoid_link = null "
			+ "and c.pcontractid_link = :pcontractid_link "
			+ "and (c.productid_link = :productid_link or 0 = :productid_link) "
			+ "and (:userid_link is null or c.merchandiserid_link = :userid_link) "
			+ "and (c.po_typeid_link = 0 or c.po_typeid_link = 1 or c.po_typeid_link = 11)")
	public List<PContract_PO> getPO_Duyet(@Param ("orgrootid_link")final  Long orgrootid_link,
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
			+ "where c.parentpoid_link = :pcontractpo_id_link")
	public List<PContract_PO> getby_parentid_link(
			@Param ("pcontractpo_id_link")final  Long pcontractpo_id_link);
	
	@Query(value = "select c from PContract_PO c "
			+ "where c.parentpoid_link = :parentid_link "
			+ "and c.po_typeid_link =:po_typeid_link "
			+ "order by shipdate asc")
	public List<PContract_PO> getby_parent_and_type(
			@Param ("parentid_link")final  Long parentid_link,
			@Param ("po_typeid_link")final  Integer po_typeid_link);
		
	
	@Query(value = "select c from PContract_PO c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and c.pcontractid_link = :pcontractid_link "
			+ "and c.po_typeid_link < 10")
	public List<PContract_PO> getPOByContract(@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("pcontractid_link")final  Long pcontractid_link);
	
	@Query(value = "select c from PContract_PO c "
			+ "where c.pcontractid_link = :pcontractid_link "
			+ "and (:productid_link is null or c.productid_link = :productid_link) "
			+ "and c.parentpoid_link != null "
			+ "and c.status > -3")
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
	
	@Query(value = "select parent from PContract_PO parent "
			+ "inner join PContract_PO chil on parent.id = chil.parentpoid_link "
			+ "inner join POrder_Req a on parent.id = chil.parentpoid_link "
			+ "where parent.pcontractid_link = :pcontractid_link "
			+ "and (:productid_link is null or parent.productid_link = :productid_link) "
			+ "and parent.parentpoid_link is null "
			+ "and parent.status = 0 "
			+ "and a.granttoorgid_link in :orgid_link "
			+ "group by parent")
	public List<PContract_PO> getPO_Offer_Accept_ByPContract_AndOrg(
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("productid_link")final  Long productid_link,
			@Param ("orgid_link")final  List<Long> orgid_link);

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
			+ "and (c.shipmodeid_link = :shipmodeid_link or :shipmodeid_link is null) "
			+ "and c.productid_link = :productid_link "
			+ "and c.shipdate = :shipdate "
			+ "and c.pcontractid_link = :pcontractid_link"
			)
	public List<PContract_PO> getone_by_template_set(
			@Param ("po_buyer")final  String po_buyer,
			@Param ("shipmodeid_link")final  Long shipmodeid_link, 
			@Param ("productid_link")final  Long productid_link, 
			@Param ("shipdate")final  Date shipdate,
			@Param ("pcontractid_link")final  Long pcontractid_link
			);
	
	@Query(value = "select c from PContract_PO c "
			+ "where c.shipmodeid_link = :shipmodeid_link "
			+ "and c.productid_link = :productid_link "
			+ "and (c.shipdate = :shipdate) "
			+ "and c.pcontractid_link = :pcontractid_link "
			+ "and (c.po_buyer = :po_buyer or :po_buyer is null)"
			+ "and c.po_typeid_link = 0"
			)
	public List<PContract_PO> getone_by_template(
			@Param ("shipmodeid_link")final  Long shipmodeid_link, 
			@Param ("productid_link")final  Long productid_link, 
			@Param ("shipdate")final  Date shipdate,
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("po_buyer")final  String po_buyer
			);
	
	@Query(value = "select c from PContract_PO c "
			+ "where c.productid_link = :productid_link "
			+ "and c.shipdate = :shipdate "
			+ "and c.pcontractid_link = :pcontractid_link "
			+ "and c.po_typeid_link = 10 "
			+ "and c.parentpoid_link = :parentpoid_link"
			)
	public List<PContract_PO> getone_line_giaohang(
			@Param ("productid_link")final  Long productid_link, 
			@Param ("shipdate")final  Date shipdate,
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("parentpoid_link")final  Long parentpoid_link
			);
	
	@Query(value = "select c from PContract_PO c "
			+ "where trim(lower(replace(c.po_buyer,' ',''))) = trim(lower(replace(:po_buyer, ' ',''))) "
			+ "and (c.shipmodeid_link = :shipmodeid_link or :shipmodeid_link is null) "
			+ "and c.shipdate = :shipdate "
			+ "and c.pcontractid_link = :pcontractid_link "
			+ "and c.parentpoid_link = :parentid_link "
			+ "and c.po_typeid_link = 11"
			)
	public List<PContract_PO> getone_po_upload(
			@Param ("po_buyer")final  String po_buyer,
			@Param ("shipmodeid_link")final  Long shipmodeid_link,
			@Param ("shipdate")final  Date shipdate,
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("parentid_link")final  Long parentid_link
			);
	
	@Query(value = "select c from PContract_PO c "
			+ "where trim(lower(replace(c.po_buyer,' ',''))) = trim(lower(replace(:po_no, ' ',''))) "
			+ "and c.pcontractid_link = :pcontractid_link "
			)
	public List<PContract_PO> getone_po_byPO_no(
			@Param ("po_no")final  String po_no,
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
	
	@Query(value = "select c from PContract_PO c "
			+ "inner join POrder_Req b on b.pcontract_poid_link = c.id "
			+ "where lower(c.po_buyer) like lower(concat('%',:po_code,'%')) "
			+ "and b.granttoorgid_link in :orgs "
			)
	public List<PContract_PO> getBySearch(
			@Param ("po_code")final String po_code,
			@Param ("orgs")final List<Long> orgs);
	
	@Query(value = "select c from PContract_PO c "
			+ "inner join POrder_Req b on b.pcontract_poid_link = c.id "
			+ "where lower(c.po_buyer) like lower(concat('%',:po_code,'%')) "
			)
	public List<PContract_PO> getBySearch_ProductOnly(
			@Param ("po_code")final String po_code);
	
	@Query(value = "select a.id from PContract a "
			+ "left join PContract_PO c on a.id = c.pcontractid_link "
			+ "left join POrder_Req b on b.pcontract_poid_link = c.id "
			+ "where (lower(c.po_buyer) like lower(concat('%',:po_code,'%')) or :po_code ='') "
			+ "and (b.granttoorgid_link in :orgs or :orgs is null) "
			+ "group by a.id"
			)
	public List<Long> getPContractBySearch_OrgOnly(
			@Param ("po_code")final String po_code,
			@Param ("orgs")final List<Long> orgs);
	
	@Query(value = "select c from PContract_PO c "
			+ "left join POrder_Req b on b.pcontract_poid_link = c.id "
			+ "where lower(c.po_buyer) like lower(concat('%',:po_code,'%')) "
			+ "and b.granttoorgid_link in :orgs "
			)
	public List<PContract_PO> getBySearch_OrgOnly(
			@Param ("po_code")final String po_code,
			@Param ("orgs")final List<Long> orgs);
	
	@Query(value = "select c from PContract_PO c "
			+ "inner join PContract_PO parent on c.parentpoid_link = parent.id "
			+ "inner join PContract_PO plan on plan.parentpoid_link = parent.id "
			+ "left join POrder_Req b on b.pcontract_poid_link = plan.id "
			+ "where lower(c.po_buyer) like lower(concat('%',:po_code,'%')) "
			+ "and (b.granttoorgid_link in :orgs or :orgs is null) "
			+ "and c.po_typeid_link =:po_type "
			+ "group by c"
			)
	public List<PContract_PO> getBySearch_OrgAndType(
			@Param ("po_code")final String po_code,
			@Param ("orgs")final List<Long> orgs,
			@Param ("po_type")final Integer po_type);
	
	@Query(value = "select c from PContract_PO c "
			+ "inner join PContract_PO parent on c.parentpoid_link = parent.id "
			+ "inner join PContract_PO plan on plan.parentpoid_link = parent.id "
			+ "inner join POrder porder on porder.pcontract_poid_link = plan.id "
			+ "where (porder.granttoorgid_link in :orgs or :orgs is null) "
			+ "and c.po_typeid_link =:po_type "
			+ "and c.shipdate >= :shipdate_from "
			+ "and c.shipdate <= :shipdate_to "
			+ "group by c "
			+ "order by c.shipdate ASC "
			)
	public List<PContract_PO> getby_process_shipping(
			@Param ("shipdate_from")final  Date shipdate_from,
			@Param ("shipdate_to")final  Date shipdate_to,
			@Param ("orgs")final List<Long> orgs,
			@Param ("po_type")final Integer po_type);
	
	@Query(value = "select c from PContract_PO c "
			+ "left join POrder_Req b on b.pcontract_poid_link = c.id "
			+ "where (lower(c.po_buyer) like lower(concat('%',:po_code,'%')) or :po_code = '')"
			)
	public List<PContract_PO> getBySearch_CodeOnly(
			@Param ("po_code")final String po_code);
	
	@Query(value = "select c from PContract_PO c "
			+ "inner join PContractProductSKU a on c.id = a.pcontract_poid_link "
			+ "where (lower(c.po_buyer) = lower(concat(:po_code)) "
			+ "and po_typeid_link = :type) "
			+ "and c.pcontractid_link = :pcontractid_link "
			+ "and a.productid_link = :productid_link"
			)
	public List<PContract_PO> getbycode_and_type_and_product(
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("po_code")final String po_code,
			@Param ("type")final int type,
			@Param ("productid_link")final Long productid_link);
	
	@Query(value = "select c from PContract_PO c "
			+ "where po_typeid_link = :type "
			+ "and pcontractid_link = :pcontractid_link "
			+ "and lower(c.po_buyer) not in :list_po_code"
			)
	public List<PContract_PO> getnotin_list_pono(
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("list_po_code")final List<String> list_po_code,
			@Param ("type")final int type);
	
	@Query(value = "select c from PContract_PO c "
			+ "inner join POrderGrant_SKU b on b.pcontract_poid_link = c.id "
			+ "inner join POrderGrant a on a.id = b.pordergrantid_link "
			+ "where a.porderid_link = :porderid_link "
			+ "group by c"
			)
	public List<PContract_PO> getby_porder(
			@Param ("porderid_link")final Long porderid_link);
	
	@Query(value = "select  sum(a.po_quantity), c.name "
			+ "from PContract_PO a "
			+ "inner join PContract b on a.pcontractid_link = b.id "
			+ "full join MarketType c on b.marketypeid_link = c.id "
			+ "where a.parentpoid_link is not null "
			+ "group by c.name "
			+ "order by c.name "
			)
	public List<Object[]> getForMarketTypeChart();
	
	@Query(value = "select a from PContract_PO a "
			+ "inner join PContract_PO c on a.id = c.parentpoid_link "
			+ "inner join POrder_Req b on c.id = b.pcontract_poid_link "
			+ "where a.po_typeid_link = 10 "
			+ "and b.granttoorgid_link in :orgid_link "
			+ "and a.parentpoid_link is null "
			+ "and b.status = -1 "
			+ "group by a"
			)
	public List<PContract_PO> getOffers_byOrg(
			@Param ("orgid_link")final  List<Long> orgid_link
			);
	
	@Query(value = "select a from PContract_PO a "
			+ "where pcontractid_link = :pcontractid_link "
			+ "and po_typeid_link in :type"
			)
	public List<PContract_PO> getby_pcontract_and_type(
			@Param ("type")final  List<Integer> type,
			@Param ("pcontractid_link")final  Long pcontractid_link
			);
	
	@Query(value = "select a from PContract_PO a "
			+ "where pcontractid_link = :pcontractid_link "
			+ "and po_typeid_link in :type "
			+ "and productid_link = :productid_link"
			)
	public List<PContract_PO> getby_pcontract_and_type_and_product(
			@Param ("type")final  List<Integer> type,
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("productid_link")final  Long productid_link
			);
}
