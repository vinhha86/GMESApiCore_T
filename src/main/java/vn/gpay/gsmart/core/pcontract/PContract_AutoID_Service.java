package vn.gpay.gsmart.core.pcontract;

import javax.persistence.LockTimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PContract_AutoID_Service extends AbstractService<PContract_AutoID> implements IPContract_AutoID_Service {
	@Autowired PContract_AutoID_Repository repo;
	@Override
	protected JpaRepository<PContract_AutoID, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public String getLastID(String Prefix) {
		try {
			if (repo.fetchLastID(Prefix).size() > 0){
				PContract_AutoID pOrderID = repo.fetchLastID(Prefix).get(0);
				pOrderID.setMaxvalue(pOrderID.getMaxvalue() + 1);
				repo.save(pOrderID);
				
				return pOrderID.getPrefix() + "_" + pOrderID.getMaxvalue().toString();
			} else {
				PContract_AutoID pOrderID = new PContract_AutoID();
				pOrderID.setPrefix(Prefix);
				pOrderID.setMaxvalue(1);
				repo.save(pOrderID);
				
				return pOrderID.getPrefix() + "_" + pOrderID.getMaxvalue().toString();
			}
		} catch(LockTimeoutException ex){
			long waiting = 3000;//Cho 3s va thu lai
			try {
				Thread.sleep(waiting);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (repo.fetchLastID("SX").size() > 0){
				PContract_AutoID pOrderID = repo.fetchLastID("SX").get(0);
				pOrderID.setMaxvalue(pOrderID.getMaxvalue() + 1);
				repo.save(pOrderID);
				
				return pOrderID.getPrefix() + "_" + pOrderID.getMaxvalue().toString();
			} else {
				PContract_AutoID pOrderID = new PContract_AutoID();
				pOrderID.setPrefix("SX");
				pOrderID.setMaxvalue(1);
				repo.save(pOrderID);
				
				return pOrderID.getPrefix() + "_" + pOrderID.getMaxvalue().toString();
			}
		}
	}
}
