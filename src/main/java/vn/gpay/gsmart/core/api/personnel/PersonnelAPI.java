package vn.gpay.gsmart.core.api.personnel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.personel.IPersonnel_Service;
import vn.gpay.gsmart.core.personel.IPersonnel_inout_Service;
import vn.gpay.gsmart.core.personel.Personel;
import vn.gpay.gsmart.core.personel.Personnel_inout;
import vn.gpay.gsmart.core.personnel_history.IPersonnel_His_Service;
import vn.gpay.gsmart.core.personnel_history.Personnel_His;
import vn.gpay.gsmart.core.personnel_notmap.IPersonnel_notmap_Service;
import vn.gpay.gsmart.core.personnel_notmap.Personnel_notmap;
import vn.gpay.gsmart.core.personnel_type.IPersonnelType_Service;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
import vn.gpay.gsmart.core.porder_grant_balance.IPOrderGrantBalanceService;
import vn.gpay.gsmart.core.porder_grant_balance.POrderGrantBalance;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.security.GpayUserOrg;
import vn.gpay.gsmart.core.security.IGpayUserOrgService;
import vn.gpay.gsmart.core.security.IGpayUserService;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/personnel")
public class PersonnelAPI {
	@Autowired IPersonnelType_Service personneltypeService;
	@Autowired IPersonnel_Service personService;
	@Autowired IPersonnel_His_Service hispersonService;
	@Autowired IPOrderGrant_Service pordergrantService;
	@Autowired IPOrderGrantBalanceService pordergrantBalanceService;
	@Autowired IPersonnel_notmap_Service personnelNotmapService;
	@Autowired Common commonService;
	@Autowired IGpayUserService userService;
	@Autowired IGpayUserOrgService userOrgService;
	@Autowired IPersonnel_inout_Service person_inout_Service;
	
	@RequestMapping(value = "/gettype",method = RequestMethod.POST)
	public ResponseEntity<gettype_response> getType(HttpServletRequest request ) {
		gettype_response response = new gettype_response();
		try {
			response.data = personneltypeService.findAll();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<gettype_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<gettype_response>(response,HttpStatus.OK);
		}
	}
	
	
	
	@RequestMapping(value = "/getby_org",method = RequestMethod.POST)
	public ResponseEntity<getperson_byorg_response> getType(HttpServletRequest request, @RequestBody getperson_byorgmanager_request entity ) {
		getperson_byorg_response response = new getperson_byorg_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			
			List<Personel> list = new ArrayList<Personel>();
			if(entity.orgid_link == orgrootid_link) {
				if(entity.isviewall)
					list = personService.findAll();
				else 
					list = personService.getby_orgmanager(entity.orgid_link, orgrootid_link);
			}
			else {
				if(entity.ismanager) {
					list = personService.getby_orgmanager(entity.orgid_link, orgrootid_link);
				}
				else {
					list = personService.getby_org(entity.orgid_link, orgrootid_link);
				}
			}
			
			
			response.data = list;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getperson_byorg_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<getperson_byorg_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getperson_by_user",method = RequestMethod.POST)
	public ResponseEntity<getperson_by_userid_response> getOrgByUser(HttpServletRequest request, @RequestBody getperson_by_userid_request entity ) {
		getperson_by_userid_response response = new getperson_by_userid_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long userid_link = user.getId();
			Long orgid_link = user.getOrgid_link();
			
			Long orgrootid_link = user.getRootorgid_link();
			
			List<Personel> list = new ArrayList<Personel>();
			if(orgid_link == orgrootid_link) {
				list = personService.findAll();
			}
			else {
				List<Long> orgs = new ArrayList<Long>();
				orgs.add(orgid_link);
				List<GpayUserOrg> list_user_org = userOrgService.getall_byuser(userid_link);
				for (GpayUserOrg userorg : list_user_org) {
					if(!orgs.contains(userorg.getOrgid_link())){
						orgs.add(userorg.getOrgid_link());
					}
				}
				
				list = personService.getby_orgs(orgs, orgrootid_link, true);
			}
			
			
			response.data = list;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getperson_by_userid_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<getperson_by_userid_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/viewimage",method = RequestMethod.POST)
	public ResponseEntity<personnel_viewimage_response> getType(HttpServletRequest request, @RequestBody personnel_viewimage_request entity ) {
		personnel_viewimage_response response = new personnel_viewimage_response();
		try {
			Personel person = personService.findOne(entity.id);
			String uploadRootPath = request.getServletContext().getRealPath("upload/personnel");
			String filePath = uploadRootPath+"/"+ person.getImage_name();
			Path path = Paths.get(filePath);
			byte[] data = Files.readAllBytes(path);
			response.data = data;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<personnel_viewimage_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<personnel_viewimage_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getimage",method = RequestMethod.GET)
	public ResponseEntity<byte[]> getImage(HttpServletRequest request, @RequestParam("id") Long id ) {
		try {
			Personel person = personService.findOne(id);
			String uploadRootPath = request.getServletContext().getRealPath("upload/personnel");
			String filePath = uploadRootPath+"/"+ person.getImage_name();
			Path path = Paths.get(filePath);
			byte[] data = Files.readAllBytes(path);
			
			HttpHeaders headers = new HttpHeaders();
		    headers.setCacheControl(CacheControl.noCache().getHeaderValue());
		    

			ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(data, headers, HttpStatus.OK);
		    return responseEntity;
		}catch (Exception e) {
			return null;
		}
	}
	
	
	
	@RequestMapping(value = "/create_his",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> CreateHis(HttpServletRequest request, @RequestBody personnel_create_his_request entity ) {
		ResponseBase response = new ResponseBase();
		try {
			Personnel_His person_his = entity.data;
			Personel person = personService.findOne(entity.data.getPersonnelid_link());
			if(person_his.getId() == null) {
				if(person_his.getType() == 1) {
					person.setPositionid_link(person_his.getPositionid_link());
				}
				else if(person_his.getType() == 2) {
					person.setLevelid_link(person_his.getLevelid_link());
				}
				else if(person_his.getType() == 3) {
					person.setOrgid_link(person_his.getOrgid_link());
				}
				else if(person_his.getType() == 4) {
					person.setSaltypeid_link(person_his.getSaltypeid_link());
					person.setSallevelid_link(person_his.getSallevelid_link());
				}
				personService.save(person);
			}
			else {
				//kiem tra xem co phai la sua cua hien tai khong thi moi update len thong tin person
				Long maxid = hispersonService.getmaxid_bytype_andperson(person.getId(), person_his.getType());
				if(maxid == person_his.getId()) {
					if(person_his.getType() == 1) {
						person.setPositionid_link(person_his.getPositionid_link());
					}
					else if(person_his.getType() == 2) {
						person.setLevelid_link(person_his.getLevelid_link());
					}
					else if(person_his.getType() == 3) {
						person.setOrgid_link(person_his.getOrgid_link());
					}
					else if(person_his.getType() == 4) {
						person.setSaltypeid_link(person_his.getSaltypeid_link());
						person.setSallevelid_link(person_his.getSallevelid_link());
					}
					personService.save(person);
				}
			}
			
			hispersonService.save(person_his);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/get_his_person",method = RequestMethod.POST)
	public ResponseEntity<get_person_his_response> getHis(HttpServletRequest request, @RequestBody get_person_his_request entity ) {
		get_person_his_response response = new get_person_his_response();
		try {
			response.data = hispersonService.gethis_by_person(entity.personnelid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<get_person_his_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<get_person_his_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/delete_his_person",method = RequestMethod.POST)
	public ResponseEntity<del_hisperson_response> DelHis(HttpServletRequest request, @RequestBody delete_hisperson_request entity ) {
		del_hisperson_response response = new del_hisperson_response();
		try {
			Long personnelid_link = entity.personnelid_link;
			Personnel_His his_person = hispersonService.findOne(entity.id);
			
			Long maxid = hispersonService.getmaxid_bytype_andperson(personnelid_link, his_person.getType());
			response.orgid_link = null;
			//Xoa lich su cuoi cung thi cap nhat person ve lan truoc do 
			if(maxid == entity.id) {
				Personel person = personService.findOne(personnelid_link);
				Personnel_His his_person_pre = hispersonService.getprehis_bytype_andperson(personnelid_link, his_person.getType());
				if(his_person_pre!=null) {
					if(his_person.getType() == 1) {
						person.setPositionid_link(his_person_pre.getPositionid_link());
					}
					else if(his_person.getType() == 2) {
						person.setLevelid_link(his_person_pre.getLevelid_link());
					}
					else if(his_person.getType() == 3) {
						person.setOrgid_link(his_person_pre.getOrgid_link());
						response.orgid_link = his_person_pre.getOrgid_link();
					}
					
				}
				else {
					if(his_person.getType() == 1) {
						person.setPositionid_link(null);
					}
					else if(his_person.getType() == 2) {
						person.setLevelid_link(null);
					}
					else if(his_person.getType() == 3) {
						person.setOrgid_link(null);
					}
				}
				
				personService.save(person);
			}

			hispersonService.deleteById(entity.id);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<del_hisperson_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<del_hisperson_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/upload_img", method = RequestMethod.POST)
	public ResponseEntity<upload_image_response> Upload_Img(HttpServletRequest request,
			@RequestParam("file") MultipartFile file, @RequestParam("id") long id) {
		upload_image_response response = new upload_image_response();

		try {
			Personel person = personService.findOne(id);
			String FolderPath = "upload/personnel";
			
			// Thư mục gốc upload file.			
			String uploadRootPath = request.getServletContext().getRealPath(FolderPath);

			File uploadRootDir = new File(uploadRootPath);
			// Tạo thư mục gốc upload nếu nó không tồn tại.
			if (!uploadRootDir.exists()) {
				uploadRootDir.mkdirs();
			}

			String name = file.getOriginalFilename();		
			if (name != null && name.length() > 0) {
				String[] str = name.split("\\.");
				String extend = str[str.length -1];	
				name = id+"."+extend;
				File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);

				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(file.getBytes());
				stream.close();
			}
			
			person.setImage_name(name);
			personService.save(person);
			
			response.data = file.getBytes();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<upload_image_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<upload_image_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public ResponseEntity<create_personnel_response> Create(HttpServletRequest request, @RequestBody create_personnel_request entity ) {
		create_personnel_response response = new create_personnel_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			
			Personel person = entity.data;
			Boolean isbike = person.getIsbike() == null ? false : person.getIsbike();
			person.setIsbike(isbike);
			if(person.getId() == null) {
				person.setOrgrootid_link(orgrootid_link);
				person.setStatus(0);//0-dang hoat dong;-1-da nghi viec
			}
			 
			if(person.getIsbike()) {
				person.setBike_number(commonService.get_BikeNUmber());
			}
			person = personService.save(person);
			
			response.id = person.getId();
			response.bike_number = person.getBike_number();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<create_personnel_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<create_personnel_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getby_pordergrant",method = RequestMethod.POST)
	public ResponseEntity<getperson_byorg_response> getby_pordergrant(HttpServletRequest request, @RequestBody getperson_bypordergrant_request entity ) {
		getperson_byorg_response response = new getperson_byorg_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			
			Long pordergrantid_link = entity.pordergrantid_link;
			POrderGrant porderGrant = pordergrantService.findOne(pordergrantid_link);
			Long orgid_link = porderGrant.getGranttoorgid_link();
			
			response.data = new ArrayList<Personel>();
					
			List<Personel> listPersonel = personService.getby_org(orgid_link, orgrootid_link);
			for(Personel personel : listPersonel) {
				Long personelId = personel.getId();
				List<POrderGrantBalance> listPOrderGrantBalance = 
						pordergrantBalanceService.getByPorderGrantAndPersonnel(pordergrantid_link, personelId);
				if(listPOrderGrantBalance.size() == 0) {
					response.data.add(personel);
				}
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getperson_byorg_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<getperson_byorg_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getPersonnelNotmap",method = RequestMethod.POST)
	public ResponseEntity<personnel_notmap_response> getPersonnelNotmap(HttpServletRequest request) {
		personnel_notmap_response response = new personnel_notmap_response();
		try {
			response.data = personnelNotmapService.findAll();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<personnel_notmap_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<personnel_notmap_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getByNotRegister",method = RequestMethod.POST)
	public ResponseEntity<getperson_byorg_response> getByNotRegister(HttpServletRequest request) {
		getperson_byorg_response response = new getperson_byorg_response();
		try {
			response.data = personService.getByNotRegister();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getperson_byorg_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<getperson_byorg_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/updatePersonnelNotmap",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> updatePersonnelNotmap(@RequestBody personnel_notmap_update_request entity,HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
			Personnel_notmap data = entity.data;
			Personel personnel = personService.findOne(entity.personnelid_link);
			
			personnel.setRegister_code(data.getRegister_code());
			personService.save(personnel);
			
			personnelNotmapService.deleteById(data.getId());
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getForPProcessingProductivity",method = RequestMethod.POST)
	public ResponseEntity<getperson_byorg_response> getForPProcessingProductivity(@RequestBody personnel_getForPProcessingProductivity_request entity,HttpServletRequest request) {
		getperson_byorg_response response = new getperson_byorg_response();
		try {
			Long orgid_link = entity.orgid_link;
			Integer shifttypeid_link = entity.shifttypeid_link;
			Date workingdate = entity.workingdate;
			
//			System.out.println(orgid_link);
//			System.out.println(shifttypeid_link);
//			System.out.println(workingdate);
			
			response.data = personService.getForPProcessingProductivity(orgid_link, shifttypeid_link, workingdate);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getperson_byorg_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<getperson_byorg_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/upadtetime_inout",method = RequestMethod.POST)
	public ResponseEntity<personnel_updatetime_inout_response> getForPProcessingProductivity(@RequestBody personnel_updatetime_inout_request entity,HttpServletRequest request) {
		personnel_updatetime_inout_response response = new personnel_updatetime_inout_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			for(Personnel_inout person: entity.data) {
				
				//kiem tra trong DB co chua thi them vao 
				List<Personnel_inout> person_check = person_inout_Service.getby_person(person.getId(), new Date());
				if(person_check.size() == 0) {
					Personnel_inout personnew = new Personnel_inout();
					personnew.setBike_number_out("");
					personnew.setId(null);
					personnew.setPersonnelid_link(person.getId());
					personnew.setTime_in(person.getTime_in());
					personnew.setTime_out(person.getTime_out());
					person_inout_Service.save(personnew);
				}
				else {
					person.setUsercheck_checkout(user.getId());
					person_inout_Service.save(person);
				}
			}
			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<personnel_updatetime_inout_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<personnel_updatetime_inout_response>(response,HttpStatus.OK);
		}
	}
}
