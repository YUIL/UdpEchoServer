package com.yuil.util;

/**
 * @author dj-004
 * @changedby dj-004
 */
public class JavaDataConverter {
	/**
	 * @param src	{byte[]:{"length":4}} 
	 * @return	{int}
	 */
	public static int bytesToInt(byte[] src) {  
	    int value=0;    
	    for(int i=0;i<src.length;i++){
	    	value=value|((src[i]& 0xFF)<<(i*8));
	    }
	    return value;  
	}  
	
	/**
	 * @param src	{int}
	 * @return	{byte[]:{"length":4}} 
	 */
	public static byte[] intToBytes(int src){
		
		byte[] bytes=new byte[4];
		for (int b = 0; b < bytes.length; b++) {
			bytes[b]=(byte)(src>>(b*8));
		}
		return bytes;
	}
	
	public static void subByte(byte[] src,byte[] dst,int offset){
		for (int i = 0; i < dst.length; i++) {
			dst[i]=src[i+offset];
		}
	}
	public static byte[] subByte(byte[] src,int length,int offset){
		byte[] dst=new byte[length];
		for (int i = 0; i < dst.length; i++) {
			dst[i]=src[i+offset];
		}
		return dst;
	}
}