package vn.gpay.gsmart.core.utils;
public class StockinStatus {
	public static int STOCKIN_STATUS_OK                			= 0;
    public static int STOCKIN_STATUS_ERR            			= -1;
    public static int STOCKIN_D_STATUS_OK           			= 0;
    public static int STOCKIN_D_STATUS_ERR          			= -1;
    public static int STOCKIN_EPC_STATUS_OK           			= 0;
    public static int STOCKIN_EPC_STATUS_ERR_WAREHOUSEEXISTED	= -1;//Hang da ton tai trong wwarehouse
    public static int STOCKIN_EPC_STATUS_ERR_OUTOFSKULIST       = -2;//Khong co trong bang Sku
}
