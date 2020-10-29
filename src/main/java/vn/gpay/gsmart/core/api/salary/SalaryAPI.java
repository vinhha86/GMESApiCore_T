package vn.gpay.gsmart.core.api.salary;

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
import vn.gpay.gsmart.core.salary.IOrgSal_BasicService;
import vn.gpay.gsmart.core.salary.IOrgSal_LevelService;
import vn.gpay.gsmart.core.salary.IOrgSal_TypeService;
import vn.gpay.gsmart.core.salary.IOrgSal_Type_LaborLevelService;
import vn.gpay.gsmart.core.salary.IOrgSal_Type_LevelService;
import vn.gpay.gsmart.core.salary.OrgSal_Basic;
import vn.gpay.gsmart.core.salary.OrgSal_Level;
import vn.gpay.gsmart.core.salary.OrgSal_Type;
import vn.gpay.gsmart.core.salary.OrgSal_Type_LaborLevel;
import vn.gpay.gsmart.core.salary.OrgSal_Type_Level;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/salary")
public class SalaryAPI {
	@Autowired IOrgSal_TypeService saltypeService;
	@Autowired IOrgSal_Type_LevelService saltype_levelService;
	@Autowired IOrgSal_Type_LaborLevelService saltype_laborlevelService;
	@Autowired IOrgSal_BasicService salbasicService;
	@Autowired IOrgSal_LevelService sallevelService;
	
	@RequestMapping(value = "/saltype_level_byorg", method = RequestMethod.POST)
	public ResponseEntity<saltype_level_response> saltype_byorg(HttpServletRequest request,
			@RequestBody saltype_level_request entity) {
//		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		saltype_level_response response = new saltype_level_response();
		try {
			response.data = saltype_levelService.getall_byorg_and_type(entity.orgid_link,entity.typeid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<saltype_level_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<saltype_level_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "/saltype_level_update", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> saltype_level_update(HttpServletRequest request,
			@RequestBody saltype_level_update_request entity) {
//		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ResponseBase response = new ResponseBase();
		try {
			OrgSal_Type_Level saltype_level = saltype_levelService.findOne(entity.id);
			if (null != saltype_level){
				saltype_level.setSalratio(entity.salratio);
				saltype_level.setSalamount(entity.salamount);
				saltype_levelService.save(saltype_level);
				
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("Không tồn tại ngạch lương - bậc lương");
				return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "/saltype_laborlevel", method = RequestMethod.POST)
	public ResponseEntity<saltype_laborlevel_response> saltype_laborlevel(HttpServletRequest request,
			@RequestBody saltype_laborlevel_request entity) {
//		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		saltype_laborlevel_response response = new saltype_laborlevel_response();
		try {
			response.data = saltype_laborlevelService.getall_bysaltype(entity.saltypeid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<saltype_laborlevel_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<saltype_laborlevel_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "/salbasic_byorg", method = RequestMethod.POST)
	public ResponseEntity<salbasic_response> salbasic_byorg(HttpServletRequest request,
			@RequestBody salbasic_request entity) {
//		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		salbasic_response response = new salbasic_response();
		try {
			response.data = salbasicService.getall_byorg(entity.orgid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<salbasic_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<salbasic_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/saltype_create", method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<ResponseBase> saltype_create(HttpServletRequest request,
			@RequestBody saltype_create_request entity) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long orgrootid_link = user.getRootorgid_link();
		ResponseBase response = new ResponseBase();
		try {
			//Lay danh sach tat ca cac sal_level cua dvi root
			List<OrgSal_Level> sal_level_lst = sallevelService.getall_byorgrootid(orgrootid_link);
			
			//Them moi saltype
			OrgSal_Type newSalType =  new OrgSal_Type();
			newSalType.setOrgrootid_link(orgrootid_link);
			newSalType.setOrgid_link(entity.orgid_link);
			newSalType.setCode(entity.saltype_code);
			newSalType.setName(entity.saltype_name);
			newSalType.setType(entity.typeid_link);
			newSalType = saltypeService.save(newSalType);
			
			//Them moi saltype_level cho tat ca cac sal_level
			for(OrgSal_Level sal_level:sal_level_lst){
				OrgSal_Type_Level newSalType_Level = new OrgSal_Type_Level();
				newSalType_Level.setOrgrootid_link(orgrootid_link);
				newSalType_Level.setSaltypeid_link(newSalType.getId());
				newSalType_Level.setSallevelid_link(sal_level.getId());
				
				saltype_levelService.save(newSalType_Level);
			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "/saltype_laborlevel_create", method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<ResponseBase> saltype_laborlevel_create(HttpServletRequest request,
			@RequestBody saltype_laborlevel_create_request entity) {
//		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		Long orgrootid_link = user.getRootorgid_link();
		ResponseBase response = new ResponseBase();
		try {
			for (Long laborlevelid_link:entity.listId){
				//Neu chua ton tai --> Inserrt
				if (saltype_laborlevelService.getall_bysaltype_laborlevel(entity.saltypeid_link,laborlevelid_link).size() == 0){
					OrgSal_Type_LaborLevel newLaborLevel = new OrgSal_Type_LaborLevel();
					newLaborLevel.setSaltypeid_link(entity.saltypeid_link);
					newLaborLevel.setLaborlevelid_link(laborlevelid_link);
					saltype_laborlevelService.save(newLaborLevel);
				}
			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "/saltype_laborlevel_delete", method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<ResponseBase> saltype_laborlevel_delete(HttpServletRequest request,
			@RequestBody saltype_laborlevel_delete_request entity) {
//		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		Long orgrootid_link = user.getRootorgid_link();
		ResponseBase response = new ResponseBase();
		try {
			OrgSal_Type_LaborLevel theSal_Laborlevel = saltype_laborlevelService.findOne(entity.id);
			if (null != theSal_Laborlevel){
				saltype_laborlevelService.delete(theSal_Laborlevel);
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("Mã vị trí không tồn tại");
				return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
			}

		} catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}	
	@RequestMapping(value = "/salbasic_update", method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<ResponseBase> salbasic_update(HttpServletRequest request,
			@RequestBody salbasic_update_request entity) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long orgrootid_link = user.getRootorgid_link();
		ResponseBase response = new ResponseBase();
		try {
			if (null != entity.id){
				OrgSal_Basic theSalBasic = salbasicService.findOne(entity.id);
				if (null != theSalBasic){
					theSalBasic.setSal_basic(entity.sal_basic);
					theSalBasic.setSal_min(entity.sal_min);
					theSalBasic.setWorkingdays(entity.workingdays);
					theSalBasic.setCostpersecond(entity.costpersecond);
					theSalBasic.setOvertime_normal(entity.overtime_normal);
					theSalBasic.setOvertime_weekend(entity.overtime_weekend);
					theSalBasic.setOvertime_holiday(entity.overtime_holiday);
					theSalBasic.setOvertime_night(entity.overtime_night);
					
					salbasicService.save(theSalBasic);
					
					response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
					return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
				} else {
					response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
					response.setMessage("Mã vị trí không tồn tại");
					return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
				}
			} else {
				OrgSal_Basic theSalBasic = new OrgSal_Basic();
				
				theSalBasic.setOrgrootid_link(orgrootid_link);
				theSalBasic.setOrgid_link(entity.orgid_link);
				theSalBasic.setSal_basic(entity.sal_basic);
				theSalBasic.setSal_min(entity.sal_min);
				theSalBasic.setWorkingdays(entity.workingdays);
				theSalBasic.setCostpersecond(entity.costpersecond);
				theSalBasic.setOvertime_normal(entity.overtime_normal);
				theSalBasic.setOvertime_weekend(entity.overtime_weekend);
				theSalBasic.setOvertime_holiday(entity.overtime_holiday);
				theSalBasic.setOvertime_night(entity.overtime_night);
				
				salbasicService.save(theSalBasic);
				
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
			}
		} catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}	
}
