package vn.gpay.gsmart.core.porder_bom_product;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import vn.gpay.gsmart.core.porder_bom_sku.POrderBOMSKU;

public class POrderBomProduct_Runnable implements Runnable {
	private Thread t;
	private List<Long> list_colorid;
	private POrderBomProduct pContractProductBom;
	private List<Long> List_size;
	private List<POrderBOMSKU> listbomsku;
	private List<POrderBOMSKU> listbomsku_kythuat;
	private List<POrderBOMSKU> listbomsku_sanxuat;
	private List<POrderBOMSKU> listbomsku_vien;
	private List<Map<String, String>> listdata;
	private Map<Long, String> mapcolor;
	private Map<String, Long> map_sku;
	CountDownLatch latch;
	List<POrderBomProduct> listbom;

	public POrderBomProduct_Runnable(List<Long> list_colorid, POrderBomProduct pContractProductBom,
			List<Long> List_size, List<POrderBOMSKU> listbomsku, List<POrderBOMSKU> listbomsku_kythuat,
			List<POrderBOMSKU> listbomsku_sanxuat, List<Map<String, String>> listdata, CountDownLatch latch,
			Map<Long, String> mapcolor, Map<String, Long> map_sku, List<POrderBomProduct> listbom,
			List<POrderBOMSKU> listbomsku_vien) {
		// TODO Auto-generated constructor stub
		this.list_colorid = list_colorid;
		this.pContractProductBom = pContractProductBom;
		this.List_size = List_size;
		this.listbomsku = listbomsku;
		this.listbomsku_kythuat = listbomsku_kythuat;
		this.listbomsku_sanxuat = listbomsku_sanxuat;
		this.listdata = listdata;
		this.latch = latch;
		this.mapcolor = mapcolor;
		this.map_sku = map_sku;
		this.listbom = listbom;
		this.listbomsku_vien = listbomsku_vien;
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
			for (Long colorid : list_colorid) {
				Map<String, String> map = new HashMap<String, String>();

				map.put("coKho", pContractProductBom.getCoKho() + "");

				map.put("createddate", pContractProductBom.getCreateddate() + "");

				map.put("createduserid_link", "0" + pContractProductBom.getCreateduserid_link());

				map.put("description", pContractProductBom.getDescription_product() + "");

				map.put("id", "0" + pContractProductBom.getId());

				map.put("lost_ratio", "0" + pContractProductBom.getLost_ratio());

				map.put("materialid_link", "0" + pContractProductBom.getMaterialid_link());

				map.put("materialName", pContractProductBom.getMaterialName() + "");

				map.put("materialCode", pContractProductBom.getMaterialCode() + "");

				map.put("orgrootid_link", "0" + pContractProductBom.getOrgrootid_link());

				map.put("pcontractid_link", "0" + pContractProductBom.getPcontractid_link());

				map.put("product_type", pContractProductBom.getProduct_type() + "");

				map.put("product_typeName", pContractProductBom.getProduct_typeName() + "");

				map.put("productid_link", pContractProductBom.getProductid_link() + "");

				map.put("tenMauNPL", pContractProductBom.getTenMauNPL() + "");

				map.put("thanhPhanVai", pContractProductBom.getDescription_product() + "");

				map.put("unitName", pContractProductBom.getUnitName() + "");

				map.put("unitid_link", "0" + pContractProductBom.getUnitid_link());

				map.put("colorid_link", "0" + colorid);

				String color_name = mapcolor.get(colorid);
				map.put("color_name", "" + color_name);

				Float total_amount = (float) 0;
				int total_size = 0;

				boolean check = false;
				long materialid_link = pContractProductBom.getMaterialid_link();
				for (Long size : List_size) {
					List<POrderBOMSKU> listbomsku_clone = new ArrayList<POrderBOMSKU>(listbomsku);
					List<POrderBOMSKU> listbomsku_kt_clone = new ArrayList<POrderBOMSKU>(listbomsku_kythuat);
					List<POrderBOMSKU> listbomsku_sx_clone = new ArrayList<POrderBOMSKU>(listbomsku_sanxuat);
					List<POrderBOMSKU> listbomsku_vien_clone = new ArrayList<POrderBOMSKU>(listbomsku_vien);

					long skuid_link = map_sku.get(colorid + "_" + size);
					listbomsku_clone.removeIf(c -> !c.getMaterialid_link().equals(materialid_link)
							|| !c.getSkuid_link().equals(skuid_link));

					listbomsku_kt_clone.removeIf(c -> !c.getMaterialid_link().equals(materialid_link)
							|| !c.getSkuid_link().equals(skuid_link));

					listbomsku_sx_clone.removeIf(c -> !c.getMaterialid_link().equals(materialid_link)
							|| !c.getSkuid_link().equals(skuid_link));

					listbomsku_vien_clone.removeIf(c -> !c.getMaterialid_link().equals(materialid_link)
							|| !c.getSkuid_link().equals(skuid_link));

					Float amount_size_kt = (float) 0;
					Float amount_size = (float) 0;
					Float amount_size_sx = (float) 0;
					float amount_size_vien = 0;

					if (listbomsku_clone.size() > 0)
						amount_size = listbomsku_clone.get(0).getAmount();

					if (listbomsku_kt_clone.size() > 0)
						amount_size_kt = listbomsku_kt_clone.get(0).getAmount();

					if (listbomsku_sx_clone.size() > 0)
						amount_size_sx = listbomsku_sx_clone.get(0).getAmount();

					if (listbomsku_vien_clone.size() > 0)
						amount_size_vien = listbomsku_vien_clone.get(0).getAmount();

					map.put("" + size, amount_size + "");
					map.put(size + "_KT", amount_size_kt + "");
					map.put(size + "_SX", amount_size_sx + "");
					map.put(size + "_Vien", amount_size_vien + "");
					map.put(size + "_Tong", amount_size_kt + amount_size_vien + "");
					if(amount_size != null && amount_size_kt != null) {
						DecimalFormat df = new DecimalFormat("0.00");
						Float chechLech = amount_size - amount_size_kt;
						Float phanTramChechLech = (float) 0;
						String phanTramChechLechString = "";
						if(chechLech > 0) {
							phanTramChechLech = chechLech / amount_size * 100;
							phanTramChechLechString = df.format(phanTramChechLech);
							phanTramChechLechString = "-" + phanTramChechLechString + "%";
						}else if(chechLech < 0) {
							chechLech = amount_size_kt - amount_size;
							phanTramChechLech = chechLech / amount_size * 100;
							phanTramChechLechString = df.format(phanTramChechLech);
							phanTramChechLechString = "+" + phanTramChechLechString + "%";
						}else {
							phanTramChechLech = (float) 0;
							phanTramChechLechString = phanTramChechLech + "";
						}
						map.put(size + "_PhanTramChenhLech", phanTramChechLechString);
					}

					if (amount_size > 0) {
						check = true;
						total_amount += amount_size;
						total_size++;
					}
				}

				if (total_size > 0)
					map.put("amount", "0" + (total_amount / total_size));
				else
					map.put("amount", "0");
				if (check)
					listdata.add(map);
				listbom.remove(pContractProductBom);
			}
		} catch (Exception e) {
		} finally {
			latch.countDown();
		}
	}

}
