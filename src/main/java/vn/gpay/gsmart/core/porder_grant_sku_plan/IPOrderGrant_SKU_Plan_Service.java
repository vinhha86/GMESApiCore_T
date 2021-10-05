package vn.gpay.gsmart.core.porder_grant_sku_plan;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPOrderGrant_SKU_Plan_Service extends Operations<POrderGrant_SKU_Plan>{
	List<POrderGrant_SKU_Plan> getByPorderGrantSku_Date(Long porder_grant_skuid_link, Date dateFrom, Date dateTo);
}
