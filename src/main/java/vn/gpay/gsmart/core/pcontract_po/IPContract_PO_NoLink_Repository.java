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
public interface IPContract_PO_NoLink_Repository
		extends JpaRepository<PContract_PO_NoLink, Long>, JpaSpecificationExecutor<PContract_PO_NoLink> {
	//Lấy các PO Line đã chốt, chưa có Lệnh xuất kho và có ngày giao hàng <= ngày giới hạn
	@Query(value = "select c from PContract_PO_NoLink c "
			+ "inner join PContract a on c.pcontractid_link = a.id "
			+ "where (:orgbuyerid_link is null or a.orgbuyerid_link = :orgbuyerid_link) "
			+ "and c.orgrootid_link = :orgrootid_link "
			+ "and c.po_typeid_link = 11 and c.ismap = true " 
			+ "and (c.status is null or (c.status >= 0 and c.status < 2)) " 
			+ "and (c.shipdate >= :shipdate_from and c.shipdate <= :shipdate_to)")
	public List<PContract_PO_NoLink> getPO_HavetoShip(
			@Param("orgrootid_link") final Long orgrootid_link,
			@Param("shipdate_from") final Date shipdate_from,
			@Param("shipdate_to") final Date shipdate_to,
			@Param("orgbuyerid_link") final Long orgbuyerid_link
			);
}
