package vn.gpay.gsmart.core.api.salebill;

import java.util.List;

import vn.gpay.gsmart.core.base.RequestBase;
import vn.gpay.gsmart.core.salebill.SaleBill;

public class SaleBillCreateRequest extends RequestBase{
	public List<SaleBill> data;
}
