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
	@Query(value = "select a from POrderGrant_SKU a where a.pordergrantid_link = :pordergrantid_link")
	public List<POrderGrant_SKU>getPOrderGrant_SKU(@Param ("pordergrantid_link")final Long pordergrantid_link);
	
}
