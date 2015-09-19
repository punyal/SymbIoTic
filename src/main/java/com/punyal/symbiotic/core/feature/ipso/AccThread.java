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
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class AccThread extends Thread {
    private final Core core;
    private final ConcurrentLinkedQueue<Number> incomingData = new ConcurrentLinkedQueue<>();
    private boolean running;
    short lastValue;
    
    
    public AccThread(Core core) {
        this.core = core;
        // Set as daemon to close with the program
        this.setDaemon(true);
        running = true;
        lastValue = 0;
    }
    
    public void startThread() {
        //System.out.println("Starting AccThread");
        this.start();
        // Launch thread observe
        AccRequestThread accReqThread = new AccRequestThread(incomingData);
        accReqThread.startThread();
    }
    
    public void stopThread() {
        running = false;
    }
    
    
    @Override
    public void run() {
        try {
            while (running) {
                System.out.println(incomingData.size());
                for (int i=0; i<10; i++) {
                    if (incomingData.isEmpty()) {
                        core.getController().getAccData().add(lastValue);
                    } else {
                        lastValue = (short) incomingData.remove();
                        core.getController().getAccData().add(lastValue);
                    }
                }
            
                try {
                    Thread.sleep(50); //50 ms
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt(); // This should kill it propertly
                }
            }
        }
        finally{
            System.out.println("Killing AccThread");
            // Launch thread stop2measure
        }
    }
    
    
    
    
    
}
