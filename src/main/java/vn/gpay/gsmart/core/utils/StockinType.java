package vn.gpay.gsmart.core.utils;
public class StockinType {
    public static int STOCKIN_TYPE_FROM_NEW             = 1;//Nhập mua mới, sản xuất mới
    public static int STOCKIN_TYPE_FROM_MOVE            = 2;//Nhập điều chuyển
    public static int STOCKIN_TYPE_FROM_CUTHOUSE        = 3;//Nhập vải thừa từ tổ cắt
    public static int STOCKIN_TYPE_FROM_OUTSOURCE       = 4;//Nhập vải trả lại từ gia công
    public static int STOCKIN_TYPE_FROM_ADDBYPROVIDER   = 5;//Nhập cấp bù từ nhà cung cấp
    public static int STOCKIN_TYPE_FROM_COMPLETEPRO     = 6;//Nhập mẫu
    
    //Nguyen lieu
    public static int STOCKIN_TYPE_NL_NEW             = 1;//Nhập vải mua mới, sản xuất mới
    public static int STOCKIN_TYPE_NL_MOVE            = 2;//Nhập vải điều chuyển
    public static int STOCKIN_TYPE_NL_CUTHOUSE        = 3;//Nhập vải thừa từ tổ cắt
    public static int STOCKIN_TYPE_NL_OUTSOURCE       = 4;//Nhập vải trả lại từ gia công
    public static int STOCKIN_TYPE_NL_ADDBYPROVIDER   = 5;//Nhập vải cấp bù từ nhà cung cấp
    public static int STOCKIN_TYPE_NL_COMPLETEPRO     = 6;//Nhập vải mẫu
    
	//Phu lieu
    public static int STOCKIN_TYPE_PL_NEW             = 11;//Nhập PL mua mới, sản xuất mới
    public static int STOCKIN_TYPE_PL_MOVE            = 12;//Nhập PL điều chuyển

	//Thanh pham
    public static int STOCKIN_TYPE_TP_NEW             = 21;//Nhập TP mua mới, sản xuất mới
    public static int STOCKIN_TYPE_TP_MOVE            = 22;//Nhập TP điều chuyển
    
    //Cua hang
    public static int STOCKIN_TYPE_CH_MOVE           = 31;//Nhập điều chuyển ch
}
