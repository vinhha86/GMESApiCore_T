package vn.gpay.gsmart.core.stockin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Sorts;
import com.github.wenhao.jpa.Specifications;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.utils.GPAYDateFormat;

@Service
public class StockInServiceImpl extends AbstractService<StockIn> implements IStockInService{

	@Autowired
	StockInRepository repository; 
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	protected JpaRepository<StockIn, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}
	@Override
	public List<StockIn> findByStockinCode(Long orgid_link,String stockincode) {
		// TODO Auto-generated method stub
		return repository.findByStockinCode(orgid_link,stockincode);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Results> test(String stockincode) {
		//Query query = entityManager.createNativeQuery("call NGB.getStateByName(?1)");
		//query.setParameter(1, title);
		
		Query query = entityManager.createNativeQuery("SELECT c.stockincode,c.invoicenumber from StockIn c where c.stockincode =:stockincode ");
		query.setParameter("stockincode", stockincode); 
        List<Object[]> objectList = query.getResultList();
        List<Results> result = new ArrayList<>();
        for (Object[] row : objectList) {
            result.add(new Results(row));
        }
        return result;
	}
	@Override
	public List<StockIn> stockin_getone(Long orgid_link,String stockincode, String stockcode) {
		// TODO Auto-generated method stub
		return repository.stockin_getone(orgid_link,stockincode, stockcode);
	}
	@Override
	public List<StockIn> stocin_list(Long orgid_link,Long stockintypeid_link,String stockincode,Long orgid_from_link, Long orgid_to_link, Date stockindate_from,
			Date stockindate_to,int status) {
		if(stockincode==null ) stockincode ="";
		Specification<StockIn> specification = Specifications.<StockIn>and()
	            .eq( "orgid_link", orgid_link)
	            .eq(Objects.nonNull(stockintypeid_link), "stockintypeid_link", stockintypeid_link)
	            .like(Objects.nonNull(stockincode), "stockincode", "%"+stockincode+"%")
	            .eq(Objects.nonNull(orgid_from_link), "orgid_from_link", orgid_from_link)
	            .eq(Objects.nonNull(orgid_to_link), "orgid_to_link", orgid_to_link)
	            .ge((stockindate_from!=null && stockindate_to==null),"stockindate",GPAYDateFormat.atStartOfDay(stockindate_from))
                .le((stockindate_from==null && stockindate_to!=null),"stockindate",GPAYDateFormat.atEndOfDay(stockindate_to))
                .between((stockindate_from!=null && stockindate_to!=null),"stockindate", GPAYDateFormat.atStartOfDay(stockindate_from), GPAYDateFormat.atEndOfDay(stockindate_to))
                .eq( status!=-1,"status", status)
	            .build();
		Sort sort = Sorts.builder()
		        .desc("stockindate")
		        .build();
	    return repository.findAll(specification,sort);
		//return repository.stockin_list(orgid_link,stockouttypeid_link,stockincode, orgid_from_link,orgid_to_link, stockindate_from, stockindate_to);
	}
	@Override
	public Page<StockIn> stockin_page(Long orgrootid_link, Long stockintypeid_link, Long orgid_from_link,
			Long orgid_to_link, Date stockindate_from, Date stockindate_to, int limit, int page) {
		// TODO Auto-generated method stub
		Specification<StockIn> specification = Specifications.<StockIn>and()
	            .eq( "orgrootid_link", orgrootid_link)
	            .eq(stockintypeid_link != null, "stockintypeid_link", stockintypeid_link)
	            .eq(orgid_from_link != null, "orgid_from_link", orgid_from_link)
	            .eq(orgid_to_link != null, "orgid_to_link", orgid_to_link)
//	            .ge((stockindate_from!=null && stockindate_to==null),"stockindate",DateFormat.atStartOfDay(stockindate_from))
//                .le((stockindate_from==null && stockindate_to!=null),"stockindate",DateFormat.atEndOfDay(stockindate_to))
                .between((stockindate_from!=null && stockindate_to!=null),"stockindate", GPAYDateFormat.atStartOfDay(stockindate_from), GPAYDateFormat.atEndOfDay(stockindate_to))
                .ne("status", -1)
	            .build();
		Sort sort = Sorts.builder()
		        .desc("stockindate")
		        .build();
	    return repository.findAll(specification,PageRequest.of(page - 1, limit, sort));
	}
	
}
