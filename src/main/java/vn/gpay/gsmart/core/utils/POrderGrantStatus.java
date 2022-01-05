package vn.gpay.gsmart.core.utils;

public class POrderGrantStatus {
	public static int PORDERGRANT_STATUS_PLAN  	= 1; //Lenh ke hoach
	public static int PORDERGRANT_STATUS_POLINE  = 2; //Lenh chinh thuc theo poline
    
    //Mầu hiện trên biểu đồ
    //Xanh lá đậm (064420): Hàng đã xong --> Trạng thái 6 (Nhập kho thành phẩm đủ SL yêu cầu của lệnh)
    //Xanh lá vừa (66DE93): Hàng đang sản xuất --> Trạng thái 4 và 5
    //Xanh lá nhạt (B3E283): Hàng đã phân chuyền --> Trạng thái 1
    //Vàng: Chuan bi SX --> Trạng thái 0 (có yeu cau xuất sang cắt)
    //Đỏ đậm (DA0037): Chậm giao hàng (Ngày giao hàng poline - Ngày kết thúc của lệnh map voi poline <0)- de tham so thay doi
    //Đỏ vừa (D83A56): Chậm giao hàng (Ngày giao hàng poline - Ngày kết thúc của lệnh map voi poline <5) - de tham so thay doi
    //Đỏ nhạt (FF616D): Chậm giao hàng (Ngày giao hàng poline - Ngày kết thúc của lệnh map voi poline <10)- de tham so thay doi
    
}
