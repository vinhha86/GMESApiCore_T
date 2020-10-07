package vn.gpay.gsmart.core.tagencode;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IWareHouse_Encode_D_Service extends Operations<WareHouse_Encode_D> {
	public WareHouse_Encode_D getwarehouse_encode_d_By_skucode(String skucode, long warehouse_encodeid_link);

	public WareHouse_Encode_D getwarehouse_encode_d_By_skuid(Long skuid, long warehouse_encodeid_link);

	public List<WareHouse_Encode_D> encode_getbyencodeid(Long warehouse_encodeid_link);
}
