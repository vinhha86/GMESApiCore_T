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
	//Danh sach cac lenh duoc phan cho Phan xuong nhung chua duoc phan chuyen
	public List<POrder> get_free_bygolivedate(Date golivedate_from, Date golivedate_to, Long granttoorgid_link,
			Boolean isReqPorder){
		int status = isReqPorder == true ? -1: 0;
		Specification<POrder> specification = Specifications.<POrder>and()
				.le("status", 0)
				.ge("status", status)
	            .eq("granttoorgid_link", granttoorgid_link)
	            .ge(Objects.nonNull(golivedate_from),"golivedate",DateFormat.atStartOfDay(golivedate_from))
                .le(Objects.nonNull(golivedate_to),"golivedate",DateFormat.atEndOfDay(golivedate_to))
	            .build();
//		Sort sort = Sorts.builder()
//		        .desc("ordercode")
//		        .build();
//		List<POrder> a = repo.findAll(specification,sort);
		List<POrder> a = repo.findAll(specification);
		return a;
	}
}
