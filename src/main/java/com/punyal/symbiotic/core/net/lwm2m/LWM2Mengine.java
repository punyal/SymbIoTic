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

import com.punyal.symbiotic.core.Core;
import java.util.ArrayList;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.json.simple.JSONObject;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class LWM2Mengine extends Thread{
    private final Core core;
    private boolean running;
    private final CoapClient coapClient;
    
    private enum EngineState {
        STOP,
        CONNECTING,
        CONNECTED,
        NOT_CONNECTED
    }
    
    public LWM2Mengine(Core core) {
        this.core = core;
        running = true;
        this.setDaemon(true);
        coapClient = new CoapClient();
    }
    
    public void startEngine() {
        this.start();
    }
    
    public void stopEngine() {
        running = false;
    }
    
    @Override
    public void run() {
        System.out.println("Starting LWM2MEngine...");
        EngineState state = EngineState.STOP;
        int timeout = 1000;
        String tmp = "";
        CoapResponse response = null;
        ArrayList<JSONObject> listJSON;
        try {
            while (running) {
                switch(state) {
                    case STOP:
                        timeout = 100;
                        core.getClientController().LWM2Mdisconnected();
                        state = EngineState.CONNECTING;
                        core.getClientController().LWM2Mconnecting();
                        break;
                    case CONNECTING:
                        timeout = 1000;
                        coapClient.setTimeout(200);
                        coapClient.setURI(core.getStatus().getLightWeightM2M().getAddress()+":"+core.getStatus().getLightWeightM2M().getPort()+"/rd");
                        response = coapClient.get();
                        if (response != null) {
                            state = EngineState.CONNECTED;
                            core.getClientController().LWM2Mconnected();
                        } else {
                            state = EngineState.NOT_CONNECTED;
                            core.getClientController().LWM2Mdisconnected();
                        }
                        break;
                    case CONNECTED:
                        timeout = 1000;
                        // Time to check if something change
                        response = coapClient.get();
                        
                        if (response != null) {
                            if (!tmp.equals(response.getResponseText())) { // Diferent Data, tree update needed
                                tmp = response.getResponseText();
                                // Send data to the parser.
                                listJSON = LWM2Mutils.parseResponse2JSONArray(tmp);
                                
                                for (JSONObject json: listJSON) {
                                    System.out.println("JSON: "+json.toJSONString());
                                }
                                
                            }
                        }
                            
                        else
                            state = EngineState.NOT_CONNECTED;
                        
                                                
                        
                        
                        break;
                    case NOT_CONNECTED:
                        core.getClientController().LWM2Mdisconnected();
                        stopEngine(); // Kill if it's not connected
                        break;
                    default: // Not supported
                        stopEngine();
                        break; 
                        
                }
                
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt(); // This should kill the thread in a good way
                    break;
                }
            }
        } finally {
            System.out.println("Killing LWM2MEngine...");
        }
        
        
    }
}
