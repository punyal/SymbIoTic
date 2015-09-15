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
    private final InetAddress address;
    private final int port;
    private final ArrayList<String> objectLinks;
    // LWM2M parameters - from device
    private boolean checked;
    private String manufacturer;
    private String modelNumber;
    private String serialNumber;
    private String firmwareVersion;
    
    
    public Thing(JSONObject json) throws UnknownHostException {
        registrationId = json.get(JSON_REGISTRATIONID).toString();
        address = InetAddress.getByName(json.get(JSON_ADRESS).toString());
        port = Integer.parseInt(json.get(JSON_REGISTRATIONID).toString());
        objectLinks = null;//json.get(JSON_OBJECTLINKS);
        checked = false;
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
    
    public void addDeviceInfo(JSONObject json) {
        if (checked)
            return;
        checked = true;
        
        manufacturer = "";
        modelNumber = "";
        serialNumber = "";
        firmwareVersion = "";
        
        
    }
    
    
    
}
