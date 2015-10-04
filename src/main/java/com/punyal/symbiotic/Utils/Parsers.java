/*
 * The MIT License
 *
 * Copyright 2015 Your Organisation.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.punyal.symbiotic.Utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class Parsers {
    
    public static short getInt(byte[] arr, int off) {
        return (short)(arr[2*off]&0xFF | arr[2*off+1]<<8);
    }
    
    public static String ByteArray2Hex(byte[] bytes) {
        if(bytes == null) return "null";
        StringBuilder sb = new StringBuilder();
        for(byte b:bytes)
            sb.append(String.format("%02x", b & 0xFF));
        return sb.toString();
    }
    
    public static String ByteArray2String(byte[] bytes) {
        String string;
        try {
            string = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            System.err.println("ByteArray2String UnsupportedEncodingException "+ ex);
            string = "";
        }
        return string;
    }
    
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len/2];
        for(int i = 0; i < len; i+=2)
            data[i/2] = (byte) ((Character.digit(s.charAt(i),16) << 4) +
                    Character.digit(s.charAt(i+1), 16));
        return data;
    }
    
    public static byte[] stringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len/2];
        data = s.getBytes(Charset.forName("UTF-8"));
        return data;
    }
    
    public static String stringToHex(String s) {
        return ByteArray2Hex(stringToByteArray(s));
    }
    
    public static String hexToString(String s) {
        return ByteArray2String(hexStringToByteArray(s));
    }
    
    public static JSONObject parseMulleJSONData (String s) {
        JSONObject json, tmp;
        String data;
        
        json = new JSONObject();
        tmp = (JSONObject) JSONValue.parse(s);
        
        // Save base time
        json.put("time", tmp.get("bt"));
        
        JSONArray slideContent = (JSONArray)tmp.get("e");
        Iterator i = slideContent.iterator();
        
        while (i.hasNext()) {
            JSONObject slide = (JSONObject) i.next();
            json.put(slide.get("n"), slide.get("v"));
        }
        
        return json;
    }
}
