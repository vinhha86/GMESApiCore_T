package vn.gpay.gsmart.core.porder_req;

import java.util.ArrayList;
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
import vn.gpay.gsmart.core.utils.GPAYDateFormat;

@Service
public class POrder_Req_Service extends AbstractService<POrder_Req> implements IPOrder_Req_Service {
	@Autowired IPOrder_Req_Repository repo;
	protected JpaRepository<POrder_Req, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	
	@Override
	public List<POrder_Req> getByContract(Long pcontractid_link){
		return repo.getByContract(pcontractid_link);
	}
	
	@Override
	public Long savePOrder_Req(POrder_Req porder_req){
		try {
			porder_req = this.save(porder_req);
			return porder_req.getId();
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<POrder_Req> getByContractAndProduct(Long pcontractid_link, Long productid_link){
		return repo.getByContractAndProduct(pcontractid_link,productid_link);
	}
	
	@Override
	public List<POrder_Req> getByContractAndPO(Long pcontractid_link, Long pcontract_poid_link){
		return repo.getByContractAndPO(pcontractid_link,pcontract_poid_link);
	}
	
	@Override
	public List<POrder_Req> getByPO(Long pcontract_poid_link){
		return repo.getByPO(pcontract_poid_link);
	}
	
	@Override
	public List<POrder_Req> getByOrg_PO_Product(Long pcontract_poid_link, long productid_link, long granttoorgid_link){
		return repo.getByOrg_PO_Product(pcontract_poid_link, productid_link, granttoorgid_link);	
	} 
	
	@Override
	public List<POrder_Req> getByStatus(Integer status){
		return repo.getByStatus(status);
	}
	

	@Override
	public List<POrder_Req> getFilter(
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
				Specification<POrder_Req> specification = Specifications.<POrder_Req>and()
						.eq(null!=status && status !=-1, "status", status)
			            .eq(null!=granttoorgid_link && granttoorgid_link != -1, "granttoorgid_link", granttoorgid_link)
			            .like(null!=ordercode && ordercode !="", "ordercode", "%"+ordercode+"%")
			            .like(null!=collection && collection !="", "collection", "%"+collection+"%")
			            .like(null!=season && season !="", "season", "%"+season+"%")
			            .eq(Objects.nonNull(salaryyear), "salaryyear", salaryyear)
			            .eq(null!=salarymonth && salarymonth != -1, "salarymonth", salarymonth)
			            .ge(Objects.nonNull(processingdate_from),"orderdate",GPAYDateFormat.atStartOfDay(processingdate_from))
		                .le(Objects.nonNull(processingdate_to),"orderdate",GPAYDateFormat.atEndOfDay(processingdate_to))
		                .between(processingdate_from!=null && processingdate_to!=null,"orderdate", GPAYDateFormat.atStartOfDay(processingdate_from), GPAYDateFormat.atEndOfDay(processingdate_to))
			            .build();
				Sort sort = Sorts.builder()
				        .desc("ordercode")
				        .build();
				List<POrder_Req> a = repo.findAll(specification,sort);
				return a;
			} else {
				Specification<POrder_Req> specification = Specifications.<POrder_Req>and()
						.eq(null!=status && status !=-1, "status", status)
			            .eq(null!=granttoorgid_link && granttoorgid_link != -1, "granttoorgid_link", granttoorgid_link)
			            .like(null!=ordercode && ordercode !="", "ordercode", "%"+ordercode+"%")
			            .like(null!=collection && collection !="", "collection", "%"+collection+"%")
			            .like(null!=season && season !="", "season", "%"+season+"%")
			            .eq(Objects.nonNull(salaryyear), "salaryyear", salaryyear)
//			            .eq(null!=salarymonth && salarymonth != -1, "salarymonth", salarymonth)
			            .eq("salarymonth", salarymonth)
			            .ge(Objects.nonNull(processingdate_from),"orderdate",GPAYDateFormat.atStartOfDay(processingdate_from))
		                .le(Objects.nonNull(processingdate_to),"orderdate",GPAYDateFormat.atEndOfDay(processingdate_to))
		                .between(processingdate_from!=null && processingdate_to!=null,"orderdate", GPAYDateFormat.atStartOfDay(processingdate_from), GPAYDateFormat.atEndOfDay(processingdate_to))
			            .build();
				Sort sort = Sorts.builder()
				        .desc("ordercode")
				        .build();
				List<POrder_Req> a = repo.findAll(specification,sort);
				return a;
			}
		} catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public List<POrder_Req> get_by_org(long orgid_link) {
		// TODO Auto-generated method stub
		Specification<POrder_Req> specification = Specifications.<POrder_Req>and()
				.eq("granttoorgid_link", orgid_link)
				.eq("status", 0)
				.le("pcontract_po.status", 0)
	            .build();
		Sort sort = Sorts.builder()
		        .desc("id")
		        .build();
		List<POrder_Req> a = repo.findAll(specification,sort);
		return a.size() > 0 ? a : new ArrayList<POrder_Req>();
	}
	
	@Override
	//Danh sach cac lenh duoc phan cho Phan xuong nhung chua duoc phan chuyen
	public List<POrder_Req> get_free_bygolivedate(Date golivedate_from, Date golivedate_to, Long granttoorgid_link){
		Specification<POrder_Req> specification = Specifications.<POrder_Req>and()
				.eq("status", 0)
	            .eq("granttoorgid_link", granttoorgid_link)
	            .ge(Objects.nonNull(golivedate_from),"golivedate",GPAYDateFormat.atStartOfDay(golivedate_from))
                .le(Objects.nonNull(golivedate_to),"golivedate",GPAYDateFormat.atEndOfDay(golivedate_to))
	            .build();
//		Sort sort = Sorts.builder()
//		        .desc("ordercode")
//		        .build();
//		List<POrder> a = repo.findAll(specification,sort);
		List<POrder_Req> a = repo.findAll(specification);
		return a;
	}

	@Override
	public List<POrder_Req> getByPO_is_calculate(Long pcontract_poid_link) {
		// TODO Auto-generated method stub
		return repo.getByPO_calculate(pcontract_poid_link);
	}
}
