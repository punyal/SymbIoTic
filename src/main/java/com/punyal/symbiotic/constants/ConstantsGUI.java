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
package com.punyal.symbiotic.constants;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class ConstantsGUI {
    public ConstantsGUI() {}
    
    // GUI Client window offsets
    public static final int CLIENT_X_OFFSET = 30;
    public static final int CLIENT_Y_OFFSET = 20;
    
    // GUI About window sizes
    public static final int ABOUT_WIDTH = 1200;
    public static final int ABOUT_HEIGHT = 700;
    
    // GUI window names
    public static final String WINDOW_SYMBIOTIC = "symbiotic";
    public static final String WINDOW_CLIENT = "client";
    public static final String WINDOW_ABOUT = "about";
    public static final String WINDOW_AUTHENTICATION = "authentication";
    
    // ACC Chart
    public static final int MAX_DATA_POINTS = 2000;
    public static final int CHART_MAX_VALUE = 2;
    public static final int CHART_MIN_VALUE = -2;
    
    // Default Values
    public static final int DEFAULT_FREQUENCY = 200;
    
    // Tree
    public static final String TREE_ROOT = "LWM2M Devices";
    public static final String TREE_IP = "IP: ";
    public static final String TREE_PORT = "Port: ";
    
    // IPSO
    public static final String IPSO_RECORD_DATA = "Record Data";
    public static final String IPSO_RECORDING = "Recording...";
    
    // ABOUT
    public static final String SYMBIOTIC_ABOUT = 
            "SymbIoTic is a tool developed by:\n"+
            "Pablo Puñal Pereira <pablo.punal@ltu.se>\n"+
            "\nSymbIoTic is a GUI for Mulle IoT Technology\n"+
            "Some projects like Rockvolt and Wheel-Loader are using\n"+
            "it, but it is also usefull to bench performances of\n"+
            "network, power consumption...";
}
