package com.hc.util.decompression;

import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class InflaterDecompress {
	
	public static String decompress(byte[] bytes) throws DataFormatException{
		Inflater decompress = new Inflater(); 
    	byte[] input = new byte[50000]; 
    	decompress.setInput(bytes, 0, bytes.length); 
		int resultLength = decompress.inflate(input);
		decompress.end();
		return new String(input, 0, resultLength);
	}
	
	public static byte[] compress(String message){
		Deflater compress=new Deflater();
			compress.setInput(message.getBytes());
        compress.finish();
        byte[] output=new byte[13000];
        int len=compress.deflate(output);
        byte[] res = new byte[len];
        System.arraycopy(output,0,res,0,len);
        return res;
	}
}
