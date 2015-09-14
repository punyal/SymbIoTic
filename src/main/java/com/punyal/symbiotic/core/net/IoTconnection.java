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

import static com.punyal.symbiotic.constants.ConstantsNet.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class IoTconnection {
    private InetAddress address;
    private int port;

    public IoTconnection(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }
    
    public IoTconnection(InetAddress address) {
        this.address = address;
        this.port = DEFAULT_COAP_PORT;
    }

    public IoTconnection(String address, int port) {
        try {
            this.address = InetAddress.getByName(address);
        } catch (UnknownHostException ex) {
            this.address = InetAddress.getLoopbackAddress();
        }
        this.port = port;
    }
    
    public IoTconnection(String address) {
        try {
            this.address = InetAddress.getByName(address);
        } catch (UnknownHostException ex) {
            this.address = InetAddress.getLoopbackAddress();
        }
        this.port = DEFAULT_COAP_PORT;
    }
    
    public IoTconnection() {
        this.address = InetAddress.getLoopbackAddress();
        this.port = DEFAULT_COAP_PORT;
    }
    
    public void setAddress(InetAddress address) {
        this.address = address;
    }
    
    public void setAddress(String address) {
        try {
            this.address = InetAddress.getByName(address);
        } catch (UnknownHostException ex) {
            this.address = InetAddress.getLoopbackAddress();
        }
    }
    
    public InetAddress getInetAddress() {
        return address;
    }
    
    public String getAddress() {
        return address.getHostAddress();
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
    public int getPort() {
        return port;
    }
}
