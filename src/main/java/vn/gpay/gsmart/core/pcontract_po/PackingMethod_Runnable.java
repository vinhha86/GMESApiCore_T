package vn.gpay.gsmart.core.pcontract_po;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import vn.gpay.gsmart.core.packingtype.IPackingTypeRepository;
import vn.gpay.gsmart.core.packingtype.PackingType;

public class PackingMethod_Runnable implements Runnable {
	private Thread t;
	CountDownLatch latch;
	private PContract_PO po;
	private long orgrootid_link;
	private IPackingTypeRepository packing_repo;
	private PContractPO_Shipping ship;

	public PackingMethod_Runnable(CountDownLatch latch, PContract_PO po, long orgrootid_link,
			IPackingTypeRepository packing_repo, PContractPO_Shipping ship) {
		// TODO Auto-generated constructor stub
		this.latch = latch;
		this.po = po;
		this.orgrootid_link = orgrootid_link;
		this.packing_repo = packing_repo;
		this.ship = ship;
	}

	public void start() {
		if (t == null) {
			int unboundedRandomValue = ThreadLocalRandom.current().nextInt();
			t = new Thread(this, String.valueOf(unboundedRandomValue));
			t.start();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			if (!po.getPackingnotice().equals("") && !po.getPackingnotice().equals("null")
					&& !po.getPackingnotice().equals(null)) {
				String[] arr_id = po.getPackingnotice().split(";");
				List<Long> list_id = new ArrayList<Long>();
				for (String id : arr_id) {
					list_id.add(Long.parseLong(id));
				}
				List<PackingType> list_packing = packing_repo.getbylistid(orgrootid_link, list_id);
				String packing_method = "";
				for (PackingType packing : list_packing) {
					if (packing_method != "") {
						packing_method += ", " + packing.getCode();
					} else {
						packing_method = packing.getCode();
					}
				}
				ship.setPacking_method(packing_method);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			latch.countDown();
		}
	}

}
