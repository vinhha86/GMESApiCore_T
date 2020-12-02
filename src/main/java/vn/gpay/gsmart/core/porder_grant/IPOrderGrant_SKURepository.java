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
	@Query(value = "select a from POrderGrant_SKU a where a.pordergrantid_link = :pordergrantid_link "
			+ "and a.pcontract_poid_link is not null "
			)
	public List<POrderGrant_SKU>getPOrderGrant_SKU(@Param ("pordergrantid_link")final Long pordergrantid_link);
	
//	@Query(value = "select a from POrderGrant_SKU a where a.skuid_link = :skuid_link")
//	public POrderGrant_SKU getPOrderGrant_SKUbySKUid_link(@Param ("skuid_link")final Long skuid_link);
	
	@Query(value = "select a from POrderGrant_SKU a where a.skuid_link = :skuid_link and a.pordergrantid_link = :pordergrantid_link")
	public POrderGrant_SKU getPOrderGrant_SKUbySKUid_linkAndGrantId(@Param ("skuid_link")final Long skuid_link, @Param ("pordergrantid_link")final Long pordergrantid_link);
	
	@Query(value = "select a from POrderGrant_SKU a where a.skuid_link = :skuid_link " 
			+ "and a.pordergrantid_link = :pordergrantid_link "
			+ "and a.pcontract_poid_link = :pcontract_poid_link "
			)
	public POrderGrant_SKU getPOrderGrant_SKUbySKUAndGrantAndPcontractPo(
			@Param ("skuid_link")final Long skuid_link, 
			@Param ("pordergrantid_link")final Long pordergrantid_link, 
			@Param ("pcontract_poid_link")final Long pcontract_poid_link
			);
	
	@Query(value = "select a from POrderGrant_SKU a "
		+ "where a.pcontract_poid_link = :pcontract_poid_link "
		+ "and a.skuid_link = :skuid_link "
		)
	public List<POrderGrant_SKU>getByPContractPOAndSKU(
			@Param ("pcontract_poid_link")final Long pcontract_poid_link,
			@Param ("skuid_link")final Long skuid_link
			);
}
