package vn.gpay.gsmart.core.utils;

public class POStatus {
	public static int PO_STATUS_UNCONFIRM  		= -1; //Chưa chốt
	public static int PO_STATUS_CONFIRMED       = 0; //Đã chốt, chưa giao hang
    public static int PO_STATUS_DELIVERED    	= 1; //Đã giao hang
    public static int PO_STATUS_PAID    		= 2; //Đã thanh toan
}
