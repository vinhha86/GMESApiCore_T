package vn.gpay.gsmart.core.api.salary;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.personel.IPersonnel_Service;
import vn.gpay.gsmart.core.personel.Personel;
import vn.gpay.gsmart.core.porderprocessingns.IPorderProcessingNsService;
import vn.gpay.gsmart.core.salary.IOrgSal_BasicService;
import vn.gpay.gsmart.core.salary.IOrgSal_ComService;
import vn.gpay.gsmart.core.salary.IOrgSal_Com_LaborLevelService;
import vn.gpay.gsmart.core.salary.IOrgSal_Com_PositionService;
import vn.gpay.gsmart.core.salary.IOrgSal_LevelService;
import vn.gpay.gsmart.core.salary.IOrgSal_TypeService;
import vn.gpay.gsmart.core.salary.IOrgSal_Type_LaborLevelService;
import vn.gpay.gsmart.core.salary.IOrgSal_Type_LevelService;
import vn.gpay.gsmart.core.salary.ISalary_SumService;
import vn.gpay.gsmart.core.salary.ISalary_Sum_POrdersService;
import vn.gpay.gsmart.core.salary.OrgSal_Basic;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/salarysum")
public class Salary_SumAPI {
	@Autowired IOrgSal_TypeService saltypeService;
	@Autowired IOrgSal_Type_LevelService saltype_levelService;
	@Autowired IOrgSal_Type_LaborLevelService saltype_laborlevelService;
	@Autowired IOrgSal_BasicService salbasicService;
	@Autowired IOrgSal_LevelService sallevelService;
	@Autowired IOrgSal_ComService salcomService;
	@Autowired IOrgSal_Com_LaborLevelService salcom_laborService;
	@Autowired IOrgSal_Com_PositionService salcom_positionService;
	@Autowired ISalary_SumService salarysumService;
	@Autowired IPersonnel_Service personnelService;
	@Autowired ISalary_Sum_POrdersService salarysum_pordersService;
	@Autowired IPorderProcessingNsService porderprocessing_nsService;
	
	@RequestMapping(value = "/salary_sum_byorg", method = RequestMethod.POST)
	public ResponseEntity<salary_sum_response> salary_sum_byorg(HttpServletRequest request,
			@RequestBody salary_sum_byorg_request entity) {
//		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		salary_sum_response response = new salary_sum_response();
		try {
			response.data = salarysumService.getall_byorg(entity.orgid_link, entity.year, entity.month);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<salary_sum_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<salary_sum_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "/salary_sum_calculate", method = RequestMethod.POST)
	public ResponseEntity<salary_sum_response> salary_sum_calculate(HttpServletRequest request,
			@RequestBody salary_sum_byorg_request entity) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long orgrootid_link = user.getRootorgid_link();
		salary_sum_response response = new salary_sum_response();
		try {
			//1. Lay danh sach nhan su cua don vi quan ly (orgmanagerid_link)
			List<Personel> ls_Personnel = personnelService.getby_orgmanager(entity.orgid_link, orgrootid_link);
			//2. Lay thong tin luong Basic cua don vi quan ly
			OrgSal_Basic theSalBasic = salbasicService.getone_byorg(entity.orgid_link);
			CountDownLatch latch = new CountDownLatch(ls_Personnel.size());
			for(Personel personnel:ls_Personnel){
				Salary_Personnel sal_personnel =  new Salary_Personnel(
						personnel,
						entity.year,
						entity.month,
						theSalBasic,
						entity.orgid_link,
						saltypeService,
						saltype_levelService,
						salcomService,
						salbasicService,
						salarysumService,
						salarysum_pordersService,
						porderprocessing_nsService,
						latch);
				sal_personnel.start();
			}
			latch.await();
            response.data = salarysumService.getall_byorg(entity.orgid_link, entity.year, entity.month);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<salary_sum_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<salary_sum_response>(response, HttpStatus.BAD_REQUEST);
		}
	}

}
