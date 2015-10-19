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

import com.punyal.symbiotic.Utils.Coordinates;
import com.punyal.symbiotic.Utils.StageInfo;
import com.punyal.symbiotic.core.security.AppCheck;
import com.punyal.symbiotic.core.security.AppParameters;
import javafx.stage.Stage;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class Configuration {
    private final AppParameters app;
    private StageInfo mainStage;
    private StageInfo clientStage;
    private StageInfo aboutStage;
    
    public Configuration(){
        app = AppCheck.checkApp(this.getClass().getProtectionDomain().getCodeSource().getLocation().toString());
    }
    
    public AppParameters getApp(){
        return app;
    }
    
    public void setMainStage(Stage stage) {
        mainStage = new StageInfo(stage);
    }
    
    public void setMainStage(Stage stage, Coordinates coordinates) {
        mainStage = new StageInfo(stage, coordinates);
    }
    
    public void setMainStage(Stage stage, double x, double y) {
        mainStage = new StageInfo(stage, x, y);
    }
    
    public StageInfo getMainStageInfo() {
        return mainStage;
    }
    
    public void setClientStage(Stage stage) {
        clientStage = new StageInfo(stage);
    }
    
    public void setClientStage(Stage stage, Coordinates coordinates) {
        clientStage = new StageInfo(stage, coordinates);
    }
    
    public void setClientStage(Stage stage, double x, double y) {
        clientStage = new StageInfo(stage, x, y);
    }
    
    public StageInfo getClientStageInfo() {
        return clientStage;
    }
    
    public void setAboutStage(Stage stage) {
        aboutStage = new StageInfo(stage);
    }
    
    public void setAboutStage(Stage stage, Coordinates coordinates) {
        aboutStage = new StageInfo(stage, coordinates);
    }
    
    public void setAboutStage(Stage stage, double x, double y) {
        aboutStage = new StageInfo(stage, x, y);
    }
    
    public StageInfo getAboutStageInfo() {
        return aboutStage;
    }
    
    
}
