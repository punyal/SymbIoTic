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
import java.util.Random;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class StrainThread extends Thread {
    private final Core core;
    
    public StrainThread(Core core) {
        this.core = core;
        // Set as daemon to close with the program
        this.setDaemon(true);
    }
    
    public void startThread() {
        this.start();
    }
    
    @Override
    public void run() {
        Random random = new Random();
        try {
            while (true) {
                core.getStatus().setStrainLevel(random.nextInt((int) core.getController().getBatteryGauge().getMaxValue()));
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt(); // This should kill it propertly
                }
            }
        }
        finally{
            System.out.println("Killing StrainThread");
        }
    }
    
    
    
    
    
}
