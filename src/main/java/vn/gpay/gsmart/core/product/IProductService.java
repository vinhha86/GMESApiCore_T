package vn.gpay.gsmart.core.product;

import java.util.List;

import org.springframework.data.domain.Page;

import vn.gpay.gsmart.core.api.product.Product_getall_request;
import vn.gpay.gsmart.core.attribute.Attribute;
import vn.gpay.gsmart.core.base.Operations;
import vn.gpay.gsmart.core.pcontractproduct.PContractProductBinding;


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
	public List<ProductTree> createTree(final List<PContractProductBinding> nodes, Long pcontractid_link);
	public List<Product> getby_pairid(long productpairid_link);
	Page<Product> getall_sewingthread(Long orgrootid_link, Product_getall_request request);
	public List<Product> getProductByExactBuyercode(String buyercode);
	List<Product> getProductByLikeBuyercode(String buyercode);
	List<Product> getby_code_type_description_and_color_and_size(Long orgrootid_link, String code, int type, String description, Long colorid_link, Long sizeid_link);
	List<Product> getby_code_type_description(Long orgrootid_link, String code, int type, String description);
	List<Long> getByBuyerCode(String buyercode);
	List<Product> getByBuyerCodeAndType(String buyercode, Integer producttypeid_link);
	List<Product> getByBuyerCodeAndTypeNotLike(String buyercode, Integer producttypeid_link);
	List<Product> getAllProduct(Long orgrootid_link, String buyercode, String buyername);
}
