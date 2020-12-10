package vn.gpay.gsmart.core.api.pcontractproductdocument;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.gpay.gsmart.core.api.product.Product_viewimg_response;
import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.pcontract.IPContractService;
import vn.gpay.gsmart.core.pcontract.PContract;
import vn.gpay.gsmart.core.pcontractproductdocument.IPContractProducDocumentService;
import vn.gpay.gsmart.core.pcontractproductdocument.PContractProductDocument;
import vn.gpay.gsmart.core.product.IProductService;
import vn.gpay.gsmart.core.product.Product;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/pcontractdocument")
public class PContractDocumentAPI {
	@Autowired IPContractProducDocumentService pcdService;
	@Autowired IProductService productService;
	@Autowired IPContractService pcontractService;
	
	@RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Upload_doccument(HttpServletRequest request,
			@RequestParam("file") MultipartFile file, @RequestParam("pcontractid_link") long pcontractid_link,
			@RequestParam("productid_link") long productid_link) {
		ResponseBase response = new ResponseBase();

		try {
			Product product = productService.findOne(productid_link);
			PContract pcontract = pcontractService.findOne(pcontractid_link);
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			String FolderPath = String.format("upload/pcontract/%s/%s", pcontract.getContractcode(), product.getBuyercode());
			
			// Thư mục gốc upload file.			
			String uploadRootPath = request.getServletContext().getRealPath(FolderPath);
			uploadRootPath = uploadRootPath.replace("\\webapp", "");
			File uploadRootDir = new File(uploadRootPath);
			// Tạo thư mục gốc upload nếu nó không tồn tại.
			if (!uploadRootDir.exists()) {
				uploadRootDir.mkdirs();
			}

			String name = file.getOriginalFilename();		
			if (name != null && name.length() > 0) {
				File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);

				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(file.getBytes());
				stream.close();
			}
			
			PContractProductDocument ppd = new PContractProductDocument();
			ppd.setId((long)0);
			ppd.setFilename(name);
			ppd.setPcontractid_link(pcontractid_link);
			ppd.setProductid_link(productid_link);
			ppd.setOrgrootid_link(orgrootid_link);
			ppd.setDescription("");
			
			pcdService.save(ppd);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getbyproduct", method = RequestMethod.POST)
	public ResponseEntity<PContractDocument_getbyproduct_response> ProductDocument_GetAll(HttpServletRequest request,
			@RequestBody PContractDocument_getbyproduct_request entity) {
		PContractDocument_getbyproduct_response response = new PContractDocument_getbyproduct_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			
			response.data = pcdService.getlist_byproduct(orgrootid_link, pcontractid_link, productid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContractDocument_getbyproduct_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<PContractDocument_getbyproduct_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/download", method = RequestMethod.POST)
	public ResponseEntity<Product_viewimg_response> Download(HttpServletRequest request,
			@RequestBody PContractDocument_download_request entity)
	{

		Product_viewimg_response response = new Product_viewimg_response();
		try {
			Product product = productService.findOne(entity.productid_link);
			PContract pcontract = pcontractService.findOne(entity.pcontractid_link);
			
			String FolderPath = String.format("upload/pcontract/%s/%s", pcontract.getContractcode(), product.getBuyercode());
			
			// Thư mục gốc upload file.			
			String uploadRootPath = request.getServletContext().getRealPath(FolderPath);
			uploadRootPath = uploadRootPath.replace("\\webapp", "");
			String filePath = uploadRootPath+"/"+ entity.filename;
			Path path = Paths.get(filePath);
			byte[] data = Files.readAllBytes(path);
			response.data = data;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Product_viewimg_response>(response, HttpStatus.OK);			
		}
		catch(Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Product_viewimg_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/download_test", method = RequestMethod.POST)
	public @ResponseBody byte[] DownloadTest(HttpServletRequest request,
			@RequestBody PContractDocument_download_request entity)
	{

		Product_viewimg_response response = new Product_viewimg_response();
		try {
			Product product = productService.findOne(entity.productid_link);
			PContract pcontract = pcontractService.findOne(entity.pcontractid_link);
			
			String FolderPath = String.format("upload/pcontract/%s/%s", pcontract.getContractcode(), product.getCode());
			
			// Thư mục gốc upload file.			
			String uploadRootPath = request.getServletContext().getRealPath(FolderPath);
			uploadRootPath = uploadRootPath.replace("\\webapp", "");
			String filePath = uploadRootPath+"\\"+ entity.filename;
			Path path = Paths.get(filePath);
			byte[] data = Files.readAllBytes(path);
			response.data = data;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			
			return data;
		}
		catch(Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return null;
		}
	}
	
	@RequestMapping(value ="/update", method = RequestMethod.POST)
	public  ResponseEntity<ResponseBase> Update(HttpServletRequest request, @RequestBody PContractDocument_update_request entity){
		ResponseBase response = new ResponseBase();
		
		try {
			pcdService.save(entity.data);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Delete(HttpServletRequest request,@RequestBody PContractDocument_delete_request entity){
		ResponseBase response = new ResponseBase();
		try {
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			String filename = entity.filename;
			
			Product product = productService.findOne(productid_link);
			PContract pcontract = pcontractService.findOne(pcontractid_link);
			
			pcdService.deleteById(entity.id);
			
			String FolderPath = String.format("upload/pcontract/%s/%s", pcontract.getContractcode(), product.getBuyercode());
			
			// Thư mục gốc upload file.			
			String uploadRootPath = request.getServletContext().getRealPath(FolderPath);
			uploadRootPath = uploadRootPath.replace("\\webapp", "");
			String filePath = uploadRootPath+"\\"+ filename;
			File file = new File(filePath);
			file.delete();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
}
