package vn.gpay.gsmart.core.stockout;

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
public class StockOutServiceImpl extends AbstractService<StockOut> implements IStockOutService{

	@Autowired
	StockOutRepository repository; 
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	protected JpaRepository<StockOut, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}
	@Override
	public List<StockOut> findByStockinCode(Long orgid_link,String stockoutcode) {
		// TODO Auto-generated method stub
		return repository.findByStockinCode(orgid_link,stockoutcode);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Results> test(String stockoutcode) {
		//Query query = entityManager.createNativeQuery("call NGB.getStateByName(?1)");
		//query.setParameter(1, title);
		
		Query query = entityManager.createNativeQuery("SELECT c.stockincode,c.invoicenumber from StockOut c where c.stockoutcode =:stockoutcode ");
		query.setParameter("stockoutcode", stockoutcode); 
        List<Object[]> objectList = query.getResultList();
        List<Results> result = new ArrayList<>();
        for (Object[] row : objectList) {
            result.add(new Results(row));
        }
        return result;
	}
	@Override
	public List<StockOut> stockout_getone(Long orgid_link,String stockoutcode, String stockcode) {
		// TODO Auto-generated method stub
		return repository.stockout_getone(orgid_link,stockoutcode, stockcode);
	}
	@Override
	public List<StockOut> stockout_list(Long orgid_link,Integer stockouttypeid_link,String stockoutcode, Long orgid_to_link, Date stockoutdate_from,
			Date stockoutdate_to) {
		// TODO Auto-generated method stub
		//return repository.stockout_list(stockouttypeid_link,stockoutcode, orgid_to_link,DateFormat.DateToString(stockoutdate_from),DateFormat.DateToString(stockoutdate_to));
		return repository.stockout_list(orgid_link,stockouttypeid_link,stockoutcode, orgid_to_link, stockoutdate_from, stockoutdate_to);
	}

	@Override
	public List<StockOut> stockout_listByOrgTo(Integer stockouttypeid_link, long orgid_to_link) {
		// TODO Auto-generated method stub
		return repository.stockout_listByOrgTo(stockouttypeid_link, orgid_to_link);
	}
	
	@Override
	public void updateStatusById(long id) {
		// TODO Auto-generated method stub
		repository.updateStatusById(id);
	}	
	@Override
	public Page<StockOut> stockout_list_page(Long orgrootid_link,Integer stockouttypeid_link,String stockoutcode,Long orgid_from_link, Long orgid_to_link, Date stockoutdate_from,
			Date stockoutdate_to,int page, int limit) {
		Specification<StockOut> specification = Specifications.<StockOut>and()
	            .eq( "orgrootid_link", orgrootid_link)
	            .eq(stockouttypeid_link != null, "stockouttypeid_link", stockouttypeid_link)
	            .like(Objects.nonNull(stockoutcode), "stockoutcode", "%"+stockoutcode+"%")
	            .eq(orgid_from_link != null, "orgid_from_link", orgid_from_link)
	            .eq(orgid_to_link != null, "orgid_to_link", orgid_to_link)
	            .ge(this.check1(stockoutdate_from,stockoutdate_to),"stockoutdate",GPAYDateFormat.atStartOfDay(stockoutdate_from))
                .le(this.check2(stockoutdate_from,stockoutdate_to),"stockoutdate",GPAYDateFormat.atEndOfDay(stockoutdate_to))
                .between(this.check3(stockoutdate_from,stockoutdate_to),"stockoutdate", GPAYDateFormat.atStartOfDay(stockoutdate_from), GPAYDateFormat.atEndOfDay(stockoutdate_to))
                .ne("status", -1)
	            .build();
		Sort sort = Sorts.builder()
		        .desc("stockoutdate")
		        .build();
	    return repository.findAll(specification, PageRequest.of(page - 1, limit, sort));
	}
	public boolean check1(Date stockoutdate_from,Date stockoutdate_to) {
		if(stockoutdate_from!=null && stockoutdate_to==null) return true;
		return false;
	}
	public boolean check2(Date stockoutdate_from,Date stockoutdate_to) {
		if(stockoutdate_from==null && stockoutdate_to!=null) return true;
		return false;
	}
	public boolean check3(Date stockoutdate_from,Date stockoutdate_to) {
		if(stockoutdate_from!=null && stockoutdate_to!=null) return true;
		return false;
	}
	@Override
	public List<StockOut> findByPO_Type_Status(Long pcontract_poid_link, Integer stockouttypeid_link, Integer status) {
		return repository.findByPO_Type_Status(pcontract_poid_link, stockouttypeid_link, status);
	}
	
}
