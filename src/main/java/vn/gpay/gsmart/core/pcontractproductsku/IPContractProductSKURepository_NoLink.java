package vn.gpay.gsmart.core.pcontractproductsku;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPContractProductSKURepository_NoLink extends JpaRepository<PContractProductSKU_NoLink, Long> {
	//Hung Dai Bang
	@Query(value = "select c from PContractProductSKU_NoLink c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and c.pcontractid_link = :pcontractid_link")
	public List<PContractProductSKU_NoLink> getlistsku_bypcontract_nolink(@Param("orgrootid_link") final Long orgrootid_link,
			@Param("pcontractid_link") final long pcontractid_link);

	@Query(value = "select c.productid_link as productid_link, c.skuid_link as skuid_link, "
			+ "sum(pquantity_porder) as pquantity_porder, "
			+ "sum(pquantity_sample) as pquantity_sample, "
			+ "sum(pquantity_granted) as pquantity_granted, "
			+ "sum(c.pquantity_total) as pquantity_total "
			+ "from PContractProductSKU_NoLink c "
			+ "where pcontractid_link = :pcontractid_link " + "group by c.productid_link, c.skuid_link")
	public List<Object[]> getsumsku_bypcontract(@Param("pcontractid_link") final long pcontractid_link);
}
