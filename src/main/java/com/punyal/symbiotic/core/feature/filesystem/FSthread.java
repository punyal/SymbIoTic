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

import static com.punyal.symbiotic.constants.ConstantsNet.RESOURCE_FILE_SYSTEM;
import com.punyal.symbiotic.core.Core;
import com.punyal.symbiotic.core.net.CoapObserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet6Address;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.input.DataFormat;
import javafx.stage.FileChooser;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
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
                    //String incoming = response.getResponseText();
                    //System.out.println(incoming);
                    //JSONObject json;
                    //json = (JSONObject) JSONValue.parse(response.getResponseText());
                    //System.out.println(json.get("type"));
                    //json.put("files", response.getResponseText());
                    //json = (JSONObject) JSONValue.parse(response.getResponseText());
                    Object obj = JSONValue.parse(response.getResponseText());
                    JSONArray slideContent = (JSONArray)obj;
                    //JSONArray slideContent = (JSONArray);
                    
                    Iterator i = slideContent.iterator();
                    core.getStatus().getFileSystem().init();
                    while (i.hasNext()) {
                        JSONObject slide = (JSONObject) i.next();
                        System.out.println(slide.get("name")+":"+slide.get("size")+":"+slide.get("lock"));
                        core.getStatus().getFileSystem().add(slide.get("name").toString(), Integer.parseInt(slide.get("size").toString()), Integer.parseInt(slide.get("lock").toString()) == 1?"locked":"unlocked");
                    }
                    /*String tmp;
                    tmp = incoming.substring(1, incoming.length()-1);
                    System.out.println(tmp);
                    List<String> items = Arrays.asList(tmp.split("\\s*,\\s*"));
                    for (String item : items) {
                        System.out.println(item);
                    }*/
                    
                    
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
    
    public void getFile(String fileName) {
        String uri;
        if (core.getStatus().getSelectedThing().getInetAddress() instanceof Inet6Address)
            uri = "coap://["+core.getStatus().getSelectedThing().getAddress()+"]:"+core.getStatus().getSelectedThing().getPort()+"/"+RESOURCE_FILE_SYSTEM+"?f="+fileName;
        else
            uri = "coap://"+core.getStatus().getSelectedThing().getAddress()+":"+core.getStatus().getSelectedThing().getPort()+"/"+RESOURCE_FILE_SYSTEM+"?f="+fileName;
        
        //System.out.println("GET uri:"+uri);
        CoapClient coapClient = new CoapClient(uri);
        CoapResponse response = coapClient.get();
        if (response != null) {
            byte[] resp = response.getPayload();
            //System.out.println(response.getResponseText());
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save "+fileName);
            fileChooser.setInitialFileName(fileName);
            File file = fileChooser.showSaveDialog(core.getConfiguration().getMainStageInfo().getStage());
            System.out.println(file);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                try {
                    fos.write(resp);
                    fos.close();
                } catch (IOException ex) {
                    System.out.println("Error saving file: "+ex);
                }
            } catch (FileNotFoundException ex) {
                System.out.println("Error saving file: "+ex);
            }
        }
    }
    
    public void sendFile() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(core.getConfiguration().getMainStageInfo().getStage());
        System.out.println(file);
        System.out.println(file.getName());
        
        byte[] bFile = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                fileInputStream.read(bFile);
                fileInputStream.close();
                
                String uri;
                if (core.getStatus().getSelectedThing().getInetAddress() instanceof Inet6Address)
                    uri = "coap://["+core.getStatus().getSelectedThing().getAddress()+"]:"+core.getStatus().getSelectedThing().getPort()+"/"+RESOURCE_FILE_SYSTEM+"?f="+file.getName();
                else
                    uri = "coap://"+core.getStatus().getSelectedThing().getAddress()+":"+core.getStatus().getSelectedThing().getPort()+"/"+RESOURCE_FILE_SYSTEM+"?f="+file.getName();

                System.out.println("GET uri:"+uri);
                CoapClient coapClient = new CoapClient(uri);
                coapClient.useNONs();
                //coapClient.useEarlyNegotiation(64);
                coapClient.put(bFile, MediaTypeRegistry.TEXT_PLAIN);
                
                
            } catch (IOException ex) {
                System.out.println("Error loading file: "+ex);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error loading file: "+ex);
        }
        
        
    }
    
    public void deleteFile(String fileName) {
        String uri;
        if (core.getStatus().getSelectedThing().getInetAddress() instanceof Inet6Address)
            uri = "coap://["+core.getStatus().getSelectedThing().getAddress()+"]:"+core.getStatus().getSelectedThing().getPort()+"/"+RESOURCE_FILE_SYSTEM+"?f="+fileName;
        else
            uri = "coap://"+core.getStatus().getSelectedThing().getAddress()+":"+core.getStatus().getSelectedThing().getPort()+"/"+RESOURCE_FILE_SYSTEM+"?f="+fileName;
        
        System.out.println("GET uri:"+uri);
        CoapClient coapClient = new CoapClient(uri);
        coapClient.useNONs();
        coapClient.delete();
      
    }
    
    /*
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
    }*/
}
