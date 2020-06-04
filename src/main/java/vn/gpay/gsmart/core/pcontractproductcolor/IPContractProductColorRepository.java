package vn.gpay.gsmart.core.pcontractproductcolor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IPContractProductColorRepository extends JpaRepository<PContractProductColor, Long>, JpaSpecificationExecutor<PContractProductColor> {
	@Query(value = "select c from PContractProductColor c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and productid_link = :productid_link and pcontractid_link = :pcontractid_link")
	public List<PContractProductColor> getcolor_by_product_and_pcontract(@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("productid_link")final Long productid_link,@Param ("pcontractid_link")final Long pcontractid_link);
	
	@Query(value = "select c.colorid_link from PContractProductColor c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and productid_link = :productid_link and pcontractid_link = :pcontractid_link")
	public List<Long> getcolorid_by_product_and_pcontract(@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("productid_link")final Long productid_link,@Param ("pcontractid_link")final Long pcontractid_link);
}
