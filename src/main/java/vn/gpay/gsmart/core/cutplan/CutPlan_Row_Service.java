package vn.gpay.gsmart.core.cutplan;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.cutplan_processing.CutplanProcessing;
import vn.gpay.gsmart.core.cutplan_processing.ICutplanProcessingService;
import vn.gpay.gsmart.core.pcontractproductsku.IPContractProductSKURepository;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_bom_color.IPOrderBomColor_Service;
import vn.gpay.gsmart.core.porder_bom_color.PorderBomColor;
import vn.gpay.gsmart.core.porder_bom_product.IPOrderBomProduct_Service;
import vn.gpay.gsmart.core.porder_bom_product.POrderBomProduct;
import vn.gpay.gsmart.core.porder_bom_sku.IPOrderBOMSKU_Service;
import vn.gpay.gsmart.core.porder_bom_sku.POrderBOMSKU;
import vn.gpay.gsmart.core.utils.AtributeFixValues;
import vn.gpay.gsmart.core.utils.CutPlanRowType;
import vn.gpay.gsmart.core.utils.POrderBomType;

@Service
public class CutPlan_Row_Service extends AbstractService<CutPlan_Row> implements ICutPlan_Row_Service{
	@Autowired CutPlan_Row_Repository repo;
	@Autowired IPContractProductSKURepository sku_repo; 
	@Autowired IPOrderBOMSKU_Service porderbomskuService;
	@Autowired ICutPlan_Size_Service cutplan_size_Service;
	@Autowired ICutPlan_Row_Service cutrow_Service;
	@Autowired IPOrderBomProduct_Service porderbomproductService;
	@Autowired IPOrderBomColor_Service porderbomcolorService;
	@Autowired ICutplanProcessingService cutplanprocessingService;
	@Override
	protected JpaRepository<CutPlan_Row, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<CutPlan_Row> getby_color(Long porderid_link, Long material_skuid_link, Long colorid_link, Long orgrootid_link) {
		// TODO Auto-generated method stub
		Long attributeid_link = AtributeFixValues.ATTR_COLOR;
		return repo.getby_sku_and_porder( porderid_link, material_skuid_link, orgrootid_link, colorid_link, attributeid_link);
	}
	@Override
	public List<CutPlan_Row> getby_porder_matsku_productsku(Long porderid_link, Long material_skuid_link,
			Long product_skuid_link, Integer type, String name) {
		// TODO Auto-generated method stub
		return repo.getby_matsku_and_porder_and_productsku(material_skuid_link, porderid_link, product_skuid_link, type, name);
	}
	@Override
	public boolean sync_porder_bom(Long material_skuid_link, POrder porder, Long colorid_link, Long userid_link, Long orgrootid_link) {
		// TODO Auto-generated method stub
		Long productid_link = porder.getProductid_link();
		Long pcontractid_link = porder.getPcontractid_link();
		Long porderid_link = porder.getId();
		
		int tongsl_sp = 0;
		float sl_vai = 0;
		
		//tinh tong so luong san pham trong tat ca cac so do
		List<CutPlan_Size> list_sodo = cutplan_size_Service.getby_sku_and_porder_and_color(material_skuid_link, porderid_link, orgrootid_link, colorid_link);
		Map<Long, Float> map = new HashMap<Long, Float>();
		for(CutPlan_Size sodo : list_sodo) {
			CutPlan_Row cut_row = cutrow_Service.findOne(sodo.getCutplanrowid_link());
			if(cut_row.getType().equals(CutPlanRowType.sodocat)) {
				tongsl_sp += (sodo.getAmount() == null ? 0 : sodo.getAmount()) * cut_row.getLa_vai();
				if(map.get(sodo.getCutplanrowid_link()) == null) {
					float f_slvai = cut_row.getSl_vai();
					sl_vai += f_slvai;
					map.put(sodo.getCutplanrowid_link(), f_slvai);
				}
				
			}
		}
		
		
		List<Long> list_sku = sku_repo.getskuid_bycolorid_link(productid_link,pcontractid_link,  colorid_link);
		for(Long product_skuid_link : list_sku) {
			float bom = 0;
			int sl_zise = 0;
			
			//tinh so luong cua tung co
			List<CutPlan_Size> listsize_sodo = cutplan_size_Service.getby_porder_matsku_productsku(porderid_link, material_skuid_link, product_skuid_link, CutPlanRowType.sodocat, "");
			for(CutPlan_Size cut_size : listsize_sodo) {
				CutPlan_Row cut_row = cutrow_Service.findOne(cut_size.getCutplanrowid_link());
				
				if(cut_size.getProduct_skuid_link().equals(product_skuid_link))
					sl_zise += (cut_size.getAmount() == null ? 0 : cut_size.getAmount()) * cut_row.getLa_vai();
				
			}
			
			float bom_avg = (float)(sl_vai /(float)tongsl_sp);
			float size_percent = ((float)sl_zise/(float)tongsl_sp);
			bom = bom_avg * size_percent;
			bom = (float)Math.ceil((bom*10000))/10000;
					
			List<POrderBOMSKU> list_bomsku = porderbomskuService.getby_porder_and_material_and_sku_and_type(porderid_link, material_skuid_link, product_skuid_link, POrderBomType.Kythuat);
			
			//neu co dinh muc roi thi cap nhat chua co thi them vao
			if(list_bomsku.size() > 0) {				
				POrderBOMSKU bomsku = list_bomsku.get(0);
				bomsku.setAmount(bom);
				porderbomskuService.save(bomsku);
			}
			else {
				POrderBOMSKU bomsku = new POrderBOMSKU();
				bomsku.setId(null);
				bomsku.setAmount(bom);
				bomsku.setCreateddate(new Date());
				bomsku.setCreateduserid_link(userid_link);
				bomsku.setMaterialid_link(material_skuid_link);
				bomsku.setOrgrootid_link(orgrootid_link);
				bomsku.setPcontractid_link(pcontractid_link);
				bomsku.setPorderid_link(porderid_link);
				bomsku.setProductid_link(productid_link);
				bomsku.setSkuid_link(product_skuid_link);
				bomsku.setType(POrderBomType.Kythuat);

				porderbomskuService.save(bomsku);
				
			}		
		}

		//xoa het dinh muc chung va chung mau cua nguyen lieu
		//update lai bang bom amount = 0
		List<POrderBomProduct> pContractProductBom = porderbomproductService.getby_porder_and_material(porderid_link, material_skuid_link);
		if(pContractProductBom.size() > 0) {
			POrderBomProduct porderbom = pContractProductBom.get(0);
			porderbom.setAmount((float)0);
			porderbomproductService.update(porderbom);
		}
		
		
		//update lai bang sku bom
		List<PorderBomColor> listcolor = porderbomcolorService.getby_porder_and_material_and_color(porderid_link, material_skuid_link, colorid_link);
		
		for (PorderBomColor pColor : listcolor) {
			porderbomcolorService.delete(pColor);
		}
		
		return true;
	}
	@Override
	public List<CutPlan_Row> findByPOrder(Long porderid_link) {
		// TODO Auto-generated method stub
		return repo.findByPOrder(porderid_link);
	}
	@Override
	public boolean sync_porder_bom_from_cutprocesing(Long material_skuid_link, POrder porder, Long colorid_link,
			Long userid_link, Long orgrootid_link) {
		// TODO Auto-generated method stub
		Long productid_link = porder.getProductid_link();
		Long pcontractid_link = porder.getPcontractid_link();
		Long porderid_link = porder.getId();
		
		int tongsl_sp = 0;
		float sl_vai = 0;
		
		//tinh tong so luong san pham trong tat ca cac so do
		List<CutPlan_Size> list_sodo = cutplan_size_Service.getby_sku_and_porder_and_color(material_skuid_link, porderid_link, orgrootid_link, colorid_link);
		for(CutPlan_Size sodo : list_sodo) {
			List<CutplanProcessing> list_cut_processing = cutplanprocessingService.getby_cutplanrow(sodo.getCutplanrowid_link());
			if(list_cut_processing.size() > 0) {
				tongsl_sp += (sodo.getAmount() == null ? 0 : sodo.getAmount()) * list_cut_processing.get(0).getLa_vai();
				sl_vai += list_cut_processing.get(0).getAmountcut();
			}
//			CutPlan_Row cut_row = cutrow_Service.findOne(sodo.getCutplanrowid_link());
//			if(cut_row.getType() == CutPlanRowType.sodocat) {
//				tongsl_sp += (sodo.getAmount() == null ? 0 : sodo.getAmount()) * cut_row.getLa_vai();
//				sl_vai += cut_row.getSl_vai();
//			}
		}
		
		
		List<Long> list_sku = sku_repo.getskuid_bycolorid_link(productid_link,pcontractid_link,  colorid_link);
		for(Long product_skuid_link : list_sku) {
			float bom = 0;
			int sl_zise = 0;
			
			//tinh so luong cua tung co
			List<CutPlan_Size> listsize_sodo = cutplan_size_Service.getby_porder_matsku_productsku(porderid_link, material_skuid_link, product_skuid_link, CutPlanRowType.sodocat, "");
			for(CutPlan_Size cut_size : listsize_sodo) {
				CutPlan_Row cut_row = cutrow_Service.findOne(cut_size.getCutplanrowid_link());
				
				if(cut_size.getProduct_skuid_link().equals(product_skuid_link))
					sl_zise += (cut_size.getAmount() == null ? 0 : cut_size.getAmount()) * cut_row.getLa_vai();
				
			}
			
			float bom_avg = (float)(sl_vai /(float)tongsl_sp);
			float size_percent = ((float)sl_zise/(float)tongsl_sp);
			bom = bom_avg * size_percent;
			bom = (float)Math.ceil((bom*10000))/10000;
					
			List<POrderBOMSKU> list_bomsku = porderbomskuService.getby_porder_and_material_and_sku_and_type(porderid_link, material_skuid_link, product_skuid_link, POrderBomType.Kythuat);
			
			//neu co dinh muc roi thi cap nhat chua co thi them vao
			if(list_bomsku.size() > 0) {
				
				POrderBOMSKU bomsku = list_bomsku.get(0);
				bomsku.setAmount(bom);
				porderbomskuService.save(bomsku);
			}
			else {
				POrderBOMSKU bomsku = new POrderBOMSKU();
				bomsku.setId(null);
				bomsku.setAmount(bom);
				bomsku.setCreateddate(new Date());
				bomsku.setCreateduserid_link(userid_link);
				bomsku.setMaterialid_link(material_skuid_link);
				bomsku.setOrgrootid_link(orgrootid_link);
				bomsku.setPcontractid_link(pcontractid_link);
				bomsku.setPorderid_link(porderid_link);
				bomsku.setProductid_link(productid_link);
				bomsku.setSkuid_link(product_skuid_link);

				porderbomskuService.save(bomsku);
				
			}		
		}

		//xoa het dinh muc chung va chung mau cua nguyen lieu
		//update lai bang bom amount = 0
		List<POrderBomProduct> pContractProductBom = porderbomproductService.getby_porder_and_material(porderid_link, material_skuid_link);
		if(pContractProductBom.size() > 0) {
			POrderBomProduct porderbom = pContractProductBom.get(0);
			porderbom.setAmount((float)0);
			porderbomproductService.update(porderbom);
		}
		
		
		//update lai bang sku bom
		List<PorderBomColor> listcolor = porderbomcolorService.getby_porder_and_material_and_color(porderid_link, material_skuid_link, colorid_link);
		
		for (PorderBomColor pColor : listcolor) {
			porderbomcolorService.delete(pColor);
		}
		
		return true;
	}

}
