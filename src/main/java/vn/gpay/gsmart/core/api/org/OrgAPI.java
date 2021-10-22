package vn.gpay.gsmart.core.api.org;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.base.ResponseError;
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_req.IPOrder_Req_Service;
import vn.gpay.gsmart.core.porder_req.POrder_Req;
import vn.gpay.gsmart.core.security.GpayAuthentication;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.security.GpayUserOrg;
import vn.gpay.gsmart.core.security.IGpayUserOrgService;
import vn.gpay.gsmart.core.utils.OrgType;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/org")
public class OrgAPI {

	@Autowired
	IOrgService orgService;
	@Autowired
	IPOrder_Req_Service reqService;
	@Autowired
	IGpayUserOrgService userorgService;
	@Autowired
	IGpayUserOrgService userOrgService;
	@Autowired
	IPOrderGrant_Service porderGrantService;

	@GetMapping("/tosx")
	public List<Org> getAllOrgs_Tosx() throws IOException {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		List<Org> ls_tosx = orgService.findOrgByType(user.getRootorgid_link(), user.getOrgid_link(), 14);
		// Thêm điều kiện chọn tất để bỏ lọc trên giao diện
		Org all_option = new Org();
		all_option.setId((long) -1);
		all_option.setName("Xem tất");
		ls_tosx.add(all_option);

		return ls_tosx;
	}

	@RequestMapping(value = "/tosxbyparent", method = RequestMethod.POST)
	public ResponseEntity<?> getToSX_ByParent(@RequestBody GetOrgById_request entity, HttpServletRequest request) {// @RequestParam("type")
		OrgResponse response = new OrgResponse();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Org> ls_tosx = orgService.findChildByType(user.getRootorgid_link(), entity.id, 14);
			// Thêm điều kiện chọn tất để bỏ lọc trên giao diện
			Org all_option = new Org();
			all_option.setId((long) -1);
			all_option.setName("Xem tất");
			ls_tosx.add(all_option);

			response.data = ls_tosx;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getchilbytype", method = RequestMethod.POST)
	public ResponseEntity<?> getChilbyType(@RequestBody getchil_bytype_request entity, HttpServletRequest request) {// @RequestParam("type")
		OrgResponse response = new OrgResponse();
		try {
			// GpayUser user = (GpayUser)
			// SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// lấy tổ cụ thể trong đơn vị - theo tổ mà tài khoản đấy quản lý (nếu có)
//			if (user.getOrgid_link() != 1) {
//				if (user.getOrg_grant_id_link() != null) {
//					List<Org> lst_org = new ArrayList<Org>();
//					lst_org = orgService.getOrgById(user.getOrg_grant_id_link());
//					if (lst_org.size() != 0) {
//						response.data = lst_org;
//						response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//						response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
//						return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
//					}
//
//				}
//			}

			String[] listtype = entity.listtype.split(",");
			List<String> list = new ArrayList<String>();
			for (String string : listtype) {
				list.add(string);
			}

			List<Org> ls_tosx = new ArrayList<Org>();
			if (entity.parentid_link != 1)
				ls_tosx = orgService.findOrgByOrgTypeString(list, entity.parentid_link);
			else
				ls_tosx.add(orgService.findOne(1));

			response.data = ls_tosx;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getbyparent", method = RequestMethod.POST)
	public ResponseEntity<?> getByParent(@RequestBody GetOrgById_request entity, HttpServletRequest request) {// @RequestParam("type")
		OrgResponse response = new OrgResponse();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// lấy tổ cụ thể trong đơn vị - theo tổ mà tài khoản đấy quản lý (nếu có)
//			if (user.getOrgid_link() != 1) {
//				if (user.getOrg_grant_id_link() != null) {
//					List<Org> lst_org = new ArrayList<Org>();
//					lst_org = orgService.getOrgById(user.getOrg_grant_id_link());
//					if (lst_org.size() != 0) {
//						response.data = lst_org;
//						response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//						response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
//						return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
//					}
//
//				}
//			}

			List<String> list = new ArrayList<String>();
			list.add("3");
			list.add("8");
			list.add("9");
			list.add("14");
			list.add("17");
			list.add("4");
			list.add("19");
			list.add("20");
			list.add("21");
			list.add("22");
			list.add("23");
			list.add("28");
			list.add("29");
			list.add("30");
			list.add("31");
			list.add("32");
			list.add("33");
			list.add("34");
			list.add("35");
			list.add("36");
			list.add("37");
			list.add("38");
			list.add("39");
			list.add("221");

			List<Org> ls_tosx = orgService.findChildByListType(user.getRootorgid_link(), entity.id, list);

			response.data = ls_tosx;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// lấy danh sách tổ(đơn vị con: nếu có) theo user quản lý
	@RequestMapping(value = "/loadOrgByParent", method = RequestMethod.POST)
	public ResponseEntity<?> loadOrgByParent(@RequestBody GetOrgById_request entity, HttpServletRequest request) {// @RequestParam("type")
		OrgResponse response = new OrgResponse();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Long> list_org_id = new ArrayList<Long>();
			List<GpayUserOrg> list_userorg = userOrgService.getall_byuser_andtype(user.getId(),
					OrgType.ORG_TYPE_FACTORY);

			List<String> list = new ArrayList<String>();
			list.add("3");
			list.add("8");
			list.add("9");
			list.add("14");
			list.add("17");
			list.add("4");
			list.add("19");
			list.add("20");
			list.add("21");
			list.add("22");
			list.add("23");
			list.add("28");
			list.add("29");
			list.add("30");
			list.add("31");
			list.add("32");
			list.add("33");
			list.add("34");
			list.add("35");
			list.add("36");
			list.add("37");
			list.add("38");
			list.add("39");

			for (GpayUserOrg userorg : list_userorg) {
				list_org_id.add(userorg.getOrgid_link());
			}
			if (!list_org_id.contains(user.getOrgid_link())) {
				list_org_id.add(user.getOrgid_link());
			}

			// lấy tổ cụ thể trong đơn vị - theo tổ mà tài khoản đấy quản lý (nếu có)
			if (user.getOrgid_link() != 1) {
				// nếu user quản lý nhiều đơn vị
				if (list_org_id.size() > 1) {
					List<Org> ls_tosx = orgService.findChildByListType(user.getRootorgid_link(), entity.id, list);
					response.data = ls_tosx;
					response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
					return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
				} else {
					// nếu user có 1 đơn vị con và chỉ quản lý 1 đơn vị
					if (user.getOrg_grant_id_link() != null) {
						response.data = orgService.getOrgById(user.getOrg_grant_id_link());
						;
						response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
						response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
						return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);

					}
				}
			}

			List<Org> ls_tosx = orgService.findChildByListType(user.getRootorgid_link(), entity.id, list);

			response.data = ls_tosx;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getby_listtype", method = RequestMethod.POST)
	public ResponseEntity<?> getByListType(@RequestBody getorg_by_type_request entity, HttpServletRequest request) {// @RequestParam("type")
		getorg_by_type_response response = new getorg_by_type_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<String> list = new ArrayList<String>();
			String[] arr_id = entity.list_type_id.split(",");
			for (String string : arr_id) {
				list.add(string);
			}
			List<Org> ls_tosx;
			// nếu có orgtype = 25là tỉnh thành - để lấy dannh sách tỉnh thành-> không check
			// quyền user
			if (list.size() == 1 && (list.get(0).equals("25"))) {
				ls_tosx = orgService.findChildByListType(user.getRootorgid_link(), 1, list);
			} else {
				ls_tosx = orgService.findChildByListType(user.getRootorgid_link(), user.getOrgid_link(), list);
			}

			response.data = ls_tosx;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getorg_by_type_response>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getOrgByType", method = RequestMethod.POST)
	public ResponseEntity<?> GetOrgByType(@RequestBody OrgByTypeRequest entity, HttpServletRequest request) {// @RequestParam("type")
		OrgResponse response = new OrgResponse();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			response.data = orgService.findOrgByType(user.getRootorgid_link(), user.getOrgid_link(), entity.type);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getToSXByPcontractPO", method = RequestMethod.POST)
	public ResponseEntity<?> getToSXByPcontractPO(@RequestBody get_orgreq_request entity, HttpServletRequest request) {// @RequestParam("type")
		OrgResponse response = new OrgResponse();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			Long pcontract_poid_link =  entity.pcontract_poid_link;
			List<Long> orgId_list = porderGrantService.getToSXIdByPcontractPO(pcontract_poid_link);
			System.out.println(orgId_list.size());
			System.out.println(orgId_list);
			
			if(orgId_list.size() > 0) {
				List<Org> org_list = orgService.getByListId(orgId_list);
				System.out.println(org_list.size());
				if(org_list.size() > 0) {
					response.data = org_list;
				}
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getlistdonvi", method = RequestMethod.POST)
	public ResponseEntity<GetOrgByUser_response> GetOrgByUser(HttpServletRequest request) {// @RequestParam("type")
		GetOrgByUser_response response = new GetOrgByUser_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Org> list = new ArrayList<Org>();

			if (user.getOrgid_link().equals(user.getRootorgid_link())) {
				list = orgService.findOrgByType(user.getRootorgid_link(), 0, OrgType.ORG_TYPE_FACTORY);
			} else {
				List<GpayUserOrg> list_userorg = userOrgService.getall_byuser_andtype(user.getId(),
						OrgType.ORG_TYPE_FACTORY);
				List<Long> list_org = new ArrayList<Long>();
				for (GpayUserOrg gpayUserOrg : list_userorg) {
					list_org.add(gpayUserOrg.getOrgid_link());
				}
				if (!list_org.contains(user.getOrgid_link())) {
					list_org.add(user.getOrgid_link());
				}

				for (Long id : list_org) {
					list.add(orgService.findOne(id));
				}
			}

			response.data = list;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<GetOrgByUser_response>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
			return new ResponseEntity<GetOrgByUser_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> CreateOrgByType(@RequestBody Org_create_Request entity,
			HttpServletRequest request) {// @RequestParam("type")
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Org org = entity.data;

//			//thêm tỉnh. huyện , xã
			if (org.getOrgtypeid_link() == 25 || org.getOrgtypeid_link() == 26 || org.getOrgtypeid_link() == 27) {

				org.setOrgrootid_link(user.getRootorgid_link());
				orgService.save(org);

				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
			}

			if (org.getId() == 0 || org.getId() == null) {
				org.setOrgrootid_link(user.getRootorgid_link());
			} else {
				Org org_old = orgService.findOne(org.getId());
				org.setOrgrootid_link(org_old.getOrgrootid_link());
			}

			orgService.save(org);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/get_orgreq", method = RequestMethod.POST)
	public ResponseEntity<get_orgreq_response> getOrg_Req(@RequestBody get_orgreq_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		get_orgreq_response response = new get_orgreq_response();
		try {
//			List<POrder_Req> list_req = reqService.getByPO_Offer(entity.pcontract_poid_link);
//			List<Long> list_org = new ArrayList<Long>();
//			for(POrder_Req req : list_req) {
//				if(!list_org.contains(req.getGranttoorgid_link())) {
//					list_org.add(req.getGranttoorgid_link());
//				}
//			}
//			
//			for (long id : list_org) {
//				response.data.add(orgService.findOne(id));
//			}

			response.data = orgService.getOrgReq_by_po(entity.pcontract_poid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<get_orgreq_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
			return new ResponseEntity<get_orgreq_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/get_orgnotreq", method = RequestMethod.POST)
	public ResponseEntity<get_orgreq_response> getOrg_NotReq(@RequestBody get_orgreq_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		get_orgreq_response response = new get_orgreq_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgid_link = user.getOrgid_link();

			List<POrder_Req> list_req = reqService.getByPO(entity.pcontract_poid_link);
			List<Org> list_org = new ArrayList<Org>();
			List<String> list_typeid = new ArrayList<String>();
			list_typeid.add("13");

			list_org = orgService.getorgChildrenbyOrg(orgid_link, list_typeid);

			// Lay nhung don vi ma user dang quan ly
			List<GpayUserOrg> list_user_org = userorgService.getall_byuser(user.getId());
			for (GpayUserOrg gpayUserOrg : list_user_org) {
				list_org.add(gpayUserOrg.getOrg());
			}

			for (POrder_Req req : list_req) {
				list_org.removeIf(c -> c.getId().equals(req.getGranttoorgid_link()));
			}

			response.data = list_org;

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<get_orgreq_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
			return new ResponseEntity<get_orgreq_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> DeleteOrg(@RequestBody Org_delete_request entity, HttpServletRequest request) {// @RequestParam("type")
		ResponseBase response = new ResponseBase();
		try {
			Org org = orgService.findOne(entity.id);
			org.setStatus(-1);

			orgService.save(org);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/getorgdest", method = RequestMethod.POST)
	public ResponseEntity<?> GetOrgDest(@RequestBody GetOrgdestRequest entity, HttpServletRequest request) {//
		OrgResponse response = new OrgResponse();
		try {
			String menuid = entity.menuid;
			GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
			int type = user.getOrg_type();
			if ("lsstockinproduct".equals(menuid)) {
				List<Org> list1 = orgService.findOrgByType(user.getRootorgid_link(), user.getOrgId(), 7);
				List<Org> list2 = orgService.findOrgByType(user.getRootorgid_link(), user.getOrgId(), 9);
				list1.addAll(list2);
				response.data = list1;
			}
			if ("lsxuatdieuchuyen".equals(menuid)) {
				switch (type) {
				case 3:
					List<Org> list1 = orgService.findOrgByType(user.getRootorgid_link(), user.getOrgId(), 3);
					// List<Org> list2
					// =orgService.findOrgByType(user.getAppuser().getRootorgid_link(),7);
					// list1.addAll(list2);
					response.data = list1;
					break;
				case 4:
					List<Org> list3 = orgService.findOrgByType(user.getRootorgid_link(), user.getOrgId(), 4);
					List<Org> list4 = orgService.findOrgByType(user.getRootorgid_link(), user.getOrgId(), 8);
					list3.addAll(list4);
					response.data = list3;
					break;
				case 8:
					List<Org> list5 = orgService.findOrgByType(user.getRootorgid_link(), user.getOrgId(), 4);
					List<Org> list6 = orgService.findOrgByType(user.getRootorgid_link(), user.getOrgId(), 8);
					list5.addAll(list6);
					response.data = list5;
					break;
				default:
					response.data = orgService.findOrgByType(user.getRootorgid_link(), user.getOrgId(), 1);
					break;
				}
			}

			if ("lsnhapdieuchuyen".equals(menuid)) {
				switch (type) {
				case 3:
					List<Org> list1 = orgService.findOrgByType(user.getRootorgid_link(), user.getOrgId(), 3);
					// List<Org> list2
					// =orgService.findOrgByType(user.getAppuser().getRootorgid_link(),7);
					// list1.addAll(list2);
					response.data = list1;
					break;
				case 4:
					List<Org> list3 = orgService.findOrgByType(user.getRootorgid_link(), user.getOrgId(), 4);
					List<Org> list4 = orgService.findOrgByType(user.getRootorgid_link(), user.getOrgId(), 8);
					list3.addAll(list4);
					response.data = list3;
					break;
				case 8:
					List<Org> list5 = orgService.findOrgByType(user.getRootorgid_link(), user.getOrgId(), 4);
					List<Org> list6 = orgService.findOrgByType(user.getRootorgid_link(), user.getOrgId(), 8);
					list5.addAll(list6);
					response.data = list5;
					break;
				default:
					response.data = orgService.findOrgByType(user.getRootorgid_link(), user.getOrgId(), 1);
				}
			}
			if ("lsinvcheck".equals(menuid)) {
				switch (type) {
				case 1:
					List<Org> list1 = orgService.findOrgByType(user.getRootorgid_link(), user.getOrgId(), 3);
					List<Org> list3 = orgService.findOrgByType(user.getRootorgid_link(), user.getOrgId(), 4);
					List<Org> list4 = orgService.findOrgByType(user.getRootorgid_link(), user.getOrgId(), 8);
					list1.addAll(list3);
					list1.addAll(list4);
					response.data = list1;
					break;
				default:
					response.data = orgService.findStoreByType(user.getRootorgid_link(), user.getOrgId(),
							user.getOrg_type());
				}
			}
			if ("lssalebill".equals(menuid)) {
				switch (type) {
				case 1:
					List<Org> list1 = orgService.findStoreByType(user.getRootorgid_link(), null, 4);
					response.data = list1;
					break;
				default:
					response.data = orgService.findStoreByType(user.getRootorgid_link(), user.getOrgId(), 4);
				}
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getOrgInvCheckByType", method = RequestMethod.POST)
	public ResponseEntity<?> GetOrgInvCheckByType(@RequestBody OrgByTypeRequest entity, HttpServletRequest request) {// @RequestParam("type")
		OrgResponse response = new OrgResponse();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication();
			if (user.getRootorgid_link() == user.getOrgid_link()) {
				// là root
				response.data = orgService.findRootOrgInvCheckByType(user.getOrgid_link());
			} else {
				response.data = orgService.findOrgInvCheckByType(user.getOrgid_link());
			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getAllOrgByRoot", method = RequestMethod.POST)
	public ResponseEntity<?> GetAllOrgByRoot(HttpServletRequest request) {// @RequestParam("type")
		OrgResponse response = new OrgResponse();
		try {
			GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
			response.data = orgService.findOrgAllByRoot(user.getRootorgid_link());
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getOrgByTypeId", method = RequestMethod.POST)
	public ResponseEntity<OrgResponse> GetOrgByTypeid(@RequestBody Org_getbyType_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		OrgResponse response = new OrgResponse();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			// List<Long> list_org_id = new ArrayList<Long>();
			// List<Org> lst_org = new ArrayList<Org>();
			// List<GpayUserOrg> list_userorg =
			// userOrgService.getall_byuser_andtype(user.getId(),OrgType.ORG_TYPE_FACTORY);

//			for (GpayUserOrg userorg : list_userorg) {
//				list_org_id.add(userorg.getOrgid_link());
//			}
//			// nếu user có org_id khác 1(1 là công ty DHA) : tức là thuộc 1 đơn vị cụ thể
//			// thì chỉ lấy đơn vị đấy, không được lấy đơn vị khác
//			if (user.getOrgid_link() != 1) {
//				for(int i = 0 ; i<list_org_id.size();i++) {
//				List<Org>	lstlst_org= orgService.getOrgById(list_org_id.get(i));
//				lst_org.add(lstlst_org.get(0));
//				}
//				
//				response.data =lst_org;
//			} else {
//				response.data = orgService.findAllorgByTypeId(entity.orgtypeid_link, (long) user.getRootorgid_link());
//				if (entity.isAll) {
//					Org org = new Org();
//					org.setId((long) 0);
//					org.setName("Tất cả");
//					response.data.add(0, org);
//				}
//
//			}

			response.data = orgService.findAllorgByTypeId(entity.orgtypeid_link, (long) user.getRootorgid_link());
			if (entity.isAll) {
				Org org = new Org();
				org.setId((long) 0);
				org.setName("Tất cả");
				response.data.add(0, org);
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
			return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
		}
	}

	// lây danh sách org theo user quản lý
	@RequestMapping(value = "/loadOrg_ByOrgType", method = RequestMethod.POST)
	public ResponseEntity<OrgResponse> loadOrg_ByOrgType(@RequestBody Org_getbyType_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		OrgResponse response = new OrgResponse();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			List<Long> list_org_id = new ArrayList<Long>();
			List<Org> lst_org = new ArrayList<Org>();
			List<GpayUserOrg> list_userorg = userOrgService.getall_byuser_andtype(user.getId(),
					OrgType.ORG_TYPE_FACTORY);

			for (GpayUserOrg userorg : list_userorg) {
				list_org_id.add(userorg.getOrgid_link());
			}
			if (!list_org_id.contains(user.getOrgid_link())) {
				list_org_id.add(user.getOrgid_link());
			}
			// nếu user có org_id khác 1(1 là công ty DHA) : tức là thuộc 1 đơn vị cụ thể
			// thì chỉ lấy đơn vị đấy, không được lấy đơn vị khác
			if (user.getOrgid_link() != 1) {
				for (int i = 0; i < list_org_id.size(); i++) {
					List<Org> lstlst_org = orgService.getOrgById(list_org_id.get(i));
					lst_org.add(lstlst_org.get(0));
				}

				response.data = lst_org;
			} else {
				response.data = orgService.findAllorgByTypeId(entity.orgtypeid_link, (long) user.getRootorgid_link());
				if (entity.isAll) {
					Org org = new Org();
					org.setId((long) 0);
					org.setName("Tất cả");
					response.data.add(0, org);
				}

			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
			return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/getOrgById", method = RequestMethod.POST)
	public ResponseEntity<GetOrgById_response> GetOrgByid(@RequestBody GetOrgById_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		GetOrgById_response response = new GetOrgById_response();
		try {
			response.data = orgService.findOne(entity.id);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<GetOrgById_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
			return new ResponseEntity<GetOrgById_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/getallchildrenbyorg", method = RequestMethod.POST)
	public ResponseEntity<?> GetAllOrgByOrg(HttpServletRequest request,
			@RequestBody Org_getbyroot_andtypeid_request entity) {// @RequestParam("type")
		OrgResponse response = new OrgResponse();
		try {
			GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
			Long orgrootid_link = user.getRootorgid_link();
			String[] listtype = entity.listid.split(",");
			List<String> list = new ArrayList<String>();
			for (String string : listtype) {
				list.add(string);
			}

			// Lay org la con
			List<Org> listreturn = new ArrayList<Org>();

			List<Org> listorg = orgService.getorgChildrenbyOrg(user.getOrgId(), list);

			listreturn.addAll(listorg);

			// Lay org chau

			for (Org org : listorg) {
				List<Org> list_chil = orgService.getorgChildrenbyOrg(org.getId(), list);
				listreturn.addAll(list_chil);
			}

			if (user.getOrgId() == orgrootid_link) {
				// Neu nguoi dung thuoc tong cong ty, kiem tra xem co load goc hay ko
				if (entity.isincludemyself) {
					Org org = orgService.findOne(user.getOrgId());
					listreturn.add(org);
				}
			} else {
				// Neu user ko phai thuoc tong cong ty --> lay thong tin don vi cua nguoi dung
				Org org = orgService.findOne(user.getOrgId());
				listreturn.add(org);
			}
//			GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
//			Long orgrootid_link = user.getRootorgid_link();
//			String[] listtype = entity.listid.split(",");
//			List<String> list = new ArrayList<String>();
//			for (String string : listtype) {
//				list.add(string);
//			}
//
//			// Lay org la con
//			List<Org> listreturn = new ArrayList<Org>();
//
//			List<Org> listorg = orgService.getorgChildrenbyOrg(user.getOrgId(), list);
//
//			listreturn.addAll(listorg);
//
//			// Lay org chau
//
//			for (Org org : listorg) {
//				List<Org> list_chil = orgService.getorgChildrenbyOrg(org.getId(), list);
//				listreturn.addAll(list_chil);
//			}
//
//			if (user.getOrgId() == orgrootid_link) {
//				// Neu nguoi dung thuoc tong cong ty, kiem tra xem co load goc hay ko
//				if (entity.isincludemyself) {
//					Org org = orgService.findOne(user.getOrgId());
//					listreturn.add(org);
//				}
//			} else {
//				// Neu user ko phai thuoc tong cong ty --> lay thong tin don vi cua nguoi dung
//				// Lấy thông tin đơn vị của người dùng quản lý
//				List<Long> list_org_id = new ArrayList<Long>();
//				List<GpayUserOrg> list_userorg = userOrgService.getall_byuser_andtype(user.getUserId(),
//						OrgType.ORG_TYPE_FACTORY);
//				// nếu user quản lý nhiều hơn 1 đơn vị
//				if (list_userorg.size() > 1) {
//					// danh sasch id don vi user quản lý
//					for (GpayUserOrg userorg : list_userorg) {
//						list_org_id.add(userorg.getOrgid_link());
//					}
//					for (int i = 0; i < list_org_id.size(); i++) {
//						Org lstlst_org = orgService.findOne(list_org_id.get(i));
//						listreturn.add(lstlst_org);
//					}
//
//				} else {
//					Org org = orgService.findOne(user.getOrgId());
//					listreturn.add(org);
//				}
//
//			}

			response.data = listreturn;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getallbyroot_notuser", method = RequestMethod.POST)
	public ResponseEntity<?> GetAllOrgByRoot_notUser(HttpServletRequest request,
			@RequestBody Org_getbyroot_andtypeid_request entity) {// @RequestParam("type")
		OrgResponse response = new OrgResponse();
		try {
			GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
			String[] listtype = entity.listid.split(",");
			List<String> list = new ArrayList<String>();
			for (String string : listtype) {
				list.add(string);
			}

			response.data = orgService.findOrgAllByRoot(user.getRootorgid_link(), user.getOrgId(), list, false);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/findAll", method = RequestMethod.POST)
	public ResponseEntity<?> findAll(HttpServletRequest request) {// @RequestParam("type")
		OrgResponse response = new OrgResponse();
		try {
			response.data = orgService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/findOrgByOrgTypeString", method = RequestMethod.POST)
	public ResponseEntity<?> findOrgByOrgTypeString(@RequestBody Org_getByOrgTypeString_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		OrgResponse response = new OrgResponse();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			Long orgid_link = user.getOrgid_link();
//			Org org = orgService.findOne(orgid_link);

			String[] listtype = entity.orgtypestring.split(",");
			List<String> list = new ArrayList<String>();
			for (String string : listtype) {
				list.add(string.trim());
			}

			response.data = orgService.findOrgByOrgTypeString(list, null);
//			if(org.getOrgtypeid_link() == 1) { // trụ sở
//				response.data = orgService.findOrgByOrgTypeString(list, null);
//			}
//			if(org.getOrgtypeid_link() == 13){ // xưởng
//				response.data = orgService.findOrgByOrgTypeString(list, org.getId());
//			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getOrgByPorderIdLink", method = RequestMethod.POST)
	public ResponseEntity<?> getOrgByPorderIdLink(@RequestBody GetOrgById_request entity, HttpServletRequest request) {// @RequestParam("type")
		OrgResponse response = new OrgResponse();
		try {
			response.data = new ArrayList<>();
			List<Org> temp = orgService.getOrgByPorderIdLink(entity.id);
			for (Org org : temp) {
				if (response.data.contains(org))
					continue;
				response.data.add(org);
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getOrgForContractBuyerBuyerList", method = RequestMethod.POST)
	public ResponseEntity<?> getOrgForContractBuyerBuyerList(
			@RequestBody getOrgForContractBuyerBuyerList_request entity, HttpServletRequest request) {// @RequestParam("type")
		OrgResponse response = new OrgResponse();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Long> buyerIds = entity.buyerIds;
//			System.out.println(buyerIds.size());
			if (buyerIds.size() > 0) {
				response.data = orgService.getOrgForContractBuyerBuyerList(buyerIds);
			} else {
				response.data = orgService.findAllorgByTypeId(12, user.getRootorgid_link());
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getOrgByTypeKho", method = RequestMethod.POST)
	public ResponseEntity<?> getOrgByTypeKho(@RequestBody getOrgForContractBuyerBuyerList_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		OrgResponse response = new OrgResponse();
		try {
			response.data = orgService.findOrgByTypeKho();

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getOrgForVendorStoreByBuyerId", method = RequestMethod.POST)
	public ResponseEntity<?> getOrgForVendorStoreByBuyerId(@RequestBody GetOrgById_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		OrgResponse response = new OrgResponse();
		try {
			Long buyerid_link = entity.id;

			response.data = orgService.getOrgForVendorStoreByBuyerId(buyerid_link);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getOrgByTypeAndUser", method = RequestMethod.POST)
	public ResponseEntity<?> getOrgByTypeAndUser(@RequestBody Org_getbyType_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		OrgResponse response = new OrgResponse();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Integer> orgtypeList = entity.orgtypeList;
			//
			Long orgid_link = user.getOrgid_link();
			Org org = orgService.findOne(orgid_link);

			if (org.getOrgtypeid_link().equals(OrgType.ORG_TYPE_HEADQUARTER)) {
				// nếu là trụ sở chính -> lấy hết
				response.data = orgService.findOrgByOrgType(orgtypeList);
			} else if (org.getOrgtypeid_link().equals(OrgType.ORG_TYPE_FACTORY)) {
				Long parentOrgId = org.getId();
				response.data = orgService.findOrgByOrgType(orgtypeList, parentOrgId);
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getOrgInReqByPContractAndProduct", method = RequestMethod.POST)
	public ResponseEntity<?> getOrgBInReqByPContractAndProduct(@RequestBody GetOrgInReqByPcontractAndProduct entity,
			HttpServletRequest request) {// @RequestParam("type")
		OrgResponse response = new OrgResponse();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Integer> orgtypeList = entity.orgtypeList;
			//
			Long orgid_link = user.getOrgid_link();
			Org org = orgService.findOne(orgid_link);

			if (org.getOrgtypeid_link().equals(OrgType.ORG_TYPE_HEADQUARTER)) {
				// nếu là trụ sở chính -> lấy hết
				response.data = orgService.findOrgByOrgType(orgtypeList);
			} else if (org.getOrgtypeid_link().equals(OrgType.ORG_TYPE_FACTORY)) {
				long pcontractid_link = entity.pcontractid_link;
				long productid_link = entity.productid_link;

				List<Long> list_org_req = reqService.getOrgbyPContractAndProduct(pcontractid_link, productid_link);
				if (!list_org_req.contains(org.getId())) {
					list_org_req.add(orgid_link);
				}
				response.data = new ArrayList<Org>();
				for (Long orgid : list_org_req) {
					response.data.addAll(orgService.findOrgByOrgType(orgtypeList, orgid));
				}
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getOrgByTypeBanCatAndUser", method = RequestMethod.POST)
	public ResponseEntity<?> getOrgByTypeBanCatAndUser(@RequestBody Org_getbyType_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		OrgResponse response = new OrgResponse();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			List<Integer> orgtypeList = entity.orgtypeList;
			//
			Long orgid_link = user.getOrgid_link();
			Long org_grant_id_link = user.getOrg_grant_id_link();
			Org org = orgService.findOne(orgid_link);

			if (org.getOrgtypeid_link().equals(OrgType.ORG_TYPE_HEADQUARTER)) {
				// nếu là trụ sở chính -> lấy hết
				response.data = orgService.findOrgByTypeBanCat(null);
			} else if (org.getOrgtypeid_link().equals(OrgType.ORG_TYPE_FACTORY)) {
				if (org_grant_id_link == null) {
					response.data = orgService.findOrgByTypeBanCat(null);
				} else {
					Org orgGrant = orgService.findOne(org_grant_id_link);
					if (orgGrant.getOrgtypeid_link().equals(OrgType.ORG_TYPE_CAT)) {
						response.data = orgService.findOrgByTypeBanCat(orgGrant.getId());
					} else {
						response.data = orgService.findOrgByTypeBanCat(null);
					}
				}
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getOrgFromForStockoutMaterial", method = RequestMethod.POST)
	public ResponseEntity<get_orgreq_response> getOrgFromForStockoutMaterial(@RequestBody get_orgreq_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		get_orgreq_response response = new get_orgreq_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgid_link = user.getOrgid_link();
			
			if(orgid_link.equals((long)1)) {
				response.data = orgService.findAllorgByTypeId(3, 1);
			}else {
				List<Org> list_org = new ArrayList<Org>();
				List<String> list_typeid = new ArrayList<String>();
				list_typeid.add("3");

				list_org = orgService.getorgChildrenbyOrg(orgid_link, list_typeid);

				// Lay nhung don vi ma user dang quan ly
				List<GpayUserOrg> list_user_org = userorgService.getall_byuser(user.getId());
				for (GpayUserOrg gpayUserOrg : list_user_org) {
					list_org.add(gpayUserOrg.getOrg());
				}

				response.data = list_org;
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<get_orgreq_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
			return new ResponseEntity<get_orgreq_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getOrgToForStockoutMaterial_Cut", method = RequestMethod.POST)
	public ResponseEntity<get_orgreq_response> getOrgToForStockoutMaterial_Cut(@RequestBody get_orgreq_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		get_orgreq_response response = new get_orgreq_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgid_link = user.getOrgid_link();
			
			if(orgid_link.equals((long)1)) {
				response.data = orgService.findAllorgByTypeId(17, 1);
			}else {
				List<Org> result = new ArrayList<Org>();
				List<Org> list_org = new ArrayList<Org>();
				List<String> list_typeid = new ArrayList<String>();
				list_typeid.add("3");

				list_org = orgService.getorgChildrenbyOrg(orgid_link, list_typeid);

				// Lay nhung don vi ma user dang quan ly
				List<GpayUserOrg> list_user_org = userorgService.getall_byuser(user.getId());
				for (GpayUserOrg gpayUserOrg : list_user_org) {;
					list_org.add(gpayUserOrg.getOrg());
				}

				for(Org orgKho : list_org) {
					Long parentid_link = orgKho.getParentid_link();
					List<Integer> listType = new ArrayList<Integer>();
					listType.add(17);
					List<Org> listToCat = orgService.findOrgByOrgType(listType, parentid_link);
					result.addAll(listToCat);
				}
				response.data = result;
				
				System.out.println("here2");
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<get_orgreq_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
			return new ResponseEntity<get_orgreq_response>(response, HttpStatus.OK);
		}
	}
}
