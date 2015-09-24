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
package com.punyal.symbiotic.core.net.lwm2m;

import static com.punyal.symbiotic.Utils.Parsers.*;
import static com.punyal.symbiotic.constants.ConstantsJSON.*;
import static com.punyal.symbiotic.constants.ConstantsNet.*;
import java.util.ArrayList;
import java.util.Arrays;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class LWM2Mutils {
    public LWM2Mutils() {} // Prevent initialization
    
    public static ArrayList<JSONObject> parseResponse2JSONArray(String response) {
        ArrayList<JSONObject> array = new ArrayList<>();
        try {
            while (response.indexOf("{")>0) {
                array.add((JSONObject)JSONValue.parse(response.substring(response.indexOf("{"),response.indexOf("}")+1)));
                response = response.substring(response.indexOf("}")+1);
            }
        } catch (StringIndexOutOfBoundsException e) {}
        return array;
    }
    
    public static ArrayList<String> parseRAWobjectLinks(String objectLinks) {
        objectLinks = objectLinks.substring(objectLinks.indexOf("[")+1, objectLinks.lastIndexOf("]"));
        ArrayList<String> dirtyList = new ArrayList<>(Arrays.asList(objectLinks.split(",")));
        ArrayList<String> cleanList = new ArrayList<>();
        for (String dirty : dirtyList)
            cleanList.add(dirty.substring(dirty.indexOf("<")));
        return cleanList;
    }
    
    
    
    public static JSONObject decodeM2MResponse(String response) {
        JSONObject json = new JSONObject();
        // Conver to hexadecimal to do it easy
        String hex = stringToHex(response).toUpperCase();
        String[] array = hex.split("EFBFBD");
        
        String temp, conv;
        int len, type;
        for (String sector : array) {
            if(sector.length()>2) {
                temp = sector.substring(2);
                type = Integer.parseInt(sector.substring(0,2),16);
                switch(type) {
                    case LWM2M_CODE_MANUFACTURER:
                        if(temp.length()>2) {
                            len = Integer.parseInt(temp.substring(0,2),16);
                            temp = temp.substring(2);
                            conv = hexToString(temp);
                            if(len == conv.length())
                                json.put(JSON_MANUFACTURER, conv);
                        }
                        break;
                    case LWM2M_CODE_MODEL_NUMBER:
                        if(temp.length()>2) {
                            len = Integer.parseInt(temp.substring(0,2),16);
                            temp = temp.substring(2);
                            conv = hexToString(temp);
                            if(len == conv.length())
                                json.put(JSON_MODEL_NUMBER, conv);
                        }
                        break;
                    case LWM2M_CODE_SERIAL_NUMBER:
                        if(temp.length()>2) {
                            len = Integer.parseInt(temp.substring(0,2),16);
                            temp = temp.substring(2);
                            conv = hexToString(temp);
                            if(len == conv.length())
                                json.put(JSON_SERIAL_NUMBER, conv);
                        }
                        break;
                    case LWM2M_CODE_FIRMWARE_VERSION:
                        if(temp.length()>2) {
                            conv = hexToString(temp);
                            json.put(JSON_FIRMWARE_VERSION, conv);
                        }
                        break;
                    default:break; // Ignore Others
                }
            }
        }
        return json;
    }
    
    
    
}
