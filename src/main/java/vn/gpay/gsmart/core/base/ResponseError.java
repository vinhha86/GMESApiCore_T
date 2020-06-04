package vn.gpay.gsmart.core.base;


public class ResponseError {
	public static int ERRCODE_RUNTIME_EXCEPTION 	= 1000;
	public static int ERRCODE_UNKNOWN_ERROR 		= 9999;
	private int errorcode;
	private String message;
	public ResponseError() {
		this.errorcode	=	ERRCODE_UNKNOWN_ERROR;
		this.message	=	"Unknown error";
	}
	public static int getErrcodeRuntimeException() {
		return ERRCODE_RUNTIME_EXCEPTION;
	}
	public static void setErrcodeRuntimeException(int errcodeRuntimeException) {
		ERRCODE_RUNTIME_EXCEPTION = errcodeRuntimeException;
	}
	public static int getErrcodeUnknownError() {
		return ERRCODE_UNKNOWN_ERROR;
	}
	public static void setErrcodeUnknownError(int errcodeUnknownError) {
		ERRCODE_UNKNOWN_ERROR = errcodeUnknownError;
	}
	public int getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
