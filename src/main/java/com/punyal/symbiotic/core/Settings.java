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
package com.punyal.symbiotic.core;

import static com.punyal.symbiotic.constants.Constants.*;
import static com.punyal.symbiotic.constants.ConstantsNet.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class Settings {
    private final Core core;
    
    public Settings(Core core) {
        this.core = core;
    }
    
    public void save() {
        System.out.print("Saving Properties... ");
        try {
            Properties properties = new Properties();
            
            // LightWeightM2M Server
            properties.setProperty(PROPERTY_LIGHTWEIGHTM2M_ADDRESS, core.getStatus().getLightWeightM2M().getAddress());
            properties.setProperty(PROPERTY_LIGHTWEIGHTM2M_PORT, ""+core.getStatus().getLightWeightM2M().getPort());
            // Selected Thing
            properties.setProperty(PROPERTY_THING_ADDRESS, core.getStatus().getSelectedThing().getAddress());
            properties.setProperty(PROPERTY_THING_PORT, ""+core.getStatus().getSelectedThing().getPort());
            
            properties.store(new FileOutputStream(new File(PROPERTIES_FILE_NAME)), PROPERTIES_TITLE_NAME);
            System.out.println("OK!");
        } catch (IOException ex) {
            System.out.println("NOT OK!");
        }
    }
    
    public void load() {
        System.out.print("Loading Properties...");
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(PROPERTIES_FILE_NAME)));
            
            // LightWeightM2M Server
            core.getStatus().getLightWeightM2M().setAddress( (properties.getProperty(PROPERTY_LIGHTWEIGHTM2M_ADDRESS) == null) ? InetAddress.getLocalHost() : InetAddress.getByName(properties.getProperty(PROPERTY_LIGHTWEIGHTM2M_ADDRESS)) );
            core.getStatus().getLightWeightM2M().setPort( (properties.getProperty(PROPERTY_LIGHTWEIGHTM2M_PORT) == null) ? DEFAULT_COAP_PORT : Integer.parseInt(properties.getProperty(PROPERTY_LIGHTWEIGHTM2M_PORT)));
            
            // Selected Thing
            core.getStatus().getSelectedThing().setAddress( (properties.getProperty(PROPERTY_THING_ADDRESS) == null) ? InetAddress.getLocalHost() : InetAddress.getByName(properties.getProperty(PROPERTY_THING_ADDRESS)) );
            core.getStatus().getSelectedThing().setPort( (properties.getProperty(PROPERTY_THING_PORT) == null) ? DEFAULT_COAP_PORT : Integer.parseInt(properties.getProperty(PROPERTY_THING_PORT)));
            
            
            System.out.println("OK!");
        } catch (IOException ex) {
            System.out.println("NOT OK!");
        }
    }
    
    
}
