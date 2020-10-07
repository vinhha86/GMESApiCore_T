package vn.gpay.gsmart.core.utils;

public class StockoutStatus {
	public static int STOCKOUT_STATUS_OK                		= 0;
    public static int STOCKOUT_STATUS_ERR            			= -1;
    public static int STOCKOUT_D_STATUS_OK           			= 0;
    public static int STOCKOUT_D_STATUS_ERR          			= -1;
    public static int STOCKOUT_EPC_STATUS_OK           			= 0;
    public static int STOCKOUT_EPC_STATUS_ERR_WAREHOUSENOTEXIST	= -1;//Hang khong ton tai trong warehouse
    public static int STOCKOUT_EPC_STATUS_ERR_OUTOFSKULIST      = -2;//Khong co trong bang Sku
}
