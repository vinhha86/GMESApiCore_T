package vn.gpay.gsmart.core.packingtype;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;


import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PackingTypeService extends AbstractService<PackingType> implements IPackingTypeService {
	@Autowired IPackingTypeRepository repo;
	@Override
	protected JpaRepository<PackingType, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	
	@Override
	public List<PackingType> getall_byorgrootid(long orgrootid_link) {
		// TODO Auto-generated method stub
		return repo.getall_byorgrootid(orgrootid_link);
	}

	@Override
	public List<PackingType> getbyname(String name, long orgrootid_link) {
		// TODO Auto-generated method stub
		return repo.getbyname(orgrootid_link, name);
	}

	@Override
	public String getby_listid(String listid, long orgrootid_link) {
		if(listid=="") return "";
		// TODO Auto-generated method stub
		List<Long> list = new ArrayList<Long>();
		String[] arr_id = listid.split(",");
		for(String s_id : arr_id) {
			list.add(Long.parseLong(s_id));
		}
		List<PackingType> list_packing = repo.getbylistid(orgrootid_link, list);
		String name = "";
		for(PackingType packing : list_packing) {
			if(name=="")
				name += packing.getCode();
			else 
				name += "," + packing.getCode();
		}
		return name;
	}
}
