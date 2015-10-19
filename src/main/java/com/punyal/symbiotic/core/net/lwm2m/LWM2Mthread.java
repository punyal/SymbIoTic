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

import static com.punyal.symbiotic.constants.ConstantsNet.*;
import com.punyal.symbiotic.core.Core;
import com.punyal.symbiotic.core.net.Thing;
import java.net.Inet6Address;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class LWM2Mthread extends Thread{
    private final Core core;
    private final Thing thing;
    private boolean running;
    private final CoapClient coapClient;
    
    
    public LWM2Mthread(Core core, Thing thing) {
        this.core = core;
        this.thing = thing;
        this.setDaemon(true);
        coapClient = new CoapClient();
        running = true;
    }
    
    public void startThread() {
        this.start();
    }
    
    public void stopThread() {
        running = false;
    }
    
    
    
    private enum Steps {
        MANUFACTURER,
        MODEL,
        SERIAL,
        FIRMWARE
    }
    
    @Override
    public void run() {
        try {
            String uri = null, uriIP;
            CoapResponse response;
            Steps step = Steps.MANUFACTURER;
            
            // Check if it's an IPv4 or IPv6
            if (thing.getInetAddress() instanceof Inet6Address)
                uriIP = "coap://["+thing.getAddress()+"]:"+thing.getPort();
            else
                uriIP = "coap://"+thing.getAddress()+":"+thing.getPort();
            
            while (running) {
                switch(step) {
                    case MANUFACTURER:
                        uri = uriIP + LWM2M_RESOURCE_MANUFACTURER;
                        break;
                    case MODEL:
                        uri = uriIP + LWM2M_RESOURCE_MODEL_NUMBER;
                        break;
                    case SERIAL:
                        uri = uriIP + LWM2M_RESOURCE_SERIAL_NUMBER;
                        break;
                    case FIRMWARE:
                        uri = uriIP + LWM2M_RESOURCE_FIRMWARE_VERSION;
                        break;
                    default:
                        running = false;
                        break;
                }
                if (uri == null) running = false;
                else {
                    coapClient.setURI(uri);
                    response = coapClient.get();

                    if (response != null) {
                        String res = response.getResponseText();
                        String value;
                        value = (res.substring(res.indexOf("sv\":\"")+5, res.lastIndexOf("\"}")));
                        switch(step) {
                        case MANUFACTURER:
                            thing.setManufacturer(value);
                            step = Steps.MODEL;
                            break;
                        case MODEL:
                            thing.setModelNumber(value);
                            step = Steps.SERIAL;
                            break;
                        case SERIAL:
                            thing.setSerialNumber(value);
                            step = Steps.FIRMWARE;
                            break;
                        case FIRMWARE:
                            thing.setFirmwareVersion(value);
                            running = false;
                            //System.out.println("DONE");
                            break;
                        default:
                            running = false;
                            break;
                        }
                    }
                }
                
                
                try {
                    Thread.sleep(100); // Sleep 100ms
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt(); // This should kill it propertly
                }
            }
            
        } finally {
            System.out.println("Killing LWM2Mthread "+ thing.getManufacturer()+ " "+ thing.getModelNumber()+ " "+ thing.getSerialNumber()+ " "+ thing.getFirmwareVersion()+ " ");
            
        }
    }
}
