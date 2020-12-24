package vn.gpay.gsmart.core.pcontract_price;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPContract_Price_D_SKURepository extends JpaRepository<PContract_Price_D_SKU, Long>, JpaSpecificationExecutor<PContract_Price_D_SKU>{
	@Query(value = "select c from PContract_Price_D_SKU c "
			+ "inner join PContract_Price_D b on b.id = c.pcontractprice_d_id_link " 
			+ "where b.pcontract_poid_link = :pcontract_poid_link")
	public List<PContract_Price_D_SKU> getPrice_D_SKU_ByPO(
			@Param ("pcontract_poid_link")final Long pcontract_poid_link);
}
