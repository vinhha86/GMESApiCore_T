package vn.gpay.gsmart.core.salebill;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface SaleBillRepository extends JpaRepository<SaleBill, Long> ,JpaSpecificationExecutor<SaleBill>{

//	@Query(value="select a from Menu a inner join UserMenu b on a.id = b.menuid where b.userid=:userid ")
//	public List<SaleBill> findByUserid(@Param ("userid")final long userid);
}
