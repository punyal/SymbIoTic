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
package com.punyal.symbiotic.core.feature.filesystem;

import com.punyal.symbiotic.Utils.Parsers;
import static com.punyal.symbiotic.constants.ConstantsNet.RESOURCE_FILE_SYSTEM;
import com.punyal.symbiotic.core.Core;
import com.punyal.symbiotic.core.net.CoapObserver;
import java.util.Iterator;
import org.eclipse.californium.core.CoapResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class FSthread extends Thread{
    private final Core core;
    private boolean running;
    private final CoapObserver observer;
    
    public FSthread(final Core core) {
        this.core = core;
        this.setDaemon(true);
        
        observer = new CoapObserver(core, RESOURCE_FILE_SYSTEM) {
            
            @Override
            public void incomingData(CoapResponse response) {
                try {
                    System.out.println(response.getResponseText());
                    JSONObject json;
                    json = (JSONObject) JSONValue.parse(response.getResponseText());
                    System.out.println(json.get("type"));
                    JSONArray slideContent = (JSONArray)json.get("files");
                    
                    Iterator i = slideContent.iterator();
                    while (i.hasNext()) {
                        JSONObject slide = (JSONObject) i.next();
                        System.out.println(slide.get("name")+":"+slide.get("size")+":"+slide.get("lock"));
                        core.getStatus().getFileSystem().add(slide.get("name").toString(), Integer.parseInt(slide.get("size").toString()), Integer.parseInt(slide.get("lock").toString()));
                    }
                    
                } catch (NullPointerException e) {}
            }
            
            @Override
            public void error() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }
    
    public void startThread() {
        this.start();
        observer.startObserve();
    }
    
    public void stopThread() {
        running = false;
        observer.stopObserve();
    }
    
    @Override
    public void run() {
        try {
            while (running) {
                // process data here....
                try {
                    Thread.sleep(1000); // 1 s
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt(); // This should kill it propertly
                }
            }
            
        }
        finally {
            System.out.println("Killing FSthread");
            
        }
    }
}
