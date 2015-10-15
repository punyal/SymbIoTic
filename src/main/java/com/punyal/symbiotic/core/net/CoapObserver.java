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
import java.net.Inet6Address;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public abstract class CoapObserver {
    private final Core core;
    private final CoapClient coapClient;
    private final CoapHandler coapHandler;
    private CoapObserveRelation relation;
    
    public CoapObserver(Core core, String path) {
        this.core = core;
        String uri;
        // Check if it's an IPv4 or IPv6
        if (core.getStatus().getSelectedThing().getInetAddress() instanceof Inet6Address)
            uri = "coap://["+core.getStatus().getSelectedThing().getAddress()+"]:"+core.getStatus().getSelectedThing().getPort()+"/"+path;
        else
            uri = "coap://"+core.getStatus().getSelectedThing().getAddress()+":"+core.getStatus().getSelectedThing().getPort()+"/"+path;
        
        System.out.println(uri);
        coapClient = new CoapClient(uri);
        coapClient.setTimeout(60000);
        coapHandler = new CoapHandler() {
            @Override
            public void onLoad(CoapResponse cr) {
                // TODO: Check if the response is duplicated
                incomingData(cr);
            }
            @Override
            public void onError() {
                error();
            }
        };
    }
    
    public void startObserve() {
        System.out.println("Observing: "+coapClient.getURI());
        relation = coapClient.observe(coapHandler);
    }
    
    public void stopObserve() {
        System.out.println("Canceling Observe: "+coapClient.getURI());
        relation.proactiveCancel();
    }
    
    abstract public void incomingData(CoapResponse response);
    
    abstract public void error();
    
}
