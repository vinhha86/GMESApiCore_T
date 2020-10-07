package vn.gpay.gsmart.core.invcheck;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface InvcheckSkuRepository extends JpaRepository<InvcheckSku, Long>{

	@Query(value="select c from InvcheckSku c where c.invcheckid_link=:invcheckid_link")
	public List<InvcheckSku> invcheckSkuGetByInvcheckid_link(@Param ("invcheckid_link")final Long invcheckid_link);
	
//	@Modifying
//	@Query(value="update InvcheckSku c set "
//			+ "c.totalpackagecheck = (select count(epc) from InvcheckEpc d where d.rssi <> 0 and d.invcheckid_link=:invcheckid_link and d.skuid_link=:skuid_link), "
//			+ "c.ydscheck = (select sum(ydscheck) from InvcheckEpc d where d.rssi <> 0 and d.invcheckid_link=:invcheckid_link and d.skuid_link=:skuid_link) "
//			+ "where c.invcheckid_link=:invcheckid_link and c.skuid_link=:skuid_link")
//	public void updateTotalCheck(@Param ("invcheckid_link")final Long invcheckid_link,@Param ("skuid_link")final Long skuid_link);	
}
