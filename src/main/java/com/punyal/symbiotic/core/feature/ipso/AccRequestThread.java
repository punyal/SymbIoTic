package com.punyal.symbiotic.core.feature.ipso;

import com.punyal.symbiotic.Utils.Coordinates;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

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

/**
 *
 * @author mulle
 */
public class AccRequestThread extends Thread {
    private final ConcurrentLinkedQueue<Number> incomingData;
    private boolean running;
    private final CoapClient coapClient;
    
    public AccRequestThread(ConcurrentLinkedQueue<Number> incomingData) {
        this.incomingData = incomingData;
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
    
    @Override
    public void run() {
        CoapObserveRelation relation = null;
        CoapResponse response;
        try {
            /* OBSERVABLE RESOURCE */
            coapClient.setURI("coap://[fdfd:0:0:0:2905:d387:5155:edc5]:5683/acc_ctrl");
            response = coapClient.post("", MediaTypeRegistry.TEXT_PLAIN);
            
            if (response != null) {
                System.out.println("response!!:"+response.getResponseText());
            
            
                coapClient.setURI("coap://[fdfd:0:0:0:2905:d387:5155:edc5]:5683/acc");
                relation = coapClient.observe(
                    new CoapHandler() {

                        @Override
                        public void onLoad(CoapResponse response) {
                            System.out.println("size: "+response.getPayload().length);
                            byte[] bytes = response.getPayload();
                            for (int i=0; i<bytes.length/2; i++) {
                                incomingData.add(Coordinates.getInt(bytes, i));
                            }
                        }
                        
                        @Override
                        public void onError() {
                            System.err.println("OBSERVING FALIED");
                        }
                    }
                );
                //relation.proactiveCancel();
            }
            
            
        }
        finally{
            System.out.println("Killing AccRequestThread");
            // Launch thread stop2measure
        }
    }
}
