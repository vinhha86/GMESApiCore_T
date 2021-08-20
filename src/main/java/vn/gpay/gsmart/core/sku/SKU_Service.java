package vn.gpay.gsmart.core.sku;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Specifications;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class SKU_Service extends AbstractService<SKU> implements ISKU_Service {

	@Autowired
	ISKU_Repository repo;

	@Override
	protected JpaRepository<SKU, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<SKU> getlist_byProduct(Long productid_link) {
		// TODO Auto-generated method stub
		List<SKU> a = repo.getlist_byproduct(productid_link);
		return a;
	}

	@Override
	public SKU getSKU_byCode(String code, long orgrootid_link) {
		List<SKU> lsSku = repo.getSKU_byCode(code, orgrootid_link);
		if (lsSku.size() > 0)
			return lsSku.get(0);
		else
			return null;
	}

	@Override
	public List<SKU> getSKU_MainMaterial(String code) {
		try {
			Specification<SKU> specification = Specifications.<SKU>and()
					.like(Objects.nonNull(code), "code", "%" + code + "%").build();

			return repo.getSKU_MainMaterial(specification);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public List<SKU> getSKU_ByType(String code, Integer producttypeid_link) {
		if (code.length() > 0) {
			return repo.getSKU_ByTypeAndCode(code, producttypeid_link);
		} else
			return repo.getSKU_ByType(producttypeid_link);
	}

	@Override
	public List<SKU> getProductSKU_ByCode(Integer skutypeid_link, String code) {
		// TODO Auto-generated method stub
		return repo.getProductSKU_ByCode(skutypeid_link, code);
	}

	@Override
	public List<SKU> getProductSKU_ByBarCode(Integer skutypeid_link, String barcode) {
		// TODO Auto-generated method stub
		return repo.getProductSKU_ByBarCode(skutypeid_link, barcode);
	}

	@Override
	public List<SKU> getSKUforHandOver(Long porderid_link) {
		return repo.getSKUforHandOver(porderid_link);
	}

	@Override
	public List<SKU> getnpl_by_pcontract(Integer producttypeid_link, Long pcontractid_link) {
		// TODO Auto-generated method stub
		int producttypeid_link_from = producttypeid_link;
		int producttypeid_link_to = producttypeid_link_from + 10;

		return repo.getbytype_and_pcontract(producttypeid_link_from, producttypeid_link_to, pcontractid_link);
	}

	@Override
	public List<SKU> getSkuByCode(String code) {
		return repo.getSkuByCode(code);
	}

	@Override
	public List<SKU> getSkuByCodeAndType(String code, Integer typeFrom, Integer typeTo) {
		return repo.getSkuByCodeAndType(code, typeFrom, typeTo);
	}

	@Override
	public List<SKU> getnpl_by_pcontract_product(Integer producttypeid_link, Long pcontractid_link,
			Long productid_link) {
		// TODO Auto-generated method stub
		int producttypeid_link_from = producttypeid_link;
		int producttypeid_link_to = producttypeid_link_from + 10;

		return repo.getbytype_and_pcontract_product(producttypeid_link_from, producttypeid_link_to, pcontractid_link,
				productid_link);
	}
}
