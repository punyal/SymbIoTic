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
package com.punyal.symbiotic.core.feature.wheelloader;

import static com.punyal.symbiotic.constants.ConstantsNet.RESOURCE_RPM;
import com.punyal.symbiotic.core.Core;
import com.punyal.symbiotic.core.net.CoapObserver;
import org.eclipse.californium.core.CoapResponse;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class RPM {
    private final Core core;
    private final CoapObserver observer;
    
    public RPM(final Core core) {
        this.core = core;
        observer = new CoapObserver(core, RESOURCE_RPM) {
            
            @Override
            public void incomingData(CoapResponse response) {
                //System.out.println("RPM Response: "+response.getResponseText());
                if (!response.getResponseText().isEmpty()) {
                    JSONObject json = (JSONObject) JSONValue.parse(response.getResponseText());
                    core.getStatus().setWheelLoaderRPM(Integer.parseInt(json.get("rpm").toString()));
                    core.getStatus().setWheelLoaderCwTurns(Integer.parseInt(json.get("cwTurns").toString()));
                    core.getStatus().setWheelLoaderAcwTurns(Integer.parseInt(json.get("acwTurns").toString()));
                    core.getStatus().setWheelLoaderTemp(Float.parseFloat(json.get("T").toString()));
                    
                }
            }
            
            @Override
            public void error() {
                // Resource not observable acction
                System.out.println("Resource not able to observe");
            }
        };
    }
    
    public void startObserve() {
        observer.startObserve();
    }
    
    public void stopObserver() {
        observer.stopObserve();
    }
}
