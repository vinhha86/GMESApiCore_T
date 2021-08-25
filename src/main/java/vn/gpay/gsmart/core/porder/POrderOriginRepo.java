package vn.gpay.gsmart.core.porder;

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
public interface POrderOriginRepo extends JpaRepository<POrderOrigin, Long>, JpaSpecificationExecutor<POrderOrigin> {
	@Query(value = "select  a from POrderOrigin a " + "inner join PContract b on a.pcontractid_link = b.id "
			+ "inner join PContract_PO c on a.pcontract_poid_link = c.id "
			+ "inner join Product d on a.productid_link = d.id "
			+ "where  (b.orgbuyerid_link = :buyerid or :buyerid is null) "
			+ "and (b.orgvendorid_link = :vendorid or :vendorid is null) "
			+ "and (a.granttoorgid_link = :factoryid or :factoryid is null) "
			+ "and (a.granttoorgid_link = :granttoorgid_link or :granttoorgid_link is null) "
			+ "and lower(c.po_buyer) like lower(concat('%',:pobuyer,'%')) "
			+ "and (d.buyercode is null or lower(d.buyercode) like lower(concat('%',:stylebuyer,'%'))) "
			+ "and lower(b.contractcode) like lower(concat('%',:contractcode,'%')) "
			+ "and (a.status in :statuses or :statuses is null) "
			+ "and (CAST(:golivedatefrom AS date) IS NULL or a.golivedate >= :golivedatefrom) "
			+ "and (CAST(:golivedateto AS date) IS NULL or a.golivedate <= :golivedateto) "
			+ "order by a.granttoorgid_link, d.buyercode, a.golivedate ")
	public List<POrderOrigin> getPOrderBySearch(@Param("buyerid") final Long buyerid,
			@Param("vendorid") final Long vendorid, @Param("factoryid") final Long factoryid,
			@Param("pobuyer") final String pobuyer, @Param("stylebuyer") final String stylebuyer,
			@Param("contractcode") final String contractcode, @Param("statuses") final List<Integer> statuses,
			@Param("granttoorgid_link") final Long granttoorgid_link,
			@Param("golivedatefrom") final Date golivedatefrom, @Param("golivedateto") final Date golivedateto);
}
