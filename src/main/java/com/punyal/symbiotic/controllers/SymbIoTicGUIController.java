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
package com.punyal.symbiotic.controllers;

import com.punyal.symbiotic.core.Core;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;

/**
 * FXML Controller class
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class SymbIoTicGUIController implements Initializable {
    private Core core;
    
    public void setCore(Core core) {
        this.core = core;
    }
    
    /**
     * Initializes the controller before set other params.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    /**
     * Correct initialization
     */
    public void init() {
        initMainWindow();
    }
    
    
    
    
    /*------------------------------------------------------------------------*/
    /*                        Main Window Controllers                         */
    /*------------------------------------------------------------------------*/
    @FXML
    private Label windowTitle;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu menuTitle;
    @FXML
    private MenuItem menuAbout, menuPreferences, menuClose;
    
    /* Client Part */
    @FXML
    private TabPane panelClient;
    
    
    @FXML
    private void handleMenuClose(ActionEvent e) throws IOException {
        Platform.exit();
    }
    
    private void initMainWindow() {
        windowTitle.setText(core.getConfiguration().getApp().getName() +
                " v" + core.getConfiguration().getApp().getVersion()+
                " - release " + core.getConfiguration().getApp().getBuildNumber());
        menuTitle.setText(core.getConfiguration().getApp().getName());
        menuAbout.setText("About "+core.getConfiguration().getApp().getName());
        menuClose.setText("Close "+core.getConfiguration().getApp().getName());
    }
    
}
