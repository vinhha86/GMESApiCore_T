package vn.gpay.gsmart.core.invcheck;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface InvcheckEpcRepository extends JpaRepository<InvcheckEpc, Long>{
	
	@Query(value="select c from InvcheckSku c where c.invcheckid_link=:invcheckid_link")
	public List<InvcheckEpc> invcheckSkuGetByInvcheckid_link(@Param ("invcheckid_link")final Long invcheckid_link);

	@Query(value ="select c from InvcheckEpc c where c.invcheckid_link =:invcheckid_link and c.skuid_link=:skuid_link")
	List<InvcheckEpc> findEpcBySkuId(@Param ("invcheckid_link")final long invcheckid_link, @Param ("skuid_link")final long skuid_link);
}
