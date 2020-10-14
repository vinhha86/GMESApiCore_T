package vn.gpay.gsmart.core.handover;

import javax.persistence.LockTimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class Handover_AutoID_Service extends AbstractService<Handover_AutoID> implements IHandover_AutoID_Service {
	@Autowired Handover_AutoID_Repository repo;
	@Override
	protected JpaRepository<Handover_AutoID, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public String getLastID(String Prefix) {
		try {
			if (repo.fetchLastID(Prefix).size() > 0){
				Handover_AutoID handoverID = repo.fetchLastID(Prefix).get(0);
				handoverID.setMaxvalue(handoverID.getMaxvalue() + 1);
				repo.save(handoverID);
				
				return handoverID.getPrefix() + "_" + handoverID.getMaxvalue().toString();
			} else {
				Handover_AutoID handoverID = new Handover_AutoID();
				handoverID.setPrefix(Prefix);
				handoverID.setMaxvalue(1);
				repo.save(handoverID);
				
				return handoverID.getPrefix() + "_" + handoverID.getMaxvalue().toString();
			}
		} catch(LockTimeoutException ex){
			long waiting = 3000;//Cho 3s va thu lai
			try {
				Thread.sleep(waiting);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (repo.fetchLastID("P").size() > 0){
				Handover_AutoID handoverID = repo.fetchLastID("SX").get(0);
				handoverID.setMaxvalue(handoverID.getMaxvalue() + 1);
				repo.save(handoverID);
				
				return handoverID.getPrefix() + "_" + handoverID.getMaxvalue().toString();
			} else {
				Handover_AutoID pOrderID = new Handover_AutoID();
				pOrderID.setPrefix("P");
				pOrderID.setMaxvalue(1);
				repo.save(pOrderID);
				
				return pOrderID.getPrefix() + "_" + pOrderID.getMaxvalue().toString();
			}
		}
	}
}
