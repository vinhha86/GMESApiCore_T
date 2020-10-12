package vn.gpay.gsmart.core.devices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class DeviceGroupService extends AbstractService<DeviceGroup> implements IDeviceGroupService {
	@Autowired
	DeviceGroupRepository repo;
	@Override	
	protected JpaRepository<DeviceGroup, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	
	@Override
	public List<DeviceGroupTree> createTree(final List<DeviceGroup> nodes) {
		Map<Long, DeviceGroupTree> mapTmp = new HashMap<>();
//		Long index = 0L;
		
		// Save all nodes (Org) to a map with Ids as keys, Org objs as values
		for(DeviceGroup current : nodes) {
			if(current.getParentid_link()==null) {
				current.setParentid_link(-1L);
			}
			DeviceGroupTree menu = new DeviceGroupTree();
			menu.setId(current.getId());
			menu.setCode(current.getCode());
			menu.setName(current.getName());
			menu.setParentid_link(current.getParentid_link());
			menu.setExpanded(true);
			menu.setChecked(current.checked);
			mapTmp.put(current.getId(), menu);
		}
		
		//loop and assign parent/child relationships
		for(DeviceGroup current : nodes) {
			if(current.getParentid_link()==null){
				current.setParentid_link(-1L);
			}
			Long parentId = current.getParentid_link();
			
			// if == -1 >> no parent
			if(parentId != -1) {
				DeviceGroupTree parent = mapTmp.get(parentId);
				if(parent != null) {
					DeviceGroupTree current_n = new DeviceGroupTree();
					current_n.setId(current.getId());
					current_n.setCode(current.getCode());
					current_n.setName(current.getName());
					current_n.setParentid_link(current.getParentid_link());
					current_n.setChecked(current.checked);
					parent.addChild(current_n);
					parent.setExpanded(false);
					mapTmp.put(parentId, parent);
					mapTmp.put(current_n.getId(), current_n);
				}
			}
			
		}
		
		// get the root
		List<DeviceGroupTree> root = new ArrayList<DeviceGroupTree>();
		
		for(DeviceGroupTree node : mapTmp.values()) {
			if(node.getParentid_link() == -1) {
				root.add(node);
				node.setExpanded(true);
			}
		}
		return root;
	}

	@Override
	public void deleteDeviceGroupById(long id) {
		repo.deleteDeviceGroupById(id);
	}

	@Override
	public void deleteDeviceGroupByParentId(long parentid_link) {
		repo.deleteDeviceGroupByParentId(parentid_link);
	}

	@Override
	public List<DeviceGroup> findByParentId(long parentid_link) {
		return repo.findByParentId(parentid_link);
	}

	@Override
	public List<DeviceGroup> findAllParent() {
		return repo.findAllParent();
	}

}
