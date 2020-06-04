package vn.gpay.gsmart.core.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IProductBOMRepository extends JpaRepository<ProductBOM, Long>, JpaSpecificationExecutor<ProductBOM> {
	@Query(value = "select c from ProductBOM c where c.productid_link = :productid_link ")
	public List<ProductBOM> getall_material_in_productBOM(@Param ("productid_link")final  Long productid_link);
	
	@Query(value = "select c.materialid_link from ProductBOM c where c.productid_link = :productid_link ")
	public List<Long> getall_materialid_in_productBOM(@Param ("productid_link")final  Long productid_link);
}
