package vn.gpay.gsmart.core.pcontractproduct;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository
@Transactional
public interface IPContractProductRepository extends JpaRepository<PContractProduct, Long>, JpaSpecificationExecutor<PContractProduct> {
	@Query(value = "select c from PContractProduct c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and (productid_link = :productid_link or 0 = :productid_link) "
			+ "and pcontractid_link = :pcontractid_link")
	public List<PContractProduct> get_by_product_and_pcontract(
			@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("productid_link")final Long productid_link,
			@Param ("pcontractid_link")final Long pcontractid_link);
	
	@Query(value = "select c from PContractProduct c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and pcontractid_link = :pcontractid_link")
	public List<PContractProduct> get_by_pcontract(
			@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("pcontractid_link")final Long pcontractid_link);
	
	@Query(value = "select c.pcontractid_link from PContractProduct c "
			+ "where (productid_link in :productid_link or :productid_link is null) "
			+ "group by c.pcontractid_link")
	public List<Long> get_by_product(
			@Param ("productid_link")final  List<Long> productid_link);
	
	
	//Get product by buyerid_link
	@Query(value = "select c.productid_link from PContractProduct c inner join PContract b on c.pcontractid_link = b.id "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and (b.orgbuyerid_link = :orgbuyerid_link or :orgbuyerid_link is null)")
	public List<Long> get_by_orgcustomer(
			@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("orgbuyerid_link")final Long orgbuyerid_link);
}
