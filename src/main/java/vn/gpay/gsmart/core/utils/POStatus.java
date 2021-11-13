package vn.gpay.gsmart.core.utils;

public class POStatus {
	public static int PO_STATUS_CANCEL  		= -3; //Hủy đơn
	public static int PO_STATUS_PROBLEM  		= -2; //Chưa chốt, số liệu sizeset và ycsx chưa cân
	public static int PO_STATUS_UNCONFIRM  		= -1; //Chưa chốt, số liệu sizeset và ycsx đã cân
	public static int PO_STATUS_CONFIRMED       = 0; //Đã chốt, chưa giao hang
	public static int PO_STATUS_PORDER_ALL		= 1; //Đã lập đủ lệnh sx
	public static int PO_STATUS_STOCKOUT    	= 2; //Đã lập lệnh xuất kho	
    public static int PO_STATUS_DELIVERED    	= 3; //Đã giao hang
    public static int PO_STATUS_PAID    		= 4; //Đã thanh toan
}
