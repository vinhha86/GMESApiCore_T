package vn.gpay.gsmart.core.pcontractproductbom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.gpay.gsmart.core.attributevalue.Attributevalue;
import vn.gpay.gsmart.core.attributevalue.IAttributeValueService;
import vn.gpay.gsmart.core.pcontract_bom2_npl_poline.IPContract_bom2_npl_poline_Service;
import vn.gpay.gsmart.core.pcontract_bom2_npl_poline.PContract_bom2_npl_poline;
import vn.gpay.gsmart.core.pcontractbomsku.IPContractBOM2SKUService;
import vn.gpay.gsmart.core.pcontractbomsku.PContractBOM2SKU;

public class PContractProductBom2_Runnable implements Runnable{
	private final IPContract_bom2_npl_poline_Service po_npl_Service; 
	private final IAttributeValueService avService;
	private final IPContractBOM2SKUService ppbom2skuservice;
	
	private final Long productid_link;
	private final Long pcontractid_link;
	private final PContractProductBom2 pContractProductBom;
	private final List<Long> list_colorid;
	private final List<Long> List_size;
	private final List<PContractBOM2SKU> listbomsku;
	private final List<Map<String, String>> listdata;
	
	public PContractProductBom2_Runnable(Long productid_link, Long pcontractid_link, PContractProductBom2 pContractProductBom, List<Long> list_colorid, List<Long> List_size,
			List<PContractBOM2SKU> listbomsku, List<Map<String, String>> listdata, IPContract_bom2_npl_poline_Service po_npl_Service, IAttributeValueService avService,
			IPContractBOM2SKUService ppbom2skuservice) {
		// TODO Auto-generated constructor stub
		this.productid_link = productid_link;
		this.pcontractid_link = pcontractid_link;
		this.pContractProductBom = pContractProductBom;
		this.list_colorid = list_colorid;
		this.List_size = List_size;
		this.listdata = listdata;
		this.listbomsku = listbomsku;
		this.po_npl_Service = po_npl_Service;
		this.avService = avService;
		this.ppbom2skuservice = ppbom2skuservice;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		List<PContract_bom2_npl_poline> list_po = po_npl_Service.getby_product_and_npl(productid_link, pcontractid_link, pContractProductBom.getMaterialid_link());
		String str_po = "";
		for(PContract_bom2_npl_poline po_npl : list_po) {
			if(str_po == "") {
				str_po = po_npl.getPO_Buyer();
			}
			else {
				str_po += ", "+ po_npl.getPO_Buyer();
			}
			
		}
		
		for(Long colorid : list_colorid) {
			Map<String, String> map = new HashMap<String, String>();
								
			map.put("coKho", pContractProductBom.getCoKho().replace("ALL, ", "").replace(", ALL", "").replace("ALL", "")+"");
			
			map.put("createddate", pContractProductBom.getCreateddate()+"");
			
			map.put("createduserid_link", "0"+pContractProductBom.getCreateduserid_link());
			
			map.put("description", pContractProductBom.getDescription_product()+"");
			
			map.put("id", "0"+pContractProductBom.getId());
			
			map.put("lost_ratio", "0"+pContractProductBom.getLost_ratio());
			
			map.put("materialid_link", "0"+pContractProductBom.getMaterialid_link());
			
			map.put("materialName", pContractProductBom.getMaterialName()+"");
			
			map.put("materialCode", pContractProductBom.getMaterialCode()+"");
			
			map.put("orgrootid_link", "0"+pContractProductBom.getOrgrootid_link());
			
			map.put("pcontractid_link", "0"+pContractProductBom.getPcontractid_link());
			
			map.put("product_type", pContractProductBom.getProduct_type()+"");
			
			map.put("product_typename", pContractProductBom.getProduct_typeName()+"");
			
			map.put("productid_link", pContractProductBom.getProductid_link()+"");
			
			map.put("tenMauNPL", pContractProductBom.getTenMauNPL()+"");
			
			map.put("thanhPhanVai", pContractProductBom.getDescription_product()+"");
			
			map.put("unitName", pContractProductBom.getUnitName()+"");
			
			map.put("unitid_link", "0"+pContractProductBom.getUnitid_link());
			
			map.put("colorid_link", "0"+colorid);
			
			map.put("nhacungcap", pContractProductBom.getNhaCungCap());
			
			map.put("po_line", str_po);
			
			Attributevalue value = avService.findOne(colorid);
			String color_name = value.getValue();
			map.put("color_name", ""+color_name);
			
			Float total_amount = (float) 0;
			int total_size=0;
			
			boolean check = false;
			for(Long size : List_size) {
				List<PContractBOM2SKU> listbomsku_clone = new ArrayList<PContractBOM2SKU>(listbomsku);
				long skuid_link = ppbom2skuservice.getskuid_link_by_color_and_size(colorid, size, productid_link);
				listbomsku_clone.removeIf(c -> !c.getMaterial_skuid_link().equals(pContractProductBom.getMaterialid_link()) || 
						!c.getProduct_skuid_link().equals(skuid_link));
				Float amount_size = (float) 0;
				if(listbomsku_clone.size() > 0)
					amount_size = listbomsku_clone.get(0).getAmount();
				map.put(""+size, amount_size+"");
				
				if (amount_size>0){
					check = true;
					total_amount += amount_size;
					total_size++;
				}
			}
			
			if (total_size>0)
				map.put("amount", "0"+(total_amount/total_size));
			else
				map.put("amount", "0");
			if(check)
				listdata.add(map);	
		}
	}
}
