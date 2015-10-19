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
package com.punyal.symbiotic.core.feature.ipso;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import com.punyal.symbiotic.Utils.Parsers;
import static com.punyal.symbiotic.constants.ConstantsNet.RESOURCE_BATTERY;
import com.punyal.symbiotic.core.Core;
import java.io.ByteArrayOutputStream;
import java.net.Inet6Address;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class BatteryThread extends Thread {
    private final Core core;
    private boolean running;
    private final CoapClient coapClient;
    
    public BatteryThread(Core core) {
        this.core = core;
        this.setDaemon(true);
        coapClient = new CoapClient();
        running = true;
    }
    
    public void startThread() {
        //System.out.println("Starting BatThread");
        this.start();
    }
    
    public void stopThread() {
        running = false;
    }
    
    public void run() {
        try {
            int counter = 0, battery = 0;
            String uri;
            CoapResponse response;
            JSONObject json;
            while (running) {
                if (counter == 0) {
                    counter = 60; // pull the resource each minute
                    
                    // Check if it's an IPv4 or IPv6
                    if (core.getStatus().getSelectedThing().getInetAddress() instanceof Inet6Address)
                        uri = "coap://["+core.getStatus().getSelectedThing().getAddress()+"]:"+core.getStatus().getSelectedThing().getPort()+"/"+RESOURCE_BATTERY;
                    else
                        uri = "coap://"+core.getStatus().getSelectedThing().getAddress()+":"+core.getStatus().getSelectedThing().getPort()+"/"+RESOURCE_BATTERY;
                    
                    //core.getStatus().setBatteryLevel(100);
                    coapClient.setURI(uri);
                    System.out.println(uri);
                    response = coapClient.get();
                    
                    if (response != null) {
                        System.out.println("BatteryResponse:"+response.getResponseText());
                        if (!response.getResponseText().isEmpty()) {
                            json = Parsers.parseMulleJSONData(response.getResponseText());
                            //System.out.println(json);

                            battery = Integer.parseInt(json.get("Vbat").toString());
                            // adjust to percent (5000 mV)
                            battery /= 50;
                            //System.out.println(battery);
                            core.getStatus().setBatteryLevel(battery);

                            // Save data to file
                            try {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                CborBuilder cborBuilder = new CborBuilder();

                                cborBuilder.add("Battery");
                                cborBuilder.add(Integer.parseInt(json.get("time").toString()));
                                cborBuilder.add(Integer.parseInt(json.get("Vbat").toString()));
                                cborBuilder.add(Integer.parseInt(json.get("Vchar").toString()));

                                new CborEncoder(baos).encode(cborBuilder.build());

                                core.getStatus().getExportData().save2File(baos.toByteArray());

                            } catch (NumberFormatException | CborException ex) {
                                System.out.println("Error "+ex);
                            }
                        }
                    }
                }
                
                counter--;
                
                try {
                    Thread.sleep(1000); // Sleep 1s
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt(); // This should kill it propertly
                }
            }
        }
        finally {
            System.out.println("Killing BatThread");
        }
    }
    
}
