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
package com.punyal.symbiotic.core.net;

import static com.punyal.symbiotic.constants.ConstantsJSON.*;
import com.punyal.symbiotic.core.net.lwm2m.LWM2Mutils;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import org.json.simple.JSONObject;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class Thing {
    // LWM2M parameters - from server
    private final String registrationId;
    private final String endPoint;
    private final InetAddress address;
    private final int port;
    private ArrayList<String> objectLinks;
    // LWM2M parameters - from device
    private boolean checked;
    private String manufacturer;
    private String modelNumber;
    private String serialNumber;
    private String firmwareVersion;
    
    
    public Thing(JSONObject json) throws UnknownHostException {
        endPoint = json.get(JSON_ENDPOINT).toString();
        registrationId = json.get(JSON_REGISTRATIONID).toString();
        address = InetAddress.getByName(json.get(JSON_ADDRESS).toString().substring(1));
        port = Integer.parseInt(json.get(JSON_PORT).toString());
        objectLinks = LWM2Mutils.parseRAWobjectLinks(json.get(JSON_OBJECTLINKS).toString());
        checked = false;
    }
    
    public String getEndPoint() {
        return endPoint;
    }
    
    public String getID() {
        return registrationId;
    }
    
    public InetAddress getInetAddress() {
        return address;
    }
    
    public String getAddress() {
        return address.getHostAddress();
    }
    
    public int getPort() {
        return port;
    }
    
    public ArrayList<String> getObjectLinks() {
        return objectLinks;
    }
    
    public void setObjectLins(ArrayList<String> objectLinks) {
        this.objectLinks = objectLinks;
    }
    
    public void addDeviceInfo(JSONObject json) {
        if (checked)
            return;
        checked = true;
        try {
            manufacturer = json.get(JSON_MANUFACTURER).toString();
            modelNumber = json.get(JSON_MODEL_NUMBER).toString();
            serialNumber = json.get(JSON_SERIAL_NUMBER).toString();
            firmwareVersion = json.get(JSON_FIRMWARE_VERSION).toString();
        } catch (NullPointerException ex) {}
        
    }
    
    public String getManufacturer() {
        return manufacturer;
    }
    
    public String getModelNumber() {
        return modelNumber;
    }
    
    public String getSerialNumber() {
        return serialNumber;
    }
    
    public String getFirmwareVersion() {
        return firmwareVersion;
    }
    
    
    
}
