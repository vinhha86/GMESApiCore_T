package vn.gpay.gsmart.core.stock;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IStockspaceService extends Operations<Stockspace>{
	public List<Stockspace> findStockspaceByEpc(long orgid, String epc);

}
