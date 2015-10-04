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
import static com.punyal.symbiotic.constants.ConstantsNet.RESOURCE_STRAIN;
import com.punyal.symbiotic.core.Core;
import com.punyal.symbiotic.core.net.CoapObserver;
import java.io.ByteArrayOutputStream;
import javafx.scene.paint.Color;
import org.eclipse.californium.core.CoapResponse;
import org.json.simple.JSONObject;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class Strain {
    private final Core core;
    private final CoapObserver observer;
    
    public Strain (final Core core) {
        this.core = core;
        observer = new CoapObserver(core, RESOURCE_STRAIN) {
            
            @Override
            public void incomingData(CoapResponse response) {
                //System.out.println(response.getResponseText());
                JSONObject json = Parsers.parseMulleJSONData(response.getResponseText());
                //System.out.println(json);
                int strain = Integer.parseInt(json.get("strain").toString());
                boolean alarm = Integer.parseInt(json.get("alarm").toString()) == 1;
                        
                // to percent max 10000
                
                strain /= 100;
                
                if (core.getController().isAnimated()) {                  
                    if (alarm) {
                        // DO SOMETHING....
                    }
                    core.getStatus().setStrainLevel(strain, alarm);
                    
                }
                
                // Save data to file
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    CborBuilder cborBuilder = new CborBuilder();

                    cborBuilder.add("Strain");
                    cborBuilder.add(Integer.parseInt(json.get("time").toString()));
                    cborBuilder.add(Integer.parseInt(json.get("strain").toString()));
                    cborBuilder.add(Integer.parseInt(json.get("alarm").toString()));

                    new CborEncoder(baos).encode(cborBuilder.build());

                    core.getStatus().getExportData().save2File(baos.toByteArray());

                } catch (NumberFormatException | CborException ex) {
                    System.out.println("Error "+ex);
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
