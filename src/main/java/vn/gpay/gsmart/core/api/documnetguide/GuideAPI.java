package vn.gpay.gsmart.core.api.documnetguide;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
import vn.gpay.gsmart.core.base.ResponseError;
import vn.gpay.gsmart.core.documentguide.DocumentGuide;
import vn.gpay.gsmart.core.documentguide.IDocumentGuide_Service;
import vn.gpay.gsmart.core.security.GpayAuthentication;
import vn.gpay.gsmart.core.utils.AtributeFixValues;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/documentguide")
public class GuideAPI {
	@Autowired IDocumentGuide_Service guideService;
	
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public ResponseEntity<guide_create_response> Create(HttpServletRequest request,
			@RequestParam("file") MultipartFile file) {
		guide_create_response response = new guide_create_response();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			
			String FolderPath = String.format("%s/document_guide/", AtributeFixValues.folder_upload);
			
			String uploadRootPath = request.getServletContext().getRealPath("");
			File uploadRootDir = new File(uploadRootPath);
			File folder_upload = new File(uploadRootDir.getParent()+"/"+FolderPath);
			
			if (!folder_upload.exists()) {
				folder_upload.mkdirs();
			}
			
			String name = file.getOriginalFilename();		
			if (name != null && name.length() > 0) {
				File serverFile = new File(folder_upload.getAbsolutePath() + File.separator + name);

				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(file.getBytes());
				stream.close();
			}
			
			DocumentGuide guide = new DocumentGuide();
			guide.setId(null);
			guide.setOrgrootid_linkl(user.getRootorgid_link());
			guide.setFilename(name);
			guide.setName(name);
			
			guideService.save(guide);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<guide_create_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<guide_create_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getall",method = RequestMethod.POST)
	public ResponseEntity<guide_getall_response> GetAll(HttpServletRequest request) {
		guide_getall_response response = new guide_getall_response();
		try {
			response.data = guideService.findAll();					
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<guide_getall_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<guide_getall_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Update(HttpServletRequest request, @RequestBody guide_update_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			DocumentGuide guide = entity.data;
			guideService.save(guide);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/download",method = RequestMethod.POST)
	public ResponseEntity<guide_download_response> Download(HttpServletRequest request, @RequestBody guide_download_request entity) {
		guide_download_response response = new guide_download_response();
		try {
			DocumentGuide guide = guideService.findOne(entity.id);
			
			String FolderPath = String.format("%s/document_guide/",AtributeFixValues.folder_upload);
			
			// Thư mục gốc upload file.			
			String uploadRootPath = request.getServletContext().getRealPath("");
			File uploadRootDir = new File(uploadRootPath);
			File folder_upload = new File(uploadRootDir.getParent()+"/"+FolderPath);
			
			String filePath = folder_upload+"/"+ guide.getFilename();
			Path path = Paths.get(filePath);
			byte[] data = Files.readAllBytes(path);
			response.data = data;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<guide_download_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<guide_download_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Delete(HttpServletRequest request, @RequestBody guide_delete_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			String FolderPath = String.format("%s/document_guide/",AtributeFixValues.folder_upload);
			
			// Thư mục gốc upload file.			
			String uploadRootPath = request.getServletContext().getRealPath("");
			File uploadRootDir = new File(uploadRootPath);
			File folder_upload = new File(uploadRootDir.getParent()+"/"+FolderPath);
			
			String filePath = folder_upload+"/"+ entity.filename;
			File file = new File(filePath);
			file.delete();
			
			//Xoa trong db
			guideService.deleteById(entity.id);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
