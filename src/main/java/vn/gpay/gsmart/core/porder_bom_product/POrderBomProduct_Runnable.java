package vn.gpay.gsmart.core.porder_bom_product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.gpay.gsmart.core.attributevalue.Attributevalue;
import vn.gpay.gsmart.core.attributevalue.IAttributeValueService;
import vn.gpay.gsmart.core.porder_bom_sku.POrderBOMSKU;
import vn.gpay.gsmart.core.sku.ISKU_AttributeValue_Service;

public class POrderBomProduct_Runnable implements Runnable {
	private final List<Long> list_colorid;
	private final POrderBomProduct pContractProductBom;
	private final IAttributeValueService avService;
	private final List<Long> List_size;
	private final List<POrderBOMSKU> listbomsku;
	private final List<POrderBOMSKU> listbomsku_kythuat;
	private final List<POrderBOMSKU> listbomsku_sanxuat;
	private final ISKU_AttributeValue_Service skuavService;
	private final long productid_link;
	private final List<Map<String, String>> listdata;

	public POrderBomProduct_Runnable(List<Long> list_colorid, POrderBomProduct pContractProductBom,
			IAttributeValueService avService, List<Long> List_size, List<POrderBOMSKU> listbomsku,
			List<POrderBOMSKU> listbomsku_kythuat, List<POrderBOMSKU> listbomsku_sanxuat,
			ISKU_AttributeValue_Service skuavService, long productid_link, List<Map<String, String>> listdata) {
		// TODO Auto-generated constructor stub
		this.list_colorid = list_colorid;
		this.pContractProductBom = pContractProductBom;
		this.avService = avService;
		this.List_size = List_size;
		this.listbomsku = listbomsku;
		this.listbomsku_kythuat = listbomsku_kythuat;
		this.listbomsku_sanxuat = listbomsku_sanxuat;
		this.skuavService = skuavService;
		this.productid_link = productid_link;
		this.listdata = listdata;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
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

			Attributevalue value = avService.findOne(colorid);
			String color_name = value.getValue();
			map.put("color_name", "" + color_name);

			Float total_amount = (float) 0;
			int total_size = 0;

			boolean check = false;
			for (Long size : List_size) {
				List<POrderBOMSKU> listbomsku_clone = new ArrayList<POrderBOMSKU>(listbomsku);
				List<POrderBOMSKU> listbomsku_kt_clone = new ArrayList<POrderBOMSKU>(listbomsku_kythuat);
				List<POrderBOMSKU> listbomsku_sx_clone = new ArrayList<POrderBOMSKU>(listbomsku_sanxuat);

				long skuid_link = skuavService.getsku_byproduct_and_valuemau_valueco(productid_link, colorid, size);
				listbomsku_clone.removeIf(c -> !c.getMaterialid_link().equals(pContractProductBom.getMaterialid_link())
						|| !c.getSkuid_link().equals(skuid_link));
				listbomsku_kt_clone
						.removeIf(c -> !c.getMaterialid_link().equals(pContractProductBom.getMaterialid_link())
								|| !c.getSkuid_link().equals(skuid_link));

				Float amount_size_kt = (float) 0;
				Float amount_size = (float) 0;
				Float amount_size_sx = (float) 0;

				if (listbomsku_clone.size() > 0)
					amount_size = listbomsku_clone.get(0).getAmount();

				if (listbomsku_kt_clone.size() > 0)
					amount_size_kt = listbomsku_kt_clone.get(0).getAmount();

				if (listbomsku_sx_clone.size() > 0)
					amount_size_sx = listbomsku_sx_clone.get(0).getAmount();

				map.put("" + size, amount_size + "");
				map.put(size + "_KT", amount_size_kt + "");
				map.put(size + "_SX", amount_size_sx + "");

				if (amount_size > 0 || amount_size_kt > 0) {
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
		}
	}

}
