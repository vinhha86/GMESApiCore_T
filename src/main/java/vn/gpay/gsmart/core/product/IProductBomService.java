package vn.gpay.gsmart.core.product;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IProductBomService extends Operations<ProductBOM> {
	public List<ProductBOM> getproductBOMbyid(long productid_link);
}
