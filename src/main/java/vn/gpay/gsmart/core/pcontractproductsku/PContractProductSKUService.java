package vn.gpay.gsmart.core.pcontractproductsku;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import vn.gpay.gsmart.core.attributevalue.Attributevalue;
import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU;
import vn.gpay.gsmart.core.sku.ISKU_Repository;
import vn.gpay.gsmart.core.sku.SKU;

@Service
public class PContractProductSKUService extends AbstractService<PContractProductSKU>
		implements IPContractProductSKUService {
	@Autowired
	IPContractProductSKURepository repo;
	@Autowired
	IPOrder_Service porder_Service;
	@Autowired
	ISKU_Repository sku_repo;

	@Override
	protected JpaRepository<PContractProductSKU, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<PContractProductSKU> getlistsku_byproduct_and_pcontract(long orgrootid_link, long productid_link,
			long pcontractid_link) {
		// TODO Auto-generated method stub
		return repo.getlistsku_byproduct_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
	}

	@Override
	public List<PContractProductSKU> getlistsku_bypcontract(long orgrootid_link, long pcontractid_link) {
		// TODO Auto-generated method stub
		return repo.getlistsku_bypcontract(orgrootid_link, pcontractid_link);
	}

	@Override
	public List<PContractProductSKU> getsumsku_bypcontract(long pcontractid_link) {
		List<PContractProductSKU> result = new ArrayList<PContractProductSKU>();
		List<Object[]> rs = repo.getsumsku_bypcontract(pcontractid_link);
		for (Object[] record : rs) {
			PContractProductSKU sku = new PContractProductSKU();
			sku.setSkuid_link((Long) record[0]);
			sku.setPquantity_total(((Long) record[1]).intValue());
			result.add(sku);
		}
		return result;
	}

	@Override
	public List<PContractProductSKU> getlistsku_bypo_and_pcontract(long orgrootid_link, long pcontract_poid_link,
			long pcontractid_link) {
		// TODO Auto-generated method stub
		return repo.getlistsku_bypo_and_pcontract(pcontract_poid_link, pcontractid_link);
	}

	@Override
	public List<PContractProductSKU> getlistsku_bypo_and_pcontract_free(long orgrootid_link, long pcontract_poid_link,
			long pcontractid_link) {
		try {
			List<PContractProductSKU> lstPContractProductSKU = repo.getlistsku_bypo(pcontract_poid_link);
			for (PContractProductSKU thePContractProductSKU : lstPContractProductSKU) {
				thePContractProductSKU.setPquantity_lenhsx(0);
			}
			// Update SL da phan lenh, SL con lai
			List<POrder> lsPOrders = porder_Service.getByContractAndPO_Granted(pcontractid_link, pcontract_poid_link);
			for (POrder thePOrder : lsPOrders) {
				for (POrder_Product_SKU thePorderSKU : thePOrder.getPorder_product_sku()) {
					// a.removeIf(sku -> sku.getSkuid_link().equals(thePorderSKU.getSkuid_link()));
					PContractProductSKU poSKU = lstPContractProductSKU.stream()
							.filter(sku -> sku.getSkuid_link().equals(thePorderSKU.getSkuid_link())).findAny()
							.orElse(null);
					if (null != poSKU) {
						int curNum_Granted = null != poSKU.getPquantity_lenhsx() ? poSKU.getPquantity_lenhsx() : 0;
						int curNum_Total = null != thePorderSKU.getPquantity_total() ? thePorderSKU.getPquantity_total()
								: 0;
						poSKU.setPquantity_lenhsx(curNum_Granted + curNum_Total);
					}
				}
			}
			return lstPContractProductSKU;
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;
		}
	}

	@Override
	public List<PContractProductSKU> getPOSKU_Free_ByProduct(long productid_link, long pcontract_poid_link) {
		try {
			List<PContractProductSKU> a = repo.getlistsku_bypo_and_product(pcontract_poid_link, productid_link);
			// Update SL da phan lenh, SL con lai
			List<POrder> lsPOrders = porder_Service.getByPOAndProduct(pcontract_poid_link, productid_link);
			for (POrder thePOrder : lsPOrders) {
				for (POrder_Product_SKU thePorderSKU : thePOrder.getPorder_product_sku()) {
					// a.removeIf(sku -> sku.getSkuid_link().equals(thePorderSKU.getSkuid_link()));
					PContractProductSKU poSKU = a.stream()
							.filter(sku -> sku.getSkuid_link().equals(thePorderSKU.getSkuid_link())).findAny()
							.orElse(null);
					if (null != poSKU) {
						int curNum_Granted = null != poSKU.getPquantity_lenhsx() ? poSKU.getPquantity_lenhsx() : 0;
						int curNum_Total = null != thePorderSKU.getPquantity_total() ? thePorderSKU.getPquantity_total()
								: 0;
						poSKU.setPquantity_lenhsx(curNum_Granted + curNum_Total);
					}
				}
			}
			return a;
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;
		}
	}

	@Override
	public List<PContractProductSKU> getlistsku_bysku_and_pcontract(long skuid_link, long pcontractid_link) {
		// TODO Auto-generated method stub
		return repo.getlistsku_bysku_and_pcontract(skuid_link, pcontractid_link);
	}

	@Override
	public List<Long> getlistvalue_by_product(long pcontractid_link, long productid_link, long attributeid_link) {
		// TODO Auto-generated method stub
		List<Long> list = repo.getvaluesize_in_product(productid_link, pcontractid_link, attributeid_link);
		return list;
	}

	@Override
	public List<Long> getsku_bycolor(long pcontractid_link, long productid_link, Long colorid_link) {
		// TODO Auto-generated method stub
		return repo.getskuid_bycolorid_link(productid_link, pcontractid_link, colorid_link);
	}

	@Override
	public List<PContractProductSKU> getbypo_and_product(long pcontract_poid_link, long productid_link) {
		// TODO Auto-generated method stub
		List<PContractProductSKU> a = repo.getlistsku_bypo_and_product(pcontract_poid_link, productid_link);
		return a;
	}

	// Chi lay cac SKU chua co trong Lenh SX
	@Override
	public List<PContractProductSKU> getbypo_and_product_free(long porderreqid_link, long pcontractid_link,
			long pcontract_poid_link, long productid_link) {
		// TODO Auto-generated method stub
		List<PContractProductSKU> a = repo.getlistsku_bypo_and_product(pcontract_poid_link, productid_link);

		List<POrder> lsPOrders = porder_Service.getByContractAndPO(pcontractid_link, pcontract_poid_link);
		for (POrder thePOrder : lsPOrders) {
			for (POrder_Product_SKU thePorderSKU : thePOrder.getPorder_product_sku()) {
				a.removeIf(sku -> sku.getSkuid_link().equals(thePorderSKU.getSkuid_link()));
			}
		}

		return a;
	}

	@Override
	public List<PContractProductSKU> getlistsku_bysku_and_product_PO(long skuid_link, long pcontract_poid_link,
			long productid_link) {
		// TODO Auto-generated method stub
		return repo.getlistsku_bysku_and_product_PO(skuid_link, productid_link, pcontract_poid_link);
	}

	@Override
	public List<PContractProductSKU> getlistsku_bypo(Long pcontract_poid_link) {
		return repo.getlistsku_bypo(pcontract_poid_link);
	}

	@Override
	public List<PContractProductSKU> getsumsku_bypo_parent(Long pcontract_poid_link) {
		List<Object[]> a = repo.getsumsku_bypo_parent(pcontract_poid_link);
		List<PContractProductSKU> ls_SKU_SUM = new ArrayList<PContractProductSKU>();
		for (Object[] sku_sum : a) {
			PContractProductSKU theSKU_SUM = new PContractProductSKU();
			theSKU_SUM.setProductid_link(Long.valueOf(sku_sum[0].toString()));
			theSKU_SUM.setSkuid_link(Long.valueOf(sku_sum[1].toString()));
			theSKU_SUM.setPquantity_total(Integer.valueOf(sku_sum[2].toString()));
			ls_SKU_SUM.add(theSKU_SUM);

		}
		return ls_SKU_SUM;
	}

	@Override
	public List<PContractProductSKU> getBySkuAndPcontractPo(Long skuid_link, Long pcontract_poid_link) {
		return repo.getBySkuAndPcontractPo(skuid_link, pcontract_poid_link);
	}

	@Override
	public List<PContractProductSKU> getsku_bycolorid_link(long pcontractid_link, long productid_link,
			long colorid_link) {
		// TODO Auto-generated method stub
		Long colorid = colorid_link == 0 ? null : colorid_link;
		return repo.getPContractProductSKU_bycolorid_link(productid_link, pcontractid_link, colorid);
	}

	@Override
	public List<POLineSKU> gettotalsku_bypo_parent_and_product(Long pcontract_poid_link, Long productid_link) {
		// TODO Auto-generated method stub
		List<Object[]> a = repo.getsumsku_bypo_parent_and_product(pcontract_poid_link, productid_link);
		List<POLineSKU> ls_SKU_SUM = new ArrayList<POLineSKU>();
		for (Object[] sku_sum : a) {
			POLineSKU theSKU_SUM = new POLineSKU();
			theSKU_SUM.setProductid_link(Long.valueOf(sku_sum[0].toString()));
			theSKU_SUM.setSkuid_link(Long.valueOf(sku_sum[1].toString()));
			theSKU_SUM.setPquantity_granted(Integer.valueOf(sku_sum[3] == null ? "0" : sku_sum[3].toString()));
			theSKU_SUM.setPquantity_production(Integer.valueOf(sku_sum[4] == null ? "0" : sku_sum[4].toString()));
			theSKU_SUM.setPquantity_sample(Integer.valueOf(sku_sum[5] == null ? "0" : sku_sum[5].toString()));
			theSKU_SUM.setPquantity_ungranted(theSKU_SUM.getPquantity_production() + theSKU_SUM.getPquantity_sample()
					- theSKU_SUM.getPquantity_granted());
			SKU sku = sku_repo.getOne(theSKU_SUM.getSkuid_link());
			theSKU_SUM.setCoSanPham(sku.getSize_name());
			theSKU_SUM.setMauSanPham(sku.getColor_name());

			if (theSKU_SUM.getPquantity_ungranted() > 0)
				ls_SKU_SUM.add(theSKU_SUM);

		}
		return ls_SKU_SUM;
	}

	@Override
	public List<String> getlistnamevalue_by_product(long pcontractid_link, long productid_link, long attributeid_link) {
		// TODO Auto-generated method stub
		return repo.getvaluename_in_product(productid_link, pcontractid_link, attributeid_link);
	}

	@Override
	public List<Long> getsku_bypcontract_and_product(long pcontractid_link, long productid_link) {
		// TODO Auto-generated method stub
		return repo.getskuid_byproduct_and_pcontract(productid_link, pcontractid_link);
	}

	@Override
	public List<String> getlist_sizeset_by_product(Long pcontractid_link, Long productid_link) {
		// TODO Auto-generated method stub
		return repo.getlist_sizeset_by_product(productid_link, pcontractid_link);
	}

	@Override
	public List<Long> getlist_size_by_product_and_sizeset(Long pcontractid_link, Long productid_link,
			Long sizesetid_link) {
		// TODO Auto-generated method stub
		return repo.getlist_size_by_product_and_sizeset(productid_link, pcontractid_link, sizesetid_link);
	}

	@Override
	public List<Attributevalue> getmausanpham_by_pcontract(Long pcontractid_link) {
		// TODO Auto-generated method stub
		return repo.getmausanpham_by_pcontract(pcontractid_link);
	}

	@Override
	public List<PContractProductSKU> getsku_notmap(Long pcontract_poid_link) {
		// TODO Auto-generated method stub
		return repo.getsku_notmap(pcontract_poid_link);
	}

	@Override
	public List<PContractProductSKU> getsku_notmap_by_product(Long pcontract_poid_link, Long productid_link) {
		// TODO Auto-generated method stub
		return repo.getsku_notmap_by_product(pcontract_poid_link, productid_link);
	}
}
