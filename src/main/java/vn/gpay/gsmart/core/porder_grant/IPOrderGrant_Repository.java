package vn.gpay.gsmart.core.porder_grant;
//import java.util.List;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPOrderGrant_Repository extends JpaRepository<POrderGrant, Long>{
	@Query(value = "select a from POrderGrant a where a.granttoorgid_link = :granttoorgid_link and a.ordercode = :ordercode")
	public List<POrderGrant>getByOrderCodeAndOrg(@Param ("granttoorgid_link")final Long granttoorgid_link, @Param ("ordercode")final String ordercode);

	@Query(value = "select a from POrderGrant a where a.porderid_link = :porderid_link")
	public List<POrderGrant>getByOrderId(@Param ("porderid_link")final Long porderid_link);
	
}
