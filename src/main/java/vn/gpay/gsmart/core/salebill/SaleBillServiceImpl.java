package vn.gpay.gsmart.core.salebill;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Sorts;
import com.github.wenhao.jpa.Specifications;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.utils.GPAYDateFormat;

@Service
public class SaleBillServiceImpl extends AbstractService<SaleBill> implements ISaleBillService{

	@Autowired
	SaleBillRepository repository; 
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	protected JpaRepository<SaleBill, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}

	@Override
	public List<SaleBill> salebill_list(Long orgbillid_link, String billcode, Date salebilldate_from,Date salebilldate_to) {
		// TODO Auto-generated method stub
		Specification<SaleBill> specification = Specifications.<SaleBill>and()
	            .eq( Objects.nonNull(orgbillid_link),"orgbillid_link", orgbillid_link)
	            .like(Objects.nonNull(billcode), "billcode", "%"+billcode+"%")
	            .ge((salebilldate_from!=null && salebilldate_to==null),"billdate",GPAYDateFormat.atStartOfDay(salebilldate_from))
                .le((salebilldate_from==null && salebilldate_to!=null),"billdate",GPAYDateFormat.atEndOfDay(salebilldate_to))
                .between((salebilldate_from!=null && salebilldate_to!=null),"billdate", GPAYDateFormat.atStartOfDay(salebilldate_from), GPAYDateFormat.atEndOfDay(salebilldate_to))
	            .build();
		Sort sort = Sorts.builder()
		        .desc("id")
		        .build();
	    return repository.findAll(specification,sort);
	}

	
	
}
