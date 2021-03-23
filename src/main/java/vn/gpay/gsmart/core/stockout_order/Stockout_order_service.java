package vn.gpay.gsmart.core.stockout_order;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import com.github.wenhao.jpa.Sorts;
import com.github.wenhao.jpa.Specifications;
import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.utils.GPAYDateFormat;

@Service
public class Stockout_order_service extends AbstractService<Stockout_order> implements IStockout_order_service {
	@Autowired Stockout_order_repository repo;
	@Override
	protected JpaRepository<Stockout_order, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<Stockout_order> getby_porder(Long porderid_link) {
		// TODO Auto-generated method stub
		return repo.getby_porder(porderid_link);
	}

	@Override
	public List<Stockout_order> findBySearch(Date stockoutorderdate_from, Date stockoutorderdate_to) {
		Specification<Stockout_order> specification = Specifications.<Stockout_order>and()
	            .ge(this.check1(stockoutorderdate_from,stockoutorderdate_to),"timecreate",GPAYDateFormat.atStartOfDay(stockoutorderdate_from))
                .le(this.check2(stockoutorderdate_from,stockoutorderdate_to),"timecreate",GPAYDateFormat.atEndOfDay(stockoutorderdate_to))
                .between(this.check3(stockoutorderdate_from,stockoutorderdate_to),"timecreate", GPAYDateFormat.atStartOfDay(stockoutorderdate_from), GPAYDateFormat.atEndOfDay(stockoutorderdate_to))
	            .build();
		Sort sort = Sorts.builder()
		        .desc("timecreate")
		        .build();
	    return repo.findAll(specification,sort);
	}
	
	private boolean check1(Date stockoutorderdate_from,Date stockoutorderdate_to) {
		if(stockoutorderdate_from!=null && stockoutorderdate_to==null) return true;
		return false;
	}
	private boolean check2(Date stockoutorderdate_from,Date stockoutorderdate_to) {
		if(stockoutorderdate_from==null && stockoutorderdate_to!=null) return true;
		return false;
	}
	private boolean check3(Date stockoutorderdate_from,Date stockoutorderdate_to) {
		if(stockoutorderdate_from!=null && stockoutorderdate_to!=null) return true;
		return false;
	}
}
