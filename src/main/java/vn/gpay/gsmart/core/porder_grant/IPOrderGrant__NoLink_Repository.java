package vn.gpay.gsmart.core.porder_grant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPOrderGrant__NoLink_Repository
		extends JpaRepository<POrderGrant_NoLink, Long>, JpaSpecificationExecutor<POrderGrant_NoLink> {

	@Query(value = "select a from POrderGrant_NoLink a "
			+ "inner join POrder b on a.porderid_link = b.id "
			+ "where "
			+ "b.productid_link in (select id from Product where :stylebuyer = '' or lower(buyercode) like lower(concat('%',:stylebuyer,'%'))) "
			+ "and (:pobuyer = '' or lower(a.lineinfo) like lower(concat('%',:pobuyer,'%'))) "
			+ "and b.pcontractid_link in (select id from PContract where "
			+ "(:buyerid is null or orgbuyerid_link = :buyerid) "
			+ "and (:vendorid is null or orgvendorid_link = :vendorid) "
//			+ "and (:contractcode = '' or lower(contractcode) like lower(concat('%',:contractcode,'%')))"
			+ ")"
			)
	public List<POrderGrant_NoLink> getPOrderGrant_PContract(
			@Param("stylebuyer") final String stylebuyer,
			@Param("pobuyer") final String pobuyer,
			@Param("buyerid") final Long buyerid, 
			@Param("vendorid") final Long vendorid
//			@Param("contractcode") final String contractcode
			);
	
	@Query(value = "select a from POrderGrant_NoLink a "
			+ "inner join POrder b on a.porderid_link = b.id "
			+ "where "
			+ "b.productid_link in (select id from Product where :stylebuyer = '' or lower(buyercode) like lower(concat('%',:stylebuyer,'%'))) "
			+ "and (:pobuyer = '' or lower(a.lineinfo) like lower(concat('%',:pobuyer,'%'))) "
			)
	public List<POrderGrant_NoLink> getPOrderGrant(
			@Param("stylebuyer") final String stylebuyer,
			@Param("pobuyer") final String pobuyer
//			@Param("contractcode") final String contractcode
			);
}
