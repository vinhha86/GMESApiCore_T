package vn.gpay.gsmart.core.productpairing;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IProductPairingService extends Operations<ProductPairing> {
	public List<ProductPairing> getproduct_pairing_bycontract(long orgrootid_link, long pcontractid_link);

	public List<ProductPairing> getproduct_pairing_detail_bycontract(long orgrootid_link, long pcontractid_link,
			long productpairid_link);

	public List<Long> getproductid_pairing_bycontract(long orgrootid_link, long pcontractid_link);

	ProductPairing getproduct_pairing_bykey(long productid_link, long productpairid_link);

	List<ProductPairing> getby_product(Long productid_link);

	List<ProductPairing> getbypcontract_product(Long pcontractid_link, Long productid_link, Long orgrootid_link);
}
