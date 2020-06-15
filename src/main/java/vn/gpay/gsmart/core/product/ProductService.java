package vn.gpay.gsmart.core.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Sorts;
import com.github.wenhao.jpa.Specifications;

import vn.gpay.gsmart.core.api.product.Product_getall_request;
import vn.gpay.gsmart.core.attribute.Attribute;
import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.pcontractproduct.IPContractProductService;
import vn.gpay.gsmart.core.pcontractproduct.PContractProductBinding;
import vn.gpay.gsmart.core.pcontractproductbom.IPContractProductBomRepository;
import vn.gpay.gsmart.core.pcontractproductpairing.IPContractProductPairingRepository;
import vn.gpay.gsmart.core.pcontractproductpairing.PContractProductPairing;
import vn.gpay.gsmart.core.productattributevalue.IProductAttributeRepository;
import vn.gpay.gsmart.core.productattributevalue.ProductAttributeValue;

@Service
public class ProductService extends AbstractService<Product> implements IProductService {

	@Autowired IProductRepository repo;
	@Autowired IProductBOMRepository productbom_repo;
	@Autowired IPContractProductBomRepository ppbom_repo;
	@Autowired IProductTypeRepository repo_productype;
	@Autowired IProductAttributeRepository productAttr;
	@Autowired IPContractProductService serviceContractProduct;
	@Autowired EntityManager em;
	@Autowired IPContractProductPairingRepository pcontractpairingRepo;
	@Override
	protected JpaRepository<Product, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<Product> getall_by_orgrootid(Long orgrootid_link, int product_type) {
		// TODO Auto-generated method stub
		return repo.getall_product_byorgrootid_link(orgrootid_link, product_type);
	}

	@Override
	public List<Product> getone_by_code(Long orgrootid_link, String code, Long productid_link, int product_type) {
		// TODO Auto-generated method stub
		return repo.get_byorgid_link_and_code(orgrootid_link,productid_link, code, product_type);
	}

	@Override
	public List<Product> filter(Long orgrootid_link, int product_type,String code,String partnercode, List<Attribute> attributes, Long productid_link, Long orgcustomerid_link) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Product> cq_product = cb.createQuery(Product.class);
		Root<Product> rootProduct = cq_product.from(Product.class);
		List<Predicate> predicates_Products = new ArrayList<>();

		//Danh sach product cua khach hang
		List<Long> lsProduct_Id = new ArrayList<Long>();
		if (null!=orgcustomerid_link && orgcustomerid_link!=0){
			lsProduct_Id = serviceContractProduct.get_by_orgcustomer(orgrootid_link, orgcustomerid_link);
			if (lsProduct_Id.size() == 0)
				return null;
		}		
		//Tim danh sach Product thoa man dieu kien trong Attribute
		List<Long> lsAttr_Final = new ArrayList<Long>();
		if (attributes.size() > 0){
		    CriteriaQuery<Long> cq = cb.createQuery(Long.class);		
			Root<ProductAttributeValue> root = cq.from(ProductAttributeValue.class);
//			List<Predicate> predicates_AttrValues = new ArrayList<>();
			
			List<Long> lsAttr_Tmp = new ArrayList<Long>();
			for (Attribute Attr: attributes){
				List<Predicate> predicates_or = new ArrayList<>();
				String [] selectedValues = Attr.getSelectedids().split(";");
				for (String selectedValue:selectedValues){
					List<Predicate> predicates = new ArrayList<>();
					predicates.add(cb.equal(root.get("attributeid_link"), Attr.getId()));
					predicates.add(cb.equal(root.get("attributevalueid_link"), Integer.parseInt(selectedValue)));
					predicates_or.add(cb.and(predicates.toArray(new Predicate[0])));
				}
				
				Predicate pre_Attr = cb.or(predicates_or.toArray(new Predicate[0]));
				cq.where(pre_Attr);
				cq.select(root.get("productid_link"));
				List<Long> lsAttrValue = em.createQuery(cq).getResultList();
				
				if (lsAttr_Final.size() > 0){
					lsAttr_Tmp.removeAll(lsAttrValue);
					lsAttr_Final.removeAll(lsAttr_Tmp);
					lsAttr_Tmp.clear();
					lsAttr_Tmp.addAll(lsAttr_Final);
				} else {
					lsAttr_Final.addAll(lsAttrValue);
					lsAttr_Tmp.addAll(lsAttrValue);
				}
			}				
	
			if (lsAttr_Final.size() == 0){
				//Neu co dieu kien thuoc tinh ma khong tim ra ket qua --> tra ve null luon
				return null;
			}
		}
		
		//Hop nhat danh sach Productid cua Attr va Customer de tao dieu kien tim
		if (lsAttr_Final.size() > 0){
			if (lsProduct_Id.size() > 0){
				List<Long> arr_Tmp = new ArrayList<Long>();
				arr_Tmp.addAll(lsAttr_Final);
				arr_Tmp.removeAll(lsProduct_Id);
				lsAttr_Final.removeAll(arr_Tmp);
				if (lsAttr_Final.size() > 0)
					lsProduct_Id = lsAttr_Final;
				else
					return null;//Neu 2 dieu kien Attr va OrgCustomer ko co tap chung --> return null luon
			} else
				lsProduct_Id = lsAttr_Final;
		} 
		
		if (lsProduct_Id.size() > 0){
			In<Long> inClause = cb.in(rootProduct.get("id"));
			for(Long pId:lsProduct_Id){
				inClause.value(pId);
			}
			
			predicates_Products.add(cb.and(inClause));
		}
		
		if (-1!=product_type){
			predicates_Products.add(cb.equal(rootProduct.get("producttypeid_link"), product_type));
		}
		
		if(null!=productid_link && productid_link !=0) {
			predicates_Products.add(cb.equal(rootProduct.get("id"), productid_link));
		}
		
		if (null!=code && code.length()>0){
			predicates_Products.add(cb.like(rootProduct.get("code"), "%"+code+"%"));
		}	
		if (null!=partnercode && partnercode.length()>0){
			predicates_Products.add(cb.like(rootProduct.get("partnercode"), "%"+partnercode+"%"));
		}	
		predicates_Products.add(cb.equal(rootProduct.get("status"), 1));

		Predicate p = cb.and(predicates_Products.toArray(new Predicate[0]));	
		cq_product.where(p);

		List<Product> lst = em.createQuery(cq_product).getResultList();

		return lst;
	}
	
	@Override
	public Page<Product> getall_by_orgrootid_paging(Long orgrootid_link, Product_getall_request request) {
		// TODO Auto-generated method stub
		
		Specification<Product> specification = Specifications.<Product>and()
	            .eq("product_type", request.product_type)
	            .eq("status", 1)
	            .eq("orgrootid_link", orgrootid_link)
        		.predicate(Specifications.or()
        				.like("name","%"+request.name.toUpperCase()+"%")
        	            .like("name","%"+request.name.toLowerCase()+"%")
        	            .build())
        		.predicate(Specifications.or()
                		.like("code","%"+request.code.toLowerCase()+"%")
                		.like("code","%"+request.code.toUpperCase()+"%")
        	            .build())
	            .build();
		Sort sort = Sorts.builder()
		        .asc("name")
		        .build();
		
		Page<Product> lst = repo.findAll(specification, PageRequest.of(request.page - 1, request.limit, sort));
		return lst;
	}

	@Override
	public Page<Product> getall_products(Long orgrootid_link, Product_getall_request request) {
		// TODO Auto-generated method stub
		String name = request.name;
		String code = request.code;
		Specification<Product> specification = Specifications.<Product>and()
//	            .eq("product_type", request.product_type)
	            .eq("status", 1)
	            .eq("orgrootid_link", orgrootid_link)
	            .like(name != "" && name != null,"name","%"+name+"%")
	            .like(code != "" && code != null,"code","%"+code+"%")
	            .between("producttypeid_link", 10, 19)
	            .build();
		Sort sort = Sorts.builder()
		        .asc("name")
		        .build();
		
		Page<Product> lst = repo.findAll(specification, PageRequest.of(request.page - 1, request.limit, sort));
		return lst;
	}	
	
	@Override
	public Page<Product> getall_mainmaterials(Long orgrootid_link, Product_getall_request request) {
		// TODO Auto-generated method stub
		String name = request.name;
		String code = request.code;
		Specification<Product> specification = Specifications.<Product>and()
//	            .eq("product_type", request.product_type)
	            .eq("status", 1)
	            .eq("orgrootid_link", orgrootid_link)
	            .like(name != "" && name != null,"name","%"+name+"%")
	            .like(code != "" && code != null,"code","%"+code+"%")
	            .eq("producttypeid_link", 20)
	            .build();
		Sort sort = Sorts.builder()
		        .asc("name")
		        .build();
		
		Page<Product> lst = repo.findAll(specification, PageRequest.of(request.page - 1, request.limit, sort));
		return lst;
	}	
	
	@Override
	public Page<Product> getall_sewingtrim(Long orgrootid_link, Product_getall_request request) {
		// TODO Auto-generated method stub
		String name = request.name;
		String code = request.code;
		Specification<Product> specification = Specifications.<Product>and()
//	            .eq("product_type", request.product_type)
	            .eq("status", 1)
	            .eq("orgrootid_link", orgrootid_link)
	            .like(name != "" && name != null,"name","%"+name+"%")
	            .like(code != "" && code != null,"code","%"+code+"%")
	            .between("producttypeid_link", 30, 39)
	            .build();
		Sort sort = Sorts.builder()
		        .asc("name")
		        .build();
		
		Page<Product> lst = repo.findAll(specification, PageRequest.of(request.page - 1, request.limit, sort));
		return lst;
	}	
	
	public Page<Product> getall_packingtrim(Long orgrootid_link, Product_getall_request request) {
		// TODO Auto-generated method stub
		String name = request.name;
		String code = request.code;
		
		Specification<Product> specification = Specifications.<Product>and()
	            .eq("status", 1)
	            .eq("orgrootid_link", orgrootid_link)
	            .like(name != "" && name != null,"name","%"+name+"%")
	            .like(code != "" && code != null,"code","%"+code+"%")
	            .between("producttypeid_link", 40, 49)
	            .build();
		Sort sort = Sorts.builder()
		        .asc("name")
		        .build();
		
		Page<Product> lst = repo.findAll(specification, PageRequest.of(request.page - 1, request.limit, sort));
		return lst;
	}
	
	@Override
	public List<Product> getall_materials(Long orgrootid_link, Product_getall_request request) {
		// TODO Auto-generated method stub
		String name = request.name;
		String code = request.code;
		Specification<Product> specification = Specifications.<Product>and()
	            .eq(Objects.nonNull(request.product_type),"product_type", request.product_type)
	            .eq("status", 1)
	            .eq("orgrootid_link", orgrootid_link)
	            .like(name != "" && name != null,"name","%"+name+"%")
	            .like(code != "" && code != null,"code","%"+code+"%")
	            .between("producttypeid_link", 20, 29)
	            .build();
		
		List<Product> lst = repo.findAll(specification);
		return lst;
	}
	
	@Override
	public List<Product> getList_material_notin_ProductBOM(Long orgrootid_link, String code, String name,
			String TenMauNPL, Long productid_link, int product_type) {

		List<Long> lstbom = productbom_repo.getall_materialid_in_productBOM(productid_link);
		
		Specification<Product> specification = Specifications.<Product>and()
				.eq(product_type !=0 ,"product_type", product_type)
	            .eq("status", 1)
	            .ne(product_type ==0 ,"product_type", 1)
	            .ne(product_type ==0 ,"product_type", 5)
	            .eq("orgrootid_link", orgrootid_link)
	            .like("name","%"+name+"%")
        		.like("code","%"+code+"%")
        		.notIn(lstbom.size() > 0,"id", lstbom.toArray())
	            .build();
		Sort sort = Sorts.builder()
		        .asc("name")
		        .build();
		List<Product> listNPL = repo.findAll(specification , sort);
		if(!TenMauNPL.equals(""))
		listNPL.removeIf(c -> !c.getTenMauNPL().equals(TenMauNPL));
		return listNPL;
	}

	@Override
	public List<Product> getList_material_notin_PContractProductBOM(Long orgrootid_link, String code, String name,
			String TenMauNPL, Long productid_link, int product_type, long pcontractid_link) {
List<Long> lstbom = ppbom_repo.getall_materialid_in_pcontract_productBOM(productid_link, pcontractid_link);
		
		Specification<Product> specification = Specifications.<Product>and()
				.eq(product_type !=0 ,"product_type", product_type)
	            .eq("status", 1)
	            .ne(product_type ==0 ,"product_type", 1)
	            .ne(product_type ==0 ,"product_type", 5)
	            .eq("orgrootid_link", orgrootid_link)
	            .like("name","%"+name+"%")
        		.like("code","%"+code+"%")
        		.notIn(lstbom.size() > 0,"id", lstbom.toArray())
	            .build();
		Sort sort = Sorts.builder()
		        .asc("name")
		        .build();
		List<Product> listNPL = repo.findAll(specification , sort);
		if(!TenMauNPL.equals(""))
		listNPL.removeIf(c -> !c.getTenMauNPL().equals(TenMauNPL));
		return listNPL;
	}

	@Override
	public List<ProductType> getall_ProductTypes(Integer producttypeid_min, Integer producttypeid_max){
		return repo_productype.getall_ProductTypes(producttypeid_min, producttypeid_max);
	}

	@Override
	public List<ProductTree> createTree(List<PContractProductBinding> nodes, Long pcontractid_link) {
		// TODO Auto-generated method stub
		Map<Long, ProductTree> mapTmp = new HashMap<>();
        
        //Save all nodes to a map
        for (PContractProductBinding current : nodes) {
        	ProductTree product = new ProductTree();
        	product.setId(current.getProductid_link());
        	product.setText(current.getProductName());
        	product.setCode(current.getProductCode());
        	product.setImgproduct(current.getImgproduct());
            mapTmp.put(current.getProductid_link(), product);
        }
 
        //loop and assign parent/child relationships
        for (PContractProductBinding current : nodes) {
        	Long parentId = null;
        	if(current.getProducttypeid_link() != 5) {
        		List<PContractProductPairing> pairing = pcontractpairingRepo.get_pairing_bypcontract_and_product
        				(pcontractid_link, current.getProductid_link());
        		if(pairing.size()> 0)
        			parentId = pairing.get(0).getProductpairid_link();
        	}
             
            if (parentId != null ) {
            	ProductTree parent = mapTmp.get(parentId);
                if (parent != null) {
                	ProductTree current_n = new ProductTree();
                	current_n.setId(current.getProductid_link());
                	current_n.setText(current.getProductName());
                	current_n.setCode(current.getProductCode());
                	current_n.setImgproduct(current.getImgproduct());
                	current_n.setParent_id(parentId);
                	
                    parent.getChildren().add(current_n);
                    mapTmp.put(parentId, parent);
                    mapTmp.put(current_n.getId(), current_n);
                    mapTmp.remove(current_n.getId());
                }
            }
 
        }
        
    
        //get the root
        List<ProductTree> root = new ArrayList<ProductTree>();
        for (ProductTree node : mapTmp.values()) {
            if(node.getParent_id()==null) {
                root.add(node);
            }
        }
 
        return root;
	}

	@Override
	public List<Product> getby_pairid(long productpairid_link) {
		// TODO Auto-generated method stub
		return repo.getby_pairid(productpairid_link);
	}
}
