package vn.gpay.gsmart.core.porder;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Sorts;
import com.github.wenhao.jpa.Specifications;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.utils.DateFormat;

@Service
public class POrder_Service extends AbstractService<POrder> implements IPOrder_Service {
	@Autowired IPOrder_Repository repo;
	@Autowired IPOrder_AutoID_Service porder_AutoID_Service;
	@Override
	protected JpaRepository<POrder, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	
	@Override
	public List<POrder> getByContract(Long pcontractid_link){
		return repo.getByContract(pcontractid_link);
	}
	
	@Override
	public Long savePOrder(POrder porder, String po_code){
		try {
			if (porder.getId() == null || porder.getId() == 0) {
				porder.setOrdercode(porder_AutoID_Service.getLastID(po_code));
			} 
			porder = this.save(porder);
			return porder.getId();
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<POrder> getByContractAndProduct(Long pcontractid_link, Long productid_link){
		return repo.getByContractAndProduct(pcontractid_link,productid_link);
	}
	
	@Override
	public List<POrder> getByContractAndPO(Long pcontractid_link, Long pcontract_poid_link){
		return repo.getByContractAndPO(pcontractid_link,pcontract_poid_link);
	}
	
	@Override
	public List<POrder> getByPOrder_Req(Long pcontract_poid_link, Long porderreqid_link){
		return repo.getByPOrder_Req(pcontract_poid_link,porderreqid_link);
	}
	
	
	@Override
	public List<POrder> getByStatus(Integer status){
		return repo.getByStatus(status);
	}
	
	@Override
	public Integer getMaxPriority(){
		return repo.getMaxPriority();
	}
	
	@Override
	public List<POrder> getFilter(
			String ordercode,
			Integer status,
			Long granttoorgid_link,
			String collection, 
			String season, 
			Integer salaryyear,
			Integer salarymonth,
			Date processingdate_from,
			Date processingdate_to
			) {
		try {
			if (null != salarymonth){
				Specification<POrder> specification = Specifications.<POrder>and()
						.eq(null!=status && status !=-1, "status", status)
			            .eq(null!=granttoorgid_link && granttoorgid_link != -1, "granttoorgid_link", granttoorgid_link)
			            .like(null!=ordercode && ordercode !="", "ordercode", "%"+ordercode+"%")
			            .like(null!=collection && collection !="", "collection", "%"+collection+"%")
			            .like(null!=season && season !="", "season", "%"+season+"%")
			            .eq(Objects.nonNull(salaryyear), "salaryyear", salaryyear)
			            .eq(null!=salarymonth && salarymonth != -1, "salarymonth", salarymonth)
			            .ge(Objects.nonNull(processingdate_from),"orderdate",DateFormat.atStartOfDay(processingdate_from))
		                .le(Objects.nonNull(processingdate_to),"orderdate",DateFormat.atEndOfDay(processingdate_to))
		                .between(processingdate_from!=null && processingdate_to!=null,"orderdate", DateFormat.atStartOfDay(processingdate_from), DateFormat.atEndOfDay(processingdate_to))
			            .build();
				Sort sort = Sorts.builder()
				        .desc("ordercode")
				        .build();
				List<POrder> a = repo.findAll(specification,sort);
				return a;
			} else {
				Specification<POrder> specification = Specifications.<POrder>and()
						.eq(null!=status && status !=-1, "status", status)
			            .eq(null!=granttoorgid_link && granttoorgid_link != -1, "granttoorgid_link", granttoorgid_link)
			            .like(null!=ordercode && ordercode !="", "ordercode", "%"+ordercode+"%")
			            .like(null!=collection && collection !="", "collection", "%"+collection+"%")
			            .like(null!=season && season !="", "season", "%"+season+"%")
			            .eq(Objects.nonNull(salaryyear), "salaryyear", salaryyear)
//			            .eq(null!=salarymonth && salarymonth != -1, "salarymonth", salarymonth)
			            .eq("salarymonth", salarymonth)
			            .ge(Objects.nonNull(processingdate_from),"orderdate",DateFormat.atStartOfDay(processingdate_from))
		                .le(Objects.nonNull(processingdate_to),"orderdate",DateFormat.atEndOfDay(processingdate_to))
		                .between(processingdate_from!=null && processingdate_to!=null,"orderdate", DateFormat.atStartOfDay(processingdate_from), DateFormat.atEndOfDay(processingdate_to))
			            .build();
				Sort sort = Sorts.builder()
				        .desc("ordercode")
				        .build();
				List<POrder> a = repo.findAll(specification,sort);
				return a;
			}
		} catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public List<POrder> get_by_org(long orgid_link) {
		// TODO Auto-generated method stub
		Specification<POrder> specification = Specifications.<POrder>and()
				.ne("status", -1)
				.le("status", 5)
	            .eq("granttoorgid_link", orgid_link)
	            .build();
		Sort sort = Sorts.builder()
		        .desc("ordercode")
		        .build();
		List<POrder> a = repo.findAll(specification,sort);
		return a;
	}
	
	@Override
	//Lay Porder duy nhat theo tung PO, san pham va dai co
	public POrder get_oneby_po_price(long orgrootid_link, long granttoorgid_link, long pcontract_poid_link,long productid_link, long sizesetid_link) {
		Specification<POrder> specification = Specifications.<POrder>and()
				.eq("orgrootid_link", orgrootid_link)
				.eq("granttoorgid_link", granttoorgid_link)
				.eq("pcontract_poid_link", pcontract_poid_link)
				.eq("productid_link", productid_link)
	            .eq("sizesetid_link", sizesetid_link)
	            .build();
		List<POrder> a = repo.findAll(specification);
		if (a.size() > 0)
			return a.get(0);
		else
			return null;
	}
	
	@Override
	//Danh sach cac lenh duoc phan cho Phan xuong nhung chua duoc phan chuyen
	public List<POrder> get_free_bygolivedate(Date golivedate_from, Date golivedate_to, Long granttoorgid_link,String PO_code,
			Long orgbuyerid_link,Long orgvendorid_link){
		int status = 0;
		Specification<POrder> specification = Specifications.<POrder>and()
				.eq("status", status)
	            .eq("granttoorgid_link", granttoorgid_link)
	            .ge(Objects.nonNull(golivedate_from),"golivedate",DateFormat.atStartOfDay(golivedate_from))
                .le(Objects.nonNull(golivedate_to),"golivedate",DateFormat.atEndOfDay(golivedate_to))
                .like(Objects.nonNull(PO_code), "pcontract_po.po_buyer", "%"+PO_code+"%")
                .eq(orgbuyerid_link != 0, "pcontract.orgbuyerid_link",orgbuyerid_link)
                .eq(orgvendorid_link!=0, "pcontract.orgvendorid_link", orgvendorid_link)
	            .build();
//		Sort sort = Sorts.builder()
//		        .desc("ordercode")
//		        .build();
//		List<POrder> a = repo.findAll(specification,sort);
		List<POrder> a = repo.findAll(specification);
		return a;
	}

	@Override
	public List<POrder> getPOrderListBySearch(String ordercode, String po, String style, Long buyerid, Long vendorid, Date orderdatefrom, Date orderdateto) {

		Specification<POrder> specification = Specifications.<POrder>and()
				.like(Objects.nonNull(ordercode), "ordercode", "%"+ordercode+"%")
				.like(Objects.nonNull(po), "pcontract_po.po_buyer", "%"+po+"%")
				.like(Objects.nonNull(style), "product.buyercode", "%"+style+"%")
				.eq(Objects.nonNull(buyerid), "pcontract.orgbuyerid_link", buyerid)
				.eq(Objects.nonNull(vendorid), "pcontract.orgvendorid_link", vendorid)
				.ge(Objects.nonNull(orderdatefrom),"orderdate",DateFormat.atStartOfDay(orderdatefrom))
                .le(Objects.nonNull(orderdateto),"orderdate",DateFormat.atEndOfDay(orderdateto))
				.build();
		
		return repo.findAll(specification);
	}
}
