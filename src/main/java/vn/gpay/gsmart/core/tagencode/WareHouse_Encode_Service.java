package vn.gpay.gsmart.core.tagencode;

import java.util.Date;

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
public class WareHouse_Encode_Service extends AbstractService<WareHouse_Encode> implements IWareHouse_Encode_Service {
	@Autowired WareHouse_Encode_Repository repo;
	@Override
	protected JpaRepository<WareHouse_Encode, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public Page<WareHouse_Encode> getlist_bypage(long orgrootid_link, long orgencodeid_link, long usercreateid_link, Date timecreatefrom,
			Date timecreateto, int limit, int page) {
		// TODO Auto-generated method stub
		Specification<WareHouse_Encode> specification = Specifications.<WareHouse_Encode>and()
	            .eq( "orgrootid_link", orgrootid_link)
	            .eq(orgencodeid_link != 0, "orgencodeid_link", orgencodeid_link)
	            .eq(usercreateid_link != 0, "usercreateid_link", usercreateid_link)
	            .ge((timecreatefrom!=null && timecreateto==null),"timecreate",GPAYDateFormat.atStartOfDay(timecreatefrom))
                .le((timecreatefrom==null && timecreateto!=null),"timecreate",GPAYDateFormat.atEndOfDay(timecreateto))
                .between((timecreatefrom!=null && timecreateto!=null),"timecreate", GPAYDateFormat.atStartOfDay(timecreatefrom), GPAYDateFormat.atEndOfDay(timecreateto))
                .ne("status", -1)
	            .build();
		Sort sort = Sorts.builder()
		        .desc("timecreate")
		        .build();
	    return repo.findAll(specification,PageRequest.of(page - 1, limit, sort));
	}

}
