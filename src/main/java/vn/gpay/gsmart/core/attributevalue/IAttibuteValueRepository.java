package vn.gpay.gsmart.core.attributevalue;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IAttibuteValueRepository extends JpaRepository<Attributevalue, Long>, JpaSpecificationExecutor<Attributevalue> {
	@Query(value = "select c from Attributevalue c "
			+ "where c.attributeid_link =:attributeid_link "
			+ "order by sortvalue")
	public List<Attributevalue> getlist_ByidAttribute(@Param ("attributeid_link")final long attributeid_link);
	
	@Query(value = "select max(sortvalue) from Attributevalue c "
			+ "where c.attributeid_link =:attributeid_link ")	
	public int getMaxSortValue(@Param ("attributeid_link")final long attributeid_link);
	
	@Query(value = "select a from Attributevalue a "
			+ "where a.attributeid_link = :attributeid_link "
			+ "and trim(lower(replace(a.value,' ',''))) = trim(lower(replace(:value, ' ',''))) "
			)
	public List<Attributevalue> getByValue(
			@Param ("value")final String value,
			@Param ("attributeid_link")final Long attributeid_link);
	
	@Query(value = "select distinct a from Attributevalue a "
			+ "inner join SKU_Attribute_Value b on a.id = b.attributevalueid_link "
			+ "inner join SKU c on c.id = b.skuid_link "
			+ "inner join StockInD d on d.skuid_link = c.id "
			+ "inner join StockIn e on d.stockinid_link = e.id "
			+ "where e.id = :stockinid_link "
			+ "and a.attributeid_link = 4 "
			+ "order by a.sortvalue "
			)
	public List<Attributevalue> getColorForStockin(
			@Param ("stockinid_link")final Long stockinid_link);
}
