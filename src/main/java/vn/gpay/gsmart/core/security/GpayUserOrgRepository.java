package vn.gpay.gsmart.core.security;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface GpayUserOrgRepository extends JpaRepository<GpayUserOrg, Long>,JpaSpecificationExecutor<GpayUserOrg> {
	@Query(value = "select c from GpayUserOrg c where c.userid_link = :userid_link ")
	public List<GpayUserOrg> getall_byuser(@Param ("userid_link")final  Long userid_link);
}