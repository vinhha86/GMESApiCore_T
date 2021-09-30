package vn.gpay.gsmart.core.porders_poline;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;

@Repository
@Transactional
public interface POrder_POLine_Repo
		extends JpaRepository<POrder_POLine, Long>, JpaSpecificationExecutor<POrder_POLine> {
	@Query(value = "select c.porderid_link from POrder_POLine c " + " where pcontract_poid_link = :pcontract_poid_link")
	public List<Long> get_porderid_by_line(@Param("pcontract_poid_link") final Long pcontract_poid_link);

	@Query(value = "select a " + "from POrder_POLine c " + "inner join POrder a on a.id = c.porderid_link "
			+ "where c.pcontract_poid_link = :pcontract_poid_link")
	public List<POrder> get_porder_by_line(@Param("pcontract_poid_link") final Long pcontract_poid_link);

	@Query(value = "select a " + "from POrder_POLine c " + "inner join POrderGrant a on a.id = c.pordergrantid_link "
			+ "where c.pcontract_poid_link = :pcontract_poid_link")
	public List<POrderGrant> get_grant_by_line(@Param("pcontract_poid_link") final Long pcontract_poid_link);

	@Query(value = "select c " + "from POrder_POLine c " + "where c.pcontract_poid_link = :pcontract_poid_link")
	public List<POrder_POLine> get_porderline_by_line(@Param("pcontract_poid_link") final Long pcontract_poid_link);
}
