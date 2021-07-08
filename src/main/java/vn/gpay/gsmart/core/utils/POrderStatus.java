package vn.gpay.gsmart.core.utils;

public class POrderStatus {
	public static int PORDER_STATUS_CANCEL  	= -3; //Hủy đơn
	public static int PORDER_STATUS_UNCONFIRM  = -1; //Chưa chốt
	public static int PORDER_STATUS_FREE       = 0; //Đã chốt, chưa phân chuyền
    public static int PORDER_STATUS_GRANTED    = 1; //Đã phân chuyền, chưa yêu cầu sx
    public static int PORDER_STATUS_READY      = 2; //Yêu cầu sx đề kho và cắt chuẩn bị
    public static int PORDER_STATUS_SUBPROCESS = 3; //Đang thực hiện công đoạn phụ (may trc 1 số bước khó) trước khi vào chuyền
    public static int PORDER_STATUS_RUNNING    = 4; //Đang sản xuất
    public static int PORDER_STATUS_DONE       = 5; //Đã sản xuất xong, chưa nhập kho TP hết
    public static int PORDER_STATUS_FINISHED   = 6; //Đã hoàn thành mã hàng
    public static int PORDER_STATUS_HasMapPOLine = 7; //Đã map với PO Line

    public static int PORDER_STATUS_CUTTING    		= 20; //Bắt đầu cắt
    public static int PORDER_STATUS_NUMBERING  		= 21; //Đánh số
    public static int PORDER_STATUS_CHECK    		= 22; //Kiểm bán thành phẩm
    public static int PORDER_STATUS_MEX    			= 23; //Ép mex
    public static int PORDER_STATUS_TOLINE    		= 24; //Chuyển lên chuyền

    public static int PORDER_SHORTVALUE_FREE       = 6; //Chưa phân chuyền
    public static int PORDER_SHORTVALUE_GRANTED    = 5; //Đã phân chuyền, chưa yêu cầu sx
    public static int PORDER_SHORTVALUE_READY      = 4; //Yêu cầu sx đề kho và cắt chuẩn bị
    public static int PORDER_SHORTVALUE_RUNNING    = 2; //Đang sản xuất
    public static int PORDER_SHORTVALUE_DONE       = 1; //Đã sản xuất xong, chưa nhập kho TP hết
    public static int PORDER_SHORTVALUE_FINISHED   = 0; //Đã hoàn thành mã hàng
    public static int PORDER_SHORTVALUE_SUBPROCESS = 3; //Đang thực hiện công đoạn phụ (may trc 1 số bước khó) trước khi vào chuyền
    
    //Mầu hiện trên biểu đồ
    //Xanh lá đậm (064420): Hàng đã xong --> Trạng thái 6 (Nhập kho thành phẩm đủ SL yêu cầu của lệnh)
    //Xanh lá vừa (66DE93): Hàng đang sản xuất --> Trạng thái 4 và 5
    //Xanh lá nhạt (B3E283): Hàng đã phân chuyền --> Trạng thái 1
    //Vàng: Đang triển khai đơn hàng --> Trạng thái 0
    //Đỏ đậm (DA0037): Chậm giao hàng (Ngày giao hàng - Ngày kết thúc của lệnh <0)
    //Đỏ vừa (D83A56): Chậm giao hàng (Ngày giao hàng - Ngày kết thúc của lệnh <5)
    //Đỏ nhạt (FF616D): Chậm giao hàng (Ngày giao hàng - Ngày kết thúc của lệnh <10)
    
}
