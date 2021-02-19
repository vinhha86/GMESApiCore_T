package vn.gpay.gsmart.core.utils;
public class WarehouseEventType {
	public static int EVENT_TYPE_ENCODE		= 1;//Chíp được mã hóa
    public static int EVENT_TYPE_STOCKIN	= 2;//Chíp nhập kho
    public static int EVENT_TYPE_STOCKOUT	= 3;//Chíp xuất kho
    public static int EVENT_TYPE_SOLDOUT	= 4;//Chíp được bán tại quầy
    public static int EVENT_TYPE_RETURN		= 5;//Chíp được người mua trả lại (đổi trả)
}
