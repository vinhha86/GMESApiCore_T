package vn.gpay.gsmart.core.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.StringAbstractService;

@Service
public class MenuServiceImpl extends StringAbstractService<Menu> implements IMenuService{

	@Autowired
	MenuRepository repository; 
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	protected JpaRepository<Menu, String> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}

	@Override
	public List<Menu> findByUserid(long userid) {
		// TODO Auto-generated method stub
		return repository.findByUserid(userid);
	}
	
	@Override
	public List<MenuTree> createTree(final List<Menu> nodes) {
		 
        Map<String, MenuTree> mapTmp = new HashMap<>();
        
        //Save all nodes to a map
        for (Menu current : nodes) {
        	MenuTree menu = new MenuTree();
        	menu.setId(current.getId());
        	menu.setText(current.getText_vi());
        	menu.setText_en(current.getText_en());
        	menu.setText_vi(current.getText_vi());
        	menu.setIconCls(current.getIcon());
        	//menu.setRowCls("nav-tree-badge");
        	menu.setRouteId(current.getId());
        	menu.setViewType(current.getXtype());
        	menu.setUrlc(current.getUrlc());
        	menu.setType(current.getType());
        	menu.setIndex(current.getIndex());
        	menu.setText_vi(current.getText_vi());
        	menu.setText_en(current.getText_en());
        	menu.setXtype_edit(current.getEdit());
        	menu.setTitle_edit_en(current.getTxtedit_en());
        	menu.setTitle_edit_vi(current.getTxtedit_vi());
        	menu.setXtype_new(current.getCreate());
        	menu.setTitle_new_en(current.getTxtcreate_en());
        	menu.setTitle_new_vi(current.getTxtcreate_vi());
        	menu.setTitle_vi(current.getTitle_list_vi());
        	menu.setTitle_en(current.getTitle_list_vi());
        	menu.setExpanded(true);
        	menu.setChecked(current.checked);
            mapTmp.put(current.getId(), menu);
        }
 
        //loop and assign parent/child relationships
        for (Menu current : nodes) {
            String parentId = current.getParent_id();
 
            if (parentId != null ) {
            	MenuTree parent = mapTmp.get(parentId);
                if (parent != null) {
                	MenuTree current_n = new MenuTree();
                	current_n.setId(current.getId());
                	current_n.setText(current.getText_vi());
                	current_n.setText_en(current.getText_en());
                	current_n.setText_vi(current.getText_vi());
                	current_n.setIconCls(current.getIcon());
                	//current_n.setRowCls("nav-tree-badge");
                	current_n.setRouteId(current.getId());
                	current_n.setViewType(current.getXtype());
                	current_n.setType(current.getType());
                	current_n.setIndex(current.getIndex());
                	current_n.setUrlc(current.getUrlc());
                	current_n.setText_vi(current.getText_vi());
                	current_n.setText_en(current.getText_en());
                	current_n.setXtype_edit(current.getEdit());
                	current_n.setTitle_edit_en(current.getTxtedit_en());
                	current_n.setTitle_edit_vi(current.getTxtedit_vi());
                	current_n.setXtype_new(current.getCreate());
                	current_n.setTitle_new_en(current.getTxtcreate_en());
                	current_n.setTitle_new_vi(current.getTxtcreate_vi());
                	current_n.setTitle_vi(current.getTitle_list_vi());
                	current_n.setTitle_en(current.getTitle_list_vi());     
                	current_n.setParent_id(parentId);
                	current_n.setChecked(current.checked);
                    parent.addChild(current_n);
                    parent.setExpanded(false);
                    parent.setRowCls("");
                    mapTmp.put(parentId, parent);
                    mapTmp.put(current_n.getId(), current_n);
                }
            }
 
        }
        
    
        //get the root
        List<MenuTree> root = new ArrayList<MenuTree>();
        for (MenuTree node : mapTmp.values()) {
            if(node.getParent_id()==null) {
                root.add(node);
            }
        }
 
        return root;
    }

	@Override
	public List<Menu> getby_parentid(String menu_parentid) {
		// TODO Auto-generated method stub
		return repository.getListMenu_byParent(menu_parentid);
	}

	@Override
	public List<Menu> getMenu_byRole(long roleid_link) {
		// TODO Auto-generated method stub
		return repository.getmenu_inroleid(roleid_link);
	}
}
