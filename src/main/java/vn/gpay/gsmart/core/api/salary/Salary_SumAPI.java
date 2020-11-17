package vn.gpay.gsmart.core.api.salary;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.personel.IPersonnel_Service;
import vn.gpay.gsmart.core.personel.Personel;
import vn.gpay.gsmart.core.salary.IOrgSal_BasicService;
import vn.gpay.gsmart.core.salary.IOrgSal_ComService;
import vn.gpay.gsmart.core.salary.IOrgSal_Com_LaborLevelService;
import vn.gpay.gsmart.core.salary.IOrgSal_Com_PositionService;
import vn.gpay.gsmart.core.salary.IOrgSal_LevelService;
import vn.gpay.gsmart.core.salary.IOrgSal_TypeService;
import vn.gpay.gsmart.core.salary.IOrgSal_Type_LaborLevelService;
import vn.gpay.gsmart.core.salary.IOrgSal_Type_LevelService;
import vn.gpay.gsmart.core.salary.ISalary_SumService;
import vn.gpay.gsmart.core.salary.OrgSal_Basic;
import vn.gpay.gsmart.core.salary.OrgSal_Com;
import vn.gpay.gsmart.core.salary.OrgSal_Com_LaborLevel;
import vn.gpay.gsmart.core.salary.OrgSal_Com_Position;
import vn.gpay.gsmart.core.salary.OrgSal_Level;
import vn.gpay.gsmart.core.salary.OrgSal_Type;
import vn.gpay.gsmart.core.salary.OrgSal_Type_LaborLevel;
import vn.gpay.gsmart.core.salary.OrgSal_Type_Level;
import vn.gpay.gsmart.core.salary.Salary_Sum;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;
import vn.gpay.gsmart.core.utils.SalaryType;

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
	
	@RequestMapping(value = "/salary_sum_byorg", method = RequestMethod.POST)
	public ResponseEntity<salary_sum_response> salary_sum_byorg(HttpServletRequest request,
			@RequestBody salary_sum_byorg_request entity) {
//		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		salary_sum_response response = new salary_sum_response();
		try {
			response.data = salarysumService.getall_byorg(entity.orgid_link);
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
		List<Salary_Sum> data_Response =  new ArrayList<Salary_Sum>();
		try {
			//1. Lay danh sach nhan su cua don vi quan ly (orgmanagerid_link)
			List<Personel> ls_Personnel = personnelService.getby_orgmanager(entity.orgid_link, orgrootid_link);
			//2. Lay thong tin luong Basic cua don vi quan ly
			OrgSal_Basic theSalBasic = salbasicService.getone_byorg(entity.orgid_link);
			
			ArrayList<Thread> arrThreads = new ArrayList<Thread>();
			for(Personel personnel:ls_Personnel){
				Thread thread = new Thread(new Runnable(){
					public void run(){
						Salary_Sum sal_sum = new Salary_Sum();
						sal_sum.setPersonnelid_link(personnel.getId());
						
						//2.Lay thong tin thang luong, bac luong cua nhan su
						Long saltypeidlink = personnel.getSaltypeid_link(); //thang luong
						Long sallevelid_link = personnel.getSallevelid_link();//bac luong
						OrgSal_Type theSal_Type = saltypeService.findOne(saltypeidlink);
						if (null!=theSal_Type){
							//Neu la luong thoi gian
							if (theSal_Type.getType() == 0){
								//Tinh gia tri luong gio theo thang luong, bac luong va so ngay lam viec
								OrgSal_Type_Level theSal_Type_Level = saltype_levelService.get_bysaltype_and_level(saltypeidlink, sallevelid_link);
								if (null != theSal_Type_Level.getSalamount() && null != theSalBasic.getWorkingdays()){
									//Tinh luong gio cua nhan su
									int salary_hour = theSal_Type_Level.getSalamount()/theSalBasic.getWorkingdays();
									//
								}
							}
							//Neu la luong nang suat
							if (theSal_Type.getType() == 1){
								
							}
						}
						
						data_Response.add(sal_sum);
				    }
				});
				thread.start();
				arrThreads.add(thread);			
			}
            for (int i = 0; i < arrThreads.size(); i++) 
            {
                arrThreads.get(i).join(); 
            }
			response.data = data_Response;
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
