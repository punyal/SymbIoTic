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
    
}
