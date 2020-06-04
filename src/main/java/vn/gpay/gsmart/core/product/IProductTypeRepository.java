package vn.gpay.gsmart.core.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IProductTypeRepository extends JpaRepository<ProductType, Long> {
	@Query(value = "select c from ProductType c where c.id >= :producttypeid_min and c.id <= :producttypeid_max")
	public List<ProductType> getall_ProductTypes(@Param ("producttypeid_min")final  int producttypeid_min, @Param ("producttypeid_max")final  int producttypeid_max);
}
