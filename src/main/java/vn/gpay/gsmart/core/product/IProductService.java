package vn.gpay.gsmart.core.product;

import java.util.List;

import org.springframework.data.domain.Page;

import vn.gpay.gsmart.core.api.product.Product_getall_request;
import vn.gpay.gsmart.core.attribute.Attribute;
import vn.gpay.gsmart.core.base.Operations;


public interface IProductService extends Operations<Product> {
	public List<Product> getall_by_orgrootid(Long orgrootid_link, int product_type);
	public Page<Product> getall_by_orgrootid_paging(Long orgrootid_link, Product_getall_request request);
	public List<Product> getone_by_code(Long orgrootid_link, String code, Long productid_link, int product_type);
	public List<Product> getList_material_notin_ProductBOM(Long orgrootid_link, String code, String name, String TenMauNPL, Long productid_link, int product_type);
	public List<Product> getList_material_notin_PContractProductBOM(Long orgrootid_link, String code, String name, String TenMauNPL, Long productid_link, int product_type, long pcontractid_link);
	Page<Product> getall_mainmaterials(Long orgrootid_link, Product_getall_request request);
	List<Product> getall_materials(Long orgrootid_link, Product_getall_request request);
	Page<Product> getall_products(Long orgrootid_link, Product_getall_request request);
	Page<Product> getall_sewingtrim(Long orgrootid_link, Product_getall_request request);
	Page<Product> getall_packingtrim(Long orgrootid_link, Product_getall_request request);
	List<ProductType> getall_ProductTypes(Integer producttypeid_min, Integer producttypeid_max);
	List<Product> filter(Long orgrootid_link,int product_type, String code, String partnercode, List<Attribute> attributes, Long productid_link, Long orgcustomerid_link);
}
