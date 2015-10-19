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
package com.punyal.symbiotic.constants;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class ConstantsNet {
    public ConstantsNet() {}
    
    // CoAP
    public static final int DEFAULT_COAP_PORT = 5683;
    
    // LightWeightM2M
    public static final int LWM2M_CODE_MANUFACTURER = 0;
    public static final int LWM2M_CODE_MODEL_NUMBER = 1;
    public static final int LWM2M_CODE_SERIAL_NUMBER = 2;
    public static final int LWM2M_CODE_FIRMWARE_VERSION = 3;
    public static final String LWM2M_RESOURCE_MANUFACTURER = "/3/0/0";
    public static final String LWM2M_RESOURCE_MODEL_NUMBER = "/3/0/1";
    public static final String LWM2M_RESOURCE_SERIAL_NUMBER = "/3/0/2";
    public static final String LWM2M_RESOURCE_FIRMWARE_VERSION = "/3/0/3";
    
    // Resources names
    public static final String RESOURCE_ACCELEROMETER = "acc";
    public static final String RESOURCE_ACCELEROMETER_FILTER = "acc/filter";
    public static final String RESOURCE_ACCELEROMETER_CONTROL = "acc/ctrl";
    public static final String RESOURCE_BATTERY = "power";
    public static final String RESOURCE_STRAIN = "strain";
    public static final String RESOURCE_ANGLE = "angle";
    public static final String RESOURCE_FILE_SYSTEM = "FileSystemPro";
    
    
}
