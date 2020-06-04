package vn.gpay.gsmart.core.utils;

public class ProductType {

	public static int SKU_TYPE_PRODUCT_MIN    		= 10;
	public static int SKU_TYPE_PRODUCT_MAX    		= 19;
    public static int SKU_TYPE_COMPLETEPRODUCT    	= 10; //Thành phẩm
    
    public static int SKU_TYPE_MATERIAL_MIN    		= 20;
    public static int SKU_TYPE_MATERIAL_MAX    		= 29;
    public static int SKU_TYPE_MAINMATERIAL      	= 20; //Vải chính
    public static int SKU_TYPE_LININGMATERIAL    	= 21; //Vải lót
    public static int SKU_TYPE_MEX       			= 22; //MEX
    public static int SKU_TYPE_MIXMATERIAL   		= 23; //Vải phối
    
    public static int SKU_TYPE_SWEINGTRIM_MIN    	= 30;
    public static int SKU_TYPE_SWEINGTRIM_MAX    	= 39;
    
    public static int SKU_TYPE_PACKINGTRIM_MIN    	= 40;
    public static int SKU_TYPE_PACKINGTRIM_MAX    	= 49;
    
    public static int SKU_TYPE_UNKNOWN   			= -1; //Không xác định

    public static String SKU_DESC_COMPLETEPRODUCT   = "Thành phẩm";
    public static String SKU_DESC_MAINMATERIAL      = "Vải chính";
    public static String SKU_DESC_LININGMATERIAL    = "Vải lót";
    public static String SKU_DESC_MEX       		= "Mex";
    public static String SKU_DESC_MIXMATERIAL   	= "Vải phối";
    public static String SKU_DESC_UNKNOWN   		= "Không xác định";
}
