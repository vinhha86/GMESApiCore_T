package vn.gpay.gsmart.core.sizesetattributevalue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class SizeSetAttributeService extends AbstractService<SizeSetAttributeValue> implements ISizeSetAttributeService{

	@Autowired ISizeSetAttributeRepository repo;
	
	@Override
	public List<SizeSetAttributeValue> getall_bySizeSetId(Long sizesetid_link) {
		// TODO Auto-generated method stub
		return repo.getall_bySizeSetId(sizesetid_link);
	}

	@Override
	public List<SizeSetAttributeValue> getList_byAttId(Long attributeid_link, Long sizesetid_link) {
		// TODO Auto-generated method stub
		return repo.getlistvalue_byattribute(attributeid_link, sizesetid_link);
	}

	@Override
	public List<SizeSetAttributeValue> getOne_bysizeset_and_value(long sizesetid_link, long attributeid_link,
			long attributevalueid_link) {
		// TODO Auto-generated method stub
		return repo.getOne_bysizeset_and_value(sizesetid_link, attributeid_link, attributevalueid_link);
	}

	@Override
	protected JpaRepository<SizeSetAttributeValue, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<SizeSetAttributeValue> getallother_bySizeSetId(Long sizesetid_link){
		return repo.getallother_bySizeSetId(sizesetid_link);
		
	}
}
