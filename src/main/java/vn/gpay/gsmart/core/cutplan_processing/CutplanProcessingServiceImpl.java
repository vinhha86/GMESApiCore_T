package vn.gpay.gsmart.core.cutplan_processing;

import java.util.Date;
import java.util.List;

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
public class CutplanProcessingServiceImpl extends AbstractService<CutplanProcessing> implements ICutplanProcessingService{

	@Autowired
	CutplanProcessingRepository repo;
	
	@Override
	protected JpaRepository<CutplanProcessing, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	
	@Override
	public Page<CutplanProcessing> cutplanProcessing_page(Date processingdate_from, Date processingdate_to, int limit, int page) {
		// TODO Auto-generated method stub
		Specification<CutplanProcessing> specification = Specifications.<CutplanProcessing>and()
//	            .ge((stockindate_from!=null && stockindate_to==null),"stockindate",DateFormat.atStartOfDay(stockindate_from))
//                .le((stockindate_from==null && stockindate_to!=null),"stockindate",DateFormat.atEndOfDay(stockindate_to))
                .between((processingdate_from!=null && processingdate_to!=null),"processingdate", GPAYDateFormat.atStartOfDay(processingdate_from), GPAYDateFormat.atEndOfDay(processingdate_to))
//                .ne("status", -1)
	            .build();
		Sort sort = Sorts.builder()
		        .desc("processingdate")
		        .build();
	    return repo.findAll(specification,PageRequest.of(page - 1, limit, sort));
	}

	@Override
	public List<CutplanProcessing> getby_cutplanrow(Long cutplanrowid_link) {
		// TODO Auto-generated method stub
		return repo.getby_cutplanrow(cutplanrowid_link);
	}

}
