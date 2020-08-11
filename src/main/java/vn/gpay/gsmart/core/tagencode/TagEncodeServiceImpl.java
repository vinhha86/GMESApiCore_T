package vn.gpay.gsmart.core.tagencode;

import java.util.Date;
import java.util.List;

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
public class TagEncodeServiceImpl extends AbstractService<TagEncode> implements ITagEncodeService{

	@Autowired
	TagEncodeRepository repository; 
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	protected JpaRepository<TagEncode, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}

	@Override
	public List<TagEncode> encode_getbydevice(Long orgid_link,Long deviceid_link) {
		// TODO Auto-generated method stub
		Specification<TagEncode> specification = Specifications.<TagEncode>and()
	            .eq("orgid_link", orgid_link)
	            .eq( "deviceid_link", deviceid_link)
	         //   .eq( "date_trunc('year',timecreate)", new Date())
	            .between("timecreate", GPAYDateFormat.atStartOfDay(new Date()), GPAYDateFormat.atEndOfDay(new Date()))
	            .build();
	            
	            Sort sort = Sorts.builder()
		        .desc("timecreate")
		        .build();
	    return repository.findAll(specification,sort);
	}

	
	@Override
	public void deleteByEpc(String epc,long orgid_link) {
		repository.deleteByEpc(epc, orgid_link);
	}	
	
}
