package vn.gpay.gsmart.core.porder_product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IPOrder_Product_Repository extends JpaRepository<POrder_Product, Long>, JpaSpecificationExecutor<POrder_Product> {
	@Query(value = "select c from POrder_Product c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and porderid_link = :porderid_link ")
	public List<POrder_Product> get_product_inporder(@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("porderid_link")final  Long porderid_link);
}
