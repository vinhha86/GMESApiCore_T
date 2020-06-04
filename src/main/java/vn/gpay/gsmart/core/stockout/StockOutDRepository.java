package vn.gpay.gsmart.core.stockout;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface StockOutDRepository extends JpaRepository<StockOutD, Long>{
	@Query(value = "select a from StockOutD a inner join StockOut b on a.stockoutid_link = b.id and b.stockouttypeid_link = :stockouttypeid_link where a.stockoutdate >= :stockoutdate_from and a.stockoutdate <= :stockoutdate_to")
	public List<StockOutD>getByDateAndType(@Param ("stockouttypeid_link")final Integer stockouttypeid_link, @Param ("stockoutdate_from")final Date stockoutdate_from, @Param ("stockoutdate_to")final Date stockoutdate_to);	

	@Query(value = "select a from StockOutD a where a.stockoutid_link = :stockoutid_link")
	public List<StockOutD>getByStockoutId(@Param ("stockoutid_link")final Long stockoutid_link);	

	@Query(value = "select a from StockOutD a inner join StockOut b on a.stockoutid_link = b.id and b.stockouttypeid_link = 2 where a.stockoutdate >= :stockoutdate_from and a.stockoutdate <= :stockoutdate_to")
	public List<StockOutD>getByDate(@Param ("stockoutdate_from")final Date stockoutdate_from, @Param ("stockoutdate_to")final Date stockoutdate_to);	

	@Query(value = "select a from StockOutD a inner join StockOut b on a.stockoutid_link = b.id and b.stockouttypeid_link = :stockouttypeid_link where a.stockoutdate >= :stockoutdate_from and a.stockoutdate <= :stockoutdate_to and a.skuid_link = :skuid_link")
	public List<StockOutD>getByDateAndSkucode(@Param ("stockouttypeid_link")final Integer stockouttypeid_link, @Param ("stockoutdate_from")final Date stockoutdate_from, @Param ("stockoutdate_to")final Date stockoutdate_to, @Param ("skuid_link")final Long skuid_link);	

}
