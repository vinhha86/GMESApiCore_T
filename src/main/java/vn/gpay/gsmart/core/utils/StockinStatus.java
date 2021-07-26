package vn.gpay.gsmart.core.utils;
public class StockinStatus {
	public static int STOCKIN_STATUS_OK                			= 0;//Đang kiểm tra
    public static int STOCKIN_STATUS_ERR            			= -1;//Chưa kiểm tra
    public static int STOCKIN_STATUS_DELETED            		= -2;//Đã xóa
    public static int STOCKIN_STATUS_APPROVED            		= 1;//Đã duyệt để đưa vào warehouse
    public static int STOCKIN_STATUS_SYNC            			= 2;//Đã đồng bộ sang hệ thống QL
    
    public static int STOCKIN_D_STATUS_OK           			= 0;//Đã kiểm tra đủ
    public static int STOCKIN_D_STATUS_ERR          			= -1;//Chưa kiểm tra đủ các pklist
    
    public static int STOCKIN_EPC_STATUS_OK           			= 0;//Đã kiểm tra
    public static int STOCKIN_EPC_STATUS_ERR					= -1;//Chưa kiểm tra
    public static int STOCKIN_EPC_STATUS_ERR_WAREHOUSEEXISTED	= -2;//Hang da ton tai trong wwarehouse
    public static int STOCKIN_EPC_STATUS_ERR_OUTOFSKULIST       = -3;//Khong co trong bang Sku
    public static int STOCKIN_EPC_STATUS_CHECK_LABEL			= 1;//Đã kiểm tra Tem
    public static int STOCKIN_EPC_STATUS_CHECK_10PERCENT		= 2;//Đã kiểm tra 10%
    
    public static int STOCKIN_LOT_CHECKED						= 0; //Lot đã kiểm
    public static int STOCKIN_LOT_UNCHECKED       				= -1;//Lot chưa kiểm
}
