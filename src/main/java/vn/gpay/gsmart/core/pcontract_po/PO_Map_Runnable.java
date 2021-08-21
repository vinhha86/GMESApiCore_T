package vn.gpay.gsmart.core.pcontract_po;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class PO_Map_Runnable implements Runnable {
	private Thread t;
	CountDownLatch latch;
	private PContractPO_Shipping ship;
	private PContract_PO po;

	public PO_Map_Runnable(CountDownLatch latch, PContractPO_Shipping ship, PContract_PO po) {
		// TODO Auto-generated constructor stub
		this.latch = latch;
		this.ship = ship;
		this.po = po;
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
		try {
//			ship.setActual_quantity(po.getActual_quantity());
//			ship.setActual_shipdate(po.getActual_shipdate());
//			ship.setCode(po.getCode());
//			ship.setComment(po.getComment());
//			ship.setCurrencyid_link(po.getCurrencyid_link());
//			ship.setDate_importdata(po.getDate_importdata());
//			ship.setDatecreated(po.getDatecreated());
//			ship.setEtm_avr(po.getEtm_avr());
//			ship.setEtm_from(po.getEtm_from());
//			ship.setEtm_to(po.getEtm_to());
//			ship.setExchangerate(po.getExchangerate());
//			ship.setId(po.getId());
//			ship.setIs_tbd(po.getIs_tbd());
//			ship.setIsauto_calculate(po.getIsauto_calculate());
//			ship.setMatdate(po.getMatdate());
//			ship.setMerchandiserid_link(po.getMerchandiserid_link());
//			ship.setOrgmerchandiseid_link(po.getOrgmerchandiseid_link());
//			ship.setOrgrootid_link(po.getOrgrootid_link());
//			ship.setPackingnotice(po.getPackingnotice());
//			ship.setParentpoid_link(po.getParentpoid_link());
//			ship.setPcontractid_link(po.getPcontractid_link());
//			ship.setPlan_linerequired(po.getPlan_linerequired());
//			ship.setPlan_productivity(po.getPlan_productivity());
//			ship.setPo_buyer(po.getPo_buyer());
//			ship.setPo_quantity(po.getPo_quantity());
//			ship.setPo_typeid_link(po.getPo_typeid_link());
//			ship.setPo_vendor(po.getPo_vendor());
//			ship.setPortfromid_link(po.getPortfromid_link());
//			ship.setPorttoid_link(po.getPorttoid_link());
//			ship.setPrice_add(po.getPrice_add());
//			ship.setPrice_cmp(po.getPrice_cmp());
//			ship.setPrice_commission(po.getPrice_commission());
//			ship.setPrice_fob(po.getPrice_fob());
//			ship.setPrice_sweingfact(po.getPrice_sweingfact());
//			ship.setPrice_sweingtarget(po.getPrice_sweingtarget());
//			ship.setProductid_link(po.getProductid_link());
//			ship.setProductiondate(po.getProductiondate());
//			ship.setProductiondays(po.getProductiondays());
//			ship.setProductiondays_ns(po.getProductiondays_ns());
//			ship.setQcorgid_link(po.getQcorgid_link());
//			ship.setQcorgname(po.getQcorgname());
//			ship.setSalaryfund(po.getSalaryfund());
//			ship.setSewtarget_percent(po.getSewtarget_percent());
//			ship.setShipdate(po.getShipdate());
//			ship.setShipmodeid_link(po.getShipmodeid_link());
//			ship.setStatus(po.getStatus());
//			ship.setUnitid_link(po.getUnitid_link());
//			ship.setUsercreatedid_link(po.getUsercreatedid_link());
//			ship.setProductbuyercode(po.getProductbuyercode());
//			ship.setPortFrom(po.getPortFrom());
//			ship.setShipmode_name(po.getShipMode());
//			ship.setIsmap(po.getIsmap());

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			latch.countDown();
		}
	}

}
