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
    
    @Override
    public void run() {
        CoapResponse response;
        String uri = "coap://"+thing.getAddress()+":"+thing.getPort()+"/3";
        //System.out.println(uri);
        coapClient.setURI(uri);
        response = coapClient.get();
        if (response != null) {
            //System.out.println(response.getResponseText());
            JSONObject json = LWM2Mutils.decodeM2MResponse(response.getResponseText());
            thing.addDeviceInfo(json);
            
            // Update tree...
            core.getClientController().removeFromTree(thing.getID());
            
            TreeItem<String> node, level1, level2;
            node = new TreeItem<>(thing.getSerialNumber());
            node.setExpanded(false);

            level1 = new TreeItem<>("ID: "+thing.getID());
            node.getChildren().add(level1);


            level1 = new TreeItem<>("IP: "+thing.getAddress()+"@"+thing.getPort());
            node.getChildren().add(level1);


            level1 = new TreeItem<>("Manufacturer: "+thing.getManufacturer());
            node.getChildren().add(level1);


            level1 = new TreeItem<>("Model Number: "+thing.getModelNumber());
            node.getChildren().add(level1);
            

            level1 = new TreeItem<>("Firmware Version: "+thing.getFirmwareVersion());
            node.getChildren().add(level1);

            level1 = new TreeItem<>("Resources");
            level1.setExpanded(false);

            for (String link : thing.getObjectLinks()) {
                if(!link.equals("</>;rt=\"oma.lwm2m\"")) {
                    level2 = new TreeItem<>(link);
                    level1.getChildren().add(level2);
                }
            }

            node.getChildren().add(level1);
            core.getClientController().add2Tree(node);
            
        } else {
            System.out.println("No response from "+thing.getID());
        }
    }
    
}
