package vn.gpay.gsmart.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.holiday.Holiday;
import vn.gpay.gsmart.core.holiday.IHolidayService;
import vn.gpay.gsmart.core.pcontractattributevalue.IPContractProductAtrributeValueService;
import vn.gpay.gsmart.core.pcontractbomcolor.IPContractBOMColorService;
import vn.gpay.gsmart.core.pcontractbomcolor.PContractBOMColor;
import vn.gpay.gsmart.core.pcontractbomsku.IPContractBOMSKUService;
import vn.gpay.gsmart.core.pcontractbomsku.PContractBOMSKU;
import vn.gpay.gsmart.core.pcontractproductbom.IPContractProductBomService;
import vn.gpay.gsmart.core.pcontractproductbom.PContractProductBom;
import vn.gpay.gsmart.core.pcontratproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.pcontratproductsku.PContractProductSKU;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sku.ISKU_AttributeValue_Service;
import vn.gpay.gsmart.core.stockingunique.IStockingUniqueService;
import vn.gpay.gsmart.core.stockingunique.StockingUniqueCode;

@Service
public class Common {
	@Autowired IPContractBOMSKUService pcontractBOMSKUService;
	@Autowired ISKU_AttributeValue_Service skuavService;
	@Autowired IPContractBOMColorService bomcolorService;
	@Autowired IPContractProductAtrributeValueService ppavService;
	@Autowired IPContractProductSKUService ppskuService;
	@Autowired IPContractProductBomService ppbomService;
	@Autowired IHolidayService holidayService;
	
	@Autowired IStockingUniqueService stockService;
	
	public List<PContractBOMSKU> getBOMSKU_PContract_Product(long pcontractid_link, long productid_link, List<PContractProductSKU> listsku){
		List<PContractBOMSKU> listbomsku = new ArrayList<PContractBOMSKU>();
		
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long orgrootid_link = user.getRootorgid_link();
				
		// Lay tu bom sku
		for (PContractProductSKU sku : listsku) {	
			List<PContractBOMSKU> lsSKUBOM = pcontractBOMSKUService.getMaterials_BySKUId(sku.getSkuid_link());
			listbomsku.addAll(lsSKUBOM);
		}
		
		
		//Lay tu bom color
		long colorid_link = 0;
		
		List<PContractBOMColor> list_color_bom = bomcolorService.getall_byproduct(pcontractid_link, productid_link);
		for(PContractBOMColor bom_color : list_color_bom ) {
			colorid_link = bom_color.getColorid_link();
			//Lay cac sku co mau trong san pham
			List<Long> list_sku_color = ppskuService.getsku_bycolor(pcontractid_link, productid_link,colorid_link);
			
			for (Long skuid_link : list_sku_color) {	
				PContractBOMSKU skubom = new PContractBOMSKU();
				skubom.setAmount(bom_color.getAmount());
				skubom.setId(null);
				skubom.setMaterialid_link(bom_color.getMaterialid_link());
				skubom.setPcontractid_link(pcontractid_link);
				skubom.setProductid_link(productid_link);
				skubom.setSkuid_link(skuid_link);
				skubom.setOrgrootid_link(orgrootid_link);
				skubom.setLost_ratio((float)0);
				
				listbomsku.add(skubom);
			}
		}
		
		//Lay tu bom product (Chung ca san pham)
		List<PContractProductBom> listproductbom = ppbomService.get_pcontract_productBOMbyid(productid_link, pcontractid_link);
		
		List<PContractProductSKU> list_sku = ppskuService.getlistsku_byproduct_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
		
		for(PContractProductBom ppbom : listproductbom) {
			for(PContractProductSKU ppsku : list_sku) {
				PContractBOMSKU skubom = new PContractBOMSKU();
				skubom.setAmount(ppbom.getAmount());
				skubom.setId(null);
				skubom.setMaterialid_link(ppbom.getMaterialid_link());
				skubom.setPcontractid_link(pcontractid_link);
				skubom.setProductid_link(productid_link);
				skubom.setSkuid_link(ppsku.getSkuid_link());
				skubom.setOrgrootid_link(orgrootid_link);
				skubom.setLost_ratio((float)0);
				
				listbomsku.add(skubom);
			}
		}
		
		
		return listbomsku;
	}

	
	public String getFolderPath(int producttypeid_link) {
		String Path = "upload/";
		if(10 <= producttypeid_link && 20 > producttypeid_link) {
			Path += "product";
		}
		else if (20 <= producttypeid_link && 30 > producttypeid_link) {
			if (producttypeid_link == 21) {
				Path += "Sub_Material"; //Vai lot
			}
			else if (producttypeid_link == 22) {
				Path += "Mix_Material"; // Vai phoi
			}
			else if (producttypeid_link == 23) {
				Path += "Mex"; // Vai phoi
			}
		}
		else if (30 <= producttypeid_link && 40 > producttypeid_link) {
			Path += "sewingtrim";
		}
		else if (40 <= producttypeid_link && 50 > producttypeid_link) {
			Path += "packingtrim";
		}
		
		return Path;
	}
	
	public String getInvoiceNumber() {
		String invoice_number = "";
		StockingUniqueCode stocking = stockService.getby_type(1);
		String prefix = stocking.getStocking_prefix();
		Integer max = stocking.getStocking_max() + 1;
		String STT = max.toString();
		
		while(STT.toString().length() < 5) {
			STT = "0"+STT;
		}
		invoice_number = prefix + "_" + STT;
		return invoice_number;
	}
	
	public static void copyFile(File source, File dest) throws IOException {
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
	}

	public int getDuration(Date startdate, Date enddate, long orgrootid_link, int year) {
		int duration = 0;		
		
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		
		start.setTime(startdate);
		end.setTime(enddate);
		
		List<Holiday> list_holiday = holidayService.getby_year(orgrootid_link, year);
		
		while(start.before(end)) {
			if(start.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				boolean check = false;
				for(Holiday holiday : list_holiday) {
					Calendar day = Calendar.getInstance();
					day.setTime(holiday.getDay());
					if(start.compareTo(day) == 0) {
						check = true;
						break;
					}
				}
				
				if(!check) {
					duration++;
				}
			}
			
			start.add(Calendar.DAY_OF_WEEK , 1);
		}
		
		return duration;
	}
	
	public boolean check_dayoff(Calendar _date, long orgrootid_link ,int year) {
		if(_date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			return true;
		else {
			List<Holiday> list_holiday = holidayService.getby_year(orgrootid_link, year);
			for(Holiday holiday : list_holiday) {
				if(_date.getTime().compareTo(holiday.getDay()) == 0) {
					return true;
				}
			}
		}
		return false;
	}
	public Date Date_Add(Date date, int amount) {
		Calendar _date = Calendar.getInstance();
		_date.setTime(date);
		_date.add(Calendar.DATE, amount);
		return _date.getTime();
	}
	public Date Date_Add_with_holiday(Date date, int amount, long orgrootid_link, int year) {
		int count = 0;
		
		Calendar _date = Calendar.getInstance();
		_date.setTime(date);
		
		while(count<amount) {
			_date.add(Calendar.DATE, 1);
			if(!check_dayoff(_date, orgrootid_link, year)) {
				count++;
			}
		}
		
		return _date.getTime();
	}
	
	public Date getEndOfDate(Date date) {
		Calendar start = Calendar.getInstance();
		start.setTime(date);
		start.add(Calendar.DAY_OF_WEEK, 1);
		start.add(Calendar.MINUTE, -1);
		Date date_ret = start.getTime();
		return date_ret;
	}
	
	public Date getPrevious(Date date) {
		Calendar start = Calendar.getInstance();
		start.setTime(date);
		start.add(Calendar.MINUTE, -1);
		Date date_ret = start.getTime();
		return date_ret;
	}
}
