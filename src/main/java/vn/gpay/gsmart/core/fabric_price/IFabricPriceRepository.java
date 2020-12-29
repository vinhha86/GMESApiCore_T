package vn.gpay.gsmart.core.fabric_price;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IFabricPriceRepository extends JpaRepository<FabricPrice, Long>,JpaSpecificationExecutor<FabricPrice>{
	
	@Query(value = "select c from FabricPrice c where c.materialid_link =:materialid_link")
	public List<FabricPrice> getByMaterial(@Param ("materialid_link")final  Long materialid_link);
}
