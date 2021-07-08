package vn.gpay.gsmart.core.utils;

public class StockoutTypes {
	//Xuat Nguyen lieu - vai
    public static int STOCKOUT_TYPE_NL_CUTHOUSE         = 1;//Xuất vai cho tổ cắt
    public static int STOCKOUT_TYPE_NL_MOVE         	= 2;//Xuất điều chuyển nội bộ (đơn khác)
    public static int STOCKOUT_TYPE_NL_OUTSOURCE        = 3;//Xuất Gia công
    public static int STOCKOUT_TYPE_NL_PROVIDER         = 4;//Xuất trả nhà cung cấp
    public static int STOCKOUT_TYPE_NL_MODEL            = 5;//Xuất mẫu
    public static int STOCKOUT_TYPE_NL_DESTROY          = 6;//Xuất tiêu hủy
    
    //Xuat phu lieu
    public static int STOCKOUT_TYPE_PL_LINE           	= 11;//Xuất phụ liệu lên chuyền
    public static int STOCKOUT_TYPE_PL_MOVE           	= 12;//Xuất phụ liệu điều chuyển
    public static int STOCKOUT_TYPE_PL_OUTSOURCE        = 13;//Xuất phụ liệu gia công
    
    //Xuat thanh pham
    public static int STOCKOUT_TYPE_TP_PO           	= 21;//Xuất thành phẩm theo đơn
    public static int STOCKOUT_TYPE_TP_MOVE           	= 22;//Xuất thành phẩm điều chuyển  
    
    //Xuat cua hang
    public static int STOCKOUT_TYPE_CH_MOVE           	= 31;//Xuất điều chuyển ch
    public static int STOCKOUT_TYPE_CH_SELL           	= 32;//Xuất bán  
}
