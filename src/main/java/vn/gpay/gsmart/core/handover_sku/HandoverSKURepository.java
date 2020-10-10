package vn.gpay.gsmart.core.handover_sku;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface HandoverSKURepository extends JpaRepository<HandoverSKU, Long>,JpaSpecificationExecutor<HandoverSKU>{
	@Query(value = "select c from HandoverSKU c where c.handoverid_link = :handoverid_link ")
	public List<HandoverSKU> getByHandoverId(@Param ("handoverid_link")final Long handoverid_link);
}
