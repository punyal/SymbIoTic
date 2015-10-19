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

import com.punyal.symbiotic.core.Core;
import com.punyal.symbiotic.core.net.lwm2m.LWM2Mutils;
import javafx.scene.control.TreeItem;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.json.simple.JSONObject;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class ThingThread extends Thread{
    private final Core core;
    private final Thing thing;
    private final CoapClient coapClient;
    
    public ThingThread(Core core, Thing thing) {
        this.core = core;
        this.thing = thing;
        coapClient = new CoapClient();
        this.setDaemon(true);
    }
    
    public void startThread() {
        this.start();
    }
    
    
    /**
     * Loading LWM2M params from Mulle
     * 
     * /3/0/0 - Manufacturer
     * /3/0/1 - Model Number
     * /3/0/2 - Serial Number
     * /3/0/3 - Firmware Version
     * 
     * 
     */
    
    
    @Override
    public void run() { // Load LWM2M params from mulle
        CoapResponse response;
        String uri = "coap://"+thing.getAddress()+":"+thing.getPort()+"/3/0/0"; // <- TODO: Fix this
        System.out.println(uri);
        coapClient.setURI(uri);
        response = coapClient.get();
        if (response != null) {
            System.out.println(response.getResponseText());
            
            //JSONObject json = LWM2Mutils.decodeM2MResponse(response.getResponseText());
            //synchronized (this){
            //    thing.addDeviceInfo(json);
            //}
            
            
        } else {
            System.out.println("No response from "+thing.getID());
        }
        
        System.out.println("Killing ThingThread...");
    }
    
}
