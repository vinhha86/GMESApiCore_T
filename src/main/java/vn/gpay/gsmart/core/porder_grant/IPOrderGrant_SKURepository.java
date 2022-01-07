package vn.gpay.gsmart.core.porder_grant;
//import java.util.List;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IPOrderGrant_SKURepository extends JpaRepository<POrderGrant_SKU, Long>, JpaSpecificationExecutor<POrderGrant_SKU>{
	@Query(value = "select a from POrderGrant_SKU a "
			+ "where a.pordergrantid_link = :pordergrantid_link "
			)
	public List<POrderGrant_SKU>getPOrderGrant_SKU(
			@Param ("pordergrantid_link")final Long pordergrantid_link);
	
	@Query(value = "select a from POrderGrant_SKU a "
			+ "where a.pordergrantid_link = :pordergrantid_link "
			+ "and (pcontract_poid_link = :pcontract_poid_link or :pcontract_poid_link is null)"
			)
	public List<POrderGrant_SKU>getgrantsku_by_grant_and_po(
			@Param ("pordergrantid_link")final Long pordergrantid_link,
			@Param ("pcontract_poid_link")final Long pcontract_poid_link);
	
	@Query(value = "select distinct b.granttoorgid_link from POrderGrant_SKU a "
			+ "inner join POrderGrant b on a.pordergrantid_link = b.id "
			+ "where a.pcontract_poid_link = :pcontract_poid_link"
			)
	public List<Long>getProductionLines(@Param ("pcontract_poid_link")final Long pcontract_poid_link);

	//	@Query(value = "select a from POrderGrant_SKU a where a.skuid_link = :skuid_link")
//	public POrderGrant_SKU getPOrderGrant_SKUbySKUid_link(@Param ("skuid_link")final Long skuid_link);
	
	@Query(value = "select a from POrderGrant_SKU a "
			+ "where a.skuid_link = :skuid_link "
			+ "and a.pordergrantid_link = :pordergrantid_link")
	public POrderGrant_SKU getPOrderGrant_SKUbySKUid_linkAndGrantId(
			@Param ("skuid_link")final Long skuid_link, 
			@Param ("pordergrantid_link")final Long pordergrantid_link);
	
	@Query(value = "select a from POrderGrant_SKU a "
			+ "where a.skuid_link = :skuid_link "
			+ "and a.pordergrantid_link = :pordergrantid_link "
			+ "and a.pcontract_poid_link = :pcontract_poid_link")
	public POrderGrant_SKU getPOrderGrant_SKUbySKUid_linkAndGrantId_AndPO(
			@Param ("skuid_link")final Long skuid_link, 
			@Param ("pordergrantid_link")final Long pordergrantid_link,
			@Param ("pcontract_poid_link")final Long pcontract_poid_link);
	
	@Query(value = "select a from POrderGrant_SKU a where a.skuid_link = :skuid_link " 
			+ "and a.pordergrantid_link = :pordergrantid_link "
			+ "and (a.pcontract_poid_link = :pcontract_poid_link or :pcontract_poid_link is null)"
			)
	public POrderGrant_SKU getPOrderGrant_SKUbySKUAndGrantAndPcontractPo(
			@Param ("skuid_link")final Long skuid_link, 
			@Param ("pordergrantid_link")final Long pordergrantid_link, 
			@Param ("pcontract_poid_link")final Long pcontract_poid_link
			);
	
	@Query(value = "select a from POrderGrant_SKU a "
		+ "where (a.pcontract_poid_link = :pcontract_poid_link or :pcontract_poid_link is null) "
		+ "and a.skuid_link = :skuid_link "
		)
	public List<POrderGrant_SKU>getByPContractPOAndSKU(
			@Param ("pcontract_poid_link")final Long pcontract_poid_link,
			@Param ("skuid_link")final Long skuid_link
			);
	
	@Query(value = "select a from POrderGrant_SKU a "
			+ "inner join SKU_Attribute_Value c on a.skuid_link = c.skuid_link "
			+ "where a.pordergrantid_link = :pordergrantid_link "
			+ "and (c.attributeid_link = 4 and c.attributevalueid_link = :colorid_link)"
			)
		public List<POrderGrant_SKU>getlistco_by_mau(
				@Param ("pordergrantid_link")final Long pordergrantid_link,
				@Param ("colorid_link")final Long colorid_link
				);
		
	
	@Query(value = "select b.value from POrderGrant_SKU a "
			+ "inner join SKU_Attribute_Value c on a.skuid_link = c.skuid_link "
			+ "inner join Attributevalue b on c.attributevalueid_link = b.id "
			+ "where a.pordergrantid_link = :pordergrantid_link and b.attributeid_link = 4 "
			+ "group by b.value"
			)
		public List<String>getlistmau(
				@Param ("pordergrantid_link")final Long pordergrantid_link
				);
	
	@Query(value = "select b.value from "
			+ "POrderGrant_SKU a "
			+ "inner join POrderGrant d on a.pordergrantid_link = d.id "
			+ "inner join SKU_Attribute_Value c on a.skuid_link = c.skuid_link "
			+ "inner join Attributevalue b on c.attributevalueid_link = b.id "
			+ "where d.porderid_link = :porderid_link and b.attributeid_link = 30 "
			+ "group by b.value, b.sortvalue "
			+ "order by b.sortvalue"
			)
		public List<String>getlistco(
				@Param ("porderid_link")final Long porderid_link
				);
	
	@Query(value = "select sum(a.grantamount) from "
			+ "POrderGrant_SKU a "
			+ "inner join POrderGrant b on a.pordergrantid_link = b.id "
			+ "where b.porderid_link = :porderid_link "
			+ "and a.skuid_link = :skuid_link "
			+ "and a.pcontract_poid_link = :pcontract_poid_link"
			)
		public Integer porder_get_qty_grant(
				@Param ("porderid_link")final Long porderid_link,
				@Param ("skuid_link")final Long skuid_link,
				@Param ("pcontract_poid_link")final Long pcontract_poid_link
				);
	
	@Query(value = "select c.pordergrantid_link as pordergrantid_link, c.skuid_link as skuid_link, "
			+ "sum(c.grantamount) as grantamount "
			+ "from POrderGrant_SKU c "
			+ "where pordergrantid_link = :pordergrantid_link " 
			+ "group by c.pordergrantid_link, c.skuid_link order by c.skuid_link")
	public List<Object[]> get_POrderGrant_SKU_byPorderGrant(
			@Param("pordergrantid_link") final Long pordergrantid_link
			);
}
