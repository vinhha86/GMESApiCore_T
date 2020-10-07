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
public class WareHouse_Encode_EPC_Service extends AbstractService<WareHouse_Encode_EPC> implements IWareHouse_Encode_EPC_Service{

	@Autowired
	WareHouse_Encode_EPC_Repository repository; 
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	protected JpaRepository<WareHouse_Encode_EPC, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}

	@Override
	public List<WareHouse_Encode_EPC> encode_getbydevice(Long orgrootid_link,Long deviceid_link) {
		// TODO Auto-generated method stub
		Specification<WareHouse_Encode_EPC> specification = Specifications.<WareHouse_Encode_EPC>and()
	            .eq("orgrootid_link", orgrootid_link)
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

	@Override
	public List<WareHouse_Encode_EPC> getTagEncode_byOldEPC(String OldEPC, long orgrootid_link, long warehouse_encodeid_link) {
		// TODO Auto-generated method stub
		return repository.getTagEncode_byOldEPC(orgrootid_link, OldEPC, warehouse_encodeid_link);
	}	
	
	@Override
	public List<WareHouse_Encode_EPC> encode_getbyepc(Long orgrootid_link, String epc) {
		// TODO Auto-generated method stub
		return repository.encode_getbyepc(orgrootid_link, epc);
	}	
	@Override
	public List<WareHouse_Encode_EPC> encode_getbyencodeid(Long warehouse_encodeid_link) {
		// TODO Auto-generated method stub
		return repository.encode_getbyencodeid(warehouse_encodeid_link);
	}		
}
