package vn.gpay.gsmart.core.encode;

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
public class Encode_Service extends AbstractService<Encode> implements IEncode_Service {
	@Autowired
	Encode_Repository repo;
	@Override
	protected JpaRepository<Encode, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<Encode> get_encode_by_porder_and_sku(long orgrootid_link, long porderid_link, long skuid_link) {
		// TODO Auto-generated method stub
		return repo.get_Encode_by_porder_and_sku(orgrootid_link, porderid_link, skuid_link);
	}
	@Override
	public List<Encode> get_encode_using_device(long orgrootid_link, long deviceid_link) {
		// TODO Auto-generated method stub
		return repo.get_Encode_using_device(orgrootid_link, deviceid_link);
	}
	@Override
	public Page<Encode> getlist_bypage(long orgrootid_link, String pordercode, long usercreateid_link,
			Date encodedatefrom, Date encodedateto, int limit, int page) {
		// TODO Auto-generated method stub
		Specification<Encode> specification = Specifications.<Encode>and()
	            .eq( "orgrootid_link", orgrootid_link)
	            .predicate(Specifications.or()
        				.like("porder.ordercode","%"+pordercode+"%")
        	            .build())
	            .eq(usercreateid_link != 0, "usercreateid_link", usercreateid_link)
	            .ge((encodedatefrom!=null && encodedateto==null),"encode_date",GPAYDateFormat.atStartOfDay(encodedatefrom))
                .le((encodedatefrom==null && encodedateto!=null),"encode_date",GPAYDateFormat.atEndOfDay(encodedateto))
                .between((encodedatefrom!=null && encodedateto!=null),"encode_date", GPAYDateFormat.atStartOfDay(encodedatefrom), GPAYDateFormat.atEndOfDay(encodedateto))
                .ne("status", -1)
	            .build();
		Sort sort = Sorts.builder()
		        .desc("id")
		        .build();
		
		Page<Encode> lst = repo.findAll(specification, PageRequest.of(page - 1, limit, sort));
		return lst;
	}

}
