package vn.gpay.gsmart.core.api.personnel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import vn.gpay.gsmart.core.personel.Personel;
import vn.gpay.gsmart.core.personnel_history.IPersonnel_His_Service;
import vn.gpay.gsmart.core.personnel_history.Personnel_His;
import vn.gpay.gsmart.core.personnel_type.IPersonnelType_Service;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/personnel")
public class PersonnelAPI {
	@Autowired IPersonnelType_Service personneltypeService;
	@Autowired IPersonnel_Service personService;
	@Autowired IPersonnel_His_Service hispersonService;
	
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
			if(person.getId() == null) {
				person.setOrgrootid_link(orgrootid_link);
			}
			person = personService.save(person);
			
			response.id = person.getId();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<create_personnel_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<create_personnel_response>(response,HttpStatus.OK);
		}
	}
}
