package vn.gpay.gsmart.core.invcheck;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IInvcheckEpcService extends Operations<InvcheckEpc>{

	public List<InvcheckEpc> findEpcBySkuId(long invcheckid_link, long skuid_link);
}
