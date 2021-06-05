package vn.gpay.gsmart.core.api.qrcode;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.personel.IPersonnel_Service;
import vn.gpay.gsmart.core.personel.Personel;
import vn.gpay.gsmart.core.utils.GenQrCode;

@RestController
@RequestMapping("/api/v1/qrocde")
public class QRcodeAPI {
	@Autowired IPersonnel_Service personService;
	
	@RequestMapping(value = "/getqr_code_personel", method = RequestMethod.GET)
	public ResponseEntity<byte[]> GetQRPersonel(HttpServletRequest request, @RequestParam("text") String text) {
		try {
			GenQrCode qrcode = new GenQrCode();
//			byte[] qr_img = qrcode.getQRCodeImage(text, 225, 225);
			while(text.length()<5) {
				text = "0" +text;
			}
			HttpHeaders headers = new HttpHeaders();
		    byte[] media = qrcode.getQRCodeImage(text, 500, 500);
		    headers.setCacheControl(CacheControl.noCache().getHeaderValue());
		    

			ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
		    return responseEntity;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	@RequestMapping(value = "/getqr_code_bike_number", method = RequestMethod.GET)
	public ResponseEntity<byte[]> GetQRBikeNumber(HttpServletRequest request, @RequestParam("text") String text) {
		try {
			if(text!= "") {
				GenQrCode qrcode = new GenQrCode();
				while(text.length() < 10) {
					text += " ";
				}
				
				HttpHeaders headers = new HttpHeaders();
			    byte[] media = qrcode.getQRCodeImage(text, 500, 500);
			    headers.setCacheControl(CacheControl.noCache().getHeaderValue());			    

				ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
			    return responseEntity;
			}
			return null;
		}
		catch(Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/getlogo", method = RequestMethod.GET)
	public ResponseEntity<byte[]> GetLoGo(HttpServletRequest request) {
		try {
			String uploadRootPath = request.getServletContext().getRealPath("upload/logo");
			String filePath = uploadRootPath+"/dha.jpg";
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
	
	@RequestMapping(value = "/getimage_person",method = RequestMethod.GET)
	public ResponseEntity<byte[]> getImagePerson(HttpServletRequest request, @RequestParam("id") Long id ) {
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
}
