package vn.gpay.gsmart.core.org;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface OrgRepository extends JpaRepository<Org, Long>,JpaSpecificationExecutor<Org>{

	@Query(value = "select c from Org c where c.orgrootid_link=:orgrootid_link"
			+ " and orgtypeid_link =:type "
			+ "and id <> :orgid_link ")
	public List<Org> findOrgByType(@Param ("orgrootid_link")final long orgrootid_link,
			@Param ("orgid_link")final long orgid_link
			,@Param ("type")final long type);
	
	@Query(value = "select c from Org c where c.id =:orgid_link and c.orgtypeid_link in(3,4,8)")
	public List<Org> findOrgInvCheckByType(@Param ("orgid_link")final long orgid_link);
	
	@Query(value = "select c from Org c where c.orgtypeid_link in(3,4,8) and c.orgrootid_link =:orgid_link")
	public List<Org> findRootOrgInvCheckByType(@Param ("orgid_link")final long orgid_link);
	
	@Query(value = "select c from Org c where c.orgrootid_link =:orgid_link")
	public List<Org> findOrgAllByRoot(@Param ("orgid_link")final long orgid_link);
	
	@Query(value = "select c from Org c where c.orgrootid_link =:orgrootid "
			+ "and orgtypeid_link = :orgtypeid_link and c.status = 1 "
			+ "order by c.code asc")
	public List<Org> findAllOrgbyType(@Param ("orgrootid")final long orgrootid,
			@Param ("orgtypeid_link")final Integer orgtypeid_link);
	
	@Query(value = "select c from Org c where c.orgtypeid_link in(1,3,8,9,13,14,17,19,21,28,29,30,31,32,33,34,35,36,37,38,39) order by c.id asc")
//	@Query(value = "select c from Org c where c.orgtypeid_link in(1,8,9,13,14,21) order by c.orgtypeid_link, c.is_manufacturer, c.code asc")
	public List<Org> findOrgByTypeForMenuOrg();
	
	@Query(value = "select c from Org c where c.parentid_link =:orgid_link")
	public List<Org> findOrgAllByParent(@Param ("orgid_link")final long orgid_link);
	
	@Query(value = "select c from Org c where c.orgtypeid_link in(:orgtypestring) and c.status = 1 order by c.name asc")
	public List<Org> findOrgByOrgTypeString(@Param ("orgtypestring")final String orgtypestring);
	
	@Query(value = "select c from Org c "
			+ "inner join POrderGrant a on c.id = a.granttoorgid_link "
			+ "inner join POrder b on a.porderid_link = b.id "
			+ "where a.porderid_link = :porderid_link order by c.id")
	public List<Org> getOrgByPorderIdLink(@Param ("porderid_link")final Long porderid_link);
	
	@Query(value = "select c from Org c "
			+ "where c.orgrootid_link = :orgrootid_link and c.code = :code")
	public List<Org> getbycode(
			@Param ("orgrootid_link")final Long orgrootid_link,
			@Param ("code")final String code);
	
	@Query(value = "select c from Org c "
			+ "where c.orgtypeid_link = :orgtypeid_link "
			+ "and trim(lower(replace(c.code,' ',''))) = trim(lower(replace(:code, ' ',''))) "
//			+ "and c.name = :name "
			)
	public List<Org> getByCodeAndType(
			@Param ("code")final String code,
			@Param ("orgtypeid_link")final Integer orgtypeid_link
			);
	
	@Query(value = "select c from Org c "
			+ "where c.orgtypeid_link = :orgtypeid_link " 
			+ "and trim(lower(replace(c.name,' ',''))) = trim(lower(replace(:name, ' ',''))) "
//			+ "and c.name = :name "
			)
	public List<Org> getByNameAndType(
			@Param ("name")final String name,
			@Param ("orgtypeid_link")final Integer orgtypeid_link
			);
	
	@Query(value = "select c from Org c where c.orgtypeid_link in(1,13,14,17) order by c.orgtypeid_link, c.name asc")
	public List<Org> findOrgByTypeForInvCheckDeviceMenuOrg();
	
	@Query(value = "select c from Org c "
			+ "inner join POrder_Req a on c.id = a.granttoorgid_link "
			+ "inner join PContract_PO b on a.pcontract_poid_link = b.id "
			+ "where b.parentpoid_link = :pcontract_poid_link "
			+ "group by c "
			+ "order by sum(a.totalorder) desc, c.code asc")
	public List<Org> getOrgReqbyPO(
			@Param ("pcontract_poid_link")final Long pcontract_poid_link);
	
	@Query(value = "select c from Org c where c.orgtypeid_link =12 and c.id not in :buyerIds and c.status = 1 order by c.code asc")
	public List<Org> getOrgForContractBuyerBuyerList(@Param ("buyerIds")final List<Long> buyerIds);
	
	@Query(value = "select c from Org c where c.id in :listid and c.status = 1 order by c.code asc")
	public List<Org> getByListId(@Param ("listid")final List<Long> listid);
	
	@Query(value = "select c from Org c "
			+ "inner join ContractBuyer a on c.id = a.vendorid_link "
			+ "inner join ContractBuyerD b on a.id = b.contractbuyerid_link "
			+ "where b.buyerid_link = :buyerid_link order by c.code asc")
	public List<Org> getOrgForVendorStoreByBuyerId(
			@Param ("buyerid_link")final Long buyerid_link
			);
	
	@Query(value = "select c from Org c where c.orgtypeid_link in(3,8,19) order by c.orgtypeid_link, c.name asc")
	public List<Org> findOrgByTypeKho();
	
	@Query(value = "select c from Org c where c.orgtypeid_link in (:orgtypestring) "
			+ "and c.status > -1 "
			+ "and :orgtypestring = :orgtypestring "
			+ "order by c.name asc "
			)
	public List<Org> findOrgByOrgType(@Param ("orgtypestring")final List<Integer> orgtypestring);
	
	@Query(value = "select c from Org c "
			+ "where c.orgtypeid_link in (:orgtypestring) "
			+ "and c.status > -1 "
			+ "and (c.parentid_link = :parentid_link or :parentid_link is null) "
			+ "and :orgtypestring = :orgtypestring "
			+ "order by c.name asc "
			)
	public List<Org> findOrgByOrgType(
			@Param ("orgtypestring")final List<Integer> orgtypestring,
			@Param ("parentid_link")final Long parentid_link
			);
	
	@Query(value = "select c from Org c "
			+ "where c.status > -1 "
			+ "and (c.parentid_link = :parentid_link or :parentid_link is null) "
			+ "and c.orgtypeid_link = 39 "
			+ "order by c.name asc "
			)
	public List<Org> findOrgByTypeBanCat(
			@Param ("parentid_link")final Long parentid_link
			);
	
	// lấy danh sách tổ theo px cho timesheetlunch_mobile
	@Query(value = " select c from Org c "
			+ " where c.orgtypeid_link in (1,3,8,9,13,14,17,19,21,28,29,30,31,32,33,34,35,36,37,38,39) " 
			+ " and c.parentid_link = :parentid_link "
			+ " and c.status > -1 "
			+ " order by c.orgtypeid_link, c.code "
			)
	public List<Org> getByParentId_for_TimeSheetLunchMobile(
			@Param ("parentid_link")final Long parentid_link
			);
	
	//lay org -huyen theo ten tinh, ten huyen,orgtypeid_link - name,orgtypeid_link,parentid_link
	@Query(value = "select c from Org c "
			+ "where c.orgtypeid_link = :orgtypeid_link " 
			+ "and trim(lower(replace(c.name,' ',''))) = trim(lower(replace(:name, ' ',''))) "
			+ "and c.parentid_link = :parentid_link "
			)
	public Org getByNameAndTypeAndParentid_link(
			@Param ("name")final String name,
			@Param ("orgtypeid_link")final Integer orgtypeid_link,
			@Param ("parentid_link")final Long parentid_link
			);
	//lay org (bo phan) theo ma va don vi quan ly - code,parentid_link
	@Query(value = "select c from Org c "
			+ "where c.parentid_link = :parentid_link "
			+ "and trim(lower(replace(c.code,' ',''))) = trim(lower(replace(:code, ' ',''))) "
			+ "and c.status > -1 " )
	public List<Org> getByCodeAndParentid_link(
			@Param ("code")final String code,
			@Param ("parentid_link")final Long parentid_link
			);
	//laasy parentid_link theo id org
	@Query(value = "select c.parentid_link from Org c "
			+ "where c.id = :id " )
	public Long getParentIdById(
			@Param ("id")final Long id);
	//lấy đơn vị theo id -(lấy đơn vị mà tài khoản quản đó quản lý)
	@Query(value = "select c from Org c "
			+ "where c.id = :id " )
	public List<Org> getOrgById(
			@Param ("id")final Long id);
	//lấy tất cả DHA,đợn vị, tổ theo đơn vị mà tài khoản quản lý
	@Query(value = "select c from Org c where c.id = :org_id  or c.id = 1 or "
			+ "( c.parentid_link = :org_id and c.orgtypeid_link in(1,3,8,9,13,14,17,19,21,28,29,30,31,32,33,34,35,36,37,38,39)) order by c.id asc")
	public List<Org> findOrgByType_Id_ParentIdForMenuOrg(@Param ("org_id")final Long org_id);
	//lấy DHA,đơn vị, tổ cụ thể theo tổ mà tài khoản quản lý
	@Query(value = "select c from Org c where c.id in( :org_id, 1, :Org_grant_id_link) "
			+ " order by c.id asc")
	public List<Org> findOrgByType_Id_ParentId_Org_grant_IdForMenuOrg(
			@Param ("org_id")final Long org_id,
			@Param ("Org_grant_id_link")final Long Org_grant_id_link);
}
