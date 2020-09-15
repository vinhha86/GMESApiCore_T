package vn.gpay.gsmart.core.utils;

public class POStatus {
	public static int PO_STATUS_PROBLEM  		= -2; //Chưa chốt, số liệu sizeset và ycsx chưa cân
	public static int PO_STATUS_UNCONFIRM  		= -1; //Chưa chốt, số liệu sizeset và ycsx đã cân
	public static int PO_STATUS_CONFIRMED       = 0; //Đã chốt, chưa giao hang
	public static int PO_STATUS_PORDER_ALL    	= 1; //Đã phân đủ lệnh	
    public static int PO_STATUS_DELIVERED    	= 2; //Đã giao hang
    public static int PO_STATUS_PAID    		= 3; //Đã thanh toan
}
