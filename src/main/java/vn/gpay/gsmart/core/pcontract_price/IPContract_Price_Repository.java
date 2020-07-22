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
public interface IPContract_Price_Repository extends JpaRepository<PContract_Price, Long>, JpaSpecificationExecutor<PContract_Price> {
	@Query(value = "select c from PContract_Price c "
			+ "where pcontract_poid_link = :pcontract_poid_link")
	public List<PContract_Price> getPrice_ByPO(
			@Param ("pcontract_poid_link")final  Long pcontract_poid_link);
	
	@Query(value = "select c from PContract_Price c "
			+ "where pcontract_poid_link = :pcontract_poid_link and "
			+ "productid_link = :productid_link and sizesetid_link = 1")
	public List<PContract_Price> getPrice_CMP(
			@Param ("pcontract_poid_link")final  Long pcontract_poid_link,
			@Param ("productid_link")final  Long productid_link
			);
}
