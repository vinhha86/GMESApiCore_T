package vn.gpay.gsmart.core.utils;

public class POrderReqStatus {
	public static int STATUS_FREE  			= 0; //Chưa ướm thử
	public static int STATUS_TESTED     	= 1; //Đã ướm thử lệnh vào chuyền
	public static int STATUS_POCONFFIRMED	= 2; //Đã chốt chào giá và tạo PO
	public static int STATUS_PORDERED		= 3; //Đã tạo lệnh sx
}