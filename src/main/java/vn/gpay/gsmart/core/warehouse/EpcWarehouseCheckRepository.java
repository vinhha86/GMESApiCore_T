package vn.gpay.gsmart.core.warehouse;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface EpcWarehouseCheckRepository extends JpaRepository<EpcWarehouseCheck, Long>{

	//@Query(value = "select c from Warehouse c where spaceepc_link =:spaceepc")
	//public List<Warehouse> findBySpaceepc(@Param ("spaceepc")final String spaceepc);
	
}
