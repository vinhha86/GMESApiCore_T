package vn.gpay.gsmart.core.porder_product_sku;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Repository
@Transactional
public interface IPOrder_Product_SKU_Repository extends JpaRepository<POrder_Product_SKU, Long>, JpaSpecificationExecutor<POrder_Product_SKU> {
	@Query(value = "select c from POrder_Product_SKU c where productid_link = :productid_link ")
	public List<POrder_Product_SKU> getby_productidlink(@Param ("productid_link")final  Long productid_link);

	@Query(value = "select c from POrder_Product_SKU c where porderid_link = :porderid_link and skuid_link = :skuid_link")
	public List<POrder_Product_SKU> getby_porderandsku(@Param ("porderid_link")final  Long porderid_link, @Param ("skuid_link")final  Long skuid_link);

	@Query(value = "select c from POrder_Product_SKU c where porderid_link = :porderid_link")
	public List<POrder_Product_SKU> getby_porder(@Param ("porderid_link")final  Long porderid_link);
	
	@Query(value = "select c from POrder_Product_SKU c "
			+ "where c.skuid_link = :skuid_link "
			+ "and porderid_link = :porderid_link ")
	public List<POrder_Product_SKU> get_sku_in_encode(@Param ("skuid_link")final  Long skuid_link,
			@Param ("porderid_link")final  Long porderid_link);
	
	@Query(value = "select c from POrder_Product_SKU c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and porderid_link = :porderid_link ")
	public List<POrder_Product_SKU> get_sku_inporder(@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("porderid_link")final  Long porderid_link);
	
	@Query(value = "select b.id from POrder_Product_SKU c "
			+ "inner join SKU_Attribute_Value a on c.skuid_link = a.skuid_link "
			+ "inner join Attributevalue b on b.id = a.attributevalueid_link "
			+ "where porderid_link = :porderid_link and b.attributeid_link = 4 "
			+ "group by b.id")
	public List<Long> get_colorid_byporder(
			@Param ("porderid_link")final  Long porderid_link);
	
	@Query(value = "select c.skuid_link as skuid_link, sum(c.pquantity_total) as pquantity_total from POrder_Product_SKU c "
			+ "where porderid_link = :porderid_link "
			+ "group by c.skuid_link")
	public List<Object[]> getsumsku_byporder(
			@Param ("porderid_link")final  long porderid_link);	
	
	@Query(value = "select b.id from SKU_Attribute_Value a "
			+ "inner join POrder_Product_SKU c on a.skuid_link = c.skuid_link "
			+ "inner join Attributevalue b on b.id = a.attributevalueid_link "
			+ "where c.porderid_link = :porderid_link "
			+ " and a.attributeid_link= :attributeid_link "
			+ "group by b.id, b.sortvalue "
			+ "order by b.sortvalue asc")
	public List<Long> getvaluesize_in_product(
			@Param ("porderid_link")final  long porderid_link, 
			@Param ("attributeid_link")final long attributeid_link);
	
	@Query(value = "select sum(a.pquantity_total) from POrder_Product_SKU a "
			+ "where a.pcontract_poid_link = :pcontract_poid_link "
			+ " and a.skuid_link= :skuid_link")
	public Integer get_qty_by_po_and_sku(
			@Param ("pcontract_poid_link")final  long pcontract_poid_link, 
			@Param ("skuid_link")final long skuid_link);
}
