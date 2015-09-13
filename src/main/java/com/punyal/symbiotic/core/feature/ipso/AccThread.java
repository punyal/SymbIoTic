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
    boolean running;
    
    public AccThread(Core core) {
        this.core = core;
        // Set as daemon to close with the program
        this.setDaemon(true);
        running = true;
    }
    
    public void startThread() {
        //System.out.println("Starting AccThread");
        this.start();
    }
    
    public void stopThread() {
        running = false;
    }
    
    
    @Override
    public void run() {
        double value;
        int iterations;
        try {
            while (running) {
                double actual = core.getController().getLineChartIPSOindex() + core.getController().getAccData().size();
                
                iterations = 5*(int)core.getController().getFreq()/100;
                
                for (int i=0; i<iterations ; i++) {
                    value = 3000*Math.sin(2*Math.PI*((actual+i)/(250*core.getController().getFreq())))*
                            Math.sin(2*Math.PI*((actual+i)/(25*core.getController().getFreq())))*
                            Math.sin(2*Math.PI*((actual+i)/(2.5*core.getController().getFreq())))*Math.sin(2*Math.PI*((actual+i)/25));

                    core.getController().getAccData().add(value);
                    //System.out.println(value);
                }
                try {
                    Thread.sleep(50); //50 ms
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt(); // This should kill it propertly
                }
            }
        }
        finally{
            //System.out.println("Killing AccThread");
        }
    }
    
    
    
    
    
}
