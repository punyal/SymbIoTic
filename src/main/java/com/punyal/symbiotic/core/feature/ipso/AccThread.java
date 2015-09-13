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

import com.punyal.symbiotic.core.Core;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class AccThread extends Thread {
    private final Core core;
    
    public AccThread(Core core) {
        this.core = core;
        // Set as daemon to close with the program
        this.setDaemon(true);
    }
    
    public void startThread() {
        this.start();
    }
    
    @Override
    public void run() {
        double value;
        try {
            while (true) {
                
                value = 1500*Math.sin(2*Math.PI*(core.getController().getLineChartIPSOindex()/25000)) +
                        1000*Math.sin(2*Math.PI*(core.getController().getLineChartIPSOindex()/2500)) +
                        500*Math.sin(2*Math.PI*(core.getController().getLineChartIPSOindex()/250));
                
                core.getController().getAccData().add(value);
                try {
                    Thread.sleep(2,500000); //2.5 ms
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt(); // This should kill it propertly
                }
            }
        }
        finally{
            System.out.println("Killing AccThread");
        }
    }
    
    
    
    
    
}
