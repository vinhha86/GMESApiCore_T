package vn.gpay.gsmart.core.api.pcontract_price;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.fob_price.FOBPrice;
import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.pcontract_price.IPContract_Price_DService;
import vn.gpay.gsmart.core.pcontract_price.IPContract_Price_Service;
import vn.gpay.gsmart.core.pcontract_price.PContract_Price;
import vn.gpay.gsmart.core.pcontract_price.PContract_Price_D;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/pcontract_price_d")
public class PContract_Price_DAPI {
	@Autowired IPContract_Price_DService pcontractPriceDservice;
	@Autowired IPContract_Price_Service pcontractPriceservice;
	@Autowired IPContract_POService pcontractPoService;
	
	@RequestMapping(value = "/getByPO", method = RequestMethod.POST)
	public ResponseEntity<get_byPo_response> GetByPO(@RequestBody get_byPo_request entity,
			HttpServletRequest request) {
		get_byPo_response response = new get_byPo_response();
		try {
			PContract_PO pcontractpo = pcontractPoService.findOne(entity.pcontract_poid_link);
			List<PContract_Price_D> list = pcontractPriceDservice.getPrice_D_ByPO(pcontractpo.getParentpoid_link() == null ? entity.pcontract_poid_link : pcontractpo.getParentpoid_link());
			response.data = new ArrayList<PContract_Price_D>();
			for(PContract_Price_D pcontractpriced : list) {
				if(pcontractpriced.getProductType() == 5) {
					// bộ
					continue;
				}else {
					// đơn
//					Float price0 = (float) 0;
					if(pcontractpriced.getIsfob() == false) continue;
//					if(pcontractpriced.getSizesetname().equals("ALL")) continue;
//					if(pcontractpriced.getPrice().equals(price0) 
//							&& pcontractpriced.getUnitid_link() == null 
//							&& pcontractpriced.getUnitprice() == null
//							&& pcontractpriced.getQuota() == null) continue;
					// nếu có size != ALL thì ko lấy ALL
//					List<PContract_Price> temp = new ArrayList<PContract_Price>();
					List<PContract_Price> temp = pcontractPriceservice.getBySizesetNotAll(pcontractpriced.getPcontract_poid_link());
					if(temp.size() > 0 && pcontractpriced.getSizesetname().equals("ALL")) continue;
					response.data.add(pcontractpriced);
				}
			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));

			return new ResponseEntity<get_byPo_response>(response, HttpStatus.OK);

		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<get_byPo_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/createPContractPriceD", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> createPContractPriceD(@RequestBody PContractPriceD_createPContractPriceD_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Date date = new Date();
			
			List<FOBPrice> listFobPrice = entity.data;
			Long pcontract_poid_link = entity.pcontract_poid_link;
			
			PContract_PO pcontractPo = pcontractPoService.findOne(pcontract_poid_link);
			if(pcontractPo.getParentpoid_link() != null) pcontract_poid_link = pcontractPo.getParentpoid_link();
			
			List<PContract_Price> listPContractPrice = pcontractPriceservice.getPrice_ByPO(pcontract_poid_link);
			// với mỗi size thêm các fobprice, nếu có rồi thì ko thêm
			for(PContract_Price pcontractPrice : listPContractPrice) {
				for(FOBPrice fobPrice : listFobPrice) {
					List<PContract_Price_D> listPContractPriceD = 
							pcontractPriceDservice.getPrice_D_ByFobPriceAndPContractPrice(
									pcontractPrice.getId(), fobPrice.getId());
					if(listPContractPriceD.size() > 0) continue;
					
					PContract_Price_D pcontractPriceD = new PContract_Price_D();
					pcontractPriceD.setId(0L);
					pcontractPriceD.setPcontractid_link(pcontractPrice.getPcontractid_link());
					pcontractPriceD.setProductid_link(pcontractPrice.getProductid_link());
					pcontractPriceD.setPcontract_poid_link(pcontractPrice.getPcontract_poid_link());
					pcontractPriceD.setPrice(0F);
					pcontractPriceD.setCurrencyid_link(0L);
					pcontractPriceD.setUsercreatedid_link(user.getId());
					pcontractPriceD.setDatecreated(date);
					pcontractPriceD.setOrgrootid_link(user.getRootorgid_link());
					pcontractPriceD.setCost(0F);
					pcontractPriceD.setIsfob(true);
					pcontractPriceD.setPcontractpriceid_link(pcontractPrice.getId());
					pcontractPriceD.setFobpriceid_link(fobPrice.getId());
					pcontractPriceDservice.save(pcontractPriceD);
					
				}
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));

			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);

		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}
}
