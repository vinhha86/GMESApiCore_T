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
public interface IPContract_Price_Repository
		extends JpaRepository<PContract_Price, Long>, JpaSpecificationExecutor<PContract_Price> {
	@Query(value = "select c from PContract_Price c " + "where pcontract_poid_link = :pcontract_poid_link")
	public List<PContract_Price> getPrice_ByPO(@Param("pcontract_poid_link") final Long pcontract_poid_link);

	@Query(value = "select c from PContract_Price c " + "where pcontract_poid_link = :pcontract_poid_link and "
			+ "productid_link = :productid_link and sizesetid_link = 1")
	public List<PContract_Price> getPrice_CMP(@Param("pcontract_poid_link") final Long pcontract_poid_link,
			@Param("productid_link") final Long productid_link);

	@Query(value = "select c from PContract_Price c " + "where pcontract_poid_link = :pcontract_poid_link and "
			+ "productid_link = :productid_link")
	public List<PContract_Price> getPrice_by_product(@Param("pcontract_poid_link") final Long pcontract_poid_link,
			@Param("productid_link") final Long productid_link);

	@Query(value = "select c from PContract_Price c " + "where pcontract_poid_link = :pcontract_poid_link and "
			+ "productid_link = :productid_link " + "and :sizesetid_link = sizesetid_link")
	public List<PContract_Price> getPrice_by_product_and_sizeset(
			@Param("pcontract_poid_link") final Long pcontract_poid_link,
			@Param("productid_link") final Long productid_link, @Param("sizesetid_link") final Long sizesetid_link);

	@Query(value = "select c from PContract_Price c " + "where sizesetid_link != 1 and "
			+ "pcontract_poid_link = :pcontract_poid_link")
	public List<PContract_Price> getBySizesetNotAll(@Param("pcontract_poid_link") final Long pcontract_poid_link);

	@Query(value = "select sum(c.totalprice) from PContract_Price c "
			+ "inner join PContract_PO a on a.id = c.pcontract_poid_link " + "where sizesetid_link = 1 and "
			+ "c.pcontractid_link = :pcontractid_link and c.productid_link = :productid_link and po_typeid_link in (0,1)")
	public Float getTotalPrice(@Param("pcontractid_link") final Long pcontractid_link,
			@Param("productid_link") final Long productid_link);
}
