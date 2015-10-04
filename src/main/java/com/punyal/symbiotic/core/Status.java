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

import com.punyal.symbiotic.core.export.ExportData;
import com.punyal.symbiotic.core.net.IoTconnection;
import com.punyal.symbiotic.core.net.ThingsList;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class Status {
    private final Core core;
    private int batteryLevel;
    private int strainLevel;
    private boolean strainAlarm;
    private final IoTconnection lightWeightM2M, selectedThing;
    private final ThingsList thingsList;
    private final ExportData exportData;
    
    public Status(Core core) {
        this.core = core;
        batteryLevel = 0;
        strainLevel = 0;
        strainAlarm = false;
        lightWeightM2M = new IoTconnection();
        selectedThing = new IoTconnection();
        thingsList = new ThingsList(core);
        exportData = new ExportData(core);
    }
    
    public int getBatteryLevel() {
        return batteryLevel;
    }
    
    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }
    
    public int getStrainLevel() {
        return strainLevel;
    }
    
    public void setStrainLevel(int strainLevel, boolean strainAlarm) {
        this.strainLevel = strainLevel;
        this.strainAlarm = strainAlarm;
    }
    
    public boolean strainAlert() {
        return strainAlarm;
    }
    
    public IoTconnection getLightWeightM2M() {
        return lightWeightM2M;
    }
    
    public IoTconnection getSelectedThing() {
        return selectedThing;
    }
    
    public ThingsList getThingsList() {
        return thingsList;
    }
    
    public ExportData getExportData() {
        return exportData;
    }
    
    
}
