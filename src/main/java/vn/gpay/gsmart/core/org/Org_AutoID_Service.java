package vn.gpay.gsmart.core.org;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.LockTimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class Org_AutoID_Service extends AbstractService<Org_AutoID> implements IOrg_AutoID_Service {
	@Autowired Org_AutoID_Repository repo;
	@Override
	protected JpaRepository<Org_AutoID, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<String> getLastID(String Prefix) {
		try {
			if (repo.fetchLastID(Prefix).size() > 0){
				Org_AutoID orgID = repo.fetchLastID(Prefix).get(0);
				orgID.setMaxvalue(orgID.getMaxvalue() + 1);
				repo.save(orgID);
				
				List<String> result = new ArrayList<String>();
				String code = orgID.getPrefix() + "_T" + orgID.getMaxvalue().toString();
				String name = "Tổ " + orgID.getMaxvalue().toString();
				result.add(code);
				result.add(name);
				
				return result;
			} else {
				Org_AutoID orgID = new Org_AutoID();
				orgID.setPrefix(Prefix);
				orgID.setMaxvalue(1);
				repo.save(orgID);
				
				List<String> result = new ArrayList<String>();
				String code = orgID.getPrefix() + "_T" + orgID.getMaxvalue().toString();
				String name = "Tổ " + orgID.getMaxvalue().toString();
				result.add(code);
				result.add(name);
				
				return result;
			}
		} catch(LockTimeoutException ex){
			long waiting = 3000;//Cho 3s va thu lai
			try {
				Thread.sleep(waiting);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			if (repo.fetchLastID("SX").size() > 0){
//				PContract_AutoID pOrderID = repo.fetchLastID("SX").get(0);
//				pOrderID.setMaxvalue(pOrderID.getMaxvalue() + 1);
//				repo.save(pOrderID);
//				
//				return pOrderID.getPrefix() + "_" + pOrderID.getMaxvalue().toString();
//			} else {
//				PContract_AutoID pOrderID = new PContract_AutoID();
//				pOrderID.setPrefix("SX");
//				pOrderID.setMaxvalue(1);
//				repo.save(pOrderID);
//				
//				return pOrderID.getPrefix() + "_" + pOrderID.getMaxvalue().toString();
//			}
		}
		return null;
	}

}
