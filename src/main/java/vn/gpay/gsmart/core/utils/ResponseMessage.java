package vn.gpay.gsmart.core.utils;

import java.util.HashMap;

public class ResponseMessage {

	public static int KEY_RC_SUCCESS                = 200;
    public static int KEY_RC_BAD_REQUEST            = 400;
    public static int KEY_RC_AUTHEN_ERROR           = 401;
    public static int KEY_RC_UN_AUTHORIZED          = 403;
    public static int KEY_RC_RS_NOT_FOUND           = 404;
    public static int KEY_RC_CREATE_INVCHECK_FAIL   = 300;
    public static int KEY_RC_SERVER_ERROR           = 500;
    public static int KEY_RC_EXCEPTION              = 900;
    public static int KEY_RC_IMEI_NOT_EXSIST        = 901;
    public static int KEY_RC_IMEI_NOT_PERMISSION    = 902;
    public static int KEY_RC_APPROVE_FAIL           = 903;
    public static int KEY_RC_ACTIVE_FAIL            = 904;
    public static int KEY_RC_KEY_DUPLICATION        = 905;
    
    public static int KEY_RC_SKU_INVALID            = 700;
    public static int KEY_RC_EPC_INVALID            = 800;
    public static String MES_RC_EPC_INVALID         = "EPC Invalid";
    
    //POrder_Req Messages (1000-1010)
    public static int KEY_POREQ_DELETE_PORDEREXISTED 		= 1000;
    public static String MES_POREQ_DELETE_PORDEREXISTED   	= "Đang tồn tại lệnh sản xuất liên quan, không thể xóa";
    
    //POrder Create Messages (1011-1020)
    public static int KEY_RC_PORDER_EXISTED 		= 1011;
    public static String MES_RC_PORDER_EXISTED   	= "Đã có lệnh sản xuất, không thể tạo thêm";
    public static int KEY_RC_PORDER_NOSKU 			= 1012;
    public static String MES_RC_PORDER_NOSKU   		= "Chưa khai báo chi tiết màu, cỡ cho sản phẩm trong chi tiết PO";    
    public static int KEY_RC_PORDER_NOPO 			= 1013;
    public static String MES_RC_PORDER_NOPO   		= "PO không tồn tại";    
    public static int KEY_RC_PORDER_CALCULATION		= 1014;
    public static String MES_RC_PORDER_CALCULATION  = "Năng suất xưởng hoặc Số lượng yêu cầu sản xuất không hợp lệ";    
    
    public static String MES_RC_SUCCESS   = "OK - Everything Worked";
    public static String MES_RC_BAD_REQUEST   = "Bad Request - Invalid Parameter or Data Integrity Issue";
    public static String MES_RC_AUTHEN_ERROR = "Authentication Error";
    public static String MES_RC_UN_AUTHORIZED     = "Unauthorized Request";
    public static String MES_RC_RS_NOT_FOUND     = "Resource Not Found";
    public static String MES_RC_SERVER_ERROR     = "Platform Internal Server Error";
    public static String MES_RC_EXCEPTION     = "Exception request API";
    public static String MES_RC_IMEI_NOT_EXSIST     = "Imei not exisit";
    public static String MES_RC_IMEI_NOT_PERMISSION     = "Imei not permission";
    public static String MES_RC_CREATE_INVCHECK_FAIL    = "Create invcheck fail";
    public static String MES_RC_KEY_DUPLICATION    		= "Key duplication";
    
    public static String MES_RC_APPROVE_FAIL      = "Approve Fail !";
    public static String MES_RC_ACTIVE_FAIL     = "ACTIVE Fail !";
    
    //saving
    public static int KEY_RC_PHONE_OR_ID_EXISTS     = 903;    
    public static String MES_KEY_RC_PHONE_OR_ID_EXISTS   = "Mobile or Identify card is Exists";
    
    public static String getMessage(int code){      
        HashMap<Integer,String> hMes = new HashMap<>();
        hMes.put(KEY_RC_SUCCESS, MES_RC_SUCCESS);
        hMes.put(KEY_RC_BAD_REQUEST, MES_RC_BAD_REQUEST);
        hMes.put(KEY_RC_AUTHEN_ERROR, MES_RC_AUTHEN_ERROR);
        hMes.put(KEY_RC_UN_AUTHORIZED, MES_RC_UN_AUTHORIZED);
        hMes.put(KEY_RC_RS_NOT_FOUND, MES_RC_RS_NOT_FOUND);
        hMes.put(KEY_RC_SERVER_ERROR, MES_RC_SERVER_ERROR);
        hMes.put(KEY_RC_EXCEPTION, MES_RC_EXCEPTION);
        hMes.put(KEY_RC_IMEI_NOT_EXSIST, MES_RC_IMEI_NOT_EXSIST);
        hMes.put(KEY_RC_IMEI_NOT_PERMISSION, MES_RC_IMEI_NOT_PERMISSION);
        hMes.put(KEY_RC_PHONE_OR_ID_EXISTS, MES_KEY_RC_PHONE_OR_ID_EXISTS);
        hMes.put(KEY_RC_APPROVE_FAIL, MES_RC_APPROVE_FAIL);
        hMes.put(KEY_RC_ACTIVE_FAIL, MES_RC_ACTIVE_FAIL);
        hMes.put(KEY_RC_CREATE_INVCHECK_FAIL, MES_RC_CREATE_INVCHECK_FAIL);
        hMes.put(KEY_RC_EPC_INVALID, MES_RC_EPC_INVALID);
        hMes.put(KEY_POREQ_DELETE_PORDEREXISTED, MES_POREQ_DELETE_PORDEREXISTED);
        return hMes.get(code);
    }
}
