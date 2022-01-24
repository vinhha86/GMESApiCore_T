package vn.gpay.gsmart.core.utils;

public class StockoutStatus {
	public static int STOCKOUT_STATUS_RECEIVED            		= 2; //Bên nhận đã xác nhận nhận hàng hoặc đã đồng bộ với PM QL
    public static int STOCKOUT_STATUS_APPROVED            		= 1; //Đã duyệt, trừ warehouse
	public static int STOCKOUT_STATUS_OK                		= 0;//Đang kiểm tra
	public static int STOCKOUT_STATUS_PICKING            		= -1;//Đang nhặt hàng
	public static int STOCKOUT_STATUS_ERR            			= -2;//Chưa nhặt hàng
    public static int STOCKOUT_STATUS_DELETED            		= -3;//Đã xóa
    
    
    public static int STOCKOUT_D_STATUS_OK           			= 0;//Đã kiểm đủ
    public static int STOCKOUT_D_STATUS_ERR          			= -1;//Chưa kiểm đủ
    
    public static int STOCKOUT_EPC_STATUS_RIP           		= 1;// cây vải đã xé
    public static int STOCKOUT_EPC_STATUS_OK           			= 0;//
    public static int STOCKOUT_EPC_STATUS_ERR					= -1;//Chưa kiểm tra
    public static int STOCKOUT_EPC_STATUS_ERR_WAREHOUSENOTEXIST	= -2;//Hang khong ton tai trong warehouse
    public static int STOCKOUT_EPC_STATUS_ERR_OUTOFSKULIST      = -3;//Khong co trong bang Sku
    
    // Thanh pham
    public static int TP_LOAI1 									= 11; 
	public static int TP_LOAI2 									= 12;
	public static int TP_LOAI3									= 13;
}
