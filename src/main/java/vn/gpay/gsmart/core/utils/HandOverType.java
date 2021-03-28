package vn.gpay.gsmart.core.utils;

public class HandOverType {
	public static Long HANDOVER_TYPE_CUT_LINE            	= 1L; // cut to line, line from cut 
	public static Long HANDOVER_TYPE_CUT_PRINT            	= 2L; // cut to print, print from cut 
	public static Long HANDOVER_TYPE_LINE_PACK            	= 4L; // line to pack, pack from line (hoan thien)
	public static Long HANDOVER_TYPE_LINE_PRINT            	= 5L; // line to print
	public static Long HANDOVER_TYPE_PACK_STOCK            	= 9L; // pack to stock (hoan thien > kho TP)
}
