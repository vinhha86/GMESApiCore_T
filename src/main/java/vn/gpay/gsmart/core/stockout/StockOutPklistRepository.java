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
public interface StockOutPklistRepository extends JpaRepository<StockOutPklist, Long>{
	@Query(value = "select a from StockOutPklist a inner join StockOutD b on a.stockoutdid_link = b.id where b.stockoutid_link =:stockoutid_link")
	List<StockOutPklist> inv_getbyid(@Param ("stockoutid_link")final long stockoutid_link) ;

	@Query(value = "select a from StockOutPklist a where a.stockoutdid_link = :stockoutdid_link")
	public List<StockOutPklist>getByStockoutdId(@Param ("stockoutdid_link")final Long stockoutdid_link);

	@Query(value = "select a from StockOutPklist a where a.stockoutid_link = :stockoutid_link")
	public List<StockOutPklist>getByStockoutId(@Param ("stockoutid_link")final Long stockoutid_link);	
	
	@Query(value = "select a from StockOutPklist a where a.stockoutid_link = :stockoutid_link and a.status = :status")
	public List<StockOutPklist>getByStockoutIdAndStatus(@Param ("stockoutid_link")final Long stockoutid_link, @Param ("status")final Integer status);
	
	@Query(value = "select a from StockOutPklist a inner join StockOut b on a.stockoutid_link = b.id and b.stockouttypeid_link = 1 where a.skuid_link = :skuid_link and a.status = 0 and a.ydsprocessed > 0")
	public List<StockOutPklist>getAvailableBySku(@Param ("skuid_link")final Long skuid_link);
	
	@Query(value = "select SUM(a.ydscheck) from StockOutPklist a inner join StockOut b on a.stockoutid_link = b.id and b.stockouttypeid_link = 1 where a.skuid_link = :skuid_link and a.status = 0")
	public Float getAvailableYdscheckSumBySku(@Param ("skuid_link")final Long skuid_link);

	@Query(value = "select SUM(a.ydsprocessed) from StockOutPklist a inner join StockOut b on a.stockoutid_link = b.id and b.stockouttypeid_link = 1 where a.skuid_link = :skuid_link and a.status = 0")
	public Float getAvailableYdsprocessedSumBySku(@Param ("skuid_link")final Long skuid_link);

	@Query(value = "select SUM(a.ydsprocessed) from StockOutPklist a inner join StockOut b on a.stockoutid_link = b.id and b.stockouttypeid_link = 2 and b.pordercode = :ordercode where a.skuid_link = :skuid_link and a.status = 0")
	public Float getStockoutSumBySkuAndOrdercode(@Param ("skuid_link")final Long skuid_link, @Param ("ordercode")final String ordercode);

	@Query(value = "select a from StockOutPklist a inner join StockOut b on a.stockoutid_link = b.id and b.stockouttypeid_link = 1 where skutypeid_link = :skutypeid_link and a.skuid_link like %:skuid_link% and a.status = 0 and a.ydsprocessed > 0")
	public List<StockOutPklist>getAvailableFilter(@Param ("skuid_link")final Long skuid_link,@Param ("skutypeid_link")final Long skutypeid_link);

//	@Query(value = "select a from StockOutPklist a inner join StockOut b on a.stockoutid_link = b.id and b.stockouttypeid_link = 1 where a.skuid_link = :skuid_link and a.epc = :epc and a.status = 0")
//	public List<StockOutPklist>getAvailableByEpc(@Param ("skuid_link")final Long skuid_link, @Param ("epc")final String epc);
	
	@Query(value = "select a from StockOutPklist a inner join StockOut b on a.stockoutid_link = b.id and b.stockouttypeid_link = 1 where a.epc = :epc and a.status = 0")
	public List<StockOutPklist>getAvailableByEpc(@Param ("epc")final String epc);

	@Query(value = "select a from StockOutPklist a inner join StockOut b on a.stockoutid_link = b.id and b.stockouttypeid_link = 1 where a.skuid_link = :skuid_link and a.epc = :epc and a.status = 1")
	public List<StockOutPklist>getStockoutedByEpc(@Param ("skuid_link")final Long skuid_link, @Param ("epc")final String epc);

	@Query(value = "select a.skuid_link, 0 as ydsorigin, 0 as ydscheck, " +
			"SUM(a.ydsprocessed) as ydsprocessed, SUM(a.totalerror) as totalerror " + 
			"from StockOutPklist a inner join StockOut b on a.stockoutid_link = b.id and b.stockouttypeid_link = 1 and b.stockoutdate >= :stockoutdate_from and b.stockoutdate <= :stockoutdate_to " +
			"group by a.skuid_link")
	public List<Object[]> getSUMStockoutForProcess(@Param ("stockoutdate_from")final Date stockoutdate_from, @Param ("stockoutdate_to")final Date stockoutdate_to);
	
	@Query(value = "select a from StockOutPklist a inner join StockOut b "
			+ "on a.stockoutid_link = b.id and b.stockouttypeid_link = 1 "
			+ "and b.stockoutdate >= :stockoutdate_from and b.stockoutdate <= :stockoutdate_to ")
	public List<StockOutPklist> getDetailStockoutForProcess(@Param ("stockoutdate_from")final Date stockoutdate_from, @Param ("stockoutdate_to")final Date stockoutdate_to);

	@Query(value = "select a.skuid_link, 0 as ydsorigin, SUM(a.ydscheck) as ydscheck, " +
			"SUM(a.ydsprocessed) as ydsprocessed, SUM(a.totalerror) as totalerror " + 
			"from StockOutPklist a inner join StockOut b on a.stockoutid_link = b.id and b.stockouttypeid_link = 1 and b.stockoutdate >= :stockoutdate_from and b.stockoutdate <= :stockoutdate_to " +
			"group by a.skuid_link")
	public List<Object[]> getSUMStockoutForProcessAlone(@Param ("stockoutdate_from")final Date stockoutdate_from, @Param ("stockoutdate_to")final Date stockoutdate_to);

	@Query(value = "select a.skuid_link, SUM(a.ydsorigin) as ydsorigin, SUM(a.ydscheck) as ydscheck, " +
			"0 as ydsprocessed, 0 as totalerror " + 
			"from StockOutPklist a inner join StockOut b on a.stockoutid_link = b.id and b.stockouttypeid_link = 0 and b.stockoutdate >= :stockoutdate_from and b.stockoutdate <= :stockoutdate_to " +
			"group by a.skuid_link"
			)
	public List<Object[]> getSUMStockoutForCheck(@Param ("stockoutdate_from")final Date stockoutdate_from, @Param ("stockoutdate_to")final Date stockoutdate_to);

	@Query(value = "select a from StockOutPklist a inner join StockOut b "
			+ "on a.stockoutid_link = b.id and b.stockouttypeid_link = 0 "
			+ "and b.stockoutdate >= :stockoutdate_from and b.stockoutdate <= :stockoutdate_to ")
	public List<StockOutPklist> getDetailStockoutForCheck(@Param ("stockoutdate_from")final Date stockoutdate_from, @Param ("stockoutdate_to")final Date stockoutdate_to);	
}
