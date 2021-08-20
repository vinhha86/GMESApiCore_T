package vn.gpay.gsmart.core.pcontract_po;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import vn.gpay.gsmart.core.productpairing.IProductPairingService;
import vn.gpay.gsmart.core.productpairing.ProductPairing;

public class Total_pair_Runnable implements Runnable {
	private Thread t;
	CountDownLatch latch;
	private IProductPairingService pairService;
	private PContract_PO po;
	private long orgrootid_link;
	private PContractPO_Shipping ship;

	public Total_pair_Runnable(CountDownLatch latch, IProductPairingService pairService, PContract_PO po,
			long orgrootid_link, PContractPO_Shipping ship) {
		// TODO Auto-generated constructor stub
		this.latch = latch;
		this.pairService = pairService;
		this.po = po;
		this.orgrootid_link = orgrootid_link;
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
			List<ProductPairing> p = pairService.getproduct_pairing_detail_bycontract(orgrootid_link,
					po.getPcontractid_link(), po.getProductid_link());
			int total = 1;
			if (p.size() > 0) {
				total = 0;
				for (ProductPairing pair : p) {
					total += pair.getAmount();
				}
			}
			ship.setTotalpair(total);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			latch.countDown();
		}
	}

}
